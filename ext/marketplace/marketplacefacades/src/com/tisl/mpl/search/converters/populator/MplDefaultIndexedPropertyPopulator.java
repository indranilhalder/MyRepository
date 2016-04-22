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

		if (source.getBoost() != null)
		{
			target.setBoost(source.getBoost());

		}


		if (source.getGenericFacet() != null)
		{

			target.setGenericFacet(source.getGenericFacet().booleanValue());

		}

		if (source.getQueryType() == null || StringUtils.isEmpty(source.getQueryType()))


		{
			if (source.getClassificationAttributeAssignments() != null)
			{

				target.setClassificationAttributeAssignments(source.getClassificationAttributeAssignments());

			}

		}
	}
}
