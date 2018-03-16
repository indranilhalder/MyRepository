import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
import each from "lodash/each";
import {
  CUSTOMER_ACCESS_TOKEN,
  GLOBAL_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  FAILURE,
  FAILURE_UPPERCASE,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS
} from "../../lib/constants";
export const USER_CART_PATH = "v2/mpl/users";
export const Get_CUSTOMER_PROFILE_REQUEST = "Get_CUSTOMER_PROFILE_REQUEST";
export const Get_CUSTOMER_PROFILE_SUCCESS = "Get_CUSTOMER_PROFILE_SUCCESS";
export const Get_CUSTOMER_PROFILE_FAILURE = "Get_CUSTOMER_PROFILE_FAILURE";

export const Get_CUSTOMER_ADDRESS_REQUEST = "Get_CUSTOMER_ADDRESS_REQUEST";
export const Get_CUSTOMER_ADDRESS_SUCCESS = "Get_CUSTOMER_ADDRESS_SUCCESS";
export const Get_CUSTOMER_ADDRESS_FAILURE = "Get_CUSTOMER_ADDRESS_FAILURE";

export function getCustomerProfileRequest() {
  return {
    type: Get_CUSTOMER_PROFILE_REQUEST,
    status: REQUESTING
  };
}
export function getCustomerProfileSuccess(customerProfileDetails) {
  return {
    type: Get_CUSTOMER_PROFILE_SUCCESS,
    status: SUCCESS,
    customerProfileDetails
  };
}

export function getCustomerProfileFailure(error) {
  return {
    type: Get_CUSTOMER_PROFILE_FAILURE,
    status: ERROR,
    error
  };
}

export function getCustomerProfileDetails() {
  return async (dispatch, getState, { api }) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    dispatch(getCustomerProfileRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).customerInfo.mobileNumber
        }/getCustomerProfile?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(getCustomerProfileSuccess(resultJson));
    } catch (e) {
      dispatch(getCustomerProfileFailure(e.message));
    }
  };
}

export function getCustomerAddressRequest() {
  return {
    type: Get_CUSTOMER_ADDRESS_REQUEST,
    status: REQUESTING
  };
}
export function getCustomerAddressSuccess(customerAddressDetails) {
  return {
    type: Get_CUSTOMER_ADDRESS_SUCCESS,
    status: SUCCESS,
    customerAddressDetails
  };
}

export function getCustomerAddressFailure(error) {
  return {
    type: Get_CUSTOMER_ADDRESS_FAILURE,
    status: ERROR,
    error
  };
}

export function getCustomerAddressDetails() {
  return async (dispatch, getState, { api }) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    dispatch(getCustomerAddressRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).customerInfo.mobileNumber
        }/addresses?platformNumber=2&access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(getCustomerAddressSuccess(resultJson));
    } catch (e) {
      dispatch(getCustomerAddressFailure(e.message));
    }
  };
}
