import { getCookieValue, getCookie } from "./Cookie.js";
import { setInterval, clearInterval } from "timers";
import * as constants from "../lib/constants.js";
import { userAddressFailure } from "../cart/actions/cart.actions";

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

export function setDataLayer(type, response, icid) {
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

  if (userDetails) {
    window.digitalData.account = {
      login: {
        customerID: userDetails.customerId
      }
    };
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
  const seoBreadCrumbs = pdpResponse.seo
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
      pdp: {
        findingMethod: window.digitalData && window.digitalData.pageName
      },
      brand: {
        name: pdpResponse.brandName
      }
    },
    flag: "internal_campaign",
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
  return data;
}

function getDigitalDataForHome() {
  const data = {
    page: {
      category: {
        primaryCategory: "homepage"
      }
    },
    pageInfo: {
      pageName: "homepage"
    }
  };

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
