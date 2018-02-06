import "isomorphic-fetch";
export const API_BASE_URL_ROOT =
  "https://fierce-bastion-16980.herokuapp.com/marketplacewebservices";

export const API_URL_ROOT = "https://cliq-json-server.herokuapp.com";

export async function postRealData(path) {
  return await fetch(`${API_BASE_URL_ROOT}/${path}`, {
    method: "POST",
    headers: {
      Authorization: "Basic " + btoa("gauravj@dewsolutions.in:gauravj@12#")
    }
  });
}

export async function post(url, payload) {
  return await fetch(`${API_URL_ROOT}/${url}`, {
    method: "POST",
    body: JSON.stringify(payload),
    headers: {
      access_token: localStorage.getItem("authorizationKey")
    }
  });
}

export async function get(url) {
  return await fetch(`${API_URL_ROOT}/${url}`, {});
}

export async function patch(url, payload) {
  return await fetch(`${API_URL_ROOT}/${url}`, {
    method: "PATCH",
    body: JSON.stringify(payload),
    headers: {
      access_token: localStorage.getItem("authorizationKey")
    }
  });
}

export async function put(url, payload) {
  return await fetch(`${API_URL_ROOT}/${url}`, {
    method: "PUT",
    body: JSON.stringify(payload),
    headers: {
      access_token: localStorage.getItem("authorizationKey")
    }
  });
}
