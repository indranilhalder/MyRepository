import "isomorphic-fetch";
import cloneDeep from "lodash.clonedeep";
import * as Cookie from "./Cookie";
import {
  SUCCESS,
  FAILURE,
  LOGGED_IN_USER_DETAILS,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS
} from "./constants.js";
import * as ErrorHandling from "../general/ErrorHandling.js";
import { CUSTOMER_ACCESS_TOKEN, GLOBAL_ACCESS_TOKEN } from "../lib/constants";
import { USER_CART_PATH } from "../cart/actions/cart.actions";
let API_URL_ROOT = "https://uat2.tataunistore.com/marketplacewebservices";
let MIDDLEWARE_API_URL_ROOT =
  "http://tmppprd.tataque.com/marketplacewebservices";
export let TATA_CLIQ_ROOT = /https?:[\/]{2}\S*?(\/\S*)/;
export const TOKEN_PATH = "oauth/token";
export let URL_ROOT = "";

if (
  process.env.REACT_APP_STAGE === "devxelp" ||
  process.env.REACT_APP_STAGE === "uat2" ||
  process.env.REACT_APP_STAGE === "local"
) {
  API_URL_ROOT = "https://uat2.tataunistore.com/marketplacewebservices";
  MIDDLEWARE_API_URL_ROOT =
    "https://uat2.tataunistore.com/marketplacewebservices";
} else if (process.env.REACT_APP_STAGE === "tmpprod") {
  API_URL_ROOT = "https://tmppprd.tataunistore.com/marketplacewebservices";
  MIDDLEWARE_API_URL_ROOT = "http://tmppprd.tataque.com/marketplacewebservices";
} else if (process.env.REACT_APP_STAGE === "production") {
  API_URL_ROOT = "https://www.tatacliq.com/marketplacewebservices";
  MIDDLEWARE_API_URL_ROOT = "https://www.tatacliq.com/marketplacewebservices";
} else if (process.env.REACT_APP_STAGE === "p2") {
  API_URL_ROOT = "https://p2.tatacliq.com/marketplacewebservices";
  MIDDLEWARE_API_URL_ROOT = "https://p2app.tatacliq.com/marketplacewebservices";
} else if (process.env.REACT_APP_STAGE === "stage") {
  API_URL_ROOT = "https://stg.tatacliq.com/marketplacewebservices";
  MIDDLEWARE_API_URL_ROOT = "https://stg.tatacliq.com/marketplacewebservices";
}

if (process.env.REACT_APP_STAGE === "tmpprod") {
  URL_ROOT = "https://tmpprd.tataunistore.com";
} else if (process.env.REACT_APP_STAGE === "production") {
  URL_ROOT = "https://www.tatacliq.com";
} else if (process.env.REACT_APP_STAGE === "p2") {
  URL_ROOT = "https://p2.tatacliq.com";
} else if (process.env.REACT_APP_STAGE === "stage") {
  URL_ROOT = "https://stg.tatacliq.com";
} else if (process.env.REACT_APP_STAGE === "devxelp") {
  URL_ROOT = "http://54.147.12.99:3000";
} else if (process.env.REACT_APP_STAGE === "uat2") {
  URL_ROOT = "https://uat2.tataunistore.com";
} else if (process.env.REACT_APP_STAGE === "local") {
  URL_ROOT = "https://uat2.tataunistore.com";
}

export const API_URL_ROOT_DUMMY =
  "https://www.tatacliq.com/marketplacewebservices";
// export const API_URL_ROOT = API_URL_ROOT_DUMMY;
export const API_URL_ROOT_MOCK = "https://cliq-json-server.herokuapp.com";
export const HOME_FEED_API_ROOT =
  "https://tataunistore.tt.omtrdc.net/rest/v1/mbox?client=tataunistore";
export const JUS_PAY_API_URL_ROOT = process.env.REACT_APP_JUS_PAY_API_URL_ROOT;

const ACCESS_TOKEN_EXPIRED_MESSAGE = "Access token expired";
const ACCESS_TOKEN_INVALID_MESSAGE = "Invalid access token";
const CLIENT_ID = "gauravj@dewsolutions.in";
const CUSTOMER_ACCESS_TOKEN_INVALID = "customerAccessTokenInvalid";
const GLOBAL_ACCESS_TOKEN_INVALID = "globalAccessTokenInvalid";
const CART_NOT_FOUND_ERROR = "CartError";

export const API_MSD_URL_ROOT = "https://ap-southeast-1-api.madstreetden.com";

