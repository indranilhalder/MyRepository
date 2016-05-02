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

import com.tisl.mpl.core.comparator.SizeFacetComparator;


/**
 * @author TCS
 *
 */
public class DefaultSizeFacetTopValuesProvider implements FacetSortProvider, Serializable
{


	private SizeFacetComparator comparator;

	/**
	 * @return the comparator
	 */
	public SizeFacetComparator getComparator()
	{
		return comparator;
	}

	/**
	 * @param comparator
	 *           the comparator to set
	 */
	public void setComparator(final SizeFacetComparator comparator)
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
