/**
 *
 */
package com.tisl.mpl.marketplacecommerceservices.daos;

import de.hybris.platform.category.model.CategoryModel;
import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.contents.contentslot.ContentSlotModel;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.cms2.model.relations.ContentSlotForPageModel;
import de.hybris.platform.cms2.model.site.CMSSiteModel;
import de.hybris.platform.cms2.servicelayer.daos.CMSPageDao;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;
import de.hybris.platform.commerceservices.search.pagedata.SearchPageData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.List;

import com.tisl.mpl.core.enums.CMSChannel;
import com.tisl.mpl.core.model.AmpMenifestModel;
import com.tisl.mpl.core.model.AmpServiceworkerModel;
import com.tisl.mpl.core.model.BrandComponentModel;
import com.tisl.mpl.core.model.MplFooterLinkModel;
import com.tisl.mpl.core.model.MplShopByLookModel;
import com.tisl.mpl.model.SellerMasterModel;


/**
 * @author TCS
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

	//SearchPageData<ContentSlotForPageModel> getContentSlotsForAppById(String pageUid, PageableData pageableData);

	//Added for TPR-798
	public SearchPageData<ContentSlotForPageModel> getContentSlotsForAppById(final String pageUid, final PageableData pageableData);

	//Added for TPR-978
	ContentPageModel getContentPageForProduct(ProductModel product);

	//TPR-1072
	public List<BrandComponentModel> getBrandsForShopByBrand();

	/**
	 * @param siteUid
	 * @return CMSSite
	 * @CAR-285
	 */
	public CMSSiteModel getSiteforId(final String siteUid) throws CMSItemNotFoundException;

	/**
	 * TPR-5733
	 *
	 * @return List<MplFooterLinkModel>
	 */
	List<MplFooterLinkModel> getAllFooterLinks();

	/**
	 * @return List<AmpServiceworkerModel>
	 */
	List<AmpServiceworkerModel> getAllAmpServiceworkers();

	/**
	 * @return List<AmpServiceworkerModel>
	 */
	List<AmpMenifestModel> getAllAmpMenifestJsons();
}
