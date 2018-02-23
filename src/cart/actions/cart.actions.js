import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  GLOBAL_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  FAILURE
} from "../../lib/constants";

export const USER_CART_PATH = "v2/mpl/users";

export const APPLY_COUPON_REQUEST = "APPLY_COUPON_REQUEST";
export const APPLY_COUPON_SUCCESS = "APPLY_COUPON_SUCCESS";
export const APPLY_COUPON_FAILURE = "APPLY_COUPON_FAILURE";

export const SELECT_DELIVERY_MODES_REQUEST = "SELECT_DELIVERY_MODES_REQUEST";
export const SELECT_DELIVERY_MODES_SUCCESS = "SELECT_DELIVERY_MODES_SUCCESS";
export const SELECT_DELIVERY_MODES_FAILURE = "SELECT_DELIVERY_MODES_FAILURE";

export const GET_USER_ADDRESS_REQUEST = "GET_USER_ADDRESS_REQUEST";
export const GET_USER_ADDRESS_SUCCESS = "GET_USER_ADDRESS_SUCCESS";
export const GET_USER_ADDRESS_FAILURE = "GET_USER_ADDRESS_FAILURE";

export const ADD_USER_ADDRESS_REQUEST = "ADD_USER_ADDRESS_REQUEST";
export const ADD_USER_ADDRESS_SUCCESS = "ADD_USER_ADDRESS_SUCCESS";
export const ADD_USER_ADDRESS_FAILURE = "ADD_USER_ADDRESS_FAILURE";

export const NET_BANKING_DETAILS_REQUEST = "NET_BANKING_DETAILS_REQUEST";
export const NET_BANKING_DETAILS_SUCCESS = "NET_BANKING_DETAILS_SUCCESS";
export const NET_BANKING_DETAILS_FAILURE = "NET_BANKING_DETAILS_FAILURE";

export const EMI_BANKING_DETAILS_REQUEST = "EMI_BANKING_DETAILS_REQUEST";
export const EMI_BANKING_DETAILS_SUCCESS = "EMI_BANKING_DETAILS_SUCCESS";
export const EMI_BANKING_DETAILS_FAILURE = "EMI_BANKING_DETAILS_FAILURE";

export const GENERATE_CART_ID_REQUEST = "GENERATE_CART_ID_REQUEST";
export const GENERATE_CART_ID_FOR_LOGGED_ID_SUCCESS =
  "GENERATE_CART_ID_FOR_LOGGED_ID_SUCCESS";
export const GENERATE_CART_ID_FAILURE = "GENERATE_CART_ID_FAILURE";
export const GENERATE_CART_ID_BY_ANONYMOUS_SUCCESS =
  "GENERATE_CART_ID_BY_ANONYMOUS_SUCCESS";

export const CART_DETAILS_REQUEST = "GENERATE_CART_ID_REQUEST";
export const CART_DETAILS_SUCCESS = "CART_DETAILS_SUCCESS";
export const CART_DETAILS_FAILURE = "CART_DETAILS_FAILURE";

export function cartDetailsRequest() {
  return {
    type: CART_DETAILS_REQUEST,
    status: REQUESTING
  };
}
export function cartDetailsSuccess(cartDetails) {
  return {
    type: CART_DETAILS_SUCCESS,
    status: SUCCESS,
    cartDetails
  };
}

export function cartDetailsFailure(error) {
  return {
    type: CART_DETAILS_FAILURE,
    status: ERROR,
    error
  };
}

