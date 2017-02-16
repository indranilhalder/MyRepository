package com.hybris.dtocache.populator.analyzer;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.fest.util.Collections;
import org.reflections.Reflections;
import org.springframework.beans.BeanUtils;

import com.hybris.dtocache.config.Config;
import com.hybris.dtocache.exception.DtoCacheException;
import com.hybris.dtocache.exception.DtoCacheRuntimeException;

import de.hybris.platform.converters.Populator;
import de.hybris.platform.core.model.ItemModel;

public class PopulatorAnalyzer
{
	
	private static Logger LOG = Logger.getLogger(PopulatorAnalyzer.class);

	/**
	 * Tries to resolve all the properties with valid getters/setters from a populator class name.
	 * 
	 * @param populatorClassName - has to be an implementation of {@link Populator}
	 * @return
	 * @throws DtoCacheException
	 */
	public static List<String> analyse(final String populatorClassName) throws DtoCacheException {
		return analyse(populatorClassName, false);
	}

	/**
	 * Tries to resolve all the properties with valid getters/setters from a populator class name.
	 * 
	 * 
	 * @param populatorClassName - has to be an implementation of {@link Populator}
	 * @param checkForConflicts - if true - it will check if the {@link ItemModel} parameter of the populate
	 * method has extensions and in that case it will throw a {@link DtoCacheRuntimeException}
	 * @return
	 * @throws DtoCacheException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<String> analyse(final String populatorClassName, boolean checkForConflicts) throws DtoCacheException
	{
		Class<? extends Populator> populator = null;
		try
		{
			populator = (Class<? extends Populator>) Class.forName(populatorClassName);
		} catch (ClassNotFoundException e)
		{
			throw new DtoCacheException("'" + populatorClassName + "' was not found");
		} catch (ClassCastException e) {
			throw new DtoCacheException("'" + populatorClassName + "' does not implement 'de.hybris.platform.converters.Populator<SOURCE, TARGET>'");			
		}
		return analyse(populator, checkForConflicts);
	}	

	/**
	 * Tries to resolve all the properties with valid getters/setters from a populator class.
	 * 
	 * @param populatorClassName - has to be an implementation of {@link Populator}
	 * @return
	 * @throws DtoCacheException
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> analyse(final Class<? extends Populator> populatorClass) throws DtoCacheException {
		return analyse(populatorClass, false);
	}
	
	/**
	 * Tries to resolve all the properties with valid getters/setters from a populator class.
	 * 
	 * 
	 * @param populatorClass - has to be an implementation of {@link Populator}
	 * @param checkForConflicts - if true - it will check if the {@link ItemModel} parameter of the populate
	 * method has extensions and in that case it will throw a {@link DtoCacheRuntimeException}
	 * @return
	 * @throws DtoCacheException
	 */
	@SuppressWarnings("rawtypes")
	public static List<String> analyse(final Class<? extends Populator> populatorClass, boolean checkForConflicts) throws DtoCacheException
	{
		final Method populate = getPopulateMethod(populatorClass);

		String populateDto = getPopulateDto(populatorClass.getName());
		String populateItemModel = getPopulateItemModel(populatorClass.getName());

		// if checkForConflicts AND one of the configured ItemModel and DTO is empty
		if (checkForConflicts && (StringUtils.isEmpty(populateDto) || StringUtils.isEmpty(populateItemModel))) {
			Class<? extends ItemModel> itemModelClass = getItemModelClassFromPopulateMethod(populate);
			scanForPossibleChildren(itemModelClass);
		}
		Class<?> dtoClass = getDtoClassFromPopulateMethod(populate);
		LOG.debug("found populator's DTO type: '" + dtoClass.getName() + "'");
		final PropertyDescriptor[] properties = BeanUtils.getPropertyDescriptors(dtoClass);
		final List<String> props = new LinkedList<String>();
		for (PropertyDescriptor p : properties)
		{
			if (!"class".equals(p.getName())) {
				props.add(p.getName());
			}
		}
		return props;
	}

