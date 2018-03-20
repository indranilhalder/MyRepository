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
export const USER_CART_PATH = "v2/mpl/users";

export const GET_USER_DETAILS_REQUEST = "GET_USER_DETAILS_REQUEST";
export const GET_USER_DETAILS_SUCCESS = "GET_USER_DETAILS_SUCCESS";
export const GET_USER_DETAILS_FAILURE = "GET_USER_DETAILS_FAILURE";

export const GET_ALL_ORDERS_REQUEST = "GET_ALL_ORDERS_REQUEST";
export const GET_ALL_ORDERS_SUCCESS = "GET_ALL_ORDERS_SUCCESS";
export const GET_ALL_ORDERS_FAILURE = "GET_ALL_ORDERS_FAILURE";

export const GET_USER_COUPON_REQUEST = "GET_USER_COUPON_REQUEST";
export const GET_USER_COUPON_SUCCESS = "GET_USER_COUPON_SUCCESS";
export const GET_USER_COUPON_FAILURE = "GET_USER_COUPON_FAILURE";

export const GET_USER_ALERTS_REQUEST = "GET_USER_ALERTS_REQUEST";
export const GET_USER_ALERTS_SUCCESS = "GET_USER_ALERTS_SUCCESS";
export const GET_USER_ALERTS_FAILURE = "GET_USER_ALERTS_FAILURE";

export const CURRENT_PAGE = 0;
export const PAGE_SIZE = 10;

// Local Storage and Cookie storage
const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
const cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
const cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);

const defaultPincode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);

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
  return async (dispatch, getState, { api }) => {
    dispatch(getAllOrdersRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
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
  return async (dispatch, getState, { api }) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    dispatch(getUserDetailsRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
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
  return async (dispatch, getState, { api }) => {
    dispatch(getUserCouponsRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
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
  return async (dispatch, getState, { api }) => {
    dispatch(getUserAlertsRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/getOrderTrackingNotifications?access_token=${
          JSON.parse(customerCookie).access_token
        }&emailId=${JSON.parse(userDetails).userName}`
      );
      const resultJson = await result.json();
      console.log(resultJson);
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
