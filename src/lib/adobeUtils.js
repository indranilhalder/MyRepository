import { getCookieValue, getCookie } from "./Cookie.js";
import { setInterval, clearInterval } from "timers";
import * as constants from "../lib/constants.js";

export const ADOBE_TARGET_COOKIE_NAME =
  "AMCV_E9174ABF55BA76BA7F000101%40AdobeOrg";
export const ADOBE_TARGET_SPLIT_VALUE = "%7C";
export const ADOBE_TARGET_MCMID = "MCMID";
export const ADOBE_TARGET_WAIT_TIME = 2000;
const ADOBE_SATELLITE_CODE = "page view";

export function setDataLayer(routerProps) {
  const path = routerProps.path;
  if (window.digitalData) {
    if (path === constants.HOME_ROUTER) {
      window.digitalData = getDigitalDataForHome();
    }
    window._satellite.track(ADOBE_SATELLITE_CODE);
  }
}

function getDigitalDataForHome() {
  const userDetails = getCookie(constants.LOGGED_IN_USER_DETAILS);
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
  if (userDetails) {
    data.account = {
      login: {
        customerID: userDetails.customerId
      }
    };
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
