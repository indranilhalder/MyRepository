/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.commerceservices.search.solrfacetsearch.provider.TopValuesProvider;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.search.FacetValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * @author 361234
 *
 */
public class DefaultMplBrandTopValuesProvider implements TopValuesProvider
{
	private int topFacetCount = 6;

	protected int getTopFacetCount()
	{
		return this.topFacetCount;
	}

	public void setTopFacetCount(final int topFacetCount)
	{
		this.topFacetCount = topFacetCount;
	}

	public List<FacetValue> getTopValues(final IndexedProperty indexedProperty, final List<FacetValue> facets)
	{

		final List topFacetValues = new ArrayList();

		if (facets != null)
		{
			for (final FacetValue facetValue : facets)
			{
				if ((facetValue == null) || ((topFacetValues.size() >= getTopFacetCount()) && (!(facetValue.isSelected()))))
				{
					continue;
				}
				topFacetValues.add(facetValue);
			}

			if (topFacetValues.size() >= facets.size())
			{
				return Collections.emptyList();
			}
		}

		return topFacetValues;
	}


}
