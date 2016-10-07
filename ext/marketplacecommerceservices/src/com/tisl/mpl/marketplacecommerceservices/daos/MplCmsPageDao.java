/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSPageDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;

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

	ContentPageModel getLandingPageForSeller(SellerMasterModel sellerMaster);

	ContentPageModel getCollectionLandingPageForMobile(CMSChannel cms, MplShopByLookModel shopByLook);

	MplShopByLookModel getShopByLookModelForCollection(String collectionId);

	ContentPageModel getSellerLandingPageForMobile(SellerMasterModel sellerMasterModel, CMSChannel cms);

	ContentSlotModel getContentSlotByUidForPage(String pageId, String contentSlotId, String catalogVersion);

	/**
	 * @param cms
	 * @param pageUid
	 * @return ContentPageModel
	 */
	ContentPageModel getHomePageForMobile(CMSChannel cms, String pageUid);

	/**
	 * @param pageUid
	 * @return ContentPageModel
	 */
	ContentPageModel getPageForAppById(String pageUid);

	SearchPageData<ContentSlotForPageModel> getContentSlotsForAppById(String pageUid, PageableData pageableData);

	ContentPageModel getContentPageForProduct(ProductModel product);

}
