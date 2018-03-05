/**
 *
 */
package com.tisl.mpl.search.converters.populator;

import de.hybris.platform.commercefacades.search.converters.populator.FacetPopulator;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;

import org.apache.commons.lang.StringUtils;


/**
 * @author 361234
 *
 */
public class MplFacetPopulator extends FacetPopulator
{

	@Override
	public void populate(final FacetData source, final FacetData target)
	{

		super.populate(source, target);

		target.setGenericFilter(source.isGenericFilter());
		target.setSelectedFilterCount(source.getSelectedFilterCount());
		if (source.getCode().equalsIgnoreCase("price"))
		{
			if (null != source.getRangeApplied())
			{
				target.setRangeApplied(source.getRangeApplied());
			}

			if (null != source.getCustomeRange())
			{
				target.setCustomeRange(source.getCustomeRange());
			}

			if (StringUtils.isNotEmpty(source.getMinPrice()))
			{
				target.setMinPrice(source.getMinPrice());
			}
			if (StringUtils.isNotEmpty(source.getMaxPrice()))
			{
				target.setMaxPrice(source.getMaxPrice());
			}
		}
	}


}
