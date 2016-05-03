/**
 *
 */
package com.tisl.mpl.storefront.constants;

/**
 * @author TCS
 *
 */
public final class RequestMappingUrlConstants
{
	private RequestMappingUrlConstants()
	{
		//empty to avoid instantiating this constant class
	}

	//Request Mapping
	public static final String REDIRECT_PREFIX = "redirect:";
	public static final String FORWARD_PREFIX = "forward:";
	public static final String ROOT = "/";
	public static final String TENANT = "tenant";
	public static final String LINK_404 = "/404";

	public static final String LINK_MY_ACCOUNT = "/my-account";
	public static final String LINK_ADDRESS_FORM = "/addressform";
	public static final String LINK_ORDERS = "/orders";
	public static final String LINK_ORDER = "/order/";
	public static final String LINK_INVOICE = "/order/requestInvoice";
	public static final String LINK_PROFILE = "/profile";
	public static final String LINK_COUPONS = "/coupons";
	public static final String UPDATE_PICKUP_DETAILS = "/updatepickUp_Details";
	public static final String CREATE_TICKET_CRM_UPDATE_PICKUP_DETAILS = "crmTicketCreateUpdatePickUpDetail";


	public static final String LINK_UPDATE_EMAIL = "/update-email";
	public static final String LINK_UPDATE_PROFILE = "/update-profile";
	public static final String LINK_UPDATE_PARSONAL_DETAIL = "/update-parsonal-detail";
	public static final String LINK_UPDATE_NICK_NAME = "/update-nickName";
	public static final String LINK_UPDATE_PASSWORD = "/update-password";
	public static final String LINK_UPDATE_PASSWORD_AJAX = "/updateAccountPassword";
	public static final String LINK_ADDRESS_BOOK = "/address-book";
	public static final String LINK_ADD_ADDRESS = "/add-address";
	public static final String LINK_ADD_NEW_ADDRESS = "/addNewAddress";
	public static final String LINK_EDIT_ADDRESS = "/edit-address/";
	public static final String LINK_EDIT_ADDRESS_NEW = "/editAddress";
	public static final String LINK_SELECT_SUGGESTED_ADDRESS = "/select-suggested-address";
	public static final String LINK_REMOVE_ADDRESS = "/remove-address/";
	public static final String LINK_SET_DEFAUT_ADDRESS = "/set-default-address/";
	public static final String LINK_PAYMENT_DETAILS = "/payment-details";
	public static final String LINK_SET_DEFAUT_PAYMENT_DETAILS = "/set-default-payment-details";
	public static final String LINK_REMOVE_PAYMENT_METHOD = "/remove-payment-method";
	public static final String LINK_WISHLIST = "/wishList";
	public static final String LINK_WISHLISTS_NEW = "/wishlists";
	public static final String LINK_CREATE_NEW_WISHLIST = "/createNewWishlist";
	public static final String LINK_CREATE_NEW_WISHLIST_WP = "/createNewWishlistWP";
	public static final String LINK_ADD_IN_EXISTING_WISHLIST = "/addInExistingWishlist";
	public static final String LINK_VIEW_PARTICULAR_WISHLIST = "/viewParticularWishlist";
	public static final String LINK_EDIT_PARTICULAR_WISHLIST_NAME = "/editParticularWishlistName";
	public static final String LINK_WISHLIST_REMOVE = "/wishList/remove";
	public static final String LINK_ADD_TO_WISHLIST = "/addToWishList";
	public static final String LINK_WISHLIST_DELETE = "/deleteWishlist";
	public static final String WISHLIST_PRODUCT_REMOVE = "/productFromWishlist/remove";
	public static final String LINK_WISHLIST_HOME_PAGE = "/wishList";
	public static final String LINK_WISHLIST_PAGE = "/default/wishList";
	public static final String LINK_WISHLIST_FLYOUT = "/wishlistAndItsItems";
	public static final String ADD_TO_BAG_FROM_WL = "/addToBagFromWl";

	public static final String LINK_LOGIN = "/login";
	public static final String LINK_FLYLOGIN = "/flylogin";
	public static final String LINK_REGISTER = "/register";
	public static final String LINK_SOCIALLOGIN = "/sociallogin";
	public static final String LINK_AUTHENTIC_AND_EXCLUSIVE = "/authenticandexclusive";
	public static final String LINK_OAUTH2_CALLBACK = "/oauth2callback";
	public static final String LINK_FRIENDS_INVITE = "/friendsInvite";
	public static final String LINK_INVITE_FRIENDS = "/inviteFriends";