	/**
	 * Looks for the populate method from a {@link Populator} class name.
	 * @param populatorClassName
	 * @return
	 * @throws DtoCacheException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Method getPopulateMethod(final String populatorClassName) throws DtoCacheException {
		Class c;
		try
		{
			c = Class.forName(populatorClassName);
		} catch (ClassNotFoundException e)
		{
			throw new DtoCacheException("class '" + populatorClassName + "' was not found");
		}
		if (Populator.class.isAssignableFrom(c)) {
			return getPopulateMethod(c);
		}
		throw new DtoCacheException("class '" + populatorClassName + "' is not a Populator.");
	}
	
	/**
	 * looks for the Method 'populate' on the given Class
	 * @param populatorClass
	 * @return
	 * @throws DtoCacheException
	 */
	@SuppressWarnings("rawtypes")
	public static Method getPopulateMethod(final Class<? extends Populator> populatorClass) throws DtoCacheException {
		final Method[] methods = populatorClass.getMethods();
		for (final Method method : methods)
		{
			if ("populate".equals(method.getName())) {
				try
				{
					// we check if a parameter of type ItemModel exists - that is the only way to be sure we are
					// inspecting the good populate method.
					getItemModelClassFromPopulateMethod(method);
					getDtoClassFromPopulateMethod(method);
				} catch (DtoCacheException e)
				{
					continue;
				}
				return method;
			}
		}
		LOG.error("found no 'populate' method with a '*Data' parameter on class '" + populatorClass.getName() + "'");
		throw new DtoCacheException("no method 'populate' found on " + populatorClass.getName());
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Class<? extends ItemModel> getItemModelClassFromPopulateMethod(Method populate) throws DtoCacheException {
		final String populatorClassName = populate.getDeclaringClass().getName();
		final String populateItemModel = getPopulateItemModel(populatorClassName);
		if (StringUtils.isNotBlank(populateItemModel)) {
			try {
				return (Class<? extends ItemModel>) Class.forName(populateItemModel);
			} catch (Exception e) {
				throw new DtoCacheRuntimeException("the populator '" + populatorClassName + "' seems misconfigured. Reason is: " + e.getMessage());
			} 
		}
		final Class[] types = populate.getParameterTypes();
		for (final Class t : types)
		{
			if (ItemModel.class.isAssignableFrom(t)) {
				LOG.debug("found populator's ItemModel type: '" + t.getClass().getName() + "'");
				return t;
			}
		}
		throw new DtoCacheException("no parameter of type 'ItemModel' found on the populate method " + populate.getDeclaringClass().getName());
	}

	@SuppressWarnings("rawtypes")
	public static Class getDtoClassFromPopulateMethod(Method populate) throws DtoCacheException {
		final String populatorClassName = populate.getDeclaringClass().getName();
		final String populateDto = getPopulateDto(populatorClassName);
		if (StringUtils.isNotBlank(populateDto)) {
			try {
				return Class.forName(populateDto);
			} catch (ClassNotFoundException e) {
				throw new DtoCacheRuntimeException("the populator '" + populatorClassName + "' seems misconfigured. Reason is: " + e.getMessage());
			}
		}
		final Class[] types = populate.getParameterTypes();
		for (final Class t : types)
		{
			if (t.getName().endsWith("Data")) {
				LOG.debug("found populator's DTO type: '" + t.getClass().getName() + "'");
				return t;
			}
		}
		throw new DtoCacheException("no DTO parameter of type '*Data' found on the populate method " + populate.getDeclaringClass().getName());
	}

	/**
	 * Throws a {@link DtoCacheRuntimeException} if the passed ItemModel class has children.
	 * It should be used only once when a CacheDescriptor is created.
	 * 
	 * It scans both the de.hybris.platform.core.model package and the base package (starting from the 3rd level)
	 * of the itemModelClass itself.
	 * 
	 * @param itemModelClass
	 */
	public static <T extends ItemModel> void scanForPossibleChildren(final Class<T> itemModelClass) {
		final Set<String> packages = new HashSet<String>();
		String packageToScan = Config.get().getString(Config.SUFFIX + Config.PARAMS_SCAN_PACKAGE, null);
		if (StringUtils.isEmpty(packageToScan)) {
			final String[] pieces = itemModelClass.getName().split("\\.", 4);
			packageToScan = StringUtils.join(pieces, ".", 0, 3);
			if (!"de.hybris.platform.core.model".contains(packageToScan)) {
				packages.add(packageToScan);
			}
		}
		packages.add("de.hybris.platform.core.model");
		Reflections reflections = new Reflections(packages);
		Set<Class<? extends T>> children = reflections.getSubTypesOf(itemModelClass);
		if (!Collections.isEmpty(children)) {
			for (Class<? extends T> c : children) {
				if (!c.getSimpleName().contains("Test")) {
					if (isStrictScanMode()) {
						throw new DtoCacheRuntimeException("potential class missmatch: please check the configuration and specify the exact ItemModel and DTO classes.");
					}
					LOG.warn("potential class missmatch on populator: please check the configuration and specify the exact ItemModel and DTO classes.");
					return;
				}
			}
		}
	}

	private static boolean isStrictScanMode() {
		return Config.get().getString(Config.SUFFIX + Config.PARAMS_SCAN_MODE, "silent").equals(Config.VALUES_SCAN_MODE_STRICT)? true : false;
	}

	private static String getPopulateItemModel(String populatorClassName) {
		return Config.get().getString(Config.SUFFIX + populatorClassName + "." + Config.PARAMS_POPULATE_ITEMMODEL, null);
	}

	private static String getPopulateDto(String populatorClassName) {
		return Config.get().getString(Config.SUFFIX + populatorClassName + "." + Config.PARAMS_POPULATE_DTO, null);
	}
}
