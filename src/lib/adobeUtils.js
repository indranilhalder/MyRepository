import { getCookieValue, getCookie } from "./Cookie.js";
import cloneDeep from "lodash/cloneDeep";
import { setInterval, clearInterval } from "timers";
import * as constants from "../lib/constants.js";
import { userAddressFailure } from "../cart/actions/cart.actions";
import {
  LOGIN_WITH_EMAIL,
  LOGIN_WITH_MOBILE,
  FACEBOOK_PLATFORM,
  GOOGLE_PLUS_PLATFORM
} from "../auth/actions/user.actions";

export const ADOBE_TARGET_COOKIE_NAME =
  "AMCV_E9174ABF55BA76BA7F000101%40AdobeOrg";
export const ADOBE_TARGET_SPLIT_VALUE = "%7C";
export const ADOBE_TARGET_MCMID = "MCMID";
export const ADOBE_TARGET_WAIT_TIME = 2000;
const ADOBE_SATELLITE_CODE = "virtual_page_load";
const INTERNAL_CAMPAIGN_TRACK = "internal_campaign";
const ADOBE_PDP_CPJ = "cpj_pdp";
const ADOBE_ADD_TO_CART = "cpj_add_to_cart";
const ADOBE_SAVE_PRODUCT = "cpj_button_save";
const ADOBE_EMI_BANK_SELECT_ON_PDP = "cpj_pdp_emi";

// direct call url for cart page
const PINCODE_SUCCESS = "pin_successful";
const PINCODE_FAILURE = "pin_failed";
const ADOBE_DIRECT_CALL_FOR_LANDING_USER = "cpj_cart_page";
const ADOBE_DIRECT_CALL_ON_CART_FOR_REMOVE_TRIGGER = "cpj_cart_removal";
const ADOVE_DIRECT_CALL_ON_CLICK_CHECKOUT = "cpj_cart_checkout";
const ADOVE_DIRECT_CALL_FOR_CHANGE_QUANTITY_ON_CART =
  "cpj_cart_quantity_change";
const ADOBE_DIRECT_CALL_FOR_APPLY_COUPON_SUCCESS =
  "cpj_checkout_payment_coupon_success";
const ADOBE_DIRECT_CALL_FOR_APPLY_COUPON_FAIL =
  "cpj_checkout_payment_coupon_fail";
const ADOBE_DIRECT_CALL_FOR_SAVE_PORDUCT_ON_CART = "cpj_button_save'";
// end of direct call url for cart page
const ADOBE_ORDER_CONFIRMATION_FAILURE = "cpj_order_fail";
const ADOBE_ORDER_CONFIRMATION_SUCCESS = "cpj_order_successful";

// checkout adobe constants
const ADOBE_LANDING_ON_ADDRESS_PAGE = "cpj_checkout_proceed_to_address";
const ADOBE_ADD_NEW_ADDRESS = "cpj_checkout_add_address";
const ADOBE_CONFIRM_ADDRESS = "cpj_checkout_confirm_address";
const ADOBE_SELECT_DELIVERY_MODES = "cpj_checkout_delivery_option_select";
const ADOVE_PROCEED_FROM_DELIVERY_MODE = "cpj_checkout_delivery_option";
const ADOBE_LANDS_ON_PAYMENT_MODES = "cpj_checkout_proceed_to_payment";
const ADOBE_SELECT_PAYMENT_MODES = "cpj_checkout_payment_selection";
const ADOBE_FINAL_PAYMENT = "cpj_place_order";
const ADOBE_SEE_ALL_BANK_OFFERS = "CPJ_Checkout_Offer_Allbankoffer";
const ADOBE_CLIQ_CASH_ON = "CPJ_Checkout_Payment_ToggleOn";
const ADOBE_CLIQ_CASH_OFF = "CPJ_Checkout_Payment_ToggleOff";
const ADOBE_CHECKOUT_APPLY_COUPON_SUCCESS =
  "cpj_checkout_payment_coupon_success";
const ADOBE_CHECKOUT_APPLY_COUPON_FAILURE = "cpj_checkout_payment_coupon_fail";
// end of checkout adobe constants
// direct call for login tracking

const ADOBE_LOGIN_SUCCESS = "login_successful";
const ADOBE_LOGIN_FAILURE = "login_failed";
// end of direct call for login tracking

// cosnt for BLP and CLP
const ADOBE_BLP_DIRECT_CALL = "cpj_brand_pages";
const ADOBE_CLP_DIRECT_CALL = "cpj_category_pages";
// end of cosnt for BLP and CLP

// type of hierarchy for MY_ACCOUNT
const MY_ACCOUNT_OVERVIEW = "myaccount_overview";
const MY_ACCOUNT_SAVED_LIST = "myaccount_default_wishlist";
const MY_ACCOUNT_ADDRESS_BOOK = "myaccount_address_book";
const MY_ACCOUNT_BRANDS = "myaccount_brands";
const MY_ACCOUNT_ORDER_HISTORY = "order history page";
const MY_ACCOUNT_ORDER_DETAIL = "order details page";
const MY_ACCOUNT_SAVED_PAYMENTS = "myaccount_payment_details";
const MY_ACCOUNT_ALERTS = "myaccount_alerts";
const MY_ACCOUNT_COUPONS = "myaccount_coupons";
const MY_ACCOUNT_GIFT_CARD = "myaccount_gift_card";
const MY_ACCOUNT_CLIQ_CASH = "myaccount_cliq_cash";
const MY_ACCOUNT_SETTING = "myaccount_update_setting";
// end of type of hierarchy for my Account

// const for follow and un follow brands adobe calls
const ADOBE_FOLLOW_BRAND = "cpj_brand_follow";
const ADOBE_UN_FOLLOW_BRAND = "cpj_brand_unfollow";
const ADOBE_ON_CLICK_WIDGETS = "cpj_widget_followed";
// end of const for follow and un follow brands adobe calls
// const or adobe call for internal search call
const ADOBE_INTERNAL_SEARCH_SUCCESS = "internal_search";
const ADOBE_INTERNAL_SEARCH_NULL = "null_search";
// end of const or adobe call for internal search call

const ADOBE_NOT_FOUND = "404_error";
const ADOBE_FOR_CLICK_ON_PRODUCT_ON_PLP = "internal_search_link_clicks";
// internal search Adobe call const
export const ADOBE_INTERNAL_SEARCH_CALL_ON_GET_PRODUCT =
  "ADOBE_INTERNAL_SEARCH_CALL_ON_GET_PRODUCT";
export const ADOBE_INTERNAL_SEARCH_CALL_ON_GET_NULL =
  "ADOBE_INTERNAL_SEARCH_CALL_ON_GET_NULL";

// end of internal search Adobe call const

export const ADOBE_ORDER_CONFIRMATION = "orderConfirmation";
export const ADOBE_HOME_TYPE = "home";
export const ADOBE_PDP_TYPE = "pdp";
export const ADOBE_CART_TYPE = "cart";
export const ADOBE_CHECKOUT_TYPE = "checkout";
export const ADOBE_PLP_TYPE = "plp";
export const ADOBE_ORDER_CANCEL = "order_cancellation";
export const ADOBE_ORDER_RETURN_CANCEL = "order_returns_cancel";
export const ADOBE_ORDER_RETURN = "cpj_order_return";
export const ICID2 = "ICID2";
export const CID = "CID";
export const SET_DATA_LAYER_FOR_ADD_TO_BAG_EVENT =
  "SET_DATA_LAYER_FOR_ADD_TO_BAG_EVENT";