export async function postAdobeTargetUrl(
  path: null,
  mbox,
  marketingCloudVisitorId: null,
  tntId: null,
  useApiRoot: true
) {
  const result = await new Promise((resolve, reject) => {
    window.adobe.target.getOffer({
      mbox: mbox,
      success: function(offer) {
        resolve(offer);
      },
      error: function(status, error) {
        reject(error);
      }
    });
  });

  return result;
}

async function corePost(path, postData, doNotUseApiSuffix) {
  const url = `${API_URL_ROOT}/${path}`;
  return await fetch(url, {
    method: "POST",
    body: JSON.stringify(postData),
    headers: {
      Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#"),
      "Content-Type": "application/json"
    }
  });
}

export async function coreGet(url) {
  return await fetch(`${API_URL_ROOT}/${url}`, {
    headers: {
      Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#")
    }
  });
}

export async function get(url) {
  const result = await coreGet(url);
  const resultClone = result.clone();
  const resultJson = await result.json();
  const errorStatus = ErrorHandling.getFailureResponse(resultJson);

  try {
    if (
      (!errorStatus.status ||
        !isInvalidAccessTokenError(errorStatus.message)) &&
      !isCartNotFoundError(resultJson)
    ) {
      return resultClone;
    }
    let newUrl;

    if (isCartNotFoundError(resultJson)) {
      newUrl = await handleCartNotFoundError(resultJson, url);
    }
    if (isInvalidAccessTokenError(errorStatus.message)) {
      newUrl = await handleInvalidGlobalAccesssTokenOrCustomerAccessToken(
        errorStatus.message,
        url
      );
    }
    return await coreGet(newUrl);
  } catch (e) {
    throw e;
  }
}

export async function coreGetMiddlewareUrl(url) {
  return await fetch(`${MIDDLEWARE_API_URL_ROOT}/${url}`, {
    headers: {
      Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#")
    }
  });
}

export async function getMiddlewareUrl(url) {
  const result = await coreGetMiddlewareUrl(url);
  const resultClone = result.clone();
  const resultJson = await result.json();
  const errorStatus = ErrorHandling.getFailureResponse(resultJson);

  try {
    if (
      (!errorStatus.status ||
        !isInvalidAccessTokenError(errorStatus.message)) &&
      !isCartNotFoundError(resultJson)
    ) {
      return resultClone;
    }
    let newUrl;

    if (isCartNotFoundError(resultJson)) {
      newUrl = await handleCartNotFoundError(resultJson, url);
    }
    if (isInvalidAccessTokenError(errorStatus.message)) {
      newUrl = await handleInvalidGlobalAccesssTokenOrCustomerAccessToken(
        errorStatus.message,
        url
      );
    }
    return await coreGetMiddlewareUrl(newUrl);
  } catch (e) {
    throw e;
  }
}

async function corePostFormData(url, payload) {
  return await fetch(`${API_URL_ROOT}/${url}`, {
    method: "POST",
    body: payload
  });
}

export async function postFormData(url, payload) {
  const result = await corePostFormData(url, payload);
  const resultClone = result.clone();
  const resultJson = await result.json();
  const errorStatus = ErrorHandling.getFailureResponse(resultJson);
  try {
    if (
      (!errorStatus.status ||
        !isInvalidAccessTokenError(errorStatus.message)) &&
      !isCartNotFoundError(resultJson)
    ) {
      return resultClone;
    }
    let newUrl;
    if (isCartNotFoundError(resultJson)) {
      newUrl = await handleCartNotFoundError(resultJson, url);
    }
    if (isInvalidAccessTokenError(errorStatus.message)) {
      newUrl = await handleInvalidGlobalAccesssTokenOrCustomerAccessToken(
        errorStatus.message,
        url
      );
    }
    return await corePostFormData(newUrl, payload);
  } catch (e) {
    throw e;
  }
}

export async function post(path, postData, doNotUseApiSuffix: true) {
  const result = await corePost(path, postData, doNotUseApiSuffix);
  const resultClone = result.clone();
  const resultJson = await result.json();
  const errorStatus = ErrorHandling.getFailureResponse(resultJson);

  try {
    if (
      (!errorStatus.status ||
        !isInvalidAccessTokenError(errorStatus.message)) &&
      !isCartNotFoundError(resultJson)
    ) {
      return resultClone;
    }
    let newUrl;
    if (isCartNotFoundError(resultJson)) {
      newUrl = await handleCartNotFoundError(resultJson, path);
    }
    if (isInvalidAccessTokenError(errorStatus.message)) {
      newUrl = await handleInvalidGlobalAccesssTokenOrCustomerAccessToken(
        errorStatus.message,
        path
      );
    }

    return await corePost(newUrl, postData);
  } catch (e) {
    throw e;
  }
}

