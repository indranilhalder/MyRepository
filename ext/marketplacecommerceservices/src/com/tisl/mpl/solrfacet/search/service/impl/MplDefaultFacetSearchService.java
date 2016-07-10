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
			valueRangeSet = property.getValueRangeSets().get("INR-ELECTRONICS");
			if (valueRangeSet != null)
			{
				valueRangesList = valueRangeSet.getValueRanges();
			}
			valueRangeSet = property.getValueRangeSets().get("INR-APPAREL");
			if (valueRangeSet != null)
			{
				valueRangesList.addAll(valueRangeSet.getValueRanges());
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