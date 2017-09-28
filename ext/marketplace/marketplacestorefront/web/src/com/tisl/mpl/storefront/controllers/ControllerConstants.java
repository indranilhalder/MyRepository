/*
 * [y] hybris Platform
 *
 * Copyright (c) 2000-2015 hybris AG
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of hybris
 * ("Confidential Information"). You shall not disclose such Confidential
 * Information and shall use it only in accordance with the terms of the
 * license agreement you entered into with hybris.
 *
 *
 */
package com.tisl.mpl.storefront.controllers;

import de.hybris.platform.acceleratorcms.model.components.CartSuggestionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.CategoryFeatureComponentModel;
import de.hybris.platform.acceleratorcms.model.components.DynamicBannerComponentModel;
import de.hybris.platform.acceleratorcms.model.components.MiniCartComponentModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarCollectionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.NavigationBarComponentModel;
import de.hybris.platform.acceleratorcms.model.components.ProductFeatureComponentModel;
import de.hybris.platform.acceleratorcms.model.components.ProductReferencesComponentModel;
import de.hybris.platform.acceleratorcms.model.components.PurchasedCategorySuggestionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleResponsiveBannerComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SubCategoryListComponentModel;
import de.hybris.platform.cms2.model.contents.components.CMSLinkComponentModel;
import de.hybris.platform.cms2lib.model.components.ProductCarouselComponentModel;
import de.hybris.platform.cms2lib.model.components.RotatingImagesComponentModel;

import com.tisl.mpl.core.model.BrandCollectionComponentModel;
import com.tisl.mpl.core.model.BrandComponentModel;
import com.tisl.mpl.core.model.ConfigureImagesCountComponentModel;
import com.tisl.mpl.core.model.ConfigureProductsCountComponentModel;
import com.tisl.mpl.core.model.CustomSkuComponentModel;
import com.tisl.mpl.core.model.DepartmentCollectionComponentModel;
import com.tisl.mpl.core.model.LuxProductCarouselComponentModel;
import com.tisl.mpl.core.model.MicrositeSelectBrandCollectionComponentModel;
import com.tisl.mpl.core.model.MplBigFourPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplBigPromoBannerComponentModel;
import com.tisl.mpl.core.model.MplBrandLogoComponentModel;
import com.tisl.mpl.core.model.MplBrandSearchBoxComponentModel;
import com.tisl.mpl.core.model.MplEnhancedSearchBoxComponentModel;
import com.tisl.mpl.core.model.MplMicrositeEnhancedSearchBoxComponentModel;
import com.tisl.mpl.core.model.MplNowTrendingProductCarouselComponentModel;
import com.tisl.mpl.core.model.MplShopByLookProductCarouselComponentModel;
import com.tisl.mpl.core.model.PremierBrandComponentModel;
import com.tisl.mpl.model.cms.components.ContactUsTabComponentModel;
import com.tisl.mpl.model.cms.components.HelpMeShopSearchComponentModel;
import com.tisl.mpl.model.cms.components.MplSequentialBannerComponentModel;
import com.tisl.mpl.model.cms.components.MyWishListInHeaderComponentModel;
import com.tisl.mpl.model.cms.components.NeedHelpComponentModel;
import com.tisl.mpl.model.cms.components.PromotionalProductsComponentModel;
import com.tisl.mpl.model.cms.components.SignInFlyOutComponentModel;
import com.tisl.mpl.model.cms.components.TrackOrderHeaderComponentModel;



public interface ControllerConstants
{
	/**
	 * Class with action name constants
	 */
	String LUXURY_STORE_ADDON_PREFIX = "addon:/luxurystoreaddon";

	interface Actions
	{
		interface Cms
		{
			String _Prefix = "/view/";
			String _Suffix = "Controller";

			/**
			 * Default CMS component controller
			 */
			String DefaultCMSComponent = _Prefix + "DefaultCMSComponentController";

