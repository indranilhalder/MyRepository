import { SUCCESS, REQUESTING, ERROR, FAILURE } from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  FAILURE_UPPERCASE,
  CART_DETAILS_FOR_ANONYMOUS,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  DEFAULT_PIN_CODE_LOCAL_STORAGE,
  GLOBAL_ACCESS_TOKEN
} from "../../lib/constants";

export const GET_USER_DETAILS_REQUEST = "GET_USER_DETAILS_REQUEST";
export const GET_USER_DETAILS_SUCCESS = "GET_USER_DETAILS_SUCCESS";
export const GET_USER_DETAILS_FAILURE = "GET_USER_DETAILS_FAILURE";

export const GET_SAVED_CARD_REQUEST = "GET_SAVED_CARD_REQUEST";
export const GET_SAVED_CARD_SUCCESS = "GET_SAVED_CARD_SUCCESS";
export const GET_SAVED_CARD_FAILURE = "GET_SAVED_CARD_FAILURE";

export const REMOVE_SAVED_CARD_REQUEST = "REMOVE_SAVED_CARD_REQUEST";
export const REMOVE_SAVED_CARD_SUCCESS = "REMOVE_SAVED_CARD_SUCCESS";
export const REMOVE_SAVED_CARD_FAILURE = "REMOVE_SAVED_CARD_FAILURE";

export const GET_ALL_ORDERS_REQUEST = "GET_ALL_ORDERS_REQUEST";
export const GET_ALL_ORDERS_SUCCESS = "GET_ALL_ORDERS_SUCCESS";
export const GET_ALL_ORDERS_FAILURE = "GET_ALL_ORDERS_FAILURE";

export const GET_WISHLIST_REQUEST = "GET_WISHLIST_REQUEST";
export const GET_WISHLIST_SUCCESS = "GET_WISHLIST_SUCCESS";
export const GET_WISHLIST_FAILURE = "GET_WISHLIST_FAILURE";

export const GET_USER_COUPON_REQUEST = "GET_USER_COUPON_REQUEST";
export const GET_USER_COUPON_SUCCESS = "GET_USER_COUPON_SUCCESS";
export const GET_USER_COUPON_FAILURE = "GET_USER_COUPON_FAILURE";

export const GET_USER_ALERTS_REQUEST = "GET_USER_ALERTS_REQUEST";
export const GET_USER_ALERTS_SUCCESS = "GET_USER_ALERTS_SUCCESS";
export const GET_USER_ALERTS_FAILURE = "GET_USER_ALERTS_FAILURE";

export const CURRENT_PAGE = 0;
export const PAGE_SIZE = 10;
export const PLATFORM_NUMBER = 2;
export const USER_PATH = "v2/mpl/users";
const CARD_TYPE = "BOTH";

export function getSavedCardRequest() {
  return {
    type: GET_SAVED_CARD_REQUEST,
    status: REQUESTING
  };
}
export function getSavedCardSuccess(savedCards) {
  return {
    type: GET_SAVED_CARD_SUCCESS,
    status: SUCCESS,
    savedCards
  };
}

export function getSavedCardFailure(error) {
  return {
    type: GET_SAVED_CARD_FAILURE,
    status: ERROR,
    error
  };
}

export function getSavedCardDetails(userId, customerAccessToken) {
  return async (dispatch, getState, { api }) => {
    dispatch(getSavedCardRequest());
    try {
      const result = await api.post(
        `${USER_PATH}/${userId}/payments/savedCards?access_token=${customerAccessToken}&cardType=${CARD_TYPE}`
      );
      const resultJson = await result.json();

      if (resultJson.errors) {
        throw new Error(resultJson.errors[0].message);
      }
      dispatch(getSavedCardSuccess(resultJson));
    } catch (e) {
      dispatch(getSavedCardFailure(e.message));
    }
  };
}

export function removeSavedCardRequest() {
  return {
    type: REMOVE_SAVED_CARD_REQUEST,
    status: REQUESTING
  };
}
export function removeSavedCardSuccess() {
  return {
    type: REMOVE_SAVED_CARD_SUCCESS,
    status: SUCCESS
  };
}

export function removeSavedCardFailure(error) {
  return {
    type: REMOVE_SAVED_CARD_FAILURE,
    status: ERROR,
    error
  };
}

export function removeSavedCardDetails(userId, customerAccessToken) {
  return async (dispatch, getState, { api }) => {
    dispatch(removeSavedCardRequest());
    try {
      const result = await api.post(
        `${USER_PATH}/${userId}/payments/savedCards?access_token=${customerAccessToken}&cardType=${CARD_TYPE}`
      );
      const resultJson = await result.json();

      if (resultJson.errors) {
        throw new Error(resultJson.errors[0].message);
      }
      dispatch(removeSavedCardSuccess(resultJson));
    } catch (e) {
      dispatch(removeSavedCardFailure(e.message));
    }
  };
}

export function getAllOrdersRequest() {
  return {
    type: GET_ALL_ORDERS_REQUEST,
    status: REQUESTING
  };
}
export function getAllOrdersSuccess(orderDetails) {
  return {
    type: GET_ALL_ORDERS_SUCCESS,
    status: SUCCESS,
    orderDetails
  };
}

