import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
import { CUSTOMER_ACCESS_TOKEN, FAILURE } from "../../lib/constants";

export const PRODUCT_CART_REQUEST = "PRODUCT_CART_REQUEST";
export const PRODUCT_CART_SUCCESS = "PRODUCT_CART_SUCCESS";
export const PRODUCT_CART_FAILURE = "PRODUCT_CART_FAILURE";

export const USER_CART_REQUEST = "USER_CART_REQUEST";
export const USER_CART_SUCCESS = "USER_CART_SUCCESS";
export const USER_CART_FAILURE = "USER_CART_FAILURE";

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

export const NET_BANKING_DETAILS_REQUEST = "NET_BANKING_DETAILS_REQUEST";
export const NET_BANKING_DETAILS_SUCCESS = "NET_BANKING_DETAILS_SUCCESS";
export const NET_BANKING_DETAILS_FAILURE = "NET_BANKING_DETAILS_FAILURE";

export const EMI_BANKING_DETAILS_REQUEST = "EMI_BANKING_DETAILS_REQUEST";
export const EMI_BANKING_DETAILS_SUCCESS = "EMI_BANKING_DETAILS_SUCCESS";
export const EMI_BANKING_DETAILS_FAILURE = "EMI_BANKING_DETAILS_FAILURE";

export function userCartRequest() {
  return {
    type: USER_CART_REQUEST,
    status: REQUESTING
  };
}
export function userCartSuccess(userCart) {
  return {
    type: USER_CART_SUCCESS,
    status: SUCCESS,
    userCart
  };
}

export function userCartFailure(error) {
  return {
    type: USER_CART_FAILURE,
    status: ERROR,
    error
  };
}
export function getUserCart() {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(userCartRequest());

    try {
      const result = await api.postUta(
        `${USER_CART_PATH}/${
          getState().user.user.customerInfo.mobileNumber
        }/carts?access_token=${JSON.parse(customerCookie).access_token}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(userCartSuccess(resultJson));
      dispatch(getProductCart(resultJson.code));
    } catch (e) {
      dispatch(userCartFailure(e.message));
    }
  };
}

export function getProductCartRequest() {
  return {
    type: PRODUCT_CART_REQUEST,
    status: REQUESTING
  };
}
export function getProductCartSuccess(cartDetails) {
  return {
    type: PRODUCT_CART_SUCCESS,
    status: SUCCESS,
    cartDetails
  };
}

export function getProductCartFailure(error) {
  return {
    type: PRODUCT_CART_FAILURE,
    status: ERROR,
    error
  };
}

export function getProductCart(code) {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(getProductCartRequest());
    try {
      const result = await api.getUta(
        `${USER_CART_PATH}/${
          getState().user.user.customerInfo.mobileNumber
        }/carts/${code}/cartDetailsCNC?access_token=${
          JSON.parse(customerCookie).access_token
        }&pincode=122001`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }

      dispatch(getProductCartSuccess(resultJson));
    } catch (e) {
      dispatch(getProductCartFailure(e.message));
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
      const result = await api.getUta(
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
      const result = await api.getUta(
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
      const result = await api.postUta(
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
      const result = await api.postUta(
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
      const result = await api.postUta(
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