export function getCartDetails(userId, accessToken, cartId) {
  return async (dispatch, getState, { api }) => {
    dispatch(cartDetailsRequest());

    try {
      const result = await api.get(
        `${USER_CART_PATH}/${userId}/carts/${cartId}/cartDetails?access_token=${accessToken}&isPwa=true`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(cartDetailsSuccess(resultJson));
    } catch (e) {
      dispatch(cartDetailsFailure(e.message));
    }
  };
}

export function applyCouponRequest() {
  return {
    type: APPLY_COUPON_REQUEST,
    status: REQUESTING
  };
}
export function applyCouponSuccess() {
  return {
    type: APPLY_COUPON_SUCCESS,
    status: SUCCESS
  };
}

export function applyCouponFailure(error) {
  return {
    type: APPLY_COUPON_FAILURE,
    status: ERROR,
    error
  };
}

export function applyCoupon(cartGuide) {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(applyCouponRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          getState().user.user.customerInfo.mobileNumber
        }/carts/applyCoupons?access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${cartGuide}&couponCode=CouponTest&paymentMode=CreditCard`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }

      dispatch(applyCouponSuccess());
    } catch (e) {
      dispatch(applyCouponFailure(e.message));
    }
  };
}

export function userAddressRequest(error) {
  return {
    type: GET_USER_ADDRESS_REQUEST,
    status: REQUESTING
  };
}

export function userAddressSuccess(userAddress) {
  return {
    type: GET_USER_ADDRESS_SUCCESS,
    status: SUCCESS,
    userAddress
  };
}

export function userAddressFailure(error) {
  return {
    type: GET_USER_ADDRESS_FAILURE,
    status: ERROR,
    error
  };
}

export function getUserAddress() {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(userAddressRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          getState().user.user.customerInfo.mobileNumber
        }/addresses?channel=mobile&emailId=${
          getState().user.user.customerInfo.mobileNumber
        }&access_token=${JSON.parse(customerCookie).access_token}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }

      dispatch(userAddressSuccess(resultJson));
    } catch (e) {
      dispatch(userAddressFailure(e.message));
    }
  };
}

export function addUserAddressRequest(error) {
  return {
    type: ADD_USER_ADDRESS_REQUEST,
    status: REQUESTING
  };
}

export function addUserAddressSuccess(userAddress) {
  return {
    type: ADD_USER_ADDRESS_SUCCESS,
    status: SUCCESS,
    userAddress
  };
}

export function addUserAddressFailure(error) {
  return {
    type: ADD_USER_ADDRESS_FAILURE,
    status: ERROR,
    error
  };
}

export function addUserAddress(userAddress) {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(userAddressRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          getState().user.user.customerInfo.mobileNumber
        }/addAddress?channel=mobile&countryIso=IN&emailId=${
          getState().user.user.customerInfo.mobileNumber
        }&access_token=${JSON.parse(customerCookie).access_token}&addressType=${
          userAddress.addressType
        }&city=${userAddress.city}&defaultAddress=${
          userAddress.defaultAddress
        }&firstName=${userAddress.firstName}&landmark=${
          userAddress.landmark
        }&line1=${userAddress.line1}&phone=${userAddress.phone}&postalCode=${
          userAddress.postalCode
        }&state=${userAddress.state}&town=${
          userAddress.town
        }&line2=&line3=&lastName=&defaultFlag=0`
      );
      const resultJson = await result.json();
      console.log(resultJson);
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }

      dispatch(userAddressSuccess(resultJson));
    } catch (e) {
      console.log(e.message);
      dispatch(userAddressFailure(e.message));
    }
  };
}

export function selectDeliveryModeRequest() {
  return {
    type: SELECT_DELIVERY_MODES_REQUEST,
    status: REQUESTING
  };
}
export function selectDeliveryModeSuccess(deliveryModes) {
  return {
    type: SELECT_DELIVERY_MODES_SUCCESS,
    status: SUCCESS,
    deliveryModes
  };
}

export function selectDeliveryModeFailure(error) {
  return {
    type: SELECT_DELIVERY_MODES_FAILURE,
    status: ERROR,
    error
  };
}

export function selectDeliveryModes(deliverModes) {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(selectDeliveryModeRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          getState().user.user.customerInfo.mobileNumber
        }/carts/15481123719086096-00652012/selectDeliveryMode?access_token=${
          JSON.parse(customerCookie).access_token
        }&deliverymodeussId=%7B%22100220MFS7601D20TURQXL %22%3A%22home-delivery%22%2C%22123762PL13939JS04AJ%22%3A%22click-and- collect%22%7D&removeExchange=0`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }

      dispatch(selectDeliveryModeSuccess(resultJson));
    } catch (e) {
      dispatch(selectDeliveryModeFailure(e.message));
    }
  };
}

export function netBankingDetailsRequest() {
  return {
    type: NET_BANKING_DETAILS_REQUEST,
    status: REQUESTING
  };
}
export function netBankingDetailsSuccess(netBankDetails) {
  return {
    type: NET_BANKING_DETAILS_SUCCESS,
    status: SUCCESS,
    netBankDetails
  };
}

export function netBankingDetailsFailure(error) {
  return {
    type: NET_BANKING_DETAILS_FAILURE,
    status: ERROR,
    error
  };
}

export function getNetBankDetails() {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(netBankingDetailsRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          getState().user.user.customerInfo.mobileNumber
        }/netbankingDetails?channel=mobile&access_token=${
          JSON.parse(customerCookie).access_token
        }`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }

      dispatch(netBankingDetailsSuccess(resultJson));
    } catch (e) {
      dispatch(netBankingDetailsFailure(e.message));
    }
  };
}