async function handleInvalidGlobalAccesssTokenOrCustomerAccessToken(
  message,
  url
) {
  let newUrl = url;
  try {
    newUrl = await handleInvalidCustomerAccessToken(message, url);
    if (newUrl) {
      return newUrl;
    }
    newUrl = await handleInvalidGlobalAccessToken(message, url);
    if (newUrl) {
      return newUrl;
    }
    return newUrl;
  } catch (e) {
    throw e;
  }
}

async function handleInvalidCustomerAccessToken(message, oldUrl) {
  let newUrl = null;
  if (isCustomerAccessTokenFailure(message)) {
    const customerAccessTokenResponse = await refreshCustomerAccessToken();
    if (!customerAccessTokenResponse) {
      throw new Error("Customer Access Token refresh failure ");
    }
    newUrl = replaceOldCustomerCookie(oldUrl, customerAccessTokenResponse);
  }
  return newUrl;
}

async function handleCartNotFoundError(response, oldUrl) {
  let newUrl = null;
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

  if (isCartNotFoundError(response)) {
    const refreshCartIdResponse = await refreshCartId();

    if (!refreshCartIdResponse) {
      throw new Error("Customer Cart id refresh failure");
    }
    if (userDetails && customerCookie) {
      newUrl = replaceOldCartCookieForLoggedInUser(
        oldUrl,
        refreshCartIdResponse
      );
    } else {
      newUrl = replaceOldCartCookieForAnonymnous(oldUrl, refreshCartIdResponse);
    }
  }
  return newUrl;
}
async function handleInvalidGlobalAccessToken(message, oldUrl) {
  let newUrl = oldUrl;
  if (isGlobalAccessTokenFailure(message)) {
    const globalAccessTokenResponse = await refreshGlobalAccessToken();
    if (!globalAccessTokenResponse) {
      throw new Error("Global Access Token refresh failure");
    }

    newUrl = replaceOldGlobalTokenCookie(oldUrl, globalAccessTokenResponse);
  }
  return newUrl;
}

function replaceOldGlobalTokenCookie(url, newGlobalTokenCookie) {
  let oldGlobalCookie = JSON.parse(Cookie.getCookie(GLOBAL_ACCESS_TOKEN));
  Cookie.deleteCookie(GLOBAL_ACCESS_TOKEN);
  Cookie.createCookie(
    GLOBAL_ACCESS_TOKEN,
    JSON.stringify(newGlobalTokenCookie)
  );

  return url.replace(
    oldGlobalCookie.access_token,
    newGlobalTokenCookie.access_token
  );
}

function replaceOldCustomerCookie(url, newCustomerCookie) {
  let oldCustomerCookie = JSON.parse(Cookie.getCookie(CUSTOMER_ACCESS_TOKEN));
  Cookie.deleteCookie(CUSTOMER_ACCESS_TOKEN);
  Cookie.createCookie(CUSTOMER_ACCESS_TOKEN, JSON.stringify(newCustomerCookie));
  return url.replace(
    oldCustomerCookie.access_token,
    newCustomerCookie.access_token
  );
}
async function replaceOldCartCookieForLoggedInUser(url, newCustomerCookie) {
  let oldCustomerCookie = JSON.parse(
    Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER)
  );
  Cookie.deleteCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  Cookie.createCookie(
    CART_DETAILS_FOR_LOGGED_IN_USER,
    JSON.stringify(newCustomerCookie)
  );
  return url.replace(oldCustomerCookie.code, newCustomerCookie.code);
}

async function replaceOldCartCookieForAnonymnous(url, newCustomerCookie) {
  let oldCustomerCookie = JSON.parse(
    Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS)
  );

  Cookie.deleteCookie(CART_DETAILS_FOR_ANONYMOUS);
  Cookie.createCookie(
    CART_DETAILS_FOR_ANONYMOUS,
    JSON.stringify(newCustomerCookie)
  );
  return url.replace(oldCustomerCookie.guid, newCustomerCookie.guid);
}
async function refreshCustomerAccessToken() {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  if (!JSON.parse(customerCookie).refresh_token) {
    throw new Error("No refresh token for expired customer access token");
  }
  const refreshTokenResponse = await post(
    `${TOKEN_PATH}?refresh_token=${
      JSON.parse(customerCookie).refresh_token
    }&client_id=${CLIENT_ID}&client_secret=secret&grant_type=refresh_token`
  );
  const refreshTokenResultJson = await refreshTokenResponse.json();
  const errorStatusObject = ErrorHandling.getFailureResponse(
    refreshTokenResultJson
  );
  if (errorStatusObject.status) {
    // the refresh token has failed
    // what do I do in this case?
    // I return null, so that I can throw an error which will be caught.
    return null;
  }

  return refreshTokenResultJson;
}

