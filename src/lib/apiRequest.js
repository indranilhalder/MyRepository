import "isomorphic-fetch";
export const API_URL_ROOT = "https://cliq-json-server.herokuapp.com";

export async function post(url, payload) {
  return await fetch(`${API_URL_ROOT}/${url}`, {
    method: "POST",
    body: JSON.stringify(payload),
    headers: {
      Authorization: localStorage.getItem("authorizationKey")
    }
  });
}

export async function get(url) {
  return await fetch(`${API_URL_ROOT}/${url}`, {
    headers: {
      Authorization: localStorage.getItem("authorizationKey")
    }
  });
}

export async function patch(url, payload) {
  return await fetch(`${API_URL_ROOT}/${url}`, {
    method: "PATCH",
    body: JSON.stringify(payload),
    headers: {
      Authorization: localStorage.getItem("authorizationKey")
    }
  });
}

export async function put(url, payload) {
  return await fetch(`${API_URL_ROOT}/${url}`, {
    method: "PUT",
    body: JSON.stringify(payload),
    headers: {
      Authorization: localStorage.getItem("authorizationKey")
    }
  });
}
