/**
 *
 */
package com.tisl.mpl.facade.cms;

import de.hybris.platform.cms2.exceptions.CMSItemNotFoundException;
import de.hybris.platform.cms2.model.pages.ContentPageModel;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.tisl.mpl.facades.cms.data.CollectionPageData;
import com.tisl.mpl.facades.cms.data.HeroProductData;
import com.tisl.mpl.facades.cms.data.MplPageData;
import com.tisl.mpl.facades.cms.data.PageData;
import com.tisl.mpl.wsdto.LuxHomePageCompWsDTO;



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
	 * @param homePageUid
	 * @return
	 */
	List<MplPageData> getPageInformationForPageId(String homePageUid);
}
