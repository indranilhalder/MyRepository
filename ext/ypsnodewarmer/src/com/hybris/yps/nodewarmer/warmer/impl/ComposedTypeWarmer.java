/**
 * 
 */
package com.hybris.yps.nodewarmer.warmer.impl;

import de.hybris.platform.core.model.type.ComposedTypeModel;
import de.hybris.platform.servicelayer.search.FlexibleSearchQuery;
import de.hybris.platform.servicelayer.search.FlexibleSearchService;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.servicelayer.type.TypeService;

import java.util.Collections;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.util.StopWatch;

import com.hybris.yps.nodewarmer.warmer.WarmerStrategy;


/**
 * Loads all the {@link ComposedType} instances into the type system cache region. This will also add the composed type
 * queries (find by code) to the query cache region.
 * 
 * @author brendan.dobbs
 * 
 */
public class ComposedTypeWarmer implements WarmerStrategy
{
	private static final Logger LOG = Logger.getLogger(ComposedTypeWarmer.class);

	@Resource
	private TypeService typeService;

	@Resource
	private FlexibleSearchService flexibleSearchService;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.hybris.yps.nodewarmer.warmer.WarmerStrategy#execute()
	 */
	@Override
	public boolean execute()
	{
		final StopWatch stopWatch = new StopWatch("ComposedType Warming");
		stopWatch.start();
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery("SELECT {code} FROM {" + ComposedTypeModel._TYPECODE + "}");
		searchQuery.setResultClassList(Collections.singletonList(String.class));
		final SearchResult<String> typeCodes = flexibleSearchService.search(searchQuery);
		for (final String typeCode : typeCodes.getResult())
		{
			typeService.getComposedTypeForCode(typeCode);
		}
		stopWatch.stop();
		LOG.info(stopWatch.shortSummary());
		return true;
	}

}
