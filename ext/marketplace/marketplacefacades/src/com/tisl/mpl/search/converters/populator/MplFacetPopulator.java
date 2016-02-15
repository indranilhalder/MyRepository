/**
 *
 */
package com.tisl.mpl.search.converters.populator;

import de.hybris.platform.commercefacades.search.converters.populator.FacetPopulator;
import de.hybris.platform.commerceservices.search.facetdata.FacetData;


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

	}


}