export function emiBankingDetailsRequest() {
  return {
    type: EMI_BANKING_DETAILS_REQUEST,
    status: REQUESTING
  };
}
export function emiBankingDetailsSuccess(emiBankDetails) {
  return {
    type: EMI_BANKING_DETAILS_SUCCESS,
    status: SUCCESS,
    emiBankDetails
  };
}

export function emiBankingDetailsFailure(error) {
  return {
    type: EMI_BANKING_DETAILS_FAILURE,
    status: ERROR,
    error
  };
}
export function getEmiBankDetails(cartValues) {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(emiBankingDetailsRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          getState().user.user.customerInfo.mobileNumber
        }/emibankingDetails?channel=mobile&cartValue=${
          cartValues.total
        }&access_token=${JSON.parse(customerCookie).access_token}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }

      dispatch(emiBankingDetailsSuccess(resultJson));
    } catch (e) {
      dispatch(emiBankingDetailsFailure(e.message));
    }
  };
}

export function generateCartIdRequest() {
  return {
    type: GENERATE_CART_ID_REQUEST,
    status: REQUESTING
  };
}
export function generateCartIdForLoggedInUserSuccess(cartDetails) {
  return {
    type: GENERATE_CART_ID_FOR_LOGGED_ID_SUCCESS,
    status: SUCCESS,
    cartDetails
  };
}

export function generateCartIdFailure(error) {
  return {
    type: GENERATE_CART_ID_FAILURE,
    status: ERROR,
    error
  };
}
export function generateCartIdForLoggedInUser() {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(generateCartIdRequest());

    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).customerInfo.mobileNumber
        }/carts?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(resultJson.message);
      }
      dispatch(generateCartIdForLoggedInUserSuccess(resultJson));
    } catch (e) {
      dispatch(generateCartIdFailure(e.message));
    }
  };
}

export function generateCartIdAnonymousSuccess(cartDetails) {
  return {
    type: GENERATE_CART_ID_BY_ANONYMOUS_SUCCESS,
    status: SUCCESS,
    cartDetails
  };
}

export function generateCartIdForAnonymous() {
  let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(generateCartIdRequest());

    try {
      const result = await api.post(
        `${USER_CART_PATH}/anonymous/carts?access_token=${
          JSON.parse(globalCookie).access_token
        }&isPwa=true`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(resultJson.message);
      }
      dispatch(generateCartIdAnonymousSuccess(resultJson));
    } catch (e) {
      dispatch(generateCartIdFailure(e.message));
    }
  };
}

// https://uat2.tataunistore.com/marketplacewebservices/v2/mpl/users/8150838465/addAddress?channel=mobile&countryIso=IN&emailId=8150838465&access_token=84fdb1ac-7195-492a-b08b-76bbe491fde7&addressType=Office&city=Krishna%20tample&defaultAddress=false&firstName=Aakarsh%20Yadav&landmark=Aegis%20tower&line1=Xelpmoc%20design&phone=9456888501&postalCode=229001&state=Kr&town=Kormangala&line2=&line3=&defaultFlag=0

// https://uat2.tataunistore.com/marketplacewebservices/v2/mpl/users/8150838465/addAddress?channel=mobile&emailId=8150838465&access_token=84fdb1ac-7195-492a-b08b-76bbe491fde7&addressType=Office&lastName=&city=Kormangala&defaultAddress=false&firstName=Aakarsh%20Yadav&landmark=Kormangal&line1=Xelpmoc&line2=&line3=&countryIso=IN&defaultFlag=0&phone=9456888501&postalCode=229001&state=Kr&town=Bangalore
