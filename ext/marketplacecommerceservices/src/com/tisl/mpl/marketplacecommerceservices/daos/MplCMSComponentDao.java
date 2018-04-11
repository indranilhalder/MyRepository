/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.Collection;
import java.util.List;


/**
 * @author Ashish Vyas
 *
 */
public interface MplCMSComponentDao
{
	public List<AbstractCMSComponentModel> getPagewiseComponent(final String pageId, final String componentId,
			final Collection<CatalogVersionModel> catalogVersions);
	public Collection<CategoryModel>  getCategoryByCode(final String categoryId, final Collection<CatalogVersionModel> catalogVersions);

}