			/**
			 * CMS components that have specific handlers
			 */
			String PurchasedCategorySuggestionComponent = _Prefix + PurchasedCategorySuggestionComponentModel._TYPECODE + _Suffix;
			String CartSuggestionComponent = _Prefix + CartSuggestionComponentModel._TYPECODE + _Suffix;
			String ProductReferencesComponent = _Prefix + ProductReferencesComponentModel._TYPECODE + _Suffix;
			String ProductCarouselComponent = _Prefix + ProductCarouselComponentModel._TYPECODE + _Suffix;
			String LuxProductCarouselComponent = _Prefix + LuxProductCarouselComponentModel._TYPECODE + _Suffix;
			String MiniCartComponent = _Prefix + MiniCartComponentModel._TYPECODE + _Suffix;
			String ProductFeatureComponent = _Prefix + ProductFeatureComponentModel._TYPECODE + _Suffix;
			String CategoryFeatureComponent = _Prefix + CategoryFeatureComponentModel._TYPECODE + _Suffix;
			String NavigationBarComponent = _Prefix + NavigationBarComponentModel._TYPECODE + _Suffix;
			String CMSLinkComponent = _Prefix + CMSLinkComponentModel._TYPECODE + _Suffix;
			String DynamicBannerComponent = _Prefix + DynamicBannerComponentModel._TYPECODE + _Suffix;
			String SubCategoryListComponent = _Prefix + SubCategoryListComponentModel._TYPECODE + _Suffix;
			String SimpleResponsiveBannerComponent = _Prefix + SimpleResponsiveBannerComponentModel._TYPECODE + _Suffix;
			String ConfigureImagesCountComponent = _Prefix + ConfigureImagesCountComponentModel._TYPECODE + _Suffix;
			String ConfigureProductsCountComponent = _Prefix + ConfigureProductsCountComponentModel._TYPECODE + _Suffix;
			String MplNowTrendingProductCarouselComponent = _Prefix + MplNowTrendingProductCarouselComponentModel._TYPECODE
					+ _Suffix;
			String MplSequentialBannerComponent = _Prefix + MplSequentialBannerComponentModel._TYPECODE + _Suffix;
			String TrackOrderHeaderComponent = _Prefix + TrackOrderHeaderComponentModel._TYPECODE + _Suffix;
			String MyWishListInHeaderComponent = _Prefix + MyWishListInHeaderComponentModel._TYPECODE + _Suffix;
			String HelpMeShopSearchComponent = _Prefix + HelpMeShopSearchComponentModel._TYPECODE + _Suffix;
			String NeedHelpComponent = _Prefix + NeedHelpComponentModel._TYPECODE + _Suffix;
			String SignInFlyOutComponent = _Prefix + SignInFlyOutComponentModel._TYPECODE + _Suffix;
			String MplEnhancedSearchBoxComponent = _Prefix + MplEnhancedSearchBoxComponentModel._TYPECODE + _Suffix;
			String MplMicrositeEnhancedSearchBoxComponent = _Prefix + MplMicrositeEnhancedSearchBoxComponentModel._TYPECODE
					+ _Suffix;
			String MplBrandSearchBoxComponent = _Prefix + MplBrandSearchBoxComponentModel._TYPECODE + _Suffix;

			String IMAGE_COUNT = "count";
			String PRODUCT_COUNT = "qtycount";
			String BrandCollectionComponent = _Prefix + BrandCollectionComponentModel._TYPECODE + _Suffix;
			String MicrositeSelectBrandCollectionComponent = _Prefix + MicrositeSelectBrandCollectionComponentModel._TYPECODE
					+ _Suffix;
			String BrandComponent = _Prefix + BrandComponentModel._TYPECODE + _Suffix;
			String DepartmentCollectionComponent = _Prefix + DepartmentCollectionComponentModel._TYPECODE + _Suffix;
			String PremierBrandComponent = _Prefix + PremierBrandComponentModel._TYPECODE + _Suffix;
			String MplBigPromoBannerComponent = _Prefix + MplBigPromoBannerComponentModel._TYPECODE + _Suffix;
			String MplBigFourPromoBannerComponent = _Prefix + MplBigFourPromoBannerComponentModel._TYPECODE + _Suffix;
			String MplShopByLookProductCarouselComponent = _Prefix + MplShopByLookProductCarouselComponentModel._TYPECODE + _Suffix;
			String PromotionalProductsComponent = _Prefix + PromotionalProductsComponentModel._TYPECODE + _Suffix;
			String ContactUsTabComponent = _Prefix + ContactUsTabComponentModel._TYPECODE + _Suffix;
			String MplBrandLogoComponent = _Prefix + MplBrandLogoComponentModel._TYPECODE + _Suffix;
			String RotatingImagesComponent = _Prefix + RotatingImagesComponentModel._TYPECODE + _Suffix;
			String CustomSkuComponent = _Prefix + CustomSkuComponentModel._TYPECODE + _Suffix;
			String NavigationBarCollectionComponent = _Prefix + NavigationBarCollectionComponentModel._TYPECODE + _Suffix;
		}
	}

	/**
	 * Class with view name constants
	 */
	interface Views
	{
		interface Cms
		{
			String ComponentPrefix = "cms/";
			String ShopByDepartment = "/navigationbarcollectioncomponent";
		}