export const SET_DATA_LAYER_FOR_SAVE_PRODUCT_EVENT_ON_PDP =
  "SET_DATA_LAYER_FOR_SAVE_PRODUCT_EVENT_ON_PDP";
export const SET_DATA_LAYER_FOR_EMI_BANK_EVENT =
  "SET_DATA_LAYER_FOR_EMI_BANK_EVENT";
export const ADOBE_DIRECT_CALLS_FOR_REMOVE_PRODUCT_ON_CART =
  "ADOBE_DIRECT_CALLS_FOR_REMOVE_PRODUCT_ON_CART";

export const ADOBE_REMOVE_ITEM = "ADOBE_REMOVE_ITEM";
export const ADOBE_CALLS_FOR_ON_CLICK_CHECKOUT =
  "ADOBE_CALLS_FOR_ON_CLICK_CHECKOUT";
export const ADOBE_CALLS_FOR_CHANGE_QUANTITY =
  "ADOBE_CALLS_FOR_CHANGE_QUANTITY";
export const ADOBE_CALLS_FOR_APPLY_COUPON_SUCCESS =
  "ADOBE_CALLS_FOR_APPLY_COUPON_SUCCESS";
export const ADOBE_CALLS_FOR_APPLY_COUPON_FAIL =
  "ADOBE_CALLS_FOR_APPLY_COUPON_FAIL";
export const ADOBE_DIRECT_CALL_FOR_SAVE_ITEM_ON_CART =
  "ADOBE_DIRECT_CALL_FOR_SAVE_ITEM_ON_CART";

export const ADOBE_DIRECT_CALLS_FOR_ORDER_CONFIRMATION_SUCCESS =
  "ADOBE_DIRECT_CALLS_FOR_ORDER_CONFIRMATION_SUCCESS";
export const ADOBE_DIRECT_CALLS_FOR_ORDER_CONFIRMATION_FAILURE =
  "ADOBE_DIRECT_CALLS_FOR_ORDER_CONFIRMATION_FAILURE";

//  constants for checkout pages
export const ADOBE_LANDING_ON_ADDRESS_TAB_ON_CHECKOUT_PAGE =
  "ADOBE_LANDING_ON_ADDRESS_TAB_ON_CHECKOUT_PAGE";
export const ADOBE_ADD_NEW_ADDRESS_ON_CHECKOUT_PAGE =
  "ADOBE_ADD_NEW_ADDRESS_ON_CHECKOUT_PAGE";
export const ADOBE_FINAL_PAYMENT_MODES = "ADOBE_FINAL_PAYMENT_MODES";
export const ADOBE_ADD_ADDRESS_TO_ORDER = "ADOBE_ADD_ADDRESS_TO_ORDER";
export const ADOBE_CALL_FOR_LANDING_ON_PAYMENT_MODE =
  "ADOBE_CALL_FOR_LANDING_ON_PAYMENT_MODE";
export const ADOBE_CALL_FOR_SELECTING_PAYMENT_MODES =
  "ADOBE_CALL_FOR_SELECTING_PAYMENT_MODES";
export const ADOBE_CALL_FOR_SELECT_DELIVERY_MODE =
  "ADOBE_CALL_FOR_SELECT_DELIVERY_MODE";
export const ADOBE_CALL_FOR_PROCCEED_FROM_DELIVERY_MODE =
  "ADOBE_CALL_FOR_PROCCEED_FROM_DELIVERY_MODE";
export const ADOBE_CALL_FOR_SEE_ALL_BANK_OFFER =
  "ADOBE_CALL_FOR_SEE_ALL_BANK_OFFER";

export const ADOBE_CALL_FOR_CLIQ_CASH_TOGGLE_ON =
  "ADOBE_CALL_FOR_CLIQ_CASH_TOGGLE_ON";
export const ADOBE_CALL_FOR_CLIQ_CASH_TOGGLE_OFF =
  "ADOBE_CALL_FOR_CLIQ_CASH_TOGGLE_OFF";
export const ADOBE_CALL_FOR_APPLY_COUPON_SUCCESS =
  "ADOBE_CALL_FOR_APPLY_COUPON_SUCCESS";
export const ADOBE_CALL_FOR_APPLY_COUPON_FAILURE =
  "ADOBE_CALL_FOR_CLIQ_CASH_TOGGLE_FAILURE";

// end of constants for checkout pages

// const for setting data layer for the login track

export const ADOBE_DIRECT_CALL_FOR_LOGIN_SUCCESS =
  "ADOBE_DIRECT_CALL_FOR_LOGIN_SUCCESS";
export const ADOBE_DIRECT_CALL_FOR_LOGIN_FAILURE =
  "ADOBE_DIRECT_CALL_FOR_LOGIN_FAILURE";

// end of const for setting data layer for the login track

// const or setting myAccount section
export const ADOBE_MY_ACCOUNT_LANDING_PAGE = "ADOBE_MY_ACCOUNT_LANDING_PAGE";
export const ADOBE_MY_ACCOUNT_SAVED_LIST = "ADOBE_MY_ACCOUNT_SAVED_LIST";
export const ADOBE_MY_ACCOUNT_ADDRESS_BOOK = "ADOBE_MY_ACCOUNT_ADDRESS_BOOK";
export const ADOBE_MY_ACCOUNT_BRANDS = "ADOBE_MY_ACCOUNT_BRANDS";
export const ADOBE_MY_ACCOUNT_ORDER_HISTORY = "ADOBE_MY_ACCOUNT_ORDER_HISTORY";
export const ADOBE_MY_ACCOUNT_ORDER_DETAILS = "ADOBE_MY_ACCOUNT_ORDER_DETAILS";
export const ADOBE_MY_ACCOUNT_SAVED_PAYMENTS =
  "ADOBE_MY_ACCOUNT_SAVED_PAYMENTS";
export const ADOBE_MY_ACCOUNT_ALERTS = "ADOBE_MY_ACCOUNT_ALERTS";
export const ADOBE_MY_ACCOUNT_COUPONS = "ADOBE_MY_ACCOUNT_COUPONS";
export const ADOBE_MY_ACCOUNT_GIFT_CARD = "ADOBE_MY_ACCOUNT_GIFT_CARD";
export const ADOBE_MY_ACCOUNT_CLIQ_CASH = "ADOBE_MY_ACCOUNT_CLIQ_CASH";
export const AODBE_MY_ACCOUNT_SETTINGS = "AODBE_MY_ACCOUNT_SETTINGS";
// end of my Account section

export const ADOBE_DIRECT_CALL_FOR_PINCODE_SUCCESS =
  "ADOBE_DIRECT_CALL_FOR_PINCODE_SUCCESS";
export const ADOBE_DIRECT_CALL_FOR_PINCODE_FAILURE =
  "ADOBE_DIRECT_CALL_FOR_PINCODE_FAILURE";

// const for myAccount adobe calls
export const ADOBE_MY_ACCOUNT_CANCEL_ORDER_SUCCESS =
  "ADOBE_MY_ACCOUNT_CANCEL_ORDER_SUCCESS";
