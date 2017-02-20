/**
 *
 */
package com.hybris.dtocache.populator;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.InitializingBean;

import com.hybris.dtocache.cache.CacheDescriptor;
import com.hybris.dtocache.cache.CacheDescriptorFactory;
import com.hybris.dtocache.config.Config;

import de.hybris.platform.cache.Cache;
import de.hybris.platform.cache.InvalidationListener;
import de.hybris.platform.cache.InvalidationManager;
import de.hybris.platform.cache.InvalidationTarget;
import de.hybris.platform.cache.InvalidationTopic;
import de.hybris.platform.cache.RemoteInvalidationSource;
import de.hybris.platform.core.PK;
import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.regioncache.CacheValueLoadException;
import de.hybris.platform.regioncache.CacheValueLoader;
import de.hybris.platform.regioncache.ConcurrentHashSet;
import de.hybris.platform.regioncache.key.CacheKey;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.type.TypeService;
import de.hybris.platform.tx.DefaultAfterSaveListenerRegistry;

/**
 * Slots based populator Cache Aspect
 *
 * @author sidi-mohamed.elaatifi@hybris.com
 *
 */
@Aspect
public class PopulatorCacheAspect implements InitializingBean
{
	private static Logger LOG = Logger.getLogger(PopulatorCacheAspect.class);

	@Resource(name = "cacheDescriptorFactory")
	private CacheDescriptorFactory descriptorFactory;

	@Resource(name = "defaultAfterSaveListenerRegistry")
	private DefaultAfterSaveListenerRegistry listenerRegistry;

	/**
	 * this registry is used to optimize the calls to the InvalidationListener: only ItemModels that are cached by
	 * psdtocache will be taken into account
	 */
	private Set<Integer> typeCodesRegistry = new ConcurrentHashSet<Integer>();

	@Resource
	private TypeService typeService;

	@Resource
	private ModelService modelService;

	@Around("execution(* de.hybris.platform.converters.Populator.populate(..))")
	public void cachePopulate(final ProceedingJoinPoint joinPoint) throws Throwable
	{
		final Object[] args = joinPoint.getArgs();
		final Object dataObject = args[1];
		final Object sourceObject = args[0];
		
		if (!(sourceObject instanceof ItemModel) || (dataObject == null))
		{
			joinPoint.proceed();
			return;
		}
		
		final ItemModel model = (ItemModel) sourceObject;

		final String populatorClassName = getPopulatorClassName(joinPoint.getTarget());
		CacheDescriptor desc = descriptorFactory.getCacheDescriptor(populatorClassName);
		if (desc == null)
		{
			String regionCacheName = Config.get().getString(Config.SUFFIX + populatorClassName, Config.VALUES_NO_CACHE);
			if (!Config.VALUES_NO_CACHE.equals(regionCacheName))
			{
				desc = descriptorFactory.createCacheDescriptor(populatorClassName, model);
			}
		}
		final CacheDescriptor descriptor = desc;
		// null cache => do not cache populator
		if (descriptor == null || descriptor.isDisabled()) 
		{
			joinPoint.proceed();
			return;
		}

		
		// Can't handle models without PK
		if (model.getPk() == null)
		{
			joinPoint.proceed();
			return;
		}

		// adding the managed typeCode to the registry
		final int typeCode = model.getPk().getTypeCode();
		if (!this.typeCodesRegistry.contains(typeCode))
		{
			this.typeCodesRegistry.add(typeCode);
		}
		
		final CacheKey key = descriptor.cacheKey(model);
		Object[] cacheEntry = (Object[]) descriptor.getCacheRegion().getWithLoader(key,
				new CacheValueLoader<Object[]>()
				{

					@Override
					public Object[] load(CacheKey paramCacheKey) throws CacheValueLoadException
					{
						try
						{
							joinPoint.proceed();
						} catch (final Throwable e)
						{
							// FIXME if the original populator fails?
						}
						return descriptor.createCacheEntry(dataObject);
					}
				});

		descriptor.copyFromCache(cacheEntry, dataObject);
	}
	
	/**
	 *
	 * @param targetObject
	 *            example net.sf.cglib.Foo$$EnhancerByCGLIB$$38272841.
	 * @return Cache name, which matches Populator Class fully qualified name (removed cglib name)
	 */
	protected String getPopulatorClassName(final Object targetObject)
	{
		final String cglibPattern = "$$";
		String name = targetObject.getClass().getName();
		final int idx = name.indexOf(cglibPattern);
		if (idx != -1)
		{
			name = name.substring(0, idx);
		}
		return name;
	}

	/**
	 * registration of an InvalidationListener
	 */
	@Override
	public void afterPropertiesSet() throws Exception
	{
		final InvalidationTopic topic = InvalidationManager.getInstance().getOrCreateInvalidationTopic(
				new Object[] { Cache.CACHEKEY_HJMP, Cache.CACHEKEY_ENTITY }); 
		topic.addInvalidationListener(new InvalidationListener()
		{

			@Override
			public void keyInvalidated(Object[] paramArrayOfObject, int paramInt,
					InvalidationTarget paramInvalidationTarget, RemoteInvalidationSource paramRemoteInvalidationSource)
			{
				PK pk = PK.parse(paramArrayOfObject[3].toString());

				// we are only interested by the events around root ItemModels
				// that are covered by the cached populators
				if (typeCodesRegistry.contains(pk.getTypeCode()))
				{
					List<CacheDescriptor> descriptors = descriptorFactory.getCacheDescriptors(pk.getTypeCodeAsString());
					if (descriptors != null)
					{
						for (CacheDescriptor d : descriptors)
						{
							// only when a descriptor is invalidable, it uses
							// the InvalidableCacheKey and because of that - we
							// can invalidate it properly.
							if (d.isInvalidate())
							{
								//final ItemModel itemModel = modelService.get(pk);
								CacheKey cacheKey = d.cacheKey(pk);
								if (d.getCacheRegion().containsKey(cacheKey)) {
									d.getCacheRegion().invalidate(cacheKey, false);
									LOG.debug("'" + d.getCacheRegion().getName() + "' cache region infalidation for the '"
											+ d.getPopulatorClassName() + "' : '" + cacheKey.toString() + "'");
								}
							}
						}
					}
				}
			}
		});
		LOG.info("the InvalidationListener was properly added to the invalidation topic.");
	}

	public CacheDescriptorFactory getDescriptorFactory()
	{
		return descriptorFactory;
	}

	public void setDescriptorFactory(CacheDescriptorFactory descriptorFactory)
	{
		this.descriptorFactory = descriptorFactory;
	}

	public DefaultAfterSaveListenerRegistry getListenerRegistry()
	{
		return listenerRegistry;
	}

	public void setListenerRegistry(DefaultAfterSaveListenerRegistry listenerRegistry)
	{
		this.listenerRegistry = listenerRegistry;
	}

	public TypeService getTypeService()
	{
		return typeService;
	}

	public void setTypeService(TypeService typeService)
	{
		this.typeService = typeService;
	}

	public ModelService getModelService()
	{
		return modelService;
	}

	public void setModelService(ModelService modelService)
	{
		this.modelService = modelService;
	}

}
