import "isomorphic-fetch";
import cloneDeep from "lodash.clonedeep";
import * as Cookie from "./Cookie";
import { LOGGED_IN_USER_DETAILS, SUCCESS, FAILURE } from "./constants.js";
import * as ErrorHandling from "../general/ErrorHandling.js";
import { CUSTOMER_ACCESS_TOKEN, GLOBAL_ACCESS_TOKEN } from "../lib/constants";
let API_URL_ROOT = "https://uat2.tataunistore.com/marketplacewebservices";
export let TATA_CLIQ_ROOT = /https?:[\/]{2}\S*?(\/\S*)/;
export const TOKEN_PATH = "oauth/token";

if (
  process.env.REACT_APP_STAGE === "devxelp" ||
  process.env.REACT_APP_STAGE === "uat2" ||
  process.env.REACT_APP_STAGE === "local"
) {
  API_URL_ROOT = "https://uat2.tataunistore.com/marketplacewebservices";
} else if (process.env.REACT_APP_STAGE === "tmpprod") {
  API_URL_ROOT = "https://p2tmppprd.tataunistore.com/marketplacewebservices";
} else if (process.env.REACT_APP_STAGE === "production") {
  API_URL_ROOT = "https://www.tatacliq.com/marketplacewebservices";
} else if (process.env.REACT_APP_STAGE === "p2") {
  API_URL_ROOT = "https://p2.tatacliq.com/marketplacewebservices";
} else if (process.env.REACT_APP_STAGE === "stage") {
  API_URL_ROOT = "https://stg.tatacliq.com/marketplacewebservices";
}

export const API_URL_ROOT_DUMMY =
  "https://www.tatacliq.com/marketplacewebservices";
// export const API_URL_ROOT = API_URL_ROOT_DUMMY;
export const API_URL_ROOT_MOCK = "https://cliq-json-server.herokuapp.com";
export const HOME_FEED_API_ROOT =
  "https://tataunistore.tt.omtrdc.net/rest/v1/mbox?client=tataunistore";
export const JUS_PAY_API_URL_ROOT = process.env.REACT_APP_JUS_PAY_API_URL_ROOT;

const API_URL_ROOT_SUFFIX = "?isPwa=true";
const ACCESS_TOKEN_EXPIRED_MESSAGE = "Access token expired";
const ACCESS_TOKEN_INVALID_MESSAGE = "Invalid access token";
const CLIENT_ID = "gauravj@dewsolutions.in";
const CUSTOMER_ACCESS_TOKEN_INVALID = "customerAccessTokenInvalid";
const GLOBAL_ACCESS_TOKEN_INVALID = "globalAccessTokenInvalid";
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

export async function post(path, postData, doNotUserApiSuffix: true) {
  const requestFunction = async path => {
    const url = `${API_URL_ROOT}/${path}`;
    const result = await fetch(url, {
      method: "POST",
      body: JSON.stringify(postData),
      headers: {
        Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#"),
        "Content-Type": "application/json"
      }
    });
    return result;
  };

  const result = await requestFunction(path);
  let isHavingAccessTokenError = await isResultHavingAccessTokenError(result);
  if (isHavingAccessTokenError !== SUCCESS) {
    if (isHavingAccessTokenError === CUSTOMER_ACCESS_TOKEN_INVALID) {
      let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

      const refreshTokenResultStatus = await refreshToken();

      if (refreshTokenResultStatus.status === SUCCESS) {
        let newUrl = path.replace(
          JSON.parse(customerCookie).access_token,
          refreshTokenResultStatus.accessToken
        );

        return requestFunction(newUrl);
      }
    } else if (isHavingAccessTokenError === GLOBAL_ACCESS_TOKEN_INVALID) {
      const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
      let globalTokenResultStatus = await globalAccessToken();

      if (globalTokenResultStatus.status === SUCCESS) {
        let newUrl = path.replace(
          JSON.parse(globalCookie).access_token,
          globalTokenResultStatus.accessToken
        );
        return requestFunction(newUrl);
      }
    } else {
      return result.clone();
    }
  }

  return result.clone();
}

