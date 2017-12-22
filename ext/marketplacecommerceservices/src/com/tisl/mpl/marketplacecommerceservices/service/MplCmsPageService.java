/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.service;

import de.hybris.platform.catalog.model.CatalogVersionModel;
import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.services.CMSPageService;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Collection;
import java.util.List;

import com.tisl.mpl.core.enums.CMSChannel;
import com.tisl.mpl.core.model.BrandComponentModel;
import com.tisl.mpl.model.SellerMasterModel;
import com.tisl.lux.model.LuxuryHomePagePreferenceModel;


/**
 * @author TCS
 *
 */
public interface MplCmsPageService extends CMSPageService
{
	ContentPageModel getLandingPageForCategory(CategoryModel category) throws CMSItemNotFoundException;

	ContentPageModel getLandingPageForSeller(final SellerMasterModel sellerMaster) throws CMSItemNotFoundException;

	ContentPageModel getCategoryLandingPageForMobile(CategoryModel category, CMSChannel cms) throws CMSItemNotFoundException;

	ContentPageModel getCollectionLandingPageForMobile(String collectionId, CMSChannel cms);

	ContentPageModel getSellerLandingPageForMobile(SellerMasterModel sellerMasterModel, CMSChannel cms)
			throws CMSItemNotFoundException, NullPointerException;

	// Collection<ContentPageModel> getAllContentPages(final CatalogVersionModel catalogmodel);
	Collection<ContentPageModel> getAllContentPages(final Collection<CatalogVersionModel> catalogmodel);

	ContentSlotModel getContentSlotByUidForPage(final String pageId, final String contentSlotId, final String catalogVersion);

	/**
	 * @param pageUid
	 * @return
	 */
	ContentPageModel getHomePageForMobile(String pageUid);

	/**
	 * @param pageUid
	 * @return ContentPageModel
	 */
	ContentPageModel getPageForAppById(String pageUid);


	//Added for TPR-798
	public SearchPageData<ContentSlotForPageModel> getContentSlotsForAppById(final String pageUid, final PageableData pageableData);

	//TPR-978
	ContentPageModel getContentPageForProduct(ProductModel product) throws CMSItemNotFoundException;

	public AbstractPageModel getPageForIdandCatalogVersion(final String id, final CatalogVersionModel cv)
			throws CMSItemNotFoundException;

	//TPR-1072
	public List<BrandComponentModel> getBrandsForShopByBrand();

	/**
	 * @param siteUid
	 * @return CMSSite
	 * @CAR-285
	 */
	public CMSSiteModel getSiteforId(String siteUid) throws CMSItemNotFoundException;
	
	public LuxuryHomePagePreferenceModel getHomePagePreference(String gender, String category);
}
