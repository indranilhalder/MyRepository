import "isomorphic-fetch";
import * as Cookie from "./Cookie";
import { LOGGED_IN_USER_DETAILS } from "./constants.js";
export const API_URL_ROOT =
  "https://uat2.tataunistore.com/marketplacewebservices";
export const API_URL_ROOT_DUMMY =
  "https://www.tatacliq.com/marketplacewebservices";
export const API_URL_ROOT_MOCK = "https://cliq-json-server.herokuapp.com";
export const HOME_FEED_API_ROOT =
  "https://tataunistore.tt.omtrdc.net/rest/v1/mbox?client=tataunistore";
export const JUS_PAY_API_URL_ROOT = "https://sandbox.juspay.in";

export const TATA_CLIQ_ROOT = "https://www.tatacliq.com";
export const API_MSD_URL_ROOT = "https://ap-southeast-1-api.madstreetden.com";

export async function postAdobeTargetUrl(
  path: null,
  mbox,
  marketingCloudVisitorId: null,
  tntId: null,
  useApiRoot: true
) {
  let url;

  // I want to use the API URL ROOT and I have a patj
  // I want to use the API url root and I have no path
  // I want to just use the path

  if (!useApiRoot) {
    url = path;
  }

  if (useApiRoot && path !== null) {
    url = `${url}/${path}`;
  }

  if (useApiRoot && path === null) {
    url = `${HOME_FEED_API_ROOT}`;
  }

  return await fetch(url, {
    method: "POST",
    body: JSON.stringify({
      mbox,
      marketingCloudVisitorId,
      tntId
    }),
    headers: {
      "Content-Type": "application/json"
    }
  });
}

export async function post(path, postData, doNotUseApiRoot: false) {
  console.log(JSON.stringify(postData));
  let url = `${API_URL_ROOT}/${path}`;
  if (doNotUseApiRoot) {
    url = path;
  }

  return await fetch(url, {
    method: "POST",
    body: JSON.stringify(postData),
    headers: {
      Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#"),
      "Content-Type": "application/json"
    }
  });
}

export async function get(url) {
  return await fetch(`${API_URL_ROOT}/${url}`, {
    headers: {
      Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#")
    }
  });
}

export async function patch(url, payload) {
  return await fetch(`${API_URL_ROOT}/${url}`, {
    method: "PATCH",
    body: JSON.stringify(payload),
    headers: {
      Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#")
    }
  });
}

export async function put(url, payload) {
  return await fetch(`${API_URL_ROOT}/${url}`, {
    method: "PUT",
    body: JSON.stringify(payload),
    headers: {
      Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#")
    }
  });
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
  return await fetch(`${API_MSD_URL_ROOT}/${url}`, {
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
  let url = `${JUS_PAY_API_URL_ROOT}/${path}`;
  return await fetch(url, {
    method: "POST",
    body: JSON.stringify(postData)
  });
}
