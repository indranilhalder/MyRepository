import { getCookieValue, getCookie } from "./Cookie.js";
import { setInterval, clearInterval } from "timers";
import * as constants from "../lib/constants.js";
import { userAddressFailure } from "../cart/actions/cart.actions";
import {
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
export const ADOBE_HOME_TYPE = "home";
export const ADOBE_PDP_TYPE = "pdp";
export const ADOBE_CART_TYPE = "cart";
export const ADOBE_PDP_CPJ = "cpj_pdp";
export const ADOBE_ADD_TO_CART = "cpj_add_to_cart";
export const ICID2 = "ICID2";
export const CID = "CID";
export const LOGIN_WITH_MOBIEL_NUMBER = "mobile";
export const LOGIN_WITH_FACEBOOK = "google";
export const LOGIN_WITH_GOOGLE = "google";
export const LOGIN_WITH_EMAIL = "mobile";
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

  if (type === ADOBE_PDP_TYPE) {
    window.digitalData = getDigitalDataForPdp(type, response);
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
  const seoBreadCrumbs = pdpResponse
    ? pdpResponse.seo
      ? pdpResponse.seo.breadcrumbs
          .map(val => {
            return val.name.toLowerCase().replace(/\s+/g, "_");
          })
          .reverse()
      : ["", "", "", ""]
    : ["", "", "", ""];
  const categoryHierarchy = pdpResponse.categoryHierarchy.map(val => {
    return val.category_name.toLowerCase().replace(/\s+/g, "_");
  });

  const data = {
    cpj: {
      product: {
        id: pdpResponse.productListingId,
        price: pdpResponse.mrpPrice.doubleValue,
        discount: pdpResponse.winningSellerPrice.doubleValue
      },

      brand: {
        name: pdpResponse.brandName
      }
    },

    page: {
      category: {
        primaryCategory: "product",
        subCategory1: categoryHierarchy[0],
        subCategory2: categoryHierarchy[1],
        subCategory3: categoryHierarchy[2]
      },
      display: {
        hierarchy: ["home", ...seoBreadCrumbs]
      },
      pageInfo: {
        pageName: "product details"
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