export async function getWithoutApiUrlRoot(url) {
  const requestFunction = async url => {
    const result = await fetch(url, {
      headers: {
        Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#")
      }
    });
    return result;
  };
  const result = await requestFunction(url);
  let isHavingAccessTokenError = await isResultHavingAccessTokenError(result);
  if (isHavingAccessTokenError !== SUCCESS) {
    if (isHavingAccessTokenError === CUSTOMER_ACCESS_TOKEN_INVALID) {
      let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

      const refreshTokenResultStatus = await refreshToken();

      if (refreshTokenResultStatus.status === SUCCESS) {
        let newUrl = url.replace(
          JSON.parse(customerCookie).access_token,
          refreshTokenResultStatus.accessToken
        );

        return requestFunction(newUrl);
      }
    } else if (isHavingAccessTokenError === GLOBAL_ACCESS_TOKEN_INVALID) {
      const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
      let globalTokenResultStatus = await globalAccessToken();
      if (globalTokenResultStatus.status === SUCCESS) {
        let newUrl = url.replace(
          JSON.parse(globalCookie).access_token,
          globalTokenResultStatus.accessToken
        );
        return requestFunction(newUrl);
      }
    } else {
      return result.clone();
    }
  }

  return result.clone();
}

export async function get(url) {
  const requestFunction = async url => {
    const result = await fetch(`${API_URL_ROOT}/${url}`, {
      headers: {
        Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#")
      }
    });
    return result;
  };
  const result = await requestFunction(url);
  let isHavingAccessTokenError = await isResultHavingAccessTokenError(result);
  if (isHavingAccessTokenError !== SUCCESS) {
    if (isHavingAccessTokenError === CUSTOMER_ACCESS_TOKEN_INVALID) {
      let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

      const refreshTokenResultStatus = await refreshToken();

      if (refreshTokenResultStatus.status === SUCCESS) {
        let newUrl = url.replace(
          JSON.parse(customerCookie).access_token,
          refreshTokenResultStatus.accessToken
        );

        return requestFunction(newUrl);
      }
    } else if (isHavingAccessTokenError === GLOBAL_ACCESS_TOKEN_INVALID) {
      const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
      let globalTokenResultStatus = await globalAccessToken();

      if (globalTokenResultStatus.status === SUCCESS) {
        let newUrl = url.replace(
          JSON.parse(globalCookie).access_token,
          globalTokenResultStatus.accessToken
        );
        return requestFunction(newUrl);
      }
    } else {
      return result.clone();
    }
  }

  return result.clone();
}

export async function patch(url, payload) {
  const requestFunction = async url => {
    const result = await fetch(`${API_URL_ROOT}/${url}`, {
      method: "PATCH",
      body: JSON.stringify(payload),
      headers: {
        Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#")
      }
    });
    return result;
  };

  const result = await requestFunction(url);
  let isHavingAccessTokenError = await isResultHavingAccessTokenError(result);
  if (isHavingAccessTokenError !== SUCCESS) {
    if (isHavingAccessTokenError === CUSTOMER_ACCESS_TOKEN_INVALID) {
      let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

      const refreshTokenResultStatus = await refreshToken();

      if (refreshTokenResultStatus.status === SUCCESS) {
        let newUrl = url.replace(
          JSON.parse(customerCookie).access_token,
          refreshTokenResultStatus.accessToken
        );

        return requestFunction(newUrl);
      }
    } else if (isHavingAccessTokenError === GLOBAL_ACCESS_TOKEN_INVALID) {
      const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
      let globalTokenResultStatus = await globalAccessToken();
      if (globalTokenResultStatus.status === SUCCESS) {
        let newUrl = url.replace(
          JSON.parse(globalCookie).access_token,
          globalTokenResultStatus.accessToken
        );
        return requestFunction(newUrl);
      }
    } else {
      return result.clone();
    }
  }

  return result.clone();
}