export const ADOBE_MY_ACCOUNT_ORDER_RETURN_CANCEL =
  "ADOBE_MY_ACCOUNT_ORDER_RETURN_CANCEL";
export const ADOBE_MY_ACCOUNT_ORDER_RETURN = "ADOBE_MY_ACCOUNT_ORDER_RETURN";
// end of const for my account adobe call

// cosnt for BLP and CLP adobe calls

export const ADOBE_BLP_PAGE_LOAD = "ADOBE_BLP_PAGE_LOAD";
export const ADOBE_CLP_PAGE_LOAD = "ADOBE_CLP_PAGE_LOAD";

export const ADOBE_DEFAULT_BLP_PAGE_LOAD = "ADOBE_DEFAULT_BLP_PAGE_LOAD";
export const ADOBE_DEFAULT_CLP_PAGE_LOAD = "ADOBE_DEFAULT_CLP_PAGE_LOAD";

// end of  cosnt for BLP and CLP adobe calls
// const for follow and un follow
export const ADOBE_ON_FOLLOW_AND_UN_FOLLOW_BRANDS =
  "ADOBE_ON_FOLLOW_AND_UN_FOLLOW_BRANDS";
export const ADOBE_ON_UN_FOLLOW_BRANDS = "ADOBE_ON_UN_FOLLOW_BRANDS";
export const ADOBE_ON_CLICK_FOLLOWED_WIDGET = "ADOBE_ON_CLICK_FOLLOWED_WIDGET";
// end const for follow and un follow

export const ADOBE_STATIC_PAGE = "ADOBE_STATIC_PAGE";
export const ADOBE_LOGIN_AND_SIGN_UP_PAGE = "ADOBE_LOGIN_AND_SIGN_UP_PAGE";

const GOOGLE = "google";
const FACEBOOK = "facebook";
const MOBILE = "mobile";
const EMAIL = "email";
const INTERNAL_CAMPAIGN = "internal_campaign";
const EXTERNAL_CAMPAIGN = "external_campaign";
export function setDataLayer(type, apiResponse, icid, icidType) {
  const response = cloneDeep(apiResponse);
  const previousDigitalData = cloneDeep(window.digitalData);
  let userDetails = getCookie(constants.LOGGED_IN_USER_DETAILS);
  if (userDetails) {
    userDetails = JSON.parse(userDetails);
  }
  if (type === ADOBE_HOME_TYPE) {
    window.digitalData = getDigitalDataForHome();
  }
  if (type === ADOBE_PLP_TYPE) {
    window.digitalData = getDigitalDataForPlp(type, response);
  }
  if (type === ADOBE_INTERNAL_SEARCH_CALL_ON_GET_PRODUCT) {
    window.digitalData = getDigitalDataForSearchPageSuccess(response);
    window._satellite.track(ADOBE_INTERNAL_SEARCH_SUCCESS);
  }
  if (type === ADOBE_INTERNAL_SEARCH_CALL_ON_GET_NULL) {
    window.digitalData = getDigitalDataForSearchPageForNullResult(response);
    window._satellite.track(ADOBE_INTERNAL_SEARCH_NULL);
  }

  if (type === ADOBE_PDP_TYPE) {
    const digitalDataForPDP = getDigitalDataForPdp(type, response);
    //  this is neccasary for when user comes from plp page to pdp
    //  then we are setting badges from plp page and we need to
    //  pass that on pdp page
    if (
      window.digitalData &&
      window.digitalData.cpj &&
      window.digitalData.cpj.product &&
      window.digitalData.cpj.product.badge
    ) {
      const badge = window.digitalData.cpj.product.badge;
      Object.assign(digitalDataForPDP.cpj.product, { badge });
    }
    window.digitalData = digitalDataForPDP;
    window._satellite.track(ADOBE_PDP_CPJ);
  }
  if (type === ADOBE_CHECKOUT_TYPE) {
    window.digitalData = getDigitalDataForCheckout(type, response);
  }
  if (type === ADOBE_CART_TYPE) {
    window.digitalData = getDigitalDataForCart(type, response);
    window._satellite.track(ADOBE_DIRECT_CALL_FOR_LANDING_USER);
  }
  if (type === ADOBE_ORDER_CONFIRMATION) {
    window.digitalData = getDigitalDataForOrderConfirmation(type, response);
  }
  if (type === ADOBE_MY_ACCOUNT_LANDING_PAGE) {
    window.digitalData = getDigitalDataForMyAccount(MY_ACCOUNT_OVERVIEW);
  }
  if (type === ADOBE_MY_ACCOUNT_SAVED_LIST) {
    window.digitalData = getDigitalDataForMyAccount(
      MY_ACCOUNT_SAVED_LIST,
      response
    );
  }
  if (type === ADOBE_MY_ACCOUNT_ADDRESS_BOOK) {
    window.digitalData = getDigitalDataForMyAccount(MY_ACCOUNT_ADDRESS_BOOK);
  }
  if (type === ADOBE_MY_ACCOUNT_BRANDS) {
    window.digitalData = getDigitalDataForMyAccount(MY_ACCOUNT_BRANDS);
  }
  if (type === ADOBE_MY_ACCOUNT_ORDER_HISTORY) {
    window.digitalData = getDigitalDataForMyAccount(MY_ACCOUNT_ORDER_HISTORY);
  }
  if (type === ADOBE_MY_ACCOUNT_SAVED_PAYMENTS) {
    window.digitalData = getDigitalDataForMyAccount(MY_ACCOUNT_SAVED_PAYMENTS);
  }
  if (type === ADOBE_MY_ACCOUNT_ALERTS) {
    window.digitalData = getDigitalDataForMyAccount(MY_ACCOUNT_ALERTS);
  }
  if (type === ADOBE_MY_ACCOUNT_COUPONS) {
    window.digitalData = getDigitalDataForMyAccount(MY_ACCOUNT_COUPONS);
  }
  if (type === ADOBE_MY_ACCOUNT_GIFT_CARD) {
    window.digitalData = getDigitalDataForMyAccount(MY_ACCOUNT_GIFT_CARD);
  }
  if (type === ADOBE_MY_ACCOUNT_CLIQ_CASH) {
    window.digitalData = getDigitalDataForMyAccount(MY_ACCOUNT_CLIQ_CASH);
  }
  if (type === AODBE_MY_ACCOUNT_SETTINGS) {
    window.digitalData = getDigitalDataForMyAccount(MY_ACCOUNT_SETTING);
  }
  if (type === ADOBE_MY_ACCOUNT_ORDER_DETAILS) {
    window.digitalData = getDigitalDataForMyAccount(MY_ACCOUNT_ORDER_DETAIL);
  }
  if (type === ADOBE_BLP_PAGE_LOAD) {
    window.digitalData = getDigitalDataForBLP(response);
    window._satellite.track(ADOBE_BLP_DIRECT_CALL);
  }
  if (type === ADOBE_CLP_PAGE_LOAD) {
    window.digitalData = getDigitalDataForCLP(response);
    window._satellite.track(ADOBE_CLP_DIRECT_CALL);
  }
  if (
    type === ADOBE_DEFAULT_BLP_PAGE_LOAD ||
    type === ADOBE_DEFAULT_CLP_PAGE_LOAD
  ) {
    window.digitalData = getDigitalDataForDefaultBlpOrClp(response);
  }
  if (type === ADOBE_LOGIN_AND_SIGN_UP_PAGE) {
    window.digitalData = getDigitalDataForLoginAndSignup();
  }
  if (type === ADOBE_STATIC_PAGE) {
    window.digitalData = getDigitalDataForStatic(response);
  }

  if (icidType === ICID2) {
    window.digitalData.flag = INTERNAL_CAMPAIGN;
    window.digitalData.internal = {
      campaign: {
        id: icid
      }
    };
    window._satellite.track(INTERNAL_CAMPAIGN_TRACK);
  } else if (icidType === CID) {
    window.digitalData.external = {
      campaign: {
        id: icid
      }
    };
    window.digitalData.flag = EXTERNAL_CAMPAIGN;
  }

  if (userDetails) {
    if (userDetails.loginType === LOGIN_WITH_EMAIL) {
      window.digitalData.account = {
        login: {
          customerID: userDetails.customerId,
          type: EMAIL
        }
      };
    } else if (userDetails.loginType === LOGIN_WITH_MOBILE) {
      window.digitalData.account = {
        login: {
          customerID: userDetails.customerId,
          type: MOBILE
        }
      };
    } else if (userDetails.loginType === FACEBOOK_PLATFORM) {
      window.digitalData.account = {
        login: {
          customerID: userDetails.customerId,
          type: FACEBOOK
        }
      };
    } else if (userDetails.loginType === GOOGLE_PLUS_PLATFORM) {
      window.digitalData.account = {
        login: {
          customerID: userDetails.customerId,
          type: GOOGLE
        }
      };
    }
  }

  const defaultPinCode = localStorage.getItem(
    constants.DEFAULT_PIN_CODE_LOCAL_STORAGE
  );

  if (defaultPinCode) {
    window.digitalData.geolocation = {
      pin: {
        code: defaultPinCode
      }
    };
  }

  if (
    previousDigitalData &&
    previousDigitalData.page &&
    previousDigitalData.page.pageInfo &&
    previousDigitalData.page.pageInfo.pageName
  ) {
    const currentDigitalData = window.digitalData;
    if (currentDigitalData.cpj) {
      if (currentDigitalData.cpj.pdp) {
        Object.assign(currentDigitalData.cpj.pdp, {
          findingMethod: previousDigitalData.page.pageInfo.pageName
        });
      } else {
        Object.assign(currentDigitalData.cpj, {
          pdp: {
            findingMethod: previousDigitalData.page.pageInfo.pageName
          }
        });
      }
    } else {
      Object.assign(currentDigitalData, {
        cpj: {
          pdp: {
            findingMethod: previousDigitalData.page.pageInfo.pageName
          }
        }
      });
    }
    window.digitalData = currentDigitalData;
  }

  window._satellite.track(ADOBE_SATELLITE_CODE);
}