	public static final String VIEW_WISHLISTS_IN_POPUP = "/viewWishlistsInPDP";
	public static final String ADD_WISHLIST_IN_POPUP = "/addToWishListInPDP";
	public static final String LINK_TERMS_N_CONDITIONS = "/termsConditionMpl";
	public static final String LINK_CHECKOUT = "/checkout";

	public static final String LINK_LOGIN_PW = "/login/pw";
	public static final String LINK_REQUEST_CONFIRMEMAIL = "/request/confirmEmail";
	public static final String LINK_REQUEST_CONFIRMEMAIL_SMS = "/request/confirmEmail/sms";
	public static final String LINK_REQUEST = "/request";
	public static final String LINK_REQUEST_EXTERNAL = "/request/external";
	public static final String LINK_REQUEST_EXTERNAL_CONF = "/request/external/conf";
	public static final String LINK_OTPVALIDATION = "/OTPValidation";
	public static final String LINK_PASSWORDOTP = " /passwordOTP";
	public static final String LINK_CHANGE = "/change";
	public static final String LINK_PW_PASSWORDEMAIL = "/pw/passwordEmail";
	public static final String LINK_POPULATE_ADDRESS_DETAIL_AJAX = "/populateAddressDetail";
	public static final String LINK_CLICK_TO_CHAT_CALL = "/clickto";
	public static final String LINK_RETURN_REQUEST = "/returnRequest";
	public static final String LINK_MARKETPLACE_PREFERENCES = "/marketplace-preference";
	public static final String LINK_SAVE_MPLPREFERENCES = "/saveMplPreferences";
	public static final String LINK_UNSUBSCRIBE_MPLPREFERENCES = "/unsubscribeMplPreferences";
	public static final String LINK_ORDER_RETURN_REASON = "/order/returnReplace";
	public static final String LINK_ORDER_RETURN_SUBMIT = "/order/submitReturnRequest";
	public static final String LINK_ORDER_RETURN_SUCCESS = "/returnSuccess";
	public static final String LINK_ORDER_CANCEL_SUCCESS = "/cancelSuccess";
	public static final String LINK_CHECK_CURRENT_PASSWORD = "/checkCurrentPassword";

	//Retun Item
	public static final String LINK_ORDER_RETURN_PINCODE_CHECK = "/order/returnPincodeCheck";
	public static final String LINK_ORDER_RETURN_PINCODE_SUBMIT = "/order/returnPincodeSubmit";

	/* For Lets Get Personal */
	public static final String MY_INTEREST = "/myInterest";
	public static final String MY_STYLE_PROFILE = "/myStyleProfile";

	public static final String MY_INTEREST_GENDER = "/myInterest/gender";
	public static final String MY_INTEREST_BRANDS = "/myInterest/brands";
	public static final String MY_INTEREST_SUBCATEGORIES = "/myInterest/subcategories";
	public static final String MY_INTEREST_SEL_SUBCATEGORIES = "/myInterest/mySubCategories";
	public static final String MY_INTEREST_REMOVE_PROFILE = "/myInterest/removePorfile";
	public static final String MY_INTEREST_REMOVE_BRAND = "/myInterest/removeBrand";
	public static final String MY_INTEREST_REMOVE_CATEGORY = "/myInterest/removeCategory";
	public static final String MY_INTEREST_MODIFY_BRAND = "/myInterest/modifyBrand";
	public static final String MY_INTEREST_MODIFY_CATEGORY = "/myInterest/modifyCategory";
	public static final String AWB_STATUS_URL = "/AWBStatusURL";
	public static final String ORDER_PLACED_EMAIL = "/store/mpl/en/my-account/order";


	//RatingReview
	public final static String CHECKUSER = "/checkUser";
	public final static String REVIEWS = "/reviews";
	public final static String REVIEW_OPERATION = "/review/{operation}";

	//Checkout
	public final static String LOGIN_CHECKOUT = "/login/checkout";
	public final static String REGISTER_PAGE = "/registerPage";

	public static final String DEPARTMENT_COLLECTION = "/departmentCollection";

	public static final String A_Z_BRANDS = "/atozbrands";

	public static final String HEADER_TRACK_ORDER = "/headerTrackOrder";
	public static final String HEADER_WISHLIST = "/headerWishlist";

	public static final String HELP_ME_SHOP_SEARCH = "/helpmeshop";

	public static final String TRANSIENTCARTAJAX = "/cart/showTransientCart";
	public static final String SITEMAP = "/site-map";

}