export async function put(url, payload) {
  const requestFunction = async url => {
    const result = await fetch(`${API_URL_ROOT}/${url}`, {
      method: "PUT",
      body: JSON.stringify(payload),
      headers: {
        Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#")
      }
    });
    return result;
  };
  const result = await requestFunction(url);
  let isHavingAccessTokenError = await isResultHavingAccessTokenError(result);
  if (isHavingAccessTokenError !== SUCCESS) {
    if (isHavingAccessTokenError === CUSTOMER_ACCESS_TOKEN_INVALID) {
      let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

      const refreshTokenResultStatus = await refreshToken();

      if (refreshTokenResultStatus.status === SUCCESS) {
        let newUrl = url.replace(
          JSON.parse(customerCookie).access_token,
          refreshTokenResultStatus.accessToken
        );

        return requestFunction(newUrl);
      }
    } else if (isHavingAccessTokenError === GLOBAL_ACCESS_TOKEN_INVALID) {
      const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
      let globalTokenResultStatus = await globalAccessToken();
      if (globalTokenResultStatus.status === SUCCESS) {
        let newUrl = url.replace(
          JSON.parse(globalCookie).access_token,
          globalTokenResultStatus.accessToken
        );
        return requestFunction(newUrl);
      }
    } else {
      return result.clone();
    }
  }

  return result.clone();
}

export async function postMock(url, payload) {
  return await fetch(`${API_URL_ROOT_MOCK}/${url}`, {
    method: "POST",
    body: JSON.stringify(payload),
    headers: {
      access_token: localStorage.getItem("authorizationKey")
    }
  });
}

export async function getMock(url) {
  return await fetch(`${API_URL_ROOT_MOCK}/${url}`, {});
}

export async function patchMock(url, payload) {
  return await fetch(`${API_URL_ROOT_MOCK}/${url}`, {
    method: "PATCH",
    body: JSON.stringify(payload),
    headers: {
      access_token: localStorage.getItem("authorizationKey")
    }
  });
}

export async function putMock(url, payload) {
  return await fetch(`${API_URL_ROOT_MOCK}/${url}`, {
    method: "PUT",
    body: JSON.stringify(payload),
    headers: {
      access_token: localStorage.getItem("authorizationKey")
    }
  });
}

export async function postMsd(url, payload) {
  return await fetch(url, {
    method: "POST",
    body: payload
  });
}

export async function getMsd(url) {
  return await fetch(`${API_URL_ROOT_DUMMY}/${url}`, {
    headers: {
      Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#")
    }
  });
}

export async function postJusPay(path, postData) {
  const requestFunction = async path => {
    let url = `${JUS_PAY_API_URL_ROOT}/${path}`;
    const result = await fetch(url, {
      method: "POST",
      body: postData
    });
    return result;
  };

  const result = await requestFunction(path);
  let isHavingAccessTokenError = await isResultHavingAccessTokenError(result);
  if (isHavingAccessTokenError !== SUCCESS) {
    if (isHavingAccessTokenError === CUSTOMER_ACCESS_TOKEN_INVALID) {
      let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

      const refreshTokenResultStatus = await refreshToken();

      if (refreshTokenResultStatus.status === SUCCESS) {
        let newUrl = path.replace(
          JSON.parse(customerCookie).access_token,
          refreshTokenResultStatus.accessToken
        );

        return requestFunction(newUrl);
      }
    } else if (isHavingAccessTokenError === GLOBAL_ACCESS_TOKEN_INVALID) {
      const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
      let globalTokenResultStatus = await globalAccessToken();
      if (globalTokenResultStatus.status === SUCCESS) {
        let newUrl = path.replace(
          JSON.parse(globalCookie).access_token,
          globalTokenResultStatus.accessToken
        );
        return requestFunction(newUrl);
      }
    } else {
      return result.clone();
    }
  }

  return result.clone();
}

