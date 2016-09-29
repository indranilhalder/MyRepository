/**
 *
 */

package com.tisl.mpl.search.converters.populator;

import de.hybris.platform.solrfacetsearch.config.IndexedProperty;
import de.hybris.platform.solrfacetsearch.converters.populator.DefaultIndexedPropertyPopulator;
import de.hybris.platform.solrfacetsearch.model.config.SolrIndexedPropertyModel;

import org.springframework.util.StringUtils;


/**
 * @author 361234
 *
 */
public class MplDefaultIndexedPropertyPopulator extends DefaultIndexedPropertyPopulator

{

	@Override
	public void populate(final SolrIndexedPropertyModel source, final IndexedProperty target)
	{
		super.populate(source, target);

		if (source.getBoostDouble() != null)
		{
			target.setBoostDouble(source.getBoostDouble());

		}


		if (source.getGenericFacet() != null)
		{

			target.setGenericFacet(source.getGenericFacet().booleanValue());

		}
		/********** TISPRO-326 changes **********/
		if (source.getClassificationProductType() != null)
		{


			target.setClassificationProductType(source.getClassificationProductType());

		}
		if (source.getQueryType() == null || StringUtils.isEmpty(source.getQueryType()))
		{
			if (source.getClassificationAttributeAssignments() != null)
			{

				target.setClassificationAttributeAssignments(source.getClassificationAttributeAssignments());

			}


		}

		if (source.getFacetTopValue() != null)
		{
			target.setFacetTopValue(source.getFacetTopValue());
		}

		//Search POC start
		if (source.getUnitType() != null)
		{

			target.setUnitType(source.getUnitType());

		}
		if (source.getIsRangeFaceted() == null)
		{

			source.setIsRangeFaceted(Boolean.FALSE);
			target.setIsRangeFaceted(source.getIsRangeFaceted());

		}
		else
		{
			target.setIsRangeFaceted(source.getIsRangeFaceted());
		}
		if (source.getIsNumeric() == null)
		{

			source.setIsNumeric(Boolean.TRUE);
			target.setIsNumeric(source.getIsNumeric());

		}
		else
		{
			target.setIsNumeric(source.getIsNumeric());
		}
		////Search POC end
	}
}