export function getAllOrdersFailure(error) {
  return {
    type: GET_ALL_ORDERS_FAILURE,

    status: ERROR,
    error
  };
}
export function getAllOrdersDetails() {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(getAllOrdersRequest());
    try {
      const result = await api.get(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/orderhistorylist?currentPage=${CURRENT_PAGE}&access_token=${
          JSON.parse(customerCookie).access_token
        }&pageSize=${PAGE_SIZE}&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();
      if (resultJson.errors) {
        throw new Error(`${resultJson.errors[0].message}`);
      }
      dispatch(getAllOrdersSuccess(resultJson));
    } catch (e) {
      dispatch(getAllOrdersFailure(e.message));
    }
  };
}

export function getUserDetailsRequest() {
  return {
    type: GET_USER_DETAILS_REQUEST,
    status: REQUESTING
  };
}
export function getUserDetailsSuccess(userDetails) {
  return {
    type: GET_USER_DETAILS_SUCCESS,
    status: SUCCESS,
    userDetails
  };
}

export function getUserDetailsFailure(error) {
  return {
    type: GET_USER_DETAILS_FAILURE,
    status: ERROR,
    error
  };
}

export function getUserDetails() {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(getUserDetailsRequest());
    try {
      const result = await api.get(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/getCustomerProfile?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true`
      );
      const resultJson = await result.json();
      if (
        resultJson.errors ||
        resultJson.status === FAILURE_UPPERCASE ||
        resultJson.status === FAILURE
      ) {
        throw new Error(`${resultJson.errors[0].message}`);
      }
      dispatch(getUserDetailsSuccess(resultJson));
    } catch (e) {
      dispatch(getUserDetailsFailure(e.message));
    }
  };
}

export function getUserCouponsRequest() {
  return {
    type: GET_USER_COUPON_REQUEST,
    status: REQUESTING
  };
}
export function getUserCouponsSuccess(userCoupons) {
  return {
    type: GET_USER_COUPON_SUCCESS,
    status: SUCCESS,
    userCoupons
  };
}

export function getUserCouponsFailure(error) {
  return {
    type: GET_USER_COUPON_FAILURE,
    status: ERROR,
    error
  };
}

export function getUserCoupons() {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(getUserCouponsRequest());
    try {
      const result = await api.get(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/getCoupons?currentPage=${CURRENT_PAGE}&access_token=${
          JSON.parse(customerCookie).access_token
        }&pageSize=${PAGE_SIZE}&usedCoupon=N&isPwa=true&platformNumber=2&channel=mobile`
      );
      const resultJson = await result.json();
      if (
        resultJson.errors ||
        resultJson.status === FAILURE_UPPERCASE ||
        resultJson.status === FAILURE
      ) {
        throw new Error(`${resultJson.errors[0].message}`);
      }
      dispatch(getUserCouponsSuccess(resultJson));
    } catch (e) {
      dispatch(getUserCouponsFailure(e.message));
    }
  };
}

export function getUserAlertsRequest() {
  return {
    type: GET_USER_ALERTS_REQUEST,
    status: REQUESTING
  };
}
export function getUserAlertsSuccess(userAlerts) {
  return {
    type: GET_USER_ALERTS_SUCCESS,
    status: SUCCESS,
    userAlerts
  };
}

export function getUserAlertsFailure(error) {
  return {
    type: GET_USER_ALERTS_FAILURE,
    status: ERROR,
    error
  };
}

export function getUserAlerts() {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(getUserAlertsRequest());
    try {
      const result = await api.get(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/getOrderTrackingNotifications?access_token=${
          JSON.parse(customerCookie).access_token
        }&emailId=${JSON.parse(userDetails).userName}`
      );
      const resultJson = await result.json();
      if (
        resultJson.errors ||
        resultJson.status === FAILURE_UPPERCASE ||
        resultJson.status === FAILURE
      ) {
        throw new Error(`${resultJson.errors[0].message}`);
      }
      dispatch(getUserAlertsSuccess(resultJson));
    } catch (e) {
      dispatch(getUserAlertsFailure(e.message));
    }
  };
}
export function getWishlistRequest() {
  return {
    type: GET_WISHLIST_REQUEST,
    status: REQUESTING
  };
}
export function getWishlistSuccess(wishlist) {
  return {
    type: GET_WISHLIST_SUCCESS,
    status: SUCCESS,
    wishlist
  };
}

export function getWishlistFailure(error) {
  return {
    type: GET_WISHLIST_FAILURE,
    status: ERROR,
    error
  };
}

export function getWishList() {
  return async (dispatch, getState, { api }) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    dispatch(getWishlistRequest());
    try {
      const result = await api.post(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/getAllWishlist?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=${PLATFORM_NUMBER}`
      );
      const resultJson = await result.json();
      if (resultJson.errors) {
        throw new Error(`${resultJson.errors[0].message}`);
      }
      dispatch(getWishlistSuccess(resultJson.wishList[0])); //we sre getting response wishlit[0]
    } catch (e) {
      dispatch(getWishlistFailure(e.message));
    }
  };
}