function getDigitalDataForPdp(type, pdpResponse) {
  const data = {
    cpj: {
      product: {
        id: pdpResponse.productListingId
      },
      brand: {
        name: pdpResponse.brandName
      }
    },

    page: {
      category: {
        primaryCategory: "product"
      }
    }
  };
  const subCategories = getSubCategories(pdpResponse);
  if (subCategories) {
    Object.assign(data.page.category, { ...subCategories });
  }
  const productBreadcrumbs = getProductBreadCrumbs(pdpResponse);
  if (productBreadcrumbs) {
    Object.assign(data.page, {
      pageInfo: {
        pageName: "product details:".concat(
          productBreadcrumbs ? productBreadcrumbs : ""
        )
      }
    });
  }
  const displayHierarchy = getDisplayHierarchy(pdpResponse);
  Object.assign(data.page, {
    display: {
      hierarchy: displayHierarchy
    }
  });
  if (pdpResponse.mrpPrice && pdpResponse.mrpPrice.doubleValue) {
    Object.assign(data.cpj.product, {
      price: pdpResponse.mrpPrice.doubleValue
    });
    if (
      pdpResponse.winningSellerPrice &&
      pdpResponse.winningSellerPrice.doubleValue
    ) {
      Object.assign(data.cpj.product, {
        discount:
          pdpResponse.mrpPrice.doubleValue -
          pdpResponse.winningSellerPrice.doubleValue
      });
    }
  }

  if (pdpResponse && pdpResponse.seo && pdpResponse.seo.breadcrumbs) {
    let categoryName = pdpResponse.seo.breadcrumbs[0].name;
    categoryName = categoryName.replace(/ /g, "_").toLowerCase();
    Object.assign(data.cpj.product, {
      category: categoryName
    });
  }
  if (
    window.digitalData &&
    window.digitalData.page &&
    window.digitalData.page.pageInfo &&
    window.digitalData.page.pageInfo.pageName
  ) {
    Object.assign(data.cpj, {
      pdp: {
        findingMethod: window.digitalData.page.pageInfo.pageName
      }
    });
  }

  return data;
}

function getDigitalDataForHome() {
  const data = {
    page: {
      category: {
        primaryCategory: "home"
      },
      pageInfo: {
        pageName: "homepage"
      }
    }
  };
  if (
    window.digitalData &&
    window.digitalData.page &&
    window.digitalData.page.pageInfo.pageName
  ) {
    Object.assign(data, {
      cpj: {
        pdp: {
          findingMethod:
            window.digitalData &&
            window.digitalData.page &&
            window.digitalData.page.pageInfo.pageName
        }
      }
    });
  }
  return data;
}
function getDigitalDataForCart(type, cartResponse) {
  let data = {
    page: {
      category: {
        primaryCategory: "cart"
      },
      pageInfo: {
        pageName: "cart page"
      }
    }
  };
  const getProductData = getProductsDigitalData(cartResponse);
  if (getProductData) {
    let {
      productIdsArray,
      productQuantityArray,
      productPriceArray,
      productBrandArray
    } = getProductData;
    Object.assign(data, {
      cpj: {
        product: {
          id: productIdsArray,
          quantity: productQuantityArray,
          price: productPriceArray
        },
        brand: {
          name: productBrandArray
        }
      }
    });
  }
  const productCategoryHierarchy = getProductCategoryHierarchy(cartResponse);
  if (productCategoryHierarchy) {
    if (data.cpj && data.cpj.product) {
      Object.assign(data.cpj.product, {
        category: productCategoryHierarchy
      });
    } else {
      Object.assign(data, {
        cpj: { product: { category: productCategoryHierarchy } }
      });
    }
  }
  const categoryHierarchy = getCategoryHierarchy(cartResponse);
  if (categoryHierarchy) {
    Object.assign(data.page.category, categoryHierarchy);
  }
  return data;
}
function getDigitalDataForCheckout(type, CheckoutResponse) {
  let data = {
    page: {
      category: {
        primaryCategory: "multistepcheckoutsummary"
      },
      pageInfo: {
        pageName: "multi checkout summary page"
      }
    }
  };
  const getProductData = getProductsDigitalData(CheckoutResponse);
  if (getProductData) {
    let {
      productIdsArray,
      productQuantityArray,
      productPriceArray,
      productBrandArray,
      categoryArray
    } = getProductData;
    Object.assign(data, {
      cpj: {
        product: {
          id: productIdsArray,
          quantity: productQuantityArray,
          price: productPriceArray,
          category: categoryArray
        },
        brand: {
          name: productBrandArray
        }
      }
    });
  }

  const categoryHierarchy = getCategoryHierarchy(CheckoutResponse);
  if (categoryHierarchy) {
    Object.assign(data.page.category, categoryHierarchy);
  }

  return data;
}

