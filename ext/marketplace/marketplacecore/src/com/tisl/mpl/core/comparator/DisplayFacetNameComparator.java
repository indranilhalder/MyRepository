/**
 *
 */
package com.tisl.mpl.core.comparator;

import de.hybris.platform.solrfacetsearch.search.FacetValue;

import java.util.Comparator;

import org.apache.commons.lang.StringUtils;


/**
 * @author TCS
 *
 */

public class DisplayFacetNameComparator implements Comparator<FacetValue>

{
	@Override
	public int compare(final FacetValue value1, final FacetValue value2)

	{
		if ((value1 == null) && (value2 == null))

		{
			return 0;
		}
		else if (value1 == null)
		{
			return -1;
		}
		else if (value2 == null)
		{
			return 1;
		}
		else if (value1.equals(value2) || value1 == value2)
		{
			return 0;
		}
		if ((StringUtils.isEmpty(value1.getDisplayName())) || (StringUtils.isEmpty(value2.getDisplayName())))

		{
			return value1.getName().compareTo(value2.getName());

		}
		return value1.getDisplayName().compareTo(value2.getDisplayName());
	}
}