		interface responsive
		{
			interface Account
			{
				String AccountLoginPage = "pages/account/accountLoginPage";
				String AccountHomePage = "pages/account/accountHomePage";
				String AccountOrderHistoryPage = "pages/account/accountOrderHistoryPage";
				String AccountOrderPage = "pages/account/accountOrderPage";
				String AccountProfilePage = "pages/account/accountProfilePage";
				String AccountProfileEditPage = "pages/account/accountProfileEditPage";
				String AccountProfileEmailEditPage = "pages/account/accountProfileEmailEditPage";
				String AccountChangePasswordPage = "pages/account/accountChangePasswordPage";
				String AccountAddressBookPage = "pages/account/accountAddressBookPage";
				String AccountEditAddressPage = "pages/account/accountEditAddressPage";
				String AccountPaymentInfoPage = "pages/account/accountPaymentInfoPage";
				String AccountRegisterPage = "pages/account/accountRegisterPage";
				String ChangePassword = "/my-account/update-password";


			}

			interface Oauth2callback
			{
				String oauth2callback = "pages/oauth2callback/oauth2callbackPage";
			}

			interface Error
			{
				String ErrorNotFoundPage = "pages/error/errorNotFoundPage";
				String CustomEtailBusinessErrorPage = "pages/error/customEtailBusinessError";
				String CustomEtailNonBusinessErrorPage = "pages/error/customEtailNonBusinessError";
			}
		}

		interface Pages
		{
			interface Account
			{
				String AccountLoginPage = "pages/account/accountLoginPage";
				String AccountLoginPopPage = "pages/account/accountLoginPopPage";
				String AccountHomePage = "pages/account/accountHomePage";
				String AccountOrderHistoryPage = "pages/account/accountOrderHistoryPage";
				String AccountOrderPage = "pages/account/accountOrderPage";
				String AccountProfilePage = "pages/account/accountProfilePage";
				String AccountProfileEditPage = "pages/account/accountProfileEditPage";
				String AccountProfileEmailEditPage = "pages/account/accountProfileEmailEditPage";
				String AccountChangePasswordPage = "pages/account/accountChangePasswordPage";
				String AccountAddressBookPage = "pages/account/accountAddressBookPage";
				String AccountEditAddressPage = "pages/account/accountEditAddressPage";
				String AccountPaymentInfoPage = "pages/account/accountPaymentInfoPage";
				String AccountRegisterPage = "pages/account/accountRegisterPage";
				String AccountReturnReqPage = "pages/account/returnRequest";
				String AccountReturnSubPage = "pages/account/returnSubmit";
				String AccountReturnSuccessPage = "pages/account/returnSuccess";
				String AccountMplPreferencePage = "pages/account/accountMarketPreference";
				String AccountMyInterestPage = "pages/account/myInterest";
				String AccountMyStyleProfilePage = "pages/account/myStyleProfile";
				String AccountInviteFriendPage = "pages/account/inviteFriends";
				String AccountCouponsPage = "pages/account/accountCouponDetailsPage";
				String AccountOrderReturnPincodeServiceCheck = "pages/account/returnPincodeAvailabilityCheck";
				String ScheduledDeliveryDate = "pages/account/scheduledDeliveryDate";
				String OTPPopup = "pages/account/otpPopup";
			}


			interface Checkout
			{
				String CheckoutRegisterPage = "pages/checkout/checkoutRegisterPage";
				String CheckoutConfirmationPage = "pages/checkout/checkoutConfirmationPage";
				String CheckoutLoginPage = "pages/checkout/checkoutLoginPage";
			}

			interface Password
			{
				String PasswordResetChangePage = "pages/password/passwordResetChangePage";
				String PasswordResetRequest = "pages/password/passwordResetRequestPage";
				String PasswordResetRequestConfirmation = "pages/password/passwordResetRequestConfirmationPage";
			}

			interface Oauth2callback
			{
				String oauth2callback = "pages/oauth2callback/oauth2callbackPage";
			}

			interface Error
			{
				String ErrorNotFoundPage = "pages/error/errorNotFoundPage";
				String CustomEtailBusinessErrorPage = "pages/error/customEtailBusinessError";
				String CustomEtailNonBusinessErrorPage = "pages/error/customEtailNonBusinessError";
			}

			interface Cart
			{
				String CartPage = "pages/cart/cartPage";
				String LUX_INDICATOR = "luxury";
				String EMPTY_CART = "emptyCart";
			}