function getDigitalDataForOrderConfirmation(type, response) {
  let data = {
    page: {
      category: {
        primaryCategory: "orderconfirmation"
      },
      pageInfo: {
        pageName: "order confirmation"
      }
    }
  };

  const getProductData = getProductsDigitalData(response);
  if (getProductData) {
    let {
      productIdsArray,
      productQuantityArray,
      productPriceArray,
      productBrandArray,
      categoryArray
    } = getProductData;
    Object.assign(data, {
      cpj: {
        product: {
          id: productIdsArray,
          quantity: productQuantityArray,
          price: productPriceArray,
          category: categoryArray
        },
        brand: {
          name: productBrandArray
        }
      }
    });
  }
  const categoryHierarchy = getCategoryHierarchy(response);
  if (categoryHierarchy) {
    Object.assign(data.page.category, categoryHierarchy);
  }
  return data;
}
// this function will update data with  cpj.proudct.id with
// reponse product's ids . this is using in many place thats why we
// need to make separate function for product ids
function getProductsDigitalData(response) {
  if (response && response.products && response.products.length > 0) {
    let productIdsArray = [],
      productQuantityArray = [],
      productPriceArray = [],
      productBrandArray = [],
      categoryArray = [];
    response.products.forEach(function(product) {
      productIdsArray.push(
        product.productcode && product.productcode.toLowerCase()
      );
      productQuantityArray.push(
        product.qtySelectedByUser
          ? product.qtySelectedByUser
          : product.quantity
            ? product.quantity
            : null
      );
      productPriceArray.push(
        product.offerPrice
          ? product.offerPrice
          : product.pricevalue
            ? product.pricevalue
            : product.price
              ? product.price
              : product.mrp && product.mrp.value
                ? product.mrp.value
                : null
      );
      productBrandArray.push(
        product.productBrand &&
          product.productBrand.replace(/ /g, "_").toLowerCase()
      );
      categoryArray.push(
        product.categoryHierarchy &&
          product.categoryHierarchy[0] &&
          product.categoryHierarchy[0].category_name &&
          product.categoryHierarchy[0].category_name
            .replace(/ /g, "_")
            .toLowerCase()
      );
    });
    return {
      productIdsArray,
      productQuantityArray,
      productPriceArray,
      productBrandArray,
      categoryArray
    };
  } else {
    return null;
  }
}
function getProductCategoryHierarchy(response) {
  let category = [];
  if (response && response.products && response.products.length > 0) {
    response.products.forEach(product => {
      if (
        product &&
        product.categoryHierarchy &&
        product.categoryHierarchy[0]
      ) {
        category.push(
          product.categoryHierarchy[0].category_name
            ? product.categoryHierarchy[0].category_name
                .replace(/\s+/g, "_")
                .toLowerCase()
            : null
        );
      }
    });
    return category;
  } else {
    return null;
  }
}
function getCategoryHierarchy(response) {
  let subCategory1 = [],
    subCategory2 = [],
    subCategory3 = [];
  if (response && response.products && response.products.length > 0) {
    response.products.forEach(product => {
      if (
        product &&
        product.categoryHierarchy &&
        product.categoryHierarchy[0]
      ) {
        subCategory1.push(
          product.categoryHierarchy[0].category_name
            ? product.categoryHierarchy[0].category_name
                .replace(/\s+/g, "_")
                .toLowerCase()
            : null
        );
      }
      if (
        product &&
        product.categoryHierarchy &&
        product.categoryHierarchy[1]
      ) {
        subCategory2.push(
          product.categoryHierarchy[1].category_name
            ? product.categoryHierarchy[1].category_name
                .replace(/\s+/, "_")
                .toLowerCase()
            : null
        );
      }
      if (
        product &&
        product.categoryHierarchy &&
        product.categoryHierarchy[2]
      ) {
        subCategory3.push(
          product.categoryHierarchy[2].category_name
            ? product.categoryHierarchy[2].category_name
                .replace(/\s+/, "_")
                .toLowerCase()
            : null
        );
      }
    });
    return {
      subCategory1: subCategory1.join(","),
      subCategory2: subCategory2.join(","),
      subCategory3: subCategory3.join(",")
    };
  } else {
    return null;
  }
}
function getDisplayHierarchy(response) {
  if (response && response.seo && response.seo.breadcrumbs) {
    const seoBreadCrumbs = response.seo.breadcrumbs.map(val => {
      return val.name.toLowerCase().replace(/\s+/g, "_");
    });
    const hierarchyArray = ["home", ...seoBreadCrumbs];
    return hierarchyArray.join(",");
  } else {
    return "home";
  }
}
function getSubCategories(response) {
  if (response && response.seo && response.seo.breadcrumbs) {
    const breadcrumbs = response.seo.breadcrumbs.reverse();
    const subCatagories = {};
    if (breadcrumbs[0]) {
      Object.assign(subCatagories, {
        subCategory1: breadcrumbs[0].name.replace(/ /g, "_").toLowerCase()
      });
    }
    if (breadcrumbs[1]) {
      Object.assign(subCatagories, {
        subCategory2: breadcrumbs[1].name.replace(/ /g, "_").toLowerCase()
      });
    }
    if (breadcrumbs[2]) {
      Object.assign(subCatagories, {
        subCategory3: breadcrumbs[2].name.replace(/ /g, "_").toLowerCase()
      });
    }

    return subCatagories;
  } else {
    return null;
  }
}
function getProductBreadCrumbs(pdpResponse) {
  if (pdpResponse && pdpResponse.seo && pdpResponse.seo.breadcrumbs) {
    const productBreadCrumbs = pdpResponse.seo.breadcrumbs.map(crumbs => {
      return (
        crumbs && crumbs.name && crumbs.name.toLowerCase().replace(/ /g, "_")
      );
    });
    return productBreadCrumbs.join(":");
  } else {
    return null;
  }
}
export async function getMcvId() {
  return new Promise((resolve, reject) => {
    let amcvCookieValue = getCookieValue(ADOBE_TARGET_COOKIE_NAME).split(
      ADOBE_TARGET_SPLIT_VALUE
    );
    let mcvId =
      amcvCookieValue[amcvCookieValue.indexOf(ADOBE_TARGET_MCMID) + 1];
    if (mcvId && mcvId.length > 0) {
      resolve(mcvId);
    } else {
      const intervalId = setInterval(() => {
        amcvCookieValue = getCookieValue(ADOBE_TARGET_COOKIE_NAME).split(
          ADOBE_TARGET_SPLIT_VALUE
        );
        mcvId =
          amcvCookieValue[amcvCookieValue.indexOf(ADOBE_TARGET_MCMID) + 1];
        if (mcvId) {
          clearInterval(intervalId);
          resolve(mcvId);
        }
      }, ADOBE_TARGET_WAIT_TIME);
    }
  });
}
export function setDataLayerForPdpDirectCalls(type, layerData: null) {
  let data = window.digitalData;

  if (type === SET_DATA_LAYER_FOR_ADD_TO_BAG_EVENT) {
    window._satellite.track(ADOBE_ADD_TO_CART);
  }
  if (type === SET_DATA_LAYER_FOR_SAVE_PRODUCT_EVENT_ON_PDP) {
    window._satellite.track(ADOBE_SAVE_PRODUCT);
  }
  if (type === SET_DATA_LAYER_FOR_EMI_BANK_EVENT) {
    Object.assign(data.cpj, {
      emi: { bank: layerData.replace(/ /g, "_").toLowerCase() }
    });
    window.digitalData = data;
    window._satellite.track(ADOBE_EMI_BANK_SELECT_ON_PDP);
  }
}

