/**
 * 
 */
package com.hybris.yps.nodewarmer.warmer.impl;

import de.hybris.platform.constants.GeneratedCoreConstants.Attributes.EnumerationMetaType;
import de.hybris.platform.core.model.enumeration.EnumerationMetaTypeModel;
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
 * Loads all the {@link EnumerationMetaType} instances into the type system cache region. This will also add the
 * enumeration type queries (find by code) to the query cache region.
 * 
 * @author brendan.dobbs
 * 
 */
public class EnumerationTypesWarmer implements WarmerStrategy
{
	private static final Logger LOG = Logger.getLogger(EnumerationTypesWarmer.class);

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
		final StopWatch stopWatch = new StopWatch("Enumeration Type warming");
		stopWatch.start();
		final FlexibleSearchQuery searchQuery = new FlexibleSearchQuery("SELECT {code} FROM {" + EnumerationMetaTypeModel._TYPECODE
				+ "}");
		searchQuery.setResultClassList(Collections.singletonList(String.class));
		final SearchResult<String> typeCodes = flexibleSearchService.search(searchQuery);
		for (final String typeCode : typeCodes.getResult())
		{
			typeService.getEnumerationTypeForCode(typeCode);
		}
		stopWatch.stop();
		LOG.info(stopWatch.shortSummary());
		return true;
	}

}
