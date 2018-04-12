/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.components.AbstractCMSComponentModel;

import java.util.Collection;
import java.util.List;


/**
 * @author Ashish Vyas
 *
 */
public interface MplCMSComponentService
{
	public List<AbstractCMSComponentModel> getPagewiseComponent(String pageId, String componentId);
	
	public Collection<CategoryModel> getCategoryByCode(String categoryId);

}
