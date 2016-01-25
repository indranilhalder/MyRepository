/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSPageDao;

import com.tisl.mpl.core.enums.CMSChannel;
import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author 765463
 *
 */
public interface MplCmsPageDao extends CMSPageDao
{
	ContentPageModel getLandingPageForCategory(CategoryModel category);

	ContentPageModel getCategoryLandingPageForMobile(CategoryModel category, CMSChannel cms);

	ContentPageModel getHomePageForMobile(CMSChannel cms);

	ContentPageModel getLandingPageForSeller(SellerMasterModel sellerMaster);

	ContentPageModel getCollectionLandingPageForMobile(CMSChannel cms, MplShopByLookModel shopByLook);

	MplShopByLookModel getShopByLookModelForCollection(String collectionId);

	ContentPageModel getSellerLandingPageForMobile(SellerMasterModel sellerMasterModel, CMSChannel cms);
}
