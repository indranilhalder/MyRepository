/**
 *
 */
package com.tisl.mpl.core.search.solrfacetsearch.provider.impl;

import de.hybris.platform.solrfacetsearch.config.FacetSortProvider;
import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.config.IndexedType;
import de.hybris.platform.solrfacetsearch.search.FacetValue;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.collections.ComparatorUtils;

import com.tisl.mpl.core.comparator.GenderFacetComparator;


/**
 * @author TCS
 *
 */
public class GenderFacetTopValuesProvider implements FacetSortProvider, Serializable
{


	private GenderFacetComparator comparator;

	/**
	 * @return the comparator
	 */
	public GenderFacetComparator getComparator()
	{
		return comparator;
	}

	/**
	 * @param comparator
	 *           the comparator to set
	 */
	public void setComparator(final GenderFacetComparator comparator)
	{
		this.comparator = comparator;
	}

	private boolean descending;

	//private Comparator<FacetValue> comparator;

	public boolean isDescending()
	{
		return this.descending;
	}

	public void setDescending(final boolean descending)
	{
		this.descending = descending;
	}

	@Override
	public Comparator<FacetValue> getComparatorForTypeAndProperty(final IndexedType paramIndexedType,
			final IndexedProperty paramIndexedProperty)
	{
		return ((isDescending()) ? ComparatorUtils.reversedComparator(this.comparator) : this.comparator);
	}
}