export function setDataLayerForCartDirectCalls(type, response) {
  let data = cloneDeep(window.digitalData);
  if (type === ADOBE_REMOVE_ITEM) {
    const getProductData = getProductsDigitalData(response);
    if (getProductData) {
      let {
        productIdsArray,
        productQuantityArray,
        productPriceArray,
        productBrandArray
      } = getProductData;
      Object.assign(data, {
        cpj: {
          product: {
            id: productIdsArray,
            quantity: productQuantityArray,
            price: productPriceArray
          },
          brand: {
            name: productBrandArray
          }
        }
      });
    }
    window.digitalData = data;
    window._satellite.track(ADOBE_DIRECT_CALL_ON_CART_FOR_REMOVE_TRIGGER);
  }
  if (type === ADOBE_CALLS_FOR_ON_CLICK_CHECKOUT) {
    window._satellite.track(ADOVE_DIRECT_CALL_ON_CLICK_CHECKOUT);
  }
  if (type === ADOBE_CALLS_FOR_CHANGE_QUANTITY) {
    window._satellite.track(ADOVE_DIRECT_CALL_FOR_CHANGE_QUANTITY_ON_CART);
  }
  if (type === ADOBE_CALLS_FOR_APPLY_COUPON_SUCCESS) {
    Object.assign(data.cpj, {
      coupon: { code: response }
    });
    window.digitalData = data;

    window._satellite.track(ADOBE_DIRECT_CALL_FOR_APPLY_COUPON_SUCCESS);
  }
  if (type === ADOBE_CALLS_FOR_APPLY_COUPON_FAIL) {
    Object.assign(data.cpj, {
      coupon: { code: response }
    });
    window.digitalData = data;

    window._satellite.track(ADOBE_DIRECT_CALL_FOR_APPLY_COUPON_FAIL);
  }
  if (type === ADOBE_DIRECT_CALL_FOR_SAVE_ITEM_ON_CART) {
    window._satellite.track(ADOBE_DIRECT_CALL_FOR_SAVE_PORDUCT_ON_CART);
  }
  if (type === ADOBE_DIRECT_CALL_FOR_PINCODE_SUCCESS) {
    window.digitalData = setDataLayerForPinCode(response);
    window._satellite.track(PINCODE_SUCCESS);
  }
  if (type === ADOBE_DIRECT_CALL_FOR_PINCODE_FAILURE) {
    window.digitalData = setDataLayerForPinCode(response);
    window._satellite.track(PINCODE_FAILURE);
  }
}
function getDigitalDataForPlp(type, response) {
  let data = {
    page: {
      category: {
        primaryCategory: "category"
      },
      pageInfo: {
        pageName: "product grid"
      }
    }
  };
  if (response.searchresult && response.searchresult.length > 0) {
    const productCodes = response.searchresult.splice(0, 9).map(product => {
      return product.productId.toLowerCase();
    });
    const impression = productCodes.join("|");
    Object.assign(data.page, {
      products: {
        impression
      }
    });
  }
  const hierarchy = getDisplayHierarchy(response);
  if (hierarchy) {
    Object.assign(data.page, {
      display: {
        hierarchy
      }
    });
  }
  const subCategories = getSubCategories(response);
  if (subCategories) {
    Object.assign(data.page.category, { ...subCategories });
  }
  return data;
}
export function getDigitalDataForSearchPageSuccess(response) {
  const data = {
    page: {
      pageInfo: { pageName: "search results page" },
      category: { primaryCategory: "productsearch" },
      display: {
        hierarchy: `home |${
          response.currentQuery ? response.currentQuery.searchQuery : null
        }`
      }
    },
    internal: {
      search: {
        category: "all",
        results: response.searchresult ? response.searchresult.length : 0,
        term: response.currentQuery ? response.currentQuery.searchQuery : null
      }
    }
  };
  if (response && response.searchresult && response.searchresult.length > 0) {
    const productCodes = response.searchresult.splice(0, 9).map(product => {
      return product.productId.toLowerCase();
    });
    const impression = productCodes.join("|");
    Object.assign(data.page, {
      products: {
        impression
      }
    });
  }
  return data;
}

