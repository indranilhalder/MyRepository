//TODO why are some cookies session and why are some timestamped?
// What is the difference?
export const ADOBE_TARGET_COOKIE_NAME =
  "AMCV_E9174ABF55BA76BA7F000101%40AdobeOrg";
export const ADOBE_TARGET_SPLIT_VALUE = "%7C";
export const ADOBE_TARGET_MCMID = "MCMID";

export function createCookie(name, value, days) {
  let expires;
  if (days) {
    let date = new Date();
    date.setSeconds(date.getSeconds() + days);
    expires = `; expires=${date}`;
  } else {
    expires = "";
  }
  document.cookie = `${name}=${value + expires}`;
}

export function getCookie(cookieName) {
  let match = document.cookie.match(new RegExp(cookieName + `=([^;]+)`));
  if (match) return match[1];
}

export function deleteCookie(cookieName) {
  document.cookie =
    cookieName + "=; Path=/; Expires=Thu, 01 Jan 1970 00:00:01 GMT;";
}
export function getCookieValue(name) {
  var b = document.cookie.match("(^|;)\\s*" + name + "\\s*=\\s*([^;]+)");

  return b ? b.pop() : "";
}