			interface StoreFinder
			{
				String StoreFinderSearchPage = "pages/storeFinder/storeFinderSearchPage";
				String StoreFinderDetailsPage = "pages/storeFinder/storeFinderDetailsPage";
				String StoreFinderViewMapPage = "pages/storeFinder/storeFinderViewMapPage";
			}

			interface Misc
			{
				String MiscRobotsPage = "pages/misc/miscRobotsPage";
				String MiscSiteMapPage = "pages/misc/miscSiteMapPage";
				String MiscIndividualSiteMapPage = "pages/misc/miscIndividualSiteMapPage";
				//Changes for TPR-5812
				String manifest = "pages/misc/manifest";
				String serviceWorker = "pages/misc/service-worker";
			}

			interface Guest
			{
				String GuestOrderPage = "pages/guest/guestOrderPage";
				String GuestOrderErrorPage = "pages/guest/guestOrderErrorPage";
			}

			interface Product
			{
				String WriteReview = "pages/product/writeReview";

			}

			interface ClickToChatCall
			{
				String chatPage = "pages/clickToChatCall/clickToChat";
				String callPage = "pages/clickToChatCall/clickToCall";
			}

			interface Order
			{
				String TrackOrderDetailsPage = "pages/order/trackOrderDetailsPage";
			}

			interface Search
			{
				// TPR-198
				String FacetResultPanel = "pages/search/facetResultPanel";

			}

			interface Pancard
			{

				String PanCardDetail = "pages/pancard/pancardupload";
				String panCardUploadSuccess = "pages/pancard/pancarduploadsuccess";
				String panCardUploadUpdate = "pages/pancard/pancarduploadupdate";
				String panCardApproved = "pages/pancard/pancardapproved";
				String panCardUploadDetailsError = "pages/pancard/pancarduploaddetailserror";



			}

		}

		interface Fragments
		{
			interface Home
			{
				String DepartmentCollection = "fragments/home/departmentCollectionPanel";
				String AtoZBrand = "fragments/home/atozBrandPanel";
				String HelpMeShop = "fragments/home/helpmeShopPanel";
				String TrackOrderPanel = "fragments/home/trackOrderPanel";
				String WishlistPanel = "fragments/home/wishlistPanel";
				String LoginPanel = "fragments/home/loginPanel";
				String MyAccountPanel = "fragments/home/myaccountPanel";
				String LatestOffers = "fragments/home/latestOffersPanel";
				String ShopByBrandImagesPanel = "fragments/home/shopByBrandImagesPanel";
				String FooterPanel = "fragments/home/footerPanel";
			}

			interface LuxuryHome
			{
				String RegisterFragment = LUXURY_STORE_ADDON_PREFIX + "/fragments/home/luxRegister";
				String LoginFragment = LUXURY_STORE_ADDON_PREFIX + "/fragments/home/luxLoginPanel";
				String ForgotPasswordPanel = LUXURY_STORE_ADDON_PREFIX + "/fragments/home/forgottenPwd";

			}

			interface Cart
			{
				String AddToCartPopup = "fragments/cart/addToCartPopup";
				String MiniCartPanel = "fragments/cart/miniCartPanel";
				String MiniCartErrorPanel = "fragments/cart/miniCartErrorPanel";
				String CartPopup = "fragments/cart/cartPopup";
				String GiftList = "fragments/cart/cartGiftList";
				public static final String pincode = "pincode";
			}

			interface Account
			{
				String CountryAddressForm = "fragments/address/countryAddressForm";
				String WishlistName = "wishlistName";
				String WishlistSize = "wishlistSize";
				String WishlistUrl = "wishlistUrl";
				String EMPTY_WISHLIST = "emptyWishlist";
			}

			interface Checkout
			{
				String TermsAndConditionsPopup = "acceleratoraddon/web/webroot/WEB-INF/views/mobile/fragments/checkout/termsAndConditionsPopup";
				String BillingAddressForm = "acceleratoraddon/web/webroot/WEB-INF/views/mobile/fragments/checkout/billingAddressForm";
			}

			interface Password
			{
				String PasswordResetRequestPopup = "fragments/password/passwordResetRequestPopup";
				String ForgotPasswordValidationMessage = "fragments/password/forgotPasswordValidationMessage";
			}

			interface Product
			{

