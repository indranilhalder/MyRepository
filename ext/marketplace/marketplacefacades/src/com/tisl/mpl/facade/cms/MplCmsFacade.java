/**
 *
 */
package com.tisl.mpl.facade.cms;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;
import de.hybris.platform.commerceservices.search.pagedata.PageableData;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.tisl.mpl.facades.cms.data.AmpMenifestData;
import com.tisl.mpl.facades.cms.data.AmpServiceWorkerData;
import com.tisl.mpl.facades.cms.data.CollectionPageData;
import com.tisl.mpl.facades.cms.data.FooterLinkData;
import com.tisl.mpl.facades.cms.data.HeroProductData;
import com.tisl.mpl.facades.cms.data.MplPageData;
import com.tisl.mpl.facades.cms.data.PageData;
import com.tisl.mpl.facades.data.FooterComponentData;
import com.tisl.mpl.wsdto.LuxBlpCompWsDTO;
import com.tisl.mpl.wsdto.LuxHomePageCompWsDTO;
import com.tisl.mpl.wsdto.LuxNavigationWsDTO;



/**
 * @author TCS
 *
 */
public interface MplCmsFacade
{

	/**
	 * @param categoryCode
	 * @return
	 * @throws CMSItemNotFoundException
	 * @throws NullPointerException
	 */
	PageData getCategoryLandingPageForMobile(String categoryCode) throws CMSItemNotFoundException, NullPointerException;

	MplPageData getHomePageForMobile();

	LuxHomePageCompWsDTO getHomePageForLuxury() throws CMSItemNotFoundException;

	PageData populateCategoryLandingPageForMobile(ContentPageModel contentPage, String categoryCode);

	PageData populateSubBrandLandingPageForMobile(ContentPageModel contentPage, String categoryCode);

	PageData populatePageType(String categoryCode, boolean isSubBrand, String pagetype);

	/**
	 * @param categoryCode
	 * @return
	 * @throws CMSItemNotFoundException
	 * @throws NullPointerException
	 */
	CollectionPageData populateCollectionPage(String categoryCode, HttpServletRequest request) throws CMSItemNotFoundException,
			NullPointerException;

	String getCategoryNameForCode(String categoryId);

	HeroProductData getHeroProducts(String categoryId);

	PageData populateSellerLandingPageForMobile(ContentPageModel contentPage, String sellerId);

	PageData populateSellerPageType(String sellerId, String PageType);

	String getSellerMasterName(String sellerId);

	PageData getSellerLandingPageForMobile(String sellerId) throws CMSItemNotFoundException, NullPointerException;

	PageData populateOfferPageType(String offerId, String PageType);

	/**
	 * Modified for TPR-798
	 *
	 * @param homePageUid
	 * @param pageableData
	 * @return
	 */

	List<MplPageData> getPageInformationForPageId(String homePageUid, final PageableData pageableData);


	/**
	 * @return
	 */
	LuxBlpCompWsDTO getlandingForBrand(String brandCode) throws CMSItemNotFoundException;

	/**
	 * @return
	 */
	LuxNavigationWsDTO getMegaNavigation() throws CMSItemNotFoundException;

	/**
	 * @param slotId
	 * @return
	 */
	FooterComponentData getContentSlotData(String slotId);

	/**
	 * TPR-5733
	 *
	 * @return List<List<FooterLinkData>>
	 */
	Map<Integer, Map<Integer, FooterLinkData>> getFooterLinkData();

	/**
	 * @return AmpServiceWorkerData
	 */
	AmpServiceWorkerData getAmpServiceWorkerData();

	/**
	 * @return AmpMenifestData
	 */
	AmpMenifestData getAmpMenifestData();
}