export async function postFormData(url, payload) {
  const requestFunction = async url => {
    const result = await fetch(`${API_URL_ROOT}/${url}`, {
      method: "POST",
      body: payload
    });
    return result;
  };

  const result = await requestFunction(url);
  let isHavingAccessTokenError = await isResultHavingAccessTokenError(result);
  if (isHavingAccessTokenError !== SUCCESS) {
    if (isHavingAccessTokenError === CUSTOMER_ACCESS_TOKEN_INVALID) {
      let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

      const refreshTokenResultStatus = await refreshToken();

      if (refreshTokenResultStatus.status === SUCCESS) {
        let newUrl = url.replace(
          JSON.parse(customerCookie).access_token,
          refreshTokenResultStatus.accessToken
        );

        return requestFunction(newUrl);
      }
    } else if (isHavingAccessTokenError === GLOBAL_ACCESS_TOKEN_INVALID) {
      const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
      let globalTokenResultStatus = await globalAccessToken();
      if (globalTokenResultStatus.status === SUCCESS) {
        let newUrl = url.replace(
          JSON.parse(globalCookie).access_token,
          globalTokenResultStatus.accessToken
        );
        return requestFunction(newUrl);
      }
    } else {
      return result.clone();
    }
  }

  return result.clone();
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

export async function refreshToken() {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const refreshTokenApiCall = await post(
    `${TOKEN_PATH}?refresh_token=${
      JSON.parse(customerCookie).refresh_token
    }&client_id=${CLIENT_ID}&client_secret=secret&grant_type=refresh_token`
  );

  const refreshTokenResultJson = await refreshTokenApiCall.clone().json();
  const refreshTokenResultJsonStatus = await isResultHavingAccessTokenError(
    refreshTokenApiCall
  );

  if (
    refreshTokenResultJsonStatus === SUCCESS &&
    refreshTokenResultJson.access_token
  ) {
    Cookie.createCookie(
      CUSTOMER_ACCESS_TOKEN,
      JSON.stringify(refreshTokenResultJson),
      refreshTokenResultJson.expires_in
    );
    return {
      status: SUCCESS,
      accessToken: refreshTokenResultJson.access_token
    };
  }

  return {
    status: FAILURE,
    accessToken: null
  };
}

export async function globalAccessToken(url, requestType, body) {
  const globalAccessTokenApiCall = await post(
    `${TOKEN_PATH}?grant_type=client_credentials&client_id=${CLIENT_ID}&client_secret=secret&isPwa=true`
  );
  const globalAccessTokenResultJson = await globalAccessTokenApiCall
    .clone()
    .json();

  const globalAccessTokenResultJsonStatus = await isResultHavingAccessTokenError(
    globalAccessTokenApiCall
  );

  if (
    globalAccessTokenResultJsonStatus === SUCCESS &&
    globalAccessTokenResultJson.access_token
  ) {
    Cookie.createCookie(
      GLOBAL_ACCESS_TOKEN,
      JSON.stringify(globalAccessTokenResultJson),
      globalAccessTokenResultJson.expires_in
    );

    return {
      status: SUCCESS,
      accessToken: globalAccessTokenResultJson.access_token
    };
  }

  return {
    status: FAILURE,
    accessToken: null
  };
}

export async function isResultHavingAccessTokenError(result) {
  const resultJson = await result.clone().json();

  const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

  if (resultJsonStatus.status) {
    if (
      resultJsonStatus.message.indexOf(ACCESS_TOKEN_EXPIRED_MESSAGE) >= 0 ||
      resultJsonStatus.message.indexOf(ACCESS_TOKEN_INVALID_MESSAGE) >= 0
    ) {
      let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
      let customerToken =
        customerCookie && JSON.parse(customerCookie).access_token;
      if (resultJsonStatus.message.indexOf(customerToken) >= 0) {
        return CUSTOMER_ACCESS_TOKEN_INVALID;
      } else {
        return GLOBAL_ACCESS_TOKEN_INVALID;
      }
    }
    return FAILURE;
  } else {
    return SUCCESS;
  }
}