				String SellerView = "pages/layout/sellersDetailPage";
				String QuickViewPopup = "fragments/product/quickViewPopup";
				String ZoomImagesPopup = "fragments/product/zoomImagesPopup";
				String SizeGuidePopup = "fragments/product/sizeGuidePopup";
				String ReviewsTab = "fragments/product/reviewsTab";
				String StorePickupSearchResults = "fragments/product/storePickupSearchResults";
				String SELLER_ID = "sellerId";
				String PINCODE_SERVICABILITY = "isPinCodeServicable";
				public static final String MIN_PRICE = "minPrice";
				public static final String FULFILLMENT = "fulfillment";
				public static final String OTHERS_SELLERS_COUNT = "othersSellersCount";
				public static final String AVAILABLESTOCK = "availablestock";
				public static final String SELLER_ARTICLE_SKU = "sellerArticleSKU";
				public static final String SELLER_NAME = "sellerName";
				public static final String PRICE = "price";
				public static final String MRP = "mrp";
				public static final String SPECIAL_PRICE = "specialPrice";
				public static final String DELIVERY_MODE_MAP = "deliveryModeMap";
				public static final String N = "N";
				public static final String OTHERSELLERS_PAGE_LIMIT = "othersellers.pageLimit";
				public static final String VIEW_SELLERS = "viewSellers";
				public static final String PRODUCT_CODE = "productCode";
				public static final String SIZESELECTED = "sizeSelected";
				public static final String BUYBOXUSSID = "buyboxussid";
				public static final String ALLSELLERS = "allsellers";
				public static final String PAGE_LIMIT = "pageLimit";
				public static final String COD = "COD";
				public static final String S_S = "\\s*,\\s*";
				public static final String DELIVERY_MODES = "deliveryModes";
				public static final String CHECK_PINCODE = "-checkPincode";
				public static final String SIZE_GUIDE = "-sizeGuide";
				public static final String NO_SELLERS = "No Sellers Defined For This Product";
				public static final String NO_PRODUCT = "Selected Size is not available for this Seller";
				public static final String PRODUCT_CODE_PATH_VARIABLE_PATTERN = "/{productCode:.*}";
				public static final String PRODUCT_CODE_PATH_NEW_PATTERN = "/**/p-{productCode:.*}";
				public static final String REVIEWS_PATH_VARIABLE_PATTERN = "{numberOfReviews:.*}";
				public static final String IS_COD = "isCod";
				public static final String ALL_OF_STOCK = "allOOStock";
				public static final String BUYBOZFORSIZEGUIDEAJAX = "-buyboxDataForSizeGuide";


				//for price breakup(TPR-3752)
				public static final String PRICE_BREAKUP = "priceBreakup";
				public static final String JEWEL_DESCRIPTION = "jewelDescription";
				/*
				 * <!-- //TPR-3752 Jewel Heading Added -->
				 */public static final String DISPLAYCONFIGATTR = "displayconfigattr";

				//ends


				//TISPRM-56
				public static final String AVAILABILITY = "availibility";

				//TISPRM-33
				public static final String SAVINGONPRODUCT = "savingsOnProduct";

				//TPR-198
				public static final String SearchResultsPanel = "fragments/product/searchResultPanel";
				public static final String SellerResultsPage = "fragments/product/sellerResultPage";

				public static final String OFFERMESSAGEMAP = "offerMessageMap";
				//UF-60
				public static final String AJAXPRODUCTDATA = "fragments/product/productPromotionSection";


				//Addition for Exchange Start
				String ExchangeGuidePopup = "fragments/product/exchangeGuidePopup";
				public static final String EXCHANGE = "-exchange";
				public static final String L3CATEGORY = "l3code";
				public static final String L3CATEGORYNAME = "l3name";
				public static final String GENARATEEXCHANGEID = "-generateExchangeId";
				public static final String PINCODE = "pincode";
				public static final String L4NAME = "l4name";
				public static final String BRAND = "brand";
				public static final String ISWORKING = "isWorking";
				public static final String COUPONVALUE = "couponValue";
				public static final String CHECK_REVERSE_PINCODE = "-checkReversePincode";
				//Addition for Exchange Ends



				public static final String USSID_CODE_PATH_NEW_PATTERN = "/**/p-{ussId:.*}";
				public static final String USSID = "ussId";
				public static final String ID_ED_SELLER_HANDLING_TIME = "isEDSeller";
				//TPR-6654
				public static final String STORE = "-allStores/{pincode}";
				String StoreLocatorPopup = "fragments/product/storeLocatorPopup";
				public static final String STORE_AVAIL = "storesAvailable";
			}


			interface NPS_Emailer
			{
				String NPSFeedback = "fragments/NPS_Emailer/npsFeedbackPage";
				String SubmitSuccess = "fragments/NPS_Emailer/submitSuccess";
				String NpsFeedbackExists = "fragments/NPS_Emailer/npsFeedbackExists";

			}

		}
	}


}
