///**
// *
// */
//package com.hybris.yps.nodewarmer.warmer.impl;
//
//import de.hybris.platform.catalog.CatalogVersionService;
//import de.hybris.platform.catalog.model.CatalogModel;
//import de.hybris.platform.catalog.model.CatalogVersionModel;
//import de.hybris.platform.cms2.model.site.CMSSiteModel;
//import de.hybris.platform.cms2.servicelayer.services.CMSSiteService;
//import de.hybris.platform.cms2.servicelayer.services.admin.CMSAdminSiteService;
//import de.hybris.platform.core.Registry;
//import de.hybris.platform.core.model.c2l.CurrencyModel;
//import de.hybris.platform.core.model.c2l.LanguageModel;
//import de.hybris.platform.core.model.product.ProductModel;
//import de.hybris.platform.servicelayer.i18n.CommonI18NService;
//import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
//import de.hybris.platform.servicelayer.search.FlexibleSearchService;
//import de.hybris.platform.servicelayer.search.SearchResult;
//import de.hybris.platform.servicelayer.session.SessionService;
//import de.hybris.platform.servicelayer.user.UserService;
//import de.hybris.platform.store.BaseStoreModel;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.concurrent.CountDownLatch;
//
//import javax.annotation.Resource;
//
//import net.sf.ehcache.Ehcache;
//
//import org.apache.commons.lang.StringUtils;
//import org.apache.log4j.Logger;
//import org.springframework.cache.Cache;
//import org.springframework.cache.ehcache.EhCacheCacheManager;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//import org.springframework.util.StopWatch;
//
//import com.hybris.dtocache.facades.CachingProductFacade;
//import com.hybris.yps.nodewarmer.warmer.WarmerStrategy;
//
//
///**
// * @author brendan.dobbs
// *
// */
//public class ProductDtoCacheWarmer implements WarmerStrategy
//{
//
//	private static final Logger LOG = Logger.getLogger(ProductDtoCacheWarmer.class);
//
//	private boolean loadAllSites = false;
//	private boolean calculateSizeOfCaches = true;
//
//	private List<String> sitesToLoad;
//
//	@Resource
//	private CachingProductFacade cachingProductFacade;
//
//	@Resource
//	private CMSAdminSiteService cmsAdminSiteService;
//
//	@Resource
//	private CMSSiteService cmsSiteService;
//	@Resource
//	private UserService userService;
//	@Resource
//	private CommonI18NService commonI18NService;
//	@Resource
//	private CatalogVersionService catalogVersionService;
//	@Resource
//	private SessionService sessionService;
//
//	private EhCacheCacheManager dtoCacheManager;
//
//	@Resource(name = "loadExecutor")
//	private ThreadPoolTaskExecutor loadExecutor;
//
//	@Resource
//	private FlexibleSearchService flexibleSearchService;
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see com.hybris.yps.nodewarmer.warmer.WarmerStrategy#execute()
//	 */
//	@Override
//	public boolean execute()
//	{
//		// ensure we have a customer so the search restrictions are working
//		userService.setCurrentUser(userService.getAnonymousUser());
//
//		if (loadAllSites)
//		{
//			final Collection<CMSSiteModel> sites = cmsSiteService.getSites();
//			for (final CMSSiteModel site : sites)
//			{
//				loadSite(site);
//			}
//		}
//		else
//		{
//			for (final String siteStr : sitesToLoad)
//			{
//				if (StringUtils.isNotEmpty(siteStr))
//				{
//					final CMSSiteModel site = cmsAdminSiteService.getSiteForId(siteStr);
//					loadSite(site);
//				}
//			}
//		}
//
//		if (calculateSizeOfCaches)
//		{
//			calculateSizesOfCaches();
//		}
//		return true;
//	}
//
//	/**
//	 * @param site
//	 */
//	private void loadSite(final CMSSiteModel site)
//	{
//		int entriesLoaded = 0;
//		final StopWatch timer = new StopWatch();
//		timer.start();
//		cmsSiteService.setCurrentSite(site);
//		for (final BaseStoreModel store : site.getStores())
//		{
//			// set the catalog versions for the store
//			final Collection<CatalogVersionModel> sessionCatalogVersions = new LinkedList();
//			for (final CatalogModel catalog : store.getCatalogs())
//			{
//				if (catalog.getActiveCatalogVersion() != null)
//				{
//					sessionCatalogVersions.add(catalog.getActiveCatalogVersion());
//				}
//			}
//			// set the catalog versions on the session
//			catalogVersionService.setSessionCatalogVersions(sessionCatalogVersions);
//
//			final FlexibleSearchQuery query = new FlexibleSearchQuery("SELECT {" + ProductModel.CODE + "} FROM {"
//					+ ProductModel._TYPECODE + "}");
//			query.setResultClassList(Collections.singletonList(String.class));
//			// get the product codes to export
//			final SearchResult<String> result = flexibleSearchService.search(query);
//			LOG.info("Found [" + result.getTotalCount() + "] products to load");
//			final List<String> productCodes = result.getResult();
//
//			final int pageSize = 50;
//			final int numberOfJobs = (int) Math.ceil((double) productCodes.size() / (double) pageSize);
//
//			final CountDownLatch done = new CountDownLatch(numberOfJobs);
//
//			for (int i = 0, start = 0; i < numberOfJobs; i++, start += pageSize)
//			{
//				final int end = Math.min(start + pageSize, productCodes.size());
//				final List<String> productCodesSubList = productCodes.subList(start, end);
//				loadExecutor.execute(new AsyncLoadTask(productCodesSubList, done, store, sessionCatalogVersions));
//				entriesLoaded += productCodesSubList.size();
//			}
//			try
//			{
//				done.await();
//			}
//			catch (final InterruptedException e)
//			{
//				// do nothing
//			}
//		}
//
//		timer.stop();
//		LOG.info("Loaded [" + entriesLoaded + "] for site [" + site.getUid() + "] in [" + timer.getTotalTimeSeconds() + "] seconds");
//		//outputCacheSummary();
//	}
//
//	protected void calculateSizesOfCaches()
//	{
//		long totalMb = 0;
//		for (final String cacheName : dtoCacheManager.getCacheNames())
//		{
//			final Cache cache = dtoCacheManager.getCache(cacheName);
//			final Ehcache ehCache = (Ehcache) cache.getNativeCache();
//
//			final long size = ehCache.calculateInMemorySize();
//			final long sizeMb = size / 1024 / 1024;
//			totalMb += sizeMb;
//			LOG.info("Loaded [" + ehCache.getSize() + "] items into cache [" + cacheName + "], retaining size [" + sizeMb
//					+ "] MB, memory per instance [" + size / 1024 / ehCache.getSize() + "] KB");
//		}
//		LOG.info("Estimated total heap usage [" + totalMb + "] MB");
//	}
//
//	private class AsyncLoadTask implements Runnable
//	{
//		private final Collection<String> productCodes;
//		private final CountDownLatch done;
//		private final BaseStoreModel store;
//		private final Collection<CatalogVersionModel> sessionCatalogVersionModels;
//
//		private AsyncLoadTask(final Collection<String> productCodes, final CountDownLatch done, final BaseStoreModel store,
//				final Collection<CatalogVersionModel> sessionCatalogVersionModels)
//		{
//			this.productCodes = productCodes;
//			this.done = done;
//			this.store = store;
//			this.sessionCatalogVersionModels = sessionCatalogVersionModels;
//		}
//
//		@Override
//		public void run()
//		{
//			initTask();
//
//			for (final String productCode : productCodes)
//			{
//				// we cache content uniquely by currency so we need to retrieve the products for each currency
//				for (final CurrencyModel currency : store.getCurrencies())
//				{
//					commonI18NService.setCurrentCurrency(currency);
//					// we cache content uniquely for we need to retrieve the products for each language
//					for (final LanguageModel lang : store.getLanguages())
//					{
//						commonI18NService.setCurrentLanguage(lang);
//						cachingProductFacade.getProductForCodeAndOptions(productCode, CachingProductFacade.ALL_OPTIONS);
//					}
//				}
//			}
//			done.countDown();
//			sessionService.closeCurrentSession();
//		}
//
//		/**
//		 *
//		 */
//		private void initTask()
//		{
//			sessionService.createNewSession();
//			Registry.activateMasterTenant();
//			userService.setCurrentUser(userService.getAnonymousUser());
//			catalogVersionService.setSessionCatalogVersions(sessionCatalogVersionModels);
//		}
//	}
//
//	/**
//	 * @param sitesToLoad
//	 *           the sitesToLoad to set
//	 */
//	public void setSitesToLoad(final List<String> sitesToLoad)
//	{
//		this.sitesToLoad = sitesToLoad;
//	}
//
//	/**
//	 * @param loadAllSites
//	 *           the loadAllSites to set
//	 */
//	public void setLoadAllSites(final boolean loadAllSites)
//	{
//		this.loadAllSites = loadAllSites;
//	}
//
//	/**
//	 * @param dtoCacheManager
//	 *           the dtoCacheManager to set
//	 */
//	public void setDtoCacheManager(final EhCacheCacheManager dtoCacheManager)
//	{
//		this.dtoCacheManager = dtoCacheManager;
//	}
//
//	/**
//	 * @param calculateSizeOfCaches
//	 *           the calculateSizeOfCaches to set
//	 */
//	public void setCalculateSizeOfCaches(final boolean calculateSizeOfCaches)
//	{
//		this.calculateSizeOfCaches = calculateSizeOfCaches;
//	}
//
//}
