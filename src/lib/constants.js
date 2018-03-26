export const SUCCESS = "success";
export const SUCCESS_UPPERCASE = "SUCCESS";
export const SUCCESS_CAMEL_CASE = "Success";
export const REQUESTING = "requesting";
export const ERROR = "error";
export const FAILURE = "Failure";
export const FAILURE_UPPERCASE = "FAILURE";
export const SINGLE_SELECT_HEADING_COPY = "Thanks!!!";
export const SINGLE_SELECT_DESCRIPTION_COPY =
  "We will curate the experience based on your choices. Loading products...";

export const MULTI_SELECT_HEADING_COPY = "Thanks!!!";
export const MULTI_SELECT_DESCRIPTION_COPY =
  "We will curate the experience based on your choices. Loading products...";
export const MOBILE_PDP_VIEW = "mobilePdpView";
export const MAIN_ROUTER = "/";
export const CUSTOMER_ACCESS_TOKEN = "customerAccessToken";
export const GLOBAL_ACCESS_TOKEN = "globalAccessToken";
export const DEFAULT_PIN_CODE_LOCAL_STORAGE = "defaultPinCode";

export const REFRESH_TOKEN = "refresh_token";
export const BRAND_OR_CATEGORY_LANDING_PAGE =
  "/:brandOrCategory/:brandOrCategoryId";
export const BRAND_PAGE = "/c-(mbh.*)";
export const BRAND_PAGE_WITH_SLUG = `/.*/${BRAND_PAGE}`;

export const CATEGORY_PAGE = "/c-(msh.*)";
export const CATEGORY_PAGE_WITH_SLUG = `/:slug/c-(msh.*)`;
export const PRODUCT_LISTINGS = "/search/(.*)";

export const PLP_CATEGORY_SEARCH = "/search/?searchCategory=all&text=shirt";

export const SEARCH_RESULTS_PAGE = "/search/";
export const HOME_ROUTER = "/";

export const BRAND_LANDING_PAGE = "/brand";

// USE THESE
export const PRODUCT_DESCRIPTION_PRODUCT_CODE = "(.*)/p-(.*)";
export const PRODUCT_DESCRIPTION_REVIEWS = `${PRODUCT_DESCRIPTION_PRODUCT_CODE}/product-reviews`;
export const PRODUCT_OTHER_SELLER_ROUTER = `${PRODUCT_DESCRIPTION_PRODUCT_CODE}/viewSellers`;

export const PRODUCT_DESCRIPTION_SLUG_PRODUCT_CODE = "/(.*)/(p-)(.*)";
export const PRODUCT_REVIEWS_PATH_SUFFIX = "/product-reviews";
export const PRODUCT_DESCRIPTION_ROUTER = PRODUCT_DESCRIPTION_PRODUCT_CODE; //TODO remove this
export const PRODUCT_REVIEW_ROUTER = "/productReview";
export const LOGIN_PATH = "/login";
export const SIGN_UP_PATH = "/sign_up";
export const PRODUCT_FILTER_ROUTER = "/filter";
export const PRODUCT_SELLER_ROUTER_SUFFIX = "/viewSellers";
export const PRODUCT_CART_ROUTER = "/cart";
export const ORDER_SUMMARY_ROUTER = "/orderSummary";
export const CHECKOUT_ROUTER = "/checkout";
export const ACCOUNT_SAVED_CARD_ROUTER = "/savedCards";
export const MY_ACCOUNT = "/my-account";
export const ORDER_PREFIX = "/my-account/order/(.*)";
export const ORDER_CODE = "orderCode";
export const ORDER = "/order";
// MyAccount Routes
export const MY_ACCOUNT_PAGE = "/my-account";
export const MY_ACCOUNT_WISHLIST_PAGE = "/wishList";
export const MY_ACCOUNT_ORDERS_PAGE = "/orders";
export const MY_ACCOUNT_GIFT_CARD_PAGE = "/giftCard";
export const MY_ACCOUNT_SAVED_CARDS_PAGE = "/payment-details";
export const MY_ACCOUNT_ADDRESS_PAGE = "/address-book";
export const MY_ACCOUNT_BRANDS_PAGE = "/brands";
export const MY_ACCOUNT_UPDATE_PROFILE_PAGE = "/update-profile";
export const MY_ACCOUNT_ALERTS_PAGE = "/alerts";
export const MY_ACCOUNT_COUPON_PAGE = "/coupons";
export const MY_ACCOUNT_CART_PAGE = "/cart";
export const MY_ACCOUNT_CLIQ_CASH_PAGE = "/cliq-cash";

//returns
export const RETURNS_PREFIX = "/returns";
export const RETURNS = "/returns/(.*)";
export const RETURN_LANDING = "/initiate";
export const RETURNS_REASON = "/reason";
export const RETURNS_MODES = "/modes";
export const RETURNS_NEW_ADDRESS = "/addDeliveryLocation";
export const RETURN_CLIQ_PIQ = "/cliqpiq";
export const RETURN_CLIQ_PIQ_ADDRESS = "/address";
export const RETURN_CLIQ_PIQ_DATE = "/dateTime";

export const RETURN_CLIQ_PIQ_RETURN_SUMMARY = "/returnSummary";

export const RETURN_TO_STORE = "/store";
export const RETURNS_STORE_MAP = "/storePick";
export const RETURNS_STORE_BANK_FORM = "/bankDetail";
export const RETURNS_STORE_FINAL = "/submit";

export const CLIQ_AND_PIQ = "/select-stores";
export const PRODUCT_CART_DELIVERY_MODES = "/deliveryModes";
export const PRODUCT_DELIVERY_ADDRESSES = "/deliveryAddress";
export const DEFAULT_BRANDS_LANDING_PAGE = "/defaultBrandsLanding";
export const PRICE_TEXT = "Price";
export const OFFER_AVAILABLE = "Offer Available";
export const EMI_AVAILABLE = "EMI Available";
export const DELIVERY_INFORMATION_TEXT = "Delivery Information";
export const DELIVERY_RATES = "Delivery Rates & Return Policy";
export const CASH_TEXT = "Cash on Delivery Available!";
export const SOCIAL_LOG_IN = "logIn";
export const SOCIAL_SIGN_UP = "signUp";
export const CART_DETAILS_FOR_LOGGED_IN_USER = "cartDetails";
export const CART_DETAILS_FOR_ANONYMOUS = "cartDetailsForAnonymous";
export const LOGGED_IN_USER_DETAILS = "userDetails";
export const CATEGORIES_LANDING_PAGE = "/categories";
export const ANONYMOUS_USER = "anonymous";

export const SAVE_LIST_PAGE = "/default/wishList";
export const PAYMENT_MODE_TYPE = "paymentMode";
// fetching feed information contant
export const BLP_OR_CLP_FEED_TYPE = "blpOrClp";
export const HOME_FEED_TYPE = "home";

export const YES = "Y";
export const NO = "N";

export const EXPRESS = "express-delivery";
export const COLLECT = "click-and-collect";
export const HOME_DELIVERY = "home-delivery";
export const SHORT_EXPRESS = "ED";
export const SHORT_COLLECT = "CNC";

export const FOLLOW = "Follow";
export const FOLLOWING = "Following";

export const TRUE = "true";

export const QUICK_DROP = "quickDrop";
export const SCHEDULED_PICKUP = "schedulePickup";
export const SELF_COURIER = "selfCourier";

export const CASH_ON_DELIVERY = "COD";
