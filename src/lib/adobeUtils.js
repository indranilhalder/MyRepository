import { getCookieValue, getCookie } from "./Cookie.js";
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
const ADOBE_PDP_CPJ = "cpj_pdp";
const ADOBE_ADD_TO_CART = "cpj_add_to_cart";
const ADOBE_SAVE_PRODUCT = "cpj_button_save";
const ADOBE_EMI_BANK_SELECT_ON_PDP = "'cpj_pdp_emi";

// direct call url for cart page
const ADOBE_DIRECT_CALL_FOR_LANDING_USER = "cpj_cart_page";
const ADOBE_DIRECT_CALL_ON_CART_FOR_REMOVE_TRIGGER = "cpj_cart_removal";
const ADOVE_DIRECT_CALL_ON_CLICK_CHECKOUT = "cpj_cart_checkout";
const ADOVE_DIRECT_CALL_FOR_CHANGE_QUANTITY_ON_CART =
  "cpj_cart_quantity_change";
const ADOBE_DIRECT_CALL_FOR_APPLY_COUPON_SUCCESS =
  "cpj_checkout_payment_coupon_success";
const ADOBE_DIRECT_CALL_FOR_APPLY_COUPON_FAIL =
  "cpj_checkout_payment_coupon_fail";
const ADOBE_DIRECT_CALL_FOR_SAVE_PORDUCT_ON_CART = "'cpj_button_save'";
// end of direct call url for cart page

export const ADOBE_ORDER_CONFIRMATION = "orderConfirmation";
export const ADOBE_HOME_TYPE = "home";
export const ADOBE_PDP_TYPE = "pdp";
export const ADOBE_CART_TYPE = "cart";
export const ADOBE_CHECKOUT_TYPE = "checkout";
export const ADOBE_PLP_TYPE = "plp";

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
const GOOGLE = "google";
const FACEBOOK = "facebook";
const MOBILE = "mobile";
const EMAIL = "email";
const INTERNAL_CAMPAIGN = "internal_campaign";
const EXTERNAM_CAMPAIGN = "external_campaign";
export function setDataLayer(type, response, icid, icidType) {
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
      const badge = window.digitalData.cpj.badge;
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
  if (icid) {
    window.digitalData.internal = {
      campaign: {
        id: icid
      }
    };
  }
  if (icidType === ICID2) {
    window.digitalData.flag = INTERNAL_CAMPAIGN;
  } else if (icidType === CID) {
    window.digitalData.flag = EXTERNAM_CAMPAIGN;
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
      },

      pageInfo: {
        pageName: "product details"
      }
    }
  };
  const subCategories = getSubCategories(pdpResponse);
  if (subCategories) {
    Object.assign(data.page.category, { ...subCategories });
  }
  if (pdpResponse && pdpResponse.seo && pdpResponse.seo.breadcrumbs) {
    const seoBreadCrumbs = pdpResponse.seo.breadcrumbs
      .map(val => {
        return val.name.toLowerCase().replace(/\s+/g, "_");
      })
      .reverse();
    Object.assign(data.page, {
      display: {
        hierarchy: ["home", ...seoBreadCrumbs]
      }
    });
  } else {
    Object.assign(data, {
      display: {
        hierarchy: ["home"]
      }
    });
  }
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
    let categoryName =
      pdpResponse.seo.breadcrumbs[pdpResponse.seo.breadcrumbs.length - 1].name;
    categoryName = categoryName.replace(/ /g, "_");
    Object.assign(data.cpj.product, {
      category: categoryName
    });
  }
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
        pageName: "cart"
      }
    }
  };
  const productIds = getProductIdArray(cartResponse);
  if (productIds) {
    Object.assign(data, {
      cpj: { product: { id: JSON.stringify(productIds) } }
    });
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
  const productIds = getProductIdArray(CheckoutResponse);
  if (productIds) {
    Object.assign(data, {
      cpj: { product: { id: JSON.stringify(productIds) } }
    });
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
        pageName: "order confirmation page"
      }
    }
  };

  const productIds = getProductIdArray(response);
  if (productIds) {
    Object.assign(data, {
      cpj: { product: { id: JSON.stringify(productIds) } }
    });
  }
  return data;
}
// this function will update data with  cpj.proudct.id with
// reponse product's ids . this is using in many place thats why we
// need to make separate function for product ids
function getProductIdArray(response) {
  if (response && response.products && response.products.length > 0) {
    return response.products.map(product => {
      return product.productcode;
    });
  } else {
    return null;
  }
}
function getHierarchyArray(response) {
  if (response.seo && response.seo.breadcrumbs) {
    const hierarchyArray = response.seo.breadcrumbs
      .reverse()
      .map(breadcrumb => {
        return breadcrumb.name.replace(/ /g, "_");
      });
    return ["home", ...hierarchyArray];
  } else {
    return null;
  }
}
function getSubCategories(response) {
  if (response.seo && response.seo.breadcrumbs) {
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
  let data = window.digitalData;
  if (type === ADOBE_REMOVE_ITEM) {
    const productIds = getProductIdArray(response);
    if (productIds) {
      Object.assign(data, {
        cpj: { product: { id: JSON.stringify(productIds) } }
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
    const impression = JSON.stringify(productCodes.join("|"));
    Object.assign(data.page, {
      products: {
        impression
      }
    });
  }

  const hierarchy = getHierarchyArray(response);
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

export function setDataLayerForPlpDirectCalls(response) {
  const data = window.digitalData;
  let badge;
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
  if (badge) {
    if (data.cpj && data.cpj.product) {
      Object.assign(data.cpj.product, { badge });
    } else if (data.cpj) {
      Object.assign(data.cpj, { product: { badge } });
    } else {
      Object.assign(data, { cpj: { product: { badge } } });
    }
    window.digitalData = data;
  }
}
