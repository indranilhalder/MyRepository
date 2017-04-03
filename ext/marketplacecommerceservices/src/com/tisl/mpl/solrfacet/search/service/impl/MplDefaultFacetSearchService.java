/**
 *
 */
package com.tisl.mpl.solrfacet.search.service.impl;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.ValueRange;
import de.hybris.platform.solrfacetsearch.config.ValueRangeSet;
import de.hybris.platform.solrfacetsearch.search.impl.DefaultFacetSearchService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.MapUtils;



public class MplDefaultFacetSearchService extends DefaultFacetSearchService
{

	@Override
	protected List<ValueRange> getValueRanges(final IndexedProperty property, final String qualifier)
	{
		List<ValueRange> valueRangesList = new ArrayList<ValueRange>();
		ValueRangeSet valueRangeSet;
		// Customized mpl price range will be taken
		if (property.getName().equalsIgnoreCase("price"))
		{
			if (MapUtils.isNotEmpty(property.getValueRangeSets()))
			{
				for (final Map.Entry<String, ValueRangeSet> entry : property.getValueRangeSets().entrySet())
				{
					valueRangesList.addAll(entry.getValue().getValueRanges());
				}
			}
			return valueRangesList;
		}
		else if (property.getName().equalsIgnoreCase("discountFlag"))
		{
			valueRangeSet = property.getValueRangeSets().get("DISCOUNT");
			if (valueRangeSet != null)
			{
				valueRangesList = valueRangeSet.getValueRanges();
			}
			return valueRangesList;
		}
		else
		{

			if (qualifier == null)
			{
				valueRangeSet = property.getValueRangeSets().get("default");
			}
			else
			{
				valueRangeSet = property.getValueRangeSets().get(qualifier);
				if (valueRangeSet == null)
				{
					valueRangeSet = property.getValueRangeSets().get("default");
				}
			}

			if (valueRangeSet != null)
			{
				valueRangesList = valueRangeSet.getValueRanges();
				return valueRangesList;
			}

		}


		return Collections.emptyList();
	}
}