export function getDigitalDataForSearchPageForNullResult(response) {
  const data = {
    internal: {
      search: {
        term: response.currentQuery ? response.currentQuery.searchQuery : null
      }
    }
  };
  return data;
}
export function setDataLayerForPlpDirectCalls(response) {
  const data = window.digitalData;
  let badge;
  if (response) {
    if (response.outOfStock) {
      badge = "out of stock";
    } else if (response.discountPercent && response.discountPercent !== "0") {
      badge = `${response.discountPercent}% off`;
    } else if (response.isOfferExisting) {
      badge = "on offer";
    } else if (response.onlineExclusive) {
      badge = "exclusive";
    } else if (response.newProduct) {
      badge = "new";
    }
  }
  if (badge && data) {
    if (data.cpj && data.cpj.product) {
      Object.assign(data.cpj.product, { badge });
    } else if (data.cpj) {
      Object.assign(data.cpj, { product: { badge } });
    } else {
      Object.assign(data, { cpj: { product: { badge } } });
    }
    window.digitalData = data;
  }
  window._satellite.track(ADOBE_FOR_CLICK_ON_PRODUCT_ON_PLP);
}
export function setDataLayerForLogin(type) {
  let userDetails = getCookie(constants.LOGGED_IN_USER_DETAILS);
  const data = {};
  if (type === ADOBE_DIRECT_CALL_FOR_LOGIN_SUCCESS) {
    if (userDetails) {
      if (userDetails.loginType === LOGIN_WITH_EMAIL) {
        Object.assign(data, {
          account: {
            login: {
              customerID: userDetails.customerId,
              type: EMAIL
            }
          }
        });
      } else if (userDetails.loginType === LOGIN_WITH_MOBILE) {
        if (data.account) {
          Object.assign(data.account, {
            login: {
              customerID: userDetails.customerId,
              type: MOBILE
            }
          });
        } else {
          Object.assign(data, {
            account: {
              login: {
                customerID: userDetails.customerId,
                type: MOBILE
              }
            }
          });
        }
      } else if (userDetails.loginType === FACEBOOK_PLATFORM) {
        if (data.account) {
          Object.assign(data.account, {
            login: {
              customerID: userDetails.customerId,
              type: FACEBOOK
            }
          });
        } else {
          Object.assign(data, {
            account: {
              login: {
                customerID: userDetails.customerId,
                type: FACEBOOK
              }
            }
          });
        }
      } else if (userDetails.loginType === GOOGLE_PLUS_PLATFORM) {
        if (data.account) {
          Object.assign(data.account, {
            login: {
              customerID: userDetails.customerId,
              type: GOOGLE
            }
          });
        } else {
          Object.assign(data, {
            account: {
              login: {
                customerID: userDetails.customerId,
                type: GOOGLE
              }
            }
          });
        }
      }
    }
    if (
      window.digitalData &&
      window.digitalData.page &&
      window.digitalData.page.pageInfo &&
      window.digitalData.page.pageInfo.pageName
    ) {
      if (data.account) {
        if (data.account.login) {
          Object.assign(data.account.login, {
            location: window.digitalData.page.pageInfo.pageName
          });
        } else {
          Object.assign(data.account, {
            login: {
              location: window.digitalData.page.pageInfo.pageName
            }
          });
        }
      } else {
        Object.assign(data, {
          account: {
            login: {
              location: window.digitalData.page.pageInfo.pageName
            }
          }
        });
      }
    }
    window.digitalData = data;
    window.digitalData.flag = ADOBE_LOGIN_SUCCESS;
    window._satellite.track(ADOBE_LOGIN_SUCCESS);
  }
  if (type === ADOBE_DIRECT_CALL_FOR_LOGIN_FAILURE) {
    window.digitalData.flag = ADOBE_LOGIN_FAILURE;
    window._satellite.track(ADOBE_LOGIN_FAILURE);
  }
}
export function setDataLayerForOrderConfirmationDirectCalls(
  type,
  orderConfirmationResponse
) {
  if (type === ADOBE_DIRECT_CALLS_FOR_ORDER_CONFIRMATION_SUCCESS) {
    let previousData = {};
    if (window.digitalData) {
      previousData = window.digitalData;
    }

    if (previousData.cpj) {
      Object.assign(previousData.cpj, {
        order: {
          id: orderConfirmationResponse
        }
      });
    } else {
      Object.assign(previousData, {
        cpj: {
          order: {
            id: orderConfirmationResponse
          }
        }
      });
    }
    window.digitalData = previousData;
    window._satellite.track(ADOBE_ORDER_CONFIRMATION_SUCCESS);
  }
  if (type === ADOBE_DIRECT_CALLS_FOR_ORDER_CONFIRMATION_FAILURE) {
    const data = {
      cpj: {
        order: {
          failureReason: orderConfirmationResponse
        }
      }
    };
    window.digitalData = data;
    window._satellite.track(ADOBE_ORDER_CONFIRMATION_FAILURE);
  }
}
export function setDataLayerForCheckoutDirectCalls(type, response) {
  let data = cloneDeep(window.digitalData);
  if (type === ADOBE_LANDING_ON_ADDRESS_TAB_ON_CHECKOUT_PAGE) {
    window._satellite.track(ADOBE_LANDING_ON_ADDRESS_PAGE);
  }
  if (type === ADOBE_ADD_NEW_ADDRESS_ON_CHECKOUT_PAGE) {
    window._satellite.track(ADOBE_ADD_NEW_ADDRESS);
  }
  if (type === ADOBE_ADD_ADDRESS_TO_ORDER) {
    window._satellite.track(ADOBE_CONFIRM_ADDRESS);
  }
  if (
    type === ADOBE_CALL_FOR_SELECT_DELIVERY_MODE ||
    type === ADOBE_CALL_FOR_PROCCEED_FROM_DELIVERY_MODE
  ) {
    // herer we are getting all delivery modes and ussid in form of object
    // like {"MP12345678":"home_delivery","MP987654321":"expres_delivery"}
    // so here we need ot pass only "home_delivery"|"express_delivery"
    const deliveryModesObj = Object.values(response).join("|");
    if (data) {
      if (data.cpj) {
        if (data.cpj.checkout) {
          Object.assign(data.cpj.checkout, {
            deliveryOption: deliveryModesObj
          });
        } else {
          Object.assign(data.cpj, {
            checkout: { deliveryOption: deliveryModesObj }
          });
        }
      } else {
        Object.assign(data, {
          cpj: { checkout: { deliveryOption: deliveryModesObj } }
        });
      }
    } else {
      Object.assign(data, {
        cpj: { checkout: { deliveryOption: deliveryModesObj } }
      });
    }

    window.digitalData = data;
    if (type === ADOBE_CALL_FOR_SELECT_DELIVERY_MODE) {
      window._satellite.track(ADOBE_SELECT_DELIVERY_MODES);
    } else {
      window._satellite.track(ADOVE_PROCEED_FROM_DELIVERY_MODE);
    }
  }
  if (type === ADOBE_CALL_FOR_SEE_ALL_BANK_OFFER) {
    window._satellite.track(ADOBE_SEE_ALL_BANK_OFFERS);
  }
  if (type === ADOBE_CALL_FOR_CLIQ_CASH_TOGGLE_ON) {
    window._satellite.track(ADOBE_CLIQ_CASH_ON);
  }
  if (type === ADOBE_CALL_FOR_CLIQ_CASH_TOGGLE_OFF) {
    window._satellite.track(ADOBE_CLIQ_CASH_OFF);
  }
  if (
    type === ADOBE_CALL_FOR_APPLY_COUPON_SUCCESS ||
    type === ADOBE_CALL_FOR_APPLY_COUPON_FAILURE
  ) {
    if (data) {
      if (data.cpj) {
        if (data.cpj.coupon) {
          data = Object.assign(data.cpj.coupon, {
            code: response
          });
        } else {
          data = Object.assign(data.cpj, {
            coupon: { code: response }
          });
        }
      } else {
        data = Object.assign(data, {
          cpj: { coupon: { code: response } }
        });
      }
    } else {
      Object.assign(data, { cpj: { coupon: { code: response } } });
    }
    window.digitalData = data;
    if (type === ADOBE_CALL_FOR_APPLY_COUPON_SUCCESS) {
      window._satellite.track(ADOBE_CHECKOUT_APPLY_COUPON_SUCCESS);
    } else {
      window._satellite.track(ADOBE_CHECKOUT_APPLY_COUPON_FAILURE);
    }
  }

  if (type === ADOBE_FINAL_PAYMENT_MODES) {
    const finalPaymentMode = localStorage.getItem(constants.PAYMENT_MODE_TYPE);

    if (finalPaymentMode) {
      if (data) {
        if (data.cpj) {
          if (data.cpj.payment) {
            Object.assign(data.cpj.payment, {
              finalMode: finalPaymentMode.replace(/ /g, "_").toLowerCase()
            });
          } else {
            Object.assign(data.cpj, {
              payment: {
                finalMode: finalPaymentMode.replace(/ /g, "_").toLowerCase()
              }
            });
          }
        } else {
          Object.assign(data, {
            cpj: {
              payment: {
                finalMode: finalPaymentMode.replace(/ /g, "_").toLowerCase()
              }
            }
          });
        }
      } else {
        Object.assign(data, {
          cpj: {
            payment: {
              finalMode: finalPaymentMode.replace(/ /g, "_").toLowerCase()
            }
          }
        });
      }
      window.digitalData = data;
    }
    window._satellite.track(ADOBE_FINAL_PAYMENT);
  }
  if (type === ADOBE_CALL_FOR_LANDING_ON_PAYMENT_MODE) {
    window._satellite.track(ADOBE_LANDS_ON_PAYMENT_MODES);
  }
  if (type === ADOBE_CALL_FOR_SELECTING_PAYMENT_MODES) {
    if (response) {
      if (data) {
        if (data.cpj) {
          if (data.cpj.payment) {
            Object.assign(data.cpj.payment, {
              finalMode: response.replace(/ /g, "_").toLowerCase()
            });
          } else {
            Object.assign(data.cpj, {
              payment: {
                finalMode: response.replace(/ /g, "_").toLowerCase()
              }
            });
          }
        } else {
          Object.assign(data, {
            cpj: {
              payment: {
                finalMode: response.replace(/ /g, "_").toLowerCase()
              }
            }
          });
        }
      } else {
        data = Object.assign(
          {},
          {
            cpj: {
              payment: {
                finalMode: response.replace(/ /g, "_").toLowerCase()
              }
            }
          }
        );
      }
      window.digitalData = data;
    }
    window._satellite.track(ADOBE_SELECT_PAYMENT_MODES);
  }
}
export function setDataLayerForMyAccountDirectCalls(
  type,
  productDetails,
  reasonObj: null
) {
  let data = cloneDeep(window.digitalData);
  if (type === ADOBE_MY_ACCOUNT_CANCEL_ORDER_SUCCESS) {
    data = Object.assign(data, {
      order: {
        cancellation: {
          reason: productDetails ? productDetails.reasonLabel : ""
        }
      }
    });
    window.digitalData = data;
    window._satellite.track(ADOBE_ORDER_CANCEL);
  }
  if (type === ADOBE_MY_ACCOUNT_ORDER_RETURN_CANCEL) {
    window._satellite.track(ADOBE_ORDER_RETURN_CANCEL);
  }
  if (type === ADOBE_MY_ACCOUNT_ORDER_RETURN) {
    data = {
      cpj: {
        product: {
          id: productDetails.productcode,
          price: productDetails.price
        }
      }
    };
    window.digitalData = data;
    window._satellite.track(ADOBE_ORDER_RETURN);
  }
}
export function getDigitalDataForMyAccount(pageTitle, response) {
  const data = {
    page: {
      pageInfo: { pageName: pageTitle },
      category: { primaryCategory: pageTitle },
      display: { hierarchy: `home|my_tata_cliq|${pageTitle}` }
    }
  };
  if (response) {
    const getProductData = getProductsDigitalData(response);
    if (getProductData) {
      let {
        productIdsArray,
        productQuantityArray,
        productPriceArray,
        productBrandArray
      } = getProductData;
      Object.assign(data, {
        cpj: {
          product: {
            id: productIdsArray,
            quantity: productQuantityArray,
            price: productPriceArray
          },
          brand: {
            name: productBrandArray
          }
        }
      });
    }
  }
  return data;
}
export function getDigitalDataForBLP(response) {
  const data = {};
  let pageTitle = "";
  if (response.pageName) {
    Object.assign(data, {
      page: {
        pageName: response.pageName
      }
    });
  }
  if (response.items && response.items.length > 0) {
    const titleObj = response.items.find(data => {
      return data.componentName === "landingPageTitleComponent";
    });

    if (titleObj && titleObj.landingPageTitleComponent) {
      pageTitle = titleObj.landingPageTitleComponent.title;
    }
    Object.assign(data, {
      cpj: { brand: { name: pageTitle } }
    });
    if (data.page) {
      Object.assign(data.page, {
        display: {
          hierarchy: pageTitle ? `home,${pageTitle}` : "home"
        }
      });
    } else {
      Object.assign(data, {
        page: {
          display: {
            hierarchy: pageTitle ? `home,${pageTitle}` : "home"
          }
        }
      });
    }
  }

  return data;
}
export function getDigitalDataForCLP(response) {
  const data = {
    page: { category: { primaryCategory: "category" } }
  };
  const subCategories = getSubCategories(response);
  if (subCategories) {
    Object.assign(data.page.category, { ...subCategories });
    Object.assign(data.page, {
      pageInfo: {
        pageName: `product grid: ${
          subCategories.subCategory1 ? subCategories.subCategory1 : null
        } : ${
          subCategories.subCategory2 ? subCategories.subCategory2 : null
        } : ${subCategories.subCategory3 ? subCategories.subCategory3 : null}`
      }
    });
  } else {
    Object.assign(data.page, {
      pageInfo: {
        pageName: `product grid: ${null}: ${null}: ${null}`
      }
    });
  }
  const hierarchy = getDisplayHierarchy(response);
  if (hierarchy) {
    Object.assign(data.page, {
      display: {
        hierarchy
      }
    });
  }
  return data;
}
export function setDataLayerForFollowAndUnFollowBrand(type, response) {
  let data = {};
  if (type === ADOBE_ON_FOLLOW_AND_UN_FOLLOW_BRANDS) {
    Object.assign(data, {
      digitalData: { cpj: { brand: { name: response.brandName } } }
    });
    if (response.followStatus) {
      window._satellite.track(ADOBE_FOLLOW_BRAND);
    } else {
      window._satellite.track(ADOBE_UN_FOLLOW_BRAND);
    }
  }
}
export function setDataLayerForPinCode(response) {
  const previousData = cloneDeep(window.digitalData);
  if (previousData) {
    if (previousData.page) {
      if (previousData.page.pin) {
        Object.assign(previousData.page.pin, {
          value: response
        });
      } else {
        Object.assign(previousData.page, {
          pin: { value: response }
        });
      }
    } else {
      Object.assign(previousData, {
        page: { pin: { value: response } }
      });
    }
  } else {
    Object.assign(previousData, {
      page: { pin: { value: response } }
    });
  }
  return previousData;
}
function getDigitalDataForDefaultBlpOrClp(response) {
  const data = {
    page: { category: { primaryCategory: "category" } }
  };
  const subCategories = getSubCategories(response);
  if (subCategories) {
    Object.assign(data.page.category, { ...subCategories });
  }
  if (response && response.pageName) {
    Object.assign(data.page, {
      pageInfo: {
        pageName: response.pageName.replace(/ /g, "_").toLowerCase()
      }
    });
  } else {
    Object.assign(data.page, {
      pageInfo: {
        pageName: window.location.pathname.replace(/\//g, "")
      }
    });
  }
  const hierarchy = getDisplayHierarchy(response);
  if (hierarchy) {
    Object.assign(data.page, {
      display: {
        hierarchy
      }
    });
  }
  return data;
}

function getDigitalDataForLoginAndSignup() {
  let pageTitle = window.location.pathname.replace(/\//g, "");
  if (pageTitle === "login") {
    pageTitle = "login page";
  }
  const data = {
    page: {
      pageInfo: { pageName: pageTitle },
      category: { primaryCategory: pageTitle },
      display: { hierarchy: `"home"|"${pageTitle}"` }
    }
  };
  return data;
}
function getDigitalDataForStatic(response) {
  const data = {
    page: { category: { primaryCategory: "category" } }
  };
  const subCategories = getSubCategories(response);
  if (subCategories) {
    Object.assign(data.page.category, { ...subCategories });
  }
  if (response && response.pageName) {
    Object.assign(data.page, {
      pageInfo: {
        pageName: response.pageName.replace(/ /g, "_").toLowerCase()
      }
    });
  } else {
    Object.assign(data.page, {
      pageInfo: {
        pageName: window.location.pathname.replace(/\//g, "")
      }
    });
  }

  if (response && response.pageName) {
    Object.assign(data.page, {
      display: {
        hierarchy: `home|${response.pageName.replace(/ /g, "").toLowerCase()}`
      }
    });
  }
  return data;
}

export function setDataLayerForNotFound() {
  window._satellite.track(ADOBE_NOT_FOUND);
}
