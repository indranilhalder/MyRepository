import { getCookieValue } from "./Cookie.js";
export const ADOBE_TARGET_COOKIE_NAME =
  "AMCV_E9174ABF55BA76BA7F000101%40AdobeOrg";
export const ADOBE_TARGET_SPLIT_VALUE = "%7C";
export const ADOBE_TARGET_MCMID = "MCMID";

export function getMcvId() {
  const amcvCookieValue = getCookieValue(ADOBE_TARGET_COOKIE_NAME).split(
    ADOBE_TARGET_SPLIT_VALUE
  );
  const mcvId =
    amcvCookieValue[amcvCookieValue.indexOf(ADOBE_TARGET_MCMID) + 1];
  return mcvId;
}