async function refreshGlobalAccessToken() {
  const result = await post(
    `${TOKEN_PATH}?grant_type=client_credentials&client_id=${CLIENT_ID}&client_secret=secret&isPwa=true`
  );
  const resultJson = await result.json();
  const errorStatusObj = ErrorHandling.getFailureResponse(resultJson);
  if (errorStatusObj.status) {
    return null;
  }

  return resultJson;
}

async function refreshCartId() {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

  if (userDetails && customerCookie) {
    return await refreshCartIdForLoggedUser();
  } else {
    return await refreshCartIdForAnonymous();
  }
}

async function refreshCartIdForLoggedUser() {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

  const resultForGetCartId = await get(
    `${USER_CART_PATH}/${JSON.parse(userDetails).userName}/carts?access_token=${
      JSON.parse(customerCookie).access_token
    }&isPwa=true`
  );
  const resultForGetCartIdJson = await resultForGetCartId.json();
  const errorStatusForGetCartIdObj = ErrorHandling.getFailureResponse(
    resultForGetCartIdJson
  );
  if (
    errorStatusForGetCartIdObj.status ||
    !resultForGetCartIdJson ||
    !resultForGetCartIdJson.code
  ) {
    const resultForCreateCartId = await post(
      `${USER_CART_PATH}/${
        JSON.parse(userDetails).userName
      }/carts?access_token=${
        JSON.parse(customerCookie).access_token
      }&isPwa=true`
    );
    const resultForCreateCartIdJson = await resultForCreateCartId.json();
    const errorStatusForCreateCartObj = ErrorHandling.getFailureResponse(
      resultForCreateCartIdJson
    );
    if (errorStatusForCreateCartObj.status) {
      return null;
    }
    return resultForCreateCartIdJson;
  }
  return resultForGetCartIdJson;
}
async function refreshCartIdForAnonymous() {
  let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  const resultForCreateCartId = await post(
    `${USER_CART_PATH}/anonymous/carts?access_token=${
      JSON.parse(globalCookie).access_token
    }&isPwa=true`
  );
  const resultForCreateCartIdJson = await resultForCreateCartId.json();
  const errorStatusForCreateCartObj = ErrorHandling.getFailureResponse(
    resultForCreateCartIdJson
  );
  if (errorStatusForCreateCartObj.status) {
    return null;
  }
  return resultForCreateCartIdJson;
}
function isCustomerAccessTokenFailure(message) {
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const customerAccessToken =
    customerCookie && JSON.parse(customerCookie).access_token;
  if (message.indexOf(customerAccessToken) >= 0) {
    return true;
  }
  return false;
}

function isGlobalAccessTokenFailure(message) {
  const globalAccessTokenCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  const globalAccessToken =
    globalAccessTokenCookie && JSON.parse(globalAccessTokenCookie).access_token;
  if (message.indexOf(globalAccessToken) >= 0) {
    return true;
  }
  return false;
}

function isInvalidAccessTokenError(message) {
  return (
    message.indexOf(ACCESS_TOKEN_EXPIRED_MESSAGE) >= 0 ||
    message.indexOf(ACCESS_TOKEN_INVALID_MESSAGE) >= 0
  );
}
function isCartNotFoundError(resultJson) {
  return (
    resultJson &&
    resultJson.errors &&
    resultJson.errors[0] &&
    resultJson.errors[0].type === CART_NOT_FOUND_ERROR
  );
}
export async function getWithoutApiUrlRoot(url) {
  return await fetch(url, {
    headers: {
      Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#")
    }
  });
}

export async function postMsd(url, payload) {
  return await fetch(url, {
    method: "POST",
    body: payload
  });
}

export async function postJusPay(path, postData) {
  let url = `${JUS_PAY_API_URL_ROOT}/${path}`;
  return await fetch(url, {
    method: "POST",
    body: postData
  });
}

// this function is using in follow and un follow brands
// because there we have to send payload in formData or Row Data format in msd api

export async function postMsdRowData(url, payload) {
  return await fetch(`${API_MSD_URL_ROOT}/${url}`, {
    method: "POST",
    body: JSON.stringify(payload),
    headers: {
      "Content-Type": "application/json"
    }
  });
}
