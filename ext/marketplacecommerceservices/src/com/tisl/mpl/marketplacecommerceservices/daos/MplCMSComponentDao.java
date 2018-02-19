/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.Collection;


/**
 * @author Ashish Vyas
 *
 */
public interface MplCMSComponentDao
{
	public AbstractCMSComponentModel getPagewiseComponent(final String pageId, final String componentId,
			final Collection<CatalogVersionModel> catalogVersions);

}
