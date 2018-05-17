import { SUCCESS, REQUESTING, FAILURE } from "../../lib/constants";

export const ALL_AUTH_CALLS_REQUEST = "ALL_AUTH_CALLS_REQUEST";
export const ALL_AUTH_CALLS_SUCCESS = "ALL_AUTH_CALLS_SUCCESS";
export const ANY_AUTH_CALLS_FAILED = "ANY_AUTH_CALLS_FAILED";
export const SET_FALSE_ALL_AUTH_CALLS_SUCCESS_FLAG =
  "SET_FALSE_ALL_AUTH_CALLS_SUCCESS_FLAG";

export const SET_URL_TO_REDIRECT_TO_AFTER_AUTH =
  "SET_URL_TO_REDIRECT_TO_AFTER_AUTH";
export const CLEAR_URL_TO_REDIRECT_TO_AFTER_AUTH =
  "CLEAR_URL_TO_REDIRECT_TO_AFTER_AUTH";
export const STOP_LOADING_ON_LOGIN = "STOP_LOADING_ON_LOGIN";
export function setUrlToRedirectToAfterAuth(url) {
  return {
    type: SET_URL_TO_REDIRECT_TO_AFTER_AUTH,
    url
  };
}

export function clearUrlToRedirectToAfterAuth() {
  return {
    type: CLEAR_URL_TO_REDIRECT_TO_AFTER_AUTH
  };
}

export function authCallsAreInProgress() {
  return {
    type: ALL_AUTH_CALLS_REQUEST,
    status: REQUESTING
  };
}

export function singleAuthCallHasFailed(error) {
  return {
    type: ANY_AUTH_CALLS_FAILED,
    status: FAILURE,
    error
  };
}

export function setIfAllAuthCallsHaveSucceeded() {
  return {
    type: ALL_AUTH_CALLS_SUCCESS,
    status: SUCCESS
  };
}

export function setFalseForAllAuthCallHasSucceedFlag() {
  return {
    type: SET_FALSE_ALL_AUTH_CALLS_SUCCESS_FLAG
  };
}
export function stopLoaderOnLoginForOTPVerification() {
  return {
    type: STOP_LOADING_ON_LOGIN
  };
}
