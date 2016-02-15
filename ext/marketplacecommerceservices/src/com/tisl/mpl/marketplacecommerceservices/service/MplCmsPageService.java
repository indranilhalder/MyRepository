/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;

import java.util.Collection;

import com.tisl.mpl.core.enums.CMSChannel;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author 765463
 *
 */
public interface MplCmsPageService extends CMSPageService
{
	ContentPageModel getLandingPageForCategory(CategoryModel category) throws CMSItemNotFoundException;

	ContentPageModel getLandingPageForSeller(final SellerMasterModel sellerMaster) throws CMSItemNotFoundException;

	ContentPageModel getCategoryLandingPageForMobile(CategoryModel category, CMSChannel cms) throws CMSItemNotFoundException;

	ContentPageModel getHomePageForMobile();

	ContentPageModel getCollectionLandingPageForMobile(String collectionId, CMSChannel cms);

	ContentPageModel getSellerLandingPageForMobile(SellerMasterModel sellerMasterModel, CMSChannel cms)
			throws CMSItemNotFoundException, NullPointerException;

	// Collection<ContentPageModel> getAllContentPages(final CatalogVersionModel catalogmodel);
	Collection<ContentPageModel> getAllContentPages(final Collection<CatalogVersionModel> catalogmodel);

	ContentSlotModel getContentSlotByUidForPage(final String pageId, final String contentSlotId, final String catalogVersion);
}

