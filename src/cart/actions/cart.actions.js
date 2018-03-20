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
  CART_DETAILS_FOR_ANONYMOUS,
  DEFAULT_PIN_CODE_LOCAL_STORAGE
} from "../../lib/constants";
export const USER_CART_PATH = "v2/mpl/users";
export const CART_PATH = "v2/mpl";
export const ALL_STORES_PATH = "v2/mpl/allStores";

export const APPLY_USER_COUPON_REQUEST = "APPLY_USER_COUPON_REQUEST";
export const APPLY_USER_COUPON_SUCCESS = "APPLY_USER_COUPON_SUCCESS";
export const APPLY_USER_COUPON_FAILURE = "APPLY_USER_COUPON_FAILURE";

export const GET_COUPON_REQUEST = "GET_COUPON_REQUEST";
export const GET_COUPON_SUCCESS = "GET_COUPON_SUCCESS";
export const GET_COUPON_FAILURE = "GET_COUPON_FAILURE";

export const RELEASE_USER_COUPON_REQUEST = "RELEASE_USER_COUPON_REQUEST";
export const RELEASE_USER_COUPON_SUCCESS = "RELEASE_USER_COUPON_SUCCESS";
export const RELEASE_USER_COUPON_FAILURE = "RELEASE_USER_COUPON_FAILURE";

export const SELECT_DELIVERY_MODES_REQUEST = "SELECT_DELIVERY_MODES_REQUEST";
export const SELECT_DELIVERY_MODES_SUCCESS = "SELECT_DELIVERY_MODES_SUCCESS";
export const SELECT_DELIVERY_MODES_FAILURE = "SELECT_DELIVERY_MODES_FAILURE";

export const GET_USER_ADDRESS_REQUEST = "GET_USER_ADDRESS_REQUEST";
export const GET_USER_ADDRESS_SUCCESS = "GET_USER_ADDRESS_SUCCESS";
export const GET_USER_ADDRESS_FAILURE = "GET_USER_ADDRESS_FAILURE";

export const ADD_USER_ADDRESS_REQUEST = "ADD_USER_ADDRESS_REQUEST";
export const ADD_USER_ADDRESS_SUCCESS = "ADD_USER_ADDRESS_SUCCESS";
export const ADD_USER_ADDRESS_FAILURE = "ADD_USER_ADDRESS_FAILURE";

export const ADD_ADDRESS_TO_CART_REQUEST = "ADD_ADDRESS_TO_CART_REQUEST";
export const ADD_ADDRESS_TO_CART_SUCCESS = "ADD_ADDRESS_TO_CART_SUCCESS";
export const ADD_ADDRESS_TO_CART_FAILURE = "ADD_ADDRESS_TO_CART_FAILURE";

export const CART_DETAILS_CNC_REQUEST = "CART_DETAILS_CNC_REQUEST";
export const CART_DETAILS_CNC_SUCCESS = "CART_DETAILS_CNC_SUCCESS";
export const CART_DETAILS_CNC_FAILURE = "CART_DETAILS_CNC_FAILURE";

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

export const CART_DETAILS_REQUEST = "CART_DETAILS_REQUEST";
export const CART_DETAILS_SUCCESS = "CART_DETAILS_SUCCESS";
export const CART_DETAILS_FAILURE = "CART_DETAILS_FAILURE";

export const ORDER_SUMMARY_REQUEST = "ORDER_SUMMARY_REQUEST";
export const ORDER_SUMMARY_SUCCESS = "ORDER_SUMMARY_SUCCESS";
export const ORDER_SUMMARY_FAILURE = "ORDER_SUMMARY_FAILURE";

export const GET_CART_ID_REQUEST = "GET_CART_ID_REQUEST";
export const GET_CART_ID_SUCCESS = "GET_CART_ID_SUCCESS";
export const GET_CART_ID_FAILURE = "GET_CART_ID_FAILURE";

export const MERGE_CART_ID_REQUEST = "MERGE_CART_ID_REQUEST";
export const MERGE_CART_ID_SUCCESS = "MERGE_CART_ID_SUCCESS";
export const MERGE_CART_ID_FAILURE = "MERGE_CART_ID_FAILURE";

export const CHECK_PIN_CODE_SERVICE_AVAILABILITY_REQUEST =
  "CHECK_PIN_CODE_SERVICE_AVAILABILITY_REQUEST";
export const CHECK_PIN_CODE_SERVICE_AVAILABILITY_SUCCESS =
  "CHECK_PIN_CODE_SERVICE_AVAILABILITY_SUCCESS";
export const CHECK_PIN_CODE_SERVICE_AVAILABILITY_FAILURE =
  "CHECK_PIN_CODE_SERVICE_AVAILABILITY_FAILURE";

export const GET_ALL_STORES_CNC_REQUEST = "GET_ALL_STORES_CNC_REQUEST";
export const GET_ALL_STORES_CNC_SUCCESS = "GET_ALL_STORES_CNC_SUCCESS";
export const GET_ALL_STORES_CNC_FAILURE = "GET_ALL_STORES_CNC_FAILURE";

export const ADD_STORE_CNC_REQUEST = "ADD_STORE_CNC_REQUEST";
export const ADD_STORE_CNC_SUCCESS = "ADD_STORE_CNC_SUCCESS";
export const ADD_STORE_CNC_FAILURE = "ADD_STORE_CNC_FAILURE";

export const ADD_PICKUP_PERSON_REQUEST = "ADD_PICKUP_PERSON_REQUEST";
export const ADD_PICKUP_PERSON_SUCCESS = "ADD_PICKUP_PERSON_SUCCESS";
export const ADD_PICKUP_PERSON_FAILURE = "ADD_PICKUP_PERSON_FAILURE";

export const SOFT_RESERVATION_REQUEST = "SOFT_RESERVATION_REQUEST";
export const SOFT_RESERVATION_SUCCESS = "SOFT_RESERVATION_SUCCESS";
export const SOFT_RESERVATION_FAILURE = "SOFT_RESERVATION_FAILURE";

export const GET_PAYMENT_MODES_REQUEST = "GET_PAYMENT_MODES_REQUEST";
export const GET_PAYMENT_MODES_SUCCESS = "GET_PAYMENT_MODES_SUCCESS";
export const GET_PAYMENT_MODES_FAILURE = "GET_PAYMENT_MODES_FAILURE";

export const RELEASE_BANK_OFFER_REQUEST = "RELEASE_BANK_OFFER_REQUEST";
export const RELEASE_BANK_OFFER_SUCCESS = "RELEASE_BANK_OFFER_SUCCESS";
export const RELEASE_BANK_OFFER_FAILURE = "RELEASE_BANK_OFFER_FAILURE";

export const APPLY_BANK_OFFER_REQUEST = "APPLY_BANK_OFFER_REQUEST";
export const APPLY_BANK_OFFER_SUCCESS = "APPLY_BANK_OFFER_SUCCESS";
export const APPLY_BANK_OFFER_FAILURE = "APPLY_BANK_OFFER_FAILURE";

export const APPLY_CLIQ_CASH_REQUEST = "APPLY_CLIQ_CASH_REQUEST";
export const APPLY_CLIQ_CASH_SUCCESS = "APPLY_CLIQ_CASH_SUCCESS";
export const APPLY_CLIQ_CASH_FAILURE = "APPLY_CLIQ_CASH_FAILURE";

export const REMOVE_CLIQ_CASH_REQUEST = "REMOVE_CLIQ_CASH_REQUEST";
export const REMOVE_CLIQ_CASH_SUCCESS = "REMOVE_CLIQ_CASH_SUCCESS";
export const REMOVE_CLIQ_CASH_FAILURE = "REMOVE_CLIQ_CASH_FAILURE";

export const BIN_VALIDATION_REQUEST = "BIN_VALIDATION_REQUEST";
export const BIN_VALIDATION_SUCCESS = "BIN_VALIDATION_SUCCESS";
export const BIN_VALIDATION_FAILURE = "BIN_VALIDATION_FAILURE";

export const SOFT_RESERVATION_FOR_PAYMENT_REQUEST =
  "SOFT_RESERVATION_FOR_PAYMENT_REQUEST";
export const SOFT_RESERVATION_FOR_PAYMENT_SUCCESS =
  "SOFT_RESERVATION_FOR_PAYMENT_SUCCESS";
export const SOFT_RESERVATION_FOR_PAYMENT_FAILURE =
  "SOFT_RESERVATION_FOR_PAYMENT_FAILURE";

export const JUS_PAY_TOKENIZE_REQUEST = "JUS_PAY_TOKENIZE_REQUEST";
export const JUS_PAY_TOKENIZE_SUCCESS = "JUS_PAY_TOKENIZE_SUCCESS";
export const JUS_PAY_TOKENIZE_FAILURE = "JUS_PAY_TOKENIZE_FAILURE";

export const CREATE_JUS_PAY_ORDER_REQUEST = "CREATE_JUS_PAY_ORDER_REQUEST";
export const CREATE_JUS_PAY_ORDER_SUCCESS = "CREATE_JUS_PAY_ORDER_SUCCESS";
export const CREATE_JUS_PAY_ORDER_FAILURE = "CREATE_JUS_PAY_ORDER_FAILURE";

export const JUS_PAY_PAYMENT_METHOD_TYPE_REQUEST =
  "JUS_PAY_PAYMENT_METHOD_TYPE_REQUEST";
export const JUS_PAY_PAYMENT_METHOD_TYPE_SUCCESS =
  "JUS_PAY_PAYMENT_METHOD_TYPE_SUCCESS";
export const JUS_PAY_PAYMENT_METHOD_TYPE_FAILURE =
  "JUS_PAY_PAYMENT_METHOD_TYPE_FAILURE";

export const UPDATE_TRANSACTION_DETAILS_REQUEST =
  "UPDATE_TRANSACTION_DETAILS_REQUEST";
export const UPDATE_TRANSACTION_DETAILS_SUCCESS =
  "UPDATE_TRANSACTION_DETAILS_SUCCESS";
export const UPDATE_TRANSACTION_DETAILS_FAILURE =
  "UPDATE_TRANSACTION_DETAILS_FAILURE";

export const ORDER_CONFIRMATION_REQUEST = "ORDER_CONFIRMATION_REQUEST";
export const ORDER_CONFIRMATION_SUCCESS = "ORDER_CONFIRMATION_SUCCESS";
export const ORDER_CONFIRMATION_FAILURE = "ORDER_CONFIRMATION_FAILURE";

export const GET_COD_ELIGIBILITY_REQUEST = "GET_COD_ELIGIBILITY_REQUEST";
export const GET_COD_ELIGIBILITY_SUCCESS = "GET_COD_ELIGIBILITY_SUCCESS";
export const GET_COD_ELIGIBILITY_FAILURE = "GET_COD_ELIGIBILITY_FAILURE";

export const BIN_VALIDATION_COD_REQUEST = "BIN_VALIDATION_COD_REQUEST";
export const BIN_VALIDATION_COD_SUCCESS = "BIN_VALIDATION_COD_SUCCESS";
export const BIN_VALIDATION_COD_FAILURE = "BIN_VALIDATION_COD_FAILURE";

export const UPDATE_TRANSACTION_DETAILS_FOR_COD_REQUEST =
  "UPDATE_TRANSACTION_DETAILS_FOR_COD_REQUEST";
export const UPDATE_TRANSACTION_DETAILS_FOR_COD_SUCCESS =
  "UPDATE_TRANSACTION_DETAILS_FOR_COD_SUCCESS";
export const UPDATE_TRANSACTION_DETAILS_FOR_COD_FAILURE =
  "UPDATE_TRANSACTION_DETAILS_FOR_COD_FAILURE";

export const SOFT_RESERVATION_FOR_COD_PAYMENT_REQUEST =
  "SOFT_RESERVATION_FOR_COD_PAYMENT_REQUEST";
export const SOFT_RESERVATION_FOR_COD_PAYMENT_SUCCESS =
  "SOFT_RESERVATION_FOR_COD_PAYMENT_SUCCESS";
export const SOFT_RESERVATION_FOR_COD_PAYMENT_FAILURE =
  "SOFT_RESERVATION_FOR_COD_PAYMENT_FAILURE";

export const ORDER_EXPERIENCE_CAPTURE_REQUEST =
  "ORDER_EXPERIENCE_CAPTURE_REQUEST";
export const ORDER_EXPERIENCE_CAPTURE_SUCCESS =
  "ORDER_EXPERIENCE_CAPTURE_SUCCESS";
export const ORDER_EXPERIENCE_CAPTURE_FAILURE =
  "ORDER_EXPERIENCE_CAPTURE_FAILURE";

export const ADD_PRODUCT_TO_WISH_LIST_REQUEST =
  "ADD_PRODUCT_TO_WISH_LIST_REQUEST";
export const ADD_PRODUCT_TO_WISH_LIST_SUCCESS =
  "ADD_PRODUCT_TO_WISH_LIST_SUCCESS";
export const ADD_PRODUCT_TO_WISH_LIST_FAILURE =
  "ADD_PRODUCT_TO_WISH_LIST_FAILURE";

export const REMOVE_ITEM_FROM_CART_LOGGED_IN_REQUEST =
  "REMOVE_ITEM_FROM_CART_LOGGED_IN_REQUEST";
export const REMOVE_ITEM_FROM_CART_LOGGED_IN_SUCCESS =
  "REMOVE_ITEM_FROM_CART_LOGGED_IN_SUCCESS";
export const REMOVE_ITEM_FROM_CART_LOGGED_IN_FAILURE =
  "REMOVE_ITEM_FROM_CART_LOGGED_IN_FAILURE";

export const REMOVE_ITEM_FROM_CART_LOGGED_OUT_REQUEST =
  "REMOVE_ITEM_FROM_CART_LOGGED_OUT_REQUEST";
export const REMOVE_ITEM_FROM_CART_LOGGED_OUT_SUCCESS =
  "REMOVE_ITEM_FROM_CART_LOGGED_OUT_SUCCESS";
export const REMOVE_ITEM_FROM_CART_LOGGED_OUT_FAILURE =
  "REMOVE_ITEM_FROM_CART_LOGGED_OUT_FAILURE";

export const UPDATE_QUANTITY_IN_CART_LOGGED_IN_REQUEST =
  "UPDATE_QUANTITY_IN_CART_LOGGED_IN_REQUEST";
export const UPDATE_QUANTITY_IN_CART_LOGGED_IN_SUCCESS =
  "UPDATE_QUANTITY_IN_CART_LOGGED_IN_SUCCESS";
export const UPDATE_QUANTITY_IN_CART_LOGGED_IN_FAILURE =
  "UPDATE_QUANTITY_IN_CART_LOGGED_IN_FAILURE";

export const UPDATE_QUANTITY_IN_CART_LOGGED_OUT_REQUEST =
  "UPDATE_QUANTITY_IN_CART_LOGGED_OUT_REQUEST";
export const UPDATE_QUANTITY_IN_CART_LOGGED_OUT_SUCCESS =
  "UPDATE_QUANTITY_IN_CART_LOGGED_OUT_SUCCESS";
export const UPDATE_QUANTITY_IN_CART_LOGGED_OUT_FAILURE =
  "UPDATE_QUANTITY_IN_CART_LOGGED_OUT_FAILURE";

export const PAYMENT_MODE = "credit card";
const pincode = 229001;
const PAYMENT_EMI = "EMI";
const CASH_ON_DELIVERY = "COD";
const MY_WISH_LIST = "MyWishList";
export const ANONYMOUS_USER = "anonymous";

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

export function getCartDetails(userId, accessToken, cartId, pinCode) {
  return async (dispatch, getState, { api }) => {
    dispatch(cartDetailsRequest());

    try {
      const result = await api.get(
        `${USER_CART_PATH}/${userId}/carts/${cartId}/cartDetails?access_token=${accessToken}&isPwa=true&platformNumber=2&pincode=${pinCode}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(cartDetailsSuccess(resultJson));
    } catch (e) {
      dispatch(cartDetailsFailure(e.message));
    }
  };
}

export function cartDetailsCNCRequest() {
  return {
    type: CART_DETAILS_CNC_REQUEST,
    status: REQUESTING
  };
}
export function cartDetailsCNCSuccess(cartDetailsCnc) {
  return {
    type: CART_DETAILS_CNC_SUCCESS,
    status: SUCCESS,
    cartDetailsCnc
  };
}

export function cartDetailsCNCFailure(error) {
  return {
    type: CART_DETAILS_CNC_FAILURE,
    status: ERROR,
    error
  };
}

export function getCartDetailsCNC(
  userId,
  accessToken,
  cartId,
  pinCode,
  isSoftReservation
) {
  return async (dispatch, getState, { api }) => {
    dispatch(cartDetailsCNCRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${userId}/carts/${cartId}/cartDetailsCNC?access_token=${accessToken}&isPwa=true&&platformNumber=2&pincode=${pinCode}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }

      if (isSoftReservation) {
        let productItems = {};
        let item = [];
        each(resultJson.products, product => {
          let productDetails = {};
          productDetails.ussId = product.USSID;
          productDetails.quantity = product.qtySelectedByUser;
          productDetails.fulfillmentType = product.fullfillmentType;

          if (product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves) {
            productDetails.deliveryMode =
              product.pinCodeResponse.validDeliveryModes[0].type;
            productDetails.serviceableSlaves =
              product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves[0];
          } else if (
            product.pinCodeResponse.validDeliveryModes[0]
              .CNCServiceableSlavesData
          ) {
            productDetails.deliveryMode =
              product.pinCodeResponse.validDeliveryModes[0].type;
            productDetails.serviceableSlaves =
              product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData[0].serviceableSlaves[0];
          }
          item.push(productDetails);
          productItems.item = item;
        });

        dispatch(softReservation(pinCode, productItems));
      }
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(cartDetailsCNCSuccess(resultJson));
    } catch (e) {
      dispatch(cartDetailsCNCFailure(e.message));
    }
  };
}

export function getCouponsRequest() {
  return {
    type: GET_COUPON_REQUEST,
    status: REQUESTING
  };
}
export function getCouponsSuccess(coupons) {
  return {
    type: GET_COUPON_SUCCESS,
    status: SUCCESS,
    coupons
  };
}

export function getCouponsFailure(error) {
  return {
    type: GET_CART_ID_FAILURE,
    status: ERROR,
    error
  };
}

export function getCoupons(couponCode) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(getCouponsRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/getCoupons?access_token=${
          JSON.parse(customerCookie).access_token
        }&currentPage=0&usedCoupon=N&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(getCouponsSuccess(resultJson));
    } catch (e) {
      dispatch(getCouponsFailure(e.message));
    }
  };
}

export function applyUserCouponRequest() {
  return {
    type: APPLY_USER_COUPON_REQUEST,
    status: REQUESTING
  };
}
export function applyUserCouponSuccess() {
  return {
    type: APPLY_USER_COUPON_SUCCESS,
    status: SUCCESS
  };
}

export function applyUserCouponFailure(error) {
  return {
    type: APPLY_USER_COUPON_FAILURE,
    status: ERROR,
    error
  };
}

export function applyUserCoupon(couponCode) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(applyUserCouponRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/applyCoupons?access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${cartId}&couponCode=${couponCode}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(applyUserCouponSuccess());
    } catch (e) {
      dispatch(applyUserCouponFailure(e.message));
    }
  };
}

export function releaseUserCouponRequest() {
  return {
    type: RELEASE_USER_COUPON_REQUEST,
    status: REQUESTING
  };
}
export function releaseUserCouponSuccess() {
  return {
    type: RELEASE_USER_COUPON_SUCCESS,
    status: SUCCESS
  };
}

export function releaseUserCouponFailure(error) {
  return {
    type: RELEASE_USER_COUPON_FAILURE,
    status: ERROR,
    error
  };
}

export function releaseUserCoupon(couponCode) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartGuId = JSON.parse(cartDetails).guid;
  let cartId = JSON.parse(cartDetails).code;
  return async (dispatch, getState, { api }) => {
    dispatch(releaseUserCouponRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/${cartId}/releaseCoupons?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2&couponCode=${couponCode}&cartGuid=${cartGuId}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(releaseUserCouponSuccess());
    } catch (e) {
      dispatch(releaseUserCouponFailure(e.message));
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
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  return async (dispatch, getState, { api }) => {
    dispatch(userAddressRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/addresses?channel=mobile&emailId=${
          JSON.parse(userDetails).userName
        }&access_token=${JSON.parse(customerCookie).access_token}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
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
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);

  return async (dispatch, getState, { api }) => {
    dispatch(addUserAddressRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/addAddress?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2&countryIso=${
          userAddress.countryIso
        }&addressType=${userAddress.addressType}&phone=${
          userAddress.phone
        }&emailId=${userAddress.emailId}&firstName=${
          userAddress.firstName
        }&lastName=${userAddress.lastName}
        &postalCode=${userAddress.postalCode}&line1=${
          userAddress.line1
        }&state=${userAddress.state}&line2=${userAddress.line2}&line3=${
          userAddress.line3
        }&town=${userAddress.town}&defaultFlag=${userAddress.defaultFlag}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }

      dispatch(addUserAddressSuccess());
    } catch (e) {
      dispatch(addUserAddressFailure(e.message));
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
export function selectDeliveryMode(deliveryUssId, pinCode) {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);

  return async (dispatch, getState, { api }) => {
    dispatch(userAddressRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${JSON.parse(userDetails).userName}/carts/${
          JSON.parse(cartDetails).code
        }/selectDeliveryMode?access_token=${
          JSON.parse(customerCookie).access_token
        }&deliverymodeussId=${JSON.stringify(deliveryUssId)}&removeExchange=0`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }

      dispatch(
        getCartDetailsCNC(
          JSON.parse(userDetails).userName,
          JSON.parse(customerCookie).access_token,
          JSON.parse(cartDetails).code,
          pinCode,
          true
        )
      );
      dispatch(selectDeliveryModeSuccess(resultJson));
    } catch (e) {
      dispatch(selectDeliveryModeFailure(e.message));
    }
  };
}

export function addAddressToCartRequest(error) {
  return {
    type: ADD_ADDRESS_TO_CART_REQUEST,
    status: REQUESTING
  };
}

export function addAddressToCartSuccess() {
  return {
    type: ADD_ADDRESS_TO_CART_SUCCESS,
    status: SUCCESS
  };
}

export function addAddressToCartFailure(error) {
  return {
    type: ADD_ADDRESS_TO_CART_FAILURE,
    status: ERROR,
    error
  };
}

export function addAddressToCart(addressId, pinCode) {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  return async (dispatch, getState, { api }) => {
    dispatch(userAddressRequest());
    try {
      let userId = JSON.parse(userDetails).userName;
      let access_token = JSON.parse(customerCookie).access_token;
      let cartId = JSON.parse(cartDetails).code;
      const result = await api.post(
        `${USER_CART_PATH}/${userId}/addAddressToOrder?channel=mobile&access_token=${access_token}&addressId=${addressId}&cartId=${cartId}&removeExchangeFromCart=0`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(getCartDetailsCNC(userId, access_token, cartId, pinCode, false));
      dispatch(addAddressToCartSuccess());
    } catch (e) {
      dispatch(userAddressFailure(e.message));
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
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  return async (dispatch, getState, { api }) => {
    dispatch(netBankingDetailsRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/netbankingDetails?channel=mobile&access_token=${
          JSON.parse(customerCookie).access_token
        }`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
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

export function getEmiBankDetails(price) {
  let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(emiBankingDetailsRequest());
    try {
      const result = await api.get(
        `${CART_PATH}/getBankDetailsforEMI?platformNumber=2&productValue=${price}&access_token=${
          JSON.parse(globalCookie).access_token
        }&isPwa=true`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(resultJson.message);
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
          JSON.parse(userDetails).userName
        }/carts?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }

      return dispatch(generateCartIdForLoggedInUserSuccess(resultJson));
    } catch (e) {
      return dispatch(generateCartIdFailure(e.message));
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
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      console.log("ANONYMOUS");
      console.log(resultJson);
      return dispatch(generateCartIdAnonymousSuccess(resultJson));
    } catch (e) {
      return dispatch(generateCartIdFailure(e.message));
    }
  };
}

export function orderSummaryRequest() {
  return {
    type: ORDER_SUMMARY_REQUEST,
    status: REQUESTING
  };
}
export function orderSumarySuccess(orderSummary) {
  return {
    type: ORDER_SUMMARY_SUCCESS,
    status: SUCCESS,
    orderSummary
  };
}

export function orderSummaryFailure(error) {
  return {
    type: ORDER_SUMMARY_FAILURE,
    status: ERROR,
    error
  };
}

export function getCartIdRequest() {
  return {
    type: GET_CART_ID_REQUEST,
    status: REQUESTING
  };
}

export function getCartIdSuccess(cartDetails) {
  return {
    type: GET_CART_ID_SUCCESS,
    status: SUCCESS,
    cartDetails
  };
}

export function getCartIdFailure(error) {
  return {
    type: GET_CART_ID_FAILURE,

    status: ERROR,
    error
  };
}

export function getOrderSummary(pincode) {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);

  return async (dispatch, getState, { api }) => {
    dispatch(orderSummaryRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${JSON.parse(userDetails).userName}/carts/${
          JSON.parse(cartDetails).code
        }/displayOrderSummary?access_token=${
          JSON.parse(customerCookie).access_token
        }&pincode=${pincode}&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();
      console.log("RESULT JSON");
      console.log(resultJson);
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(orderSumarySuccess(resultJson));
    } catch (e) {
      dispatch(orderSummaryFailure(e.message));
    }
  };
}
export function getCartId() {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

  return async (dispatch, getState, { api }) => {
    dispatch(getCartIdRequest());

    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true`
      );
      const resultJson = await result.json();
      console.log("GET CART ID");
      console.log(resultJson);
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      return dispatch(getCartIdSuccess(resultJson));
    } catch (e) {
      return dispatch(getCartIdFailure(e.message));
    }
  };
}

export function mergeCardIdRequest() {
  return {
    type: MERGE_CART_ID_REQUEST,
    status: REQUESTING
  };
}
export function mergeCartIdSuccess(cartDetails) {
  return {
    type: MERGE_CART_ID_SUCCESS,
    status: SUCCESS,
    cartDetails
  };
}

export function mergeCartIdFailure(error) {
  return {
    type: MERGE_CART_ID_FAILURE,
    status: ERROR,
    error
  };
}

export function mergeCartId(cartGuId) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);

  return async (dispatch, getState, { api }) => {
    dispatch(mergeCardIdRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2&userId=${
          JSON.parse(userDetails).userName
        }&oldCartId=${
          JSON.parse(cartDetailsAnonymous).guid
        }&toMergeCartGuid=${cartGuId}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }

      return dispatch(mergeCartIdSuccess(resultJson));
    } catch (e) {
      return dispatch(mergeCartIdFailure(e.message));
    }
  };
}

export function checkPinCodeServiceAvailabilityRequest() {
  return {
    type: CHECK_PIN_CODE_SERVICE_AVAILABILITY_REQUEST,
    status: REQUESTING
  };
}
export function checkPinCodeServiceAvailabilitySuccess(cartDetailsCnc) {
  return {
    type: CHECK_PIN_CODE_SERVICE_AVAILABILITY_SUCCESS,
    status: SUCCESS,
    cartDetailsCnc
  };
}

export function checkPinCodeServiceAvailabilityFailure(error) {
  return {
    type: CHECK_PIN_CODE_SERVICE_AVAILABILITY_FAILURE,
    status: ERROR,
    error
  };
}
export function checkPinCodeServiceAvailability(
  userName,
  accessToken,
  pinCode,
  productCode
) {
  localStorage.setItem(DEFAULT_PIN_CODE_LOCAL_STORAGE, pinCode);

  return async (dispatch, getState, { api }) => {
    dispatch(checkPinCodeServiceAvailabilityRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${userName}/checkPincode?access_token=${accessToken}&productCode=${productCode}&pin=${pinCode}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(checkPinCodeServiceAvailabilitySuccess(resultJson));
    } catch (e) {
      dispatch(checkPinCodeServiceAvailabilityFailure(e.message));
    }
  };
}

// Actions to get All Stores CNC
export function getAllStoresCNCRequest() {
  return {
    type: GET_ALL_STORES_CNC_REQUEST,
    status: REQUESTING
  };
}
export function getAllStoresCNCSuccess(storeDetails) {
  return {
    type: GET_ALL_STORES_CNC_SUCCESS,
    status: SUCCESS,
    storeDetails
  };
}

export function getAllStoresCNCFailure(error) {
  return {
    type: GET_ALL_STORES_CNC_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator for getting all stores CNC
export function getAllStoresCNC(pinCode) {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(getAllStoresCNCRequest());
    try {
      const result = await api.get(
        `${ALL_STORES_PATH}/${pinCode}?access_token=${
          JSON.parse(customerCookie).access_token
        }`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(getAllStoresCNCSuccess(resultJson.stores));
    } catch (e) {
      dispatch(getAllStoresCNCFailure(e.message));
    }
  };
}

// Actions to Add Store CNC
export function addStoreCNCRequest() {
  return {
    type: ADD_STORE_CNC_REQUEST,
    status: REQUESTING
  };
}

export function addStoreCNCSuccess(storeAdded) {
  return {
    type: ADD_STORE_CNC_SUCCESS,
    status: SUCCESS,
    storeAdded
  };
}

export function addStoreCNCFailure(error) {
  return {
    type: ADD_STORE_CNC_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator to Add Store CNC
export function addStoreCNC(ussId, slaveId) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).code;
  return async (dispatch, getState, { api }) => {
    dispatch(addStoreCNCRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/${cartId}/addStore?USSID=${ussId}&access_token=${
          JSON.parse(customerCookie).access_token
        }&slaveId=${slaveId}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(addStoreCNCSuccess(resultJson));
    } catch (e) {
      dispatch(addStoreCNCFailure(e.message));
    }
  };
}

// Actions to Add Pick up Person
export function addPickUpPersonRequest() {
  return {
    type: ADD_PICKUP_PERSON_REQUEST,
    status: REQUESTING
  };
}

export function addPickUpPersonSuccess(cartDetailsCNC) {
  return {
    type: ADD_PICKUP_PERSON_SUCCESS,
    status: SUCCESS,
    cartDetailsCNC
  };
}

export function addPickUpPersonFailure(error) {
  return {
    type: ADD_PICKUP_PERSON_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator to Add Pick Up Person
export function addPickupPersonCNC(personMobile, personName) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).code;
  return async (dispatch, getState, { api }) => {
    dispatch(addPickUpPersonRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/${cartId}/addPickupPerson?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2&personMobile=${personMobile}&personName=${personName}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(addPickUpPersonSuccess(resultJson));
    } catch (e) {
      dispatch(addPickUpPersonFailure(e.message));
    }
  };
}

// Actions to Soft Reservation
export function softReservationRequest() {
  return {
    type: SOFT_RESERVATION_REQUEST,
    status: REQUESTING
  };
}

export function softReservationSuccess(softReserve) {
  return {
    type: SOFT_RESERVATION_SUCCESS,
    status: SUCCESS,
    softReserve
  };
}

export function softReservationFailure(error) {
  return {
    type: SOFT_RESERVATION_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator for Soft Reservation
export function softReservation(pinCode, payload) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).code;
  return async (dispatch, getState, { api }) => {
    dispatch(softReservationRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/${cartId}/softReservation?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2&pincode=${pinCode}&type=cart`,
        payload
      );
      const resultJson = await result.json();

      if (resultJson.status === FAILURE) {
        throw new Error(resultJson.error);
      }
      dispatch(getOrderSummary());
      dispatch(softReservationSuccess(resultJson.reservationItem));
    } catch (e) {
      dispatch(softReservationFailure(e.message));
    }
  };
}

// Actions to Soft Reservation
export function paymentModesRequest() {
  return {
    type: GET_PAYMENT_MODES_REQUEST,
    status: REQUESTING
  };
}

export function paymentModesSuccess(paymentModes) {
  return {
    type: GET_PAYMENT_MODES_SUCCESS,
    status: SUCCESS,
    paymentModes
  };
}

export function paymentModesFailure(error) {
  return {
    type: GET_PAYMENT_MODES_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator for Soft Reservation
export function getPaymentModes(pinCode, payload) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);

  let cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(paymentModesRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/getPaymentModes?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2&cartGuid=${cartId}`,
        payload
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }

      dispatch(paymentModesSuccess(resultJson));
    } catch (e) {
      dispatch(paymentModesFailure(e.message));
    }
  };
}

// Actions to Apply Bank Offer
export function applyBankOfferRequest() {
  return {
    type: APPLY_BANK_OFFER_REQUEST,
    status: REQUESTING
  };
}
export function applyBankOfferSuccess(bankOffer) {
  return {
    type: APPLY_BANK_OFFER_SUCCESS,
    status: SUCCESS,
    bankOffer
  };
}
export function applyBankOfferFailure(error) {
  return {
    type: APPLY_BANK_OFFER_FAILURE,
    status: ERROR,
    error
  };
}

export function applyBankOffer(couponCode) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(applyBankOfferRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/applyCartCoupons?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2&paymentMode=${PAYMENT_MODE}&couponCode=${couponCode}&cartGuid=${cartId}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(applyBankOfferSuccess(resultJson));
    } catch (e) {
      dispatch(applyBankOfferFailure(e.message));
    }
  };
}
// Actions to Release Bank Offer
export function releaseBankOfferRequest() {
  return {
    type: RELEASE_BANK_OFFER_REQUEST,
    status: REQUESTING
  };
}
export function releaseBankOfferSuccess() {
  return {
    type: RELEASE_BANK_OFFER_SUCCESS,
    status: SUCCESS
  };
}
export function releaseBankOfferFailure(error) {
  return {
    type: RELEASE_BANK_OFFER_FAILURE,
    status: ERROR,
    error
  };
}

export function releaseBankOffer(couponCode) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(releaseBankOfferRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/releaseCartCoupons?access_token=${
          JSON.parse(customerCookie).access_token
        }&paymentMode=${PAYMENT_MODE}&couponCode=${couponCode}&cartGuid=${cartId}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(releaseBankOfferSuccess());
    } catch (e) {
      dispatch(releaseBankOfferFailure(e.message));
    }
  };
}

// Actions to Apply Cliq Cash
export function applyCliqCashRequest() {
  return {
    type: APPLY_CLIQ_CASH_REQUEST,
    status: REQUESTING
  };
}

export function applyCliqCashSuccess(paymentDetails) {
  return {
    type: APPLY_CLIQ_CASH_SUCCESS,
    status: SUCCESS,
    paymentDetails
  };
}

export function applyCliqCashFailure(error) {
  return {
    type: APPLY_CLIQ_CASH_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator to bin Validation
export function applyCliqCash() {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(applyCliqCashRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/applyCliqCash?access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${cartId}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }

      dispatch(applyCliqCashSuccess(resultJson));
    } catch (e) {
      dispatch(applyCliqCashFailure(e.message));
    }
  };
}

// Actions to Remove Cliq Cash
export function removeCliqCashRequest() {
  return {
    type: REMOVE_CLIQ_CASH_REQUEST,
    status: REQUESTING
  };
}

export function removeCliqCashSuccess(paymentDetails) {
  return {
    type: REMOVE_CLIQ_CASH_SUCCESS,
    status: SUCCESS,
    paymentDetails
  };
}

export function removeCliqCashFailure(error) {
  return {
    type: REMOVE_CLIQ_CASH_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator to Remove Cliq Cash
export function removeCliqCash() {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(removeCliqCashRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/removeCliqCash?access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${cartId}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(removeCliqCashSuccess(resultJson));
    } catch (e) {
      dispatch(removeCliqCashFailure(e.message));
    }
  };
}

export function binValidationRequest() {
  return {
    type: BIN_VALIDATION_REQUEST,
    status: REQUESTING
  };
}

export function binValidationSuccess(binValidation) {
  return {
    type: BIN_VALIDATION_SUCCESS,
    status: SUCCESS,
    binValidation
  };
}

export function binValidationFailure(error) {
  return {
    type: BIN_VALIDATION_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator to bin Validation
export function binValidation(paymentMode, binNo) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(binValidationRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/binValidation?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2&paymentMode=${paymentMode}&binNo=${binNo}&cartGuid=${cartId}`
      );
      const resultJson = await result.json();

      if (resultJson.error) {
        throw new Error(resultJson.error);
      }

      dispatch(binValidationSuccess(resultJson));
    } catch (e) {
      dispatch(binValidationFailure(e.message));
    }
  };
}

export function binValidationForNetBanking(paymentMode, bankName) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(binValidationRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/binValidation?channel=mobile&access_token=${
          JSON.parse(customerCookie).access_token
        }&bankName=${bankName}&binNo=&cartGuid=${cartId}&paymentMode=${paymentMode}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }

      dispatch(binValidationSuccess(resultJson));
    } catch (e) {
      dispatch(binValidationFailure(e.message));
    }
  };
}

export function softReservationForPaymentRequest() {
  return {
    type: SOFT_RESERVATION_FOR_PAYMENT_REQUEST,
    status: REQUESTING
  };
}

export function softReservationForPaymentSuccess(orderDetails) {
  return {
    type: SOFT_RESERVATION_FOR_PAYMENT_SUCCESS,
    status: SUCCESS,
    orderDetails
  };
}

export function softReservationForPaymentFailure(error) {
  return {
    type: SOFT_RESERVATION_FOR_PAYMENT_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator to soft reservation For Payment
export function softReservationForPayment(cardDetails, address, paymentMode) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    let productItems = {};
    let item = [];
    each(getState().cart.cartDetailsCNC.products, product => {
      let productDetails = {};
      productDetails.ussId = product.USSID;
      productDetails.quantity = product.qtySelectedByUser;
      productDetails.fulfillmentType = product.fullfillmentType;
      if (product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves) {
        productDetails.deliveryMode =
          product.pinCodeResponse.validDeliveryModes[0].type;
        productDetails.serviceableSlaves =
          product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves[0];
      } else if (
        product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData
      ) {
        productDetails.deliveryMode =
          product.pinCodeResponse.validDeliveryModes[0].type;
        productDetails.serviceableSlaves =
          product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData[0].serviceableSlaves[0];
      }
      item.push(productDetails);
      productItems.item = item;
    });

    let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    let cartId = JSON.parse(cartDetails).guid;
    dispatch(softReservationForPaymentRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/softReservationForPayment?access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${cartId}&pincode=${cardDetails.pinCode}`,
        productItems
      );
      const resultJson = await result.json();

      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(softReservationForPaymentSuccess(resultJson));
      dispatch(jusPayTokenize(cardDetails, address, productItems, paymentMode));
    } catch (e) {
      dispatch(softReservationForPaymentFailure(e.message));
    }
  };
}

export function softReservationPaymentForNetBanking(
  paymentMode,
  bankName,
  pinCode
) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    let productItems = {};
    let item = [];
    each(getState().cart.cartDetailsCNC.products, product => {
      let productDetails = {};
      productDetails.ussId = product.USSID;
      productDetails.quantity = product.qtySelectedByUser;
      productDetails.fulfillmentType = product.fullfillmentType;
      if (product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves) {
        productDetails.deliveryMode =
          product.pinCodeResponse.validDeliveryModes[0].type;
        productDetails.serviceableSlaves =
          product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves[0];
      } else if (
        product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData
      ) {
        productDetails.deliveryMode =
          product.pinCodeResponse.validDeliveryModes[0].type;
        productDetails.serviceableSlaves =
          product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData[0].serviceableSlaves[0];
      }
      item.push(productDetails);
      productItems.item = item;
    });

    let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    let cartId = JSON.parse(cartDetails).guid;

    dispatch(softReservationForPaymentRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/softReservationForPayment?access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${cartId}&pincode=${pinCode}&type=payment`,
        productItems
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }

      dispatch(createJusPayOrderForNetBanking(bankName, pinCode, productItems));
    } catch (e) {
      dispatch(softReservationForPaymentFailure(e.message));
    }
  };
}

export function softReservationPaymentForSavedCard(
  cardDetails,
  address,
  paymentMode
) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    let productItems = {};
    let item = [];
    each(getState().cart.cartDetailsCNC.products, product => {
      let productDetails = {};
      productDetails.ussId = product.USSID;
      productDetails.quantity = product.qtySelectedByUser;
      productDetails.fulfillmentType = product.fullfillmentType;
      if (product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves) {
        productDetails.deliveryMode =
          product.pinCodeResponse.validDeliveryModes[0].type;
        productDetails.serviceableSlaves =
          product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves[0];
      } else if (
        product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData
      ) {
        productDetails.deliveryMode =
          product.pinCodeResponse.validDeliveryModes[0].type;
        productDetails.serviceableSlaves =
          product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData[0].serviceableSlaves[0];
      }
      item.push(productDetails);
      productItems.item = item;
    });

    let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    let cartId = JSON.parse(cartDetails).guid;
    dispatch(softReservationForPaymentRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/softReservationForPayment?access_token=${
          JSON.parse(customerCookie).access_token
        }&type=payment&cartGuid=${cartId}&pincode=${cardDetails.pinCode}`,
        productItems
      );
      const resultJson = await result.json();

      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }

      dispatch(createJusPayOrderForSavedCards(cardDetails, productItems));
    } catch (e) {
      dispatch(softReservationForPaymentFailure(e.message));
    }
  };
}

export function jusPayTokenizeRequest() {
  return {
    type: JUS_PAY_TOKENIZE_REQUEST,
    status: REQUESTING
  };
}

export function jusPayTokenizeSuccess(jusPayToken) {
  return {
    type: JUS_PAY_TOKENIZE_SUCCESS,
    status: SUCCESS,
    jusPayToken
  };
}

export function jusPayTokenizeFailure(error) {
  return {
    type: JUS_PAY_TOKENIZE_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator to Just pay Tokenize
export function jusPayTokenize(
  cardDetails,
  address,
  productItems,
  paymentMode
) {
  return async (dispatch, getState, { api }) => {
    dispatch(jusPayTokenizeRequest());
    try {
      const result = await api.postJusPay(
        `card/tokenize?card_exp_month=${cardDetails.monthValue}&card_exp_year=${
          cardDetails.yearValue
        }&card_number=${cardDetails.cardNumber}&card_security_code=${
          cardDetails.cvvNumber
        }&merchant_id=${getState().cart.paymentModes.merchantID}&name_on_card=${
          cardDetails.cardName
        }`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(
        createJusPayOrder(
          resultJson.token,
          productItems,
          address,
          cardDetails,
          paymentMode
        )
      );
    } catch (e) {
      dispatch(jusPayTokenizeFailure(e.message));
    }
  };
}

export function createJusPayOrderRequest() {
  return {
    type: CREATE_JUS_PAY_ORDER_REQUEST,
    status: REQUESTING
  };
}

export function createJusPayOrderSuccess(jusPayDetails) {
  return {
    type: CREATE_JUS_PAY_ORDER_SUCCESS,
    status: SUCCESS,
    jusPayDetails
  };
}

export function createJusPayOrderFailure(error) {
  return {
    type: CREATE_JUS_PAY_ORDER_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator to create Jus Pay Order
export function createJusPayOrder(
  token,
  cartItem,
  address,
  cardDetails,
  paymentMode
) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    let orderDetails = getState().cart.cartDetailsCNC;
    dispatch(createJusPayOrderRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/createJuspayOrder?access_token=${
          JSON.parse(customerCookie).access_token
        }&firstName=${address.firstName}&lastName=${
          address.lastName
        }&addressLine1=${address.line1}&addressLine2=${
          address.line1
        }&addressLine3=${address.line1}&country=${
          address.country.isocode
        }&city=${orderDetails.addressDetailsList.addresses[0].city}&state=${
          address.state
        }&pincode=${
          address.postalCode
        }&cardSaved=true&sameAsShipping=true&cartGuid=${cartId}&token=${token}&isPwa=true&platformNumber=2`,
        cartItem
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(
        jusPayPaymentMethodType(
          resultJson.juspayOrderId,
          cardDetails,
          paymentMode
        )
      );
    } catch (e) {
      dispatch(createJusPayOrderFailure(e.message));
    }
  };
}

export function createJusPayOrderForNetBanking(bankName, pinCode, cartItem) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(createJusPayOrderRequest());

    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/createJuspayOrder?state=&addressLine2=&lastName=&firstName=${bankName}&addressLine3=&sameAsShipping=true&cardSaved=false&bankName=&cardFingerPrint=&platform=2&pincode=${pinCode}&city=&cartGuid=${cartId}&token=&cardRefNo=&country=&addressLine1=&access_token=${
          JSON.parse(customerCookie).access_token
        }`,
        cartItem
      );
      const resultJson = await result.json();

      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(
        jusPayPaymentMethodTypeForNetBanking(resultJson.juspayOrderId, bankName)
      );
    } catch (e) {
      dispatch(createJusPayOrderFailure(e.message));
    }
  };
}

export function createJusPayOrderForSavedCards(cardDetails, cartItem) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(createJusPayOrderRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/createJuspayOrder?state=&addressLine2=&lastName=&firstName=&addressLine3=&sameAsShipping=null&cardSaved=false&bankName=${
          cardDetails.cardIssuer
        }&
        cardFingerPrint=${cardDetails.cardFingerprint}&platform=2&pincode=${
          cardDetails.pinCode
        }&city=&cartGuid=${cartId}&token=&cardRefNo=${
          cardDetails.cardReferenceNumber
        }&country=&addressLine1=&access_token=${
          JSON.parse(customerCookie).access_token
        }`,
        cartItem
      );
      const resultJson = await result.json();

      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(
        jusPayPaymentMethodTypeForSavedCards(
          resultJson.juspayOrderId,
          cardDetails
        )
      );
    } catch (e) {
      dispatch(createJusPayOrderFailure(e.message));
    }
  };
}

export function updateTransactionDetailsRequest() {
  return {
    type: UPDATE_TRANSACTION_DETAILS_REQUEST,
    status: REQUESTING
  };
}

export function updateTransactionDetailsSuccess(transactionDetails) {
  return {
    type: UPDATE_TRANSACTION_DETAILS_SUCCESS,
    status: SUCCESS,
    transactionDetails
  };
}

export function updateTransactionDetailsFailure(error) {
  return {
    type: UPDATE_TRANSACTION_DETAILS_FAILURE,
    status: ERROR,
    error
  };
}

export function jusPayPaymentMethodTypeRequest() {
  return {
    type: JUS_PAY_PAYMENT_METHOD_TYPE_REQUEST,
    status: REQUESTING
  };
}

export function jusPayPaymentMethodTypeSuccess(justPayPaymentDetails) {
  return {
    type: JUS_PAY_PAYMENT_METHOD_TYPE_SUCCESS,
    status: SUCCESS,
    justPayPaymentDetails
  };
}

export function jusPayPaymentMethodTypeFailure(error) {
  return {
    type: JUS_PAY_PAYMENT_METHOD_TYPE_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator to JusPay Payment Method Type
export function jusPayPaymentMethodType(
  juspayOrderId,
  cardDetails,
  paymentMode
) {
  return async (dispatch, getState, { api }) => {
    dispatch(jusPayPaymentMethodTypeRequest());
    try {
      let url;
      if (paymentMode === PAYMENT_EMI) {
        url = `txns?payment_method_type=CARD&redirect_after_payment=true&format=json&card_exp_month=${
          cardDetails.monthValue
        }&card_exp_year=${cardDetails.yearValue}&card_number=${
          cardDetails.cardNumber
        }&emi_bank=${cardDetails.emi_bank}&emi_tenure=${
          cardDetails.emi_tenure
        }&is_emi=${cardDetails.is_emi}&card_security_code=${
          cardDetails.cvvNumber
        }&merchant_id=${getState().cart.paymentModes.merchantID}&name_on_card=${
          cardDetails.cardName
        }&order_id=${juspayOrderId}&save_to_locker=1`;
      } else {
        url = `txns?payment_method_type=CARD&redirect_after_payment=true&format=json&card_exp_month=${
          cardDetails.monthValue
        }&card_exp_year=${cardDetails.yearValue}&card_number=${
          cardDetails.cardNumber
        }&card_security_code=${cardDetails.cvvNumber}&merchant_id=${
          getState().cart.paymentModes.merchantID
        }&name_on_card=${
          cardDetails.cardName
        }&order_id=${juspayOrderId}&save_to_locker=true`;
      }
      const result = await api.postJusPay(url);
      const resultJson = await result.json();

      if (resultJson.status === FAILURE) {
        throw new Error(resultJson.error_message);
      }
      dispatch(jusPayPaymentMethodTypeSuccess(resultJson));
    } catch (e) {
      dispatch(jusPayPaymentMethodTypeFailure(e.message));
    }
  };
}

export function jusPayPaymentMethodTypeForSavedCards(
  juspayOrderId,
  cardDetails
) {
  return async (dispatch, getState, { api }) => {
    dispatch(jusPayPaymentMethodTypeRequest());
    try {
      const result = await api.postJusPay(
        `txns?payment_method_type=CARD&redirect_after_payment=true&format=json&card_security_code=${
          cardDetails.cvvNumber
        }&card_token=${cardDetails.cardToken}&merchant_id=${
          getState().cart.paymentModes.merchantID
        }&order_id=${juspayOrderId}`
      );
      const resultJson = await result.json();

      if (resultJson.status === FAILURE) {
        throw new Error(resultJson.error_message);
      }
      dispatch(jusPayPaymentMethodTypeSuccess(resultJson));
    } catch (e) {
      dispatch(jusPayPaymentMethodTypeFailure(e.message));
    }
  };
}

export function jusPayPaymentMethodTypeForNetBanking(juspayOrderId, bankName) {
  return async (dispatch, getState, { api }) => {
    dispatch(jusPayPaymentMethodTypeRequest());
    try {
      const result = await api.postJusPay(
        `txns?payment_method_type=NB&redirect_after_payment=true&format=json&merchant_id=${
          getState().cart.paymentModes.merchantID
        }&order_id=${juspayOrderId}&payment_method=${bankName}`
      );
      const resultJson = await result.json();

      if (resultJson.status === FAILURE) {
        throw new Error(resultJson.error_message);
      }
      dispatch(jusPayPaymentMethodTypeSuccess(resultJson));
    } catch (e) {
      dispatch(jusPayPaymentMethodTypeFailure(e.message));
    }
  };
}

// Action Creator to update Transaction Details
export function updateTransactionDetails(paymentMode, juspayOrderID, cartId) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

  return async (dispatch, getState, { api }) => {
    dispatch(updateTransactionDetailsRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/updateTransactionDetailsforCard?access_token=${
          JSON.parse(customerCookie).access_token
        }&platformNumber=2&isPwa=true&paymentMode=${paymentMode}&juspayOrderID=${juspayOrderID}&cartGuid=${cartId}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }

      dispatch(updateTransactionDetailsSuccess(resultJson));
      dispatch(orderConfirmation(resultJson.orderId));
    } catch (e) {
      dispatch(updateTransactionDetailsFailure(e.message));
    }
  };
}

export function orderConfirmationRequest() {
  return {
    type: ORDER_CONFIRMATION_REQUEST,
    status: REQUESTING
  };
}

export function orderConfirmationSuccess(confirmedOrderDetails) {
  return {
    type: ORDER_CONFIRMATION_SUCCESS,
    status: SUCCESS,
    confirmedOrderDetails
  };
}

export function orderConfirmationFailure(error) {
  return {
    type: ORDER_CONFIRMATION_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator for Order Confirmation
export function orderConfirmation(orderId) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(orderConfirmationRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/orderConfirmation/${orderId}?access_token=${
          JSON.parse(customerCookie).access_token
        }&platformNumber=2`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }

      dispatch(orderConfirmationSuccess(resultJson));
    } catch (e) {
      dispatch(orderConfirmationFailure(e.message));
    }
  };
}

export function captureOrderExperienceRequest() {
  return {
    type: ORDER_EXPERIENCE_CAPTURE_REQUEST,
    status: REQUESTING
  };
}

export function captureOrderExperienceSuccess(orderExperience) {
  return {
    type: ORDER_EXPERIENCE_CAPTURE_SUCCESS,
    status: SUCCESS,
    orderExperience
  };
}

export function captureOrderExperienceFailure(error) {
  return {
    type: ORDER_EXPERIENCE_CAPTURE_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator for Order Confirmation
export function captureOrderExperience(orderId, rating) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(captureOrderExperienceRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/orderExperience/${orderId}?access_token=${
          JSON.parse(customerCookie).access_token
        }&ratings=${rating}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }

      dispatch(captureOrderExperienceSuccess(resultJson));
    } catch (e) {
      dispatch(captureOrderExperienceFailure(e.message));
    }
  };
}

//Actions For get COD Eligibility
export function getCODEligibilityRequest() {
  return {
    type: GET_COD_ELIGIBILITY_REQUEST,
    status: REQUESTING
  };
}

export function getCODEligibilitySuccess(codEligibilityDetails) {
  return {
    type: GET_COD_ELIGIBILITY_SUCCESS,
    status: SUCCESS,
    codEligibilityDetails
  };
}

export function getCODEligibilityFailure(error) {
  return {
    type: GET_COD_ELIGIBILITY_FAILURE,
    status: ERROR,
    error
  };
}

//Actions creator for COD Eligibility
export function getCODEligibility() {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  const cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(getCODEligibilityRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/getCODEligibility?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2&cartGuid=${cartId}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(getCODEligibilitySuccess(resultJson));
    } catch (e) {
      dispatch(getCODEligibilityFailure(e.message));
    }
  };
}

//Actions For bin Validation COD
export function binValidationForCODRequest() {
  return {
    type: BIN_VALIDATION_COD_REQUEST,
    status: REQUESTING
  };
}

export function binValidationForCODSuccess(binValidationCOD) {
  return {
    type: BIN_VALIDATION_COD_SUCCESS,
    status: SUCCESS,
    binValidationCOD
  };
}

export function binValidationForCODFailure(error) {
  return {
    type: BIN_VALIDATION_COD_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator to bin Validation For COD
export function binValidationForCOD(paymentMode) {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  const cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(binValidationForCODRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/binValidation?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2&paymentMode=${paymentMode}&cartGuid=${cartId}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(binValidationForCODSuccess(resultJson));
    } catch (e) {
      dispatch(binValidationForCODFailure(e.message));
    }
  };
}

export function updateTransactionDetailsForCODRequest() {
  return {
    type: UPDATE_TRANSACTION_DETAILS_FOR_COD_REQUEST,
    status: REQUESTING
  };
}

export function updateTransactionDetailsForCODSuccess(transactionDetails) {
  return {
    type: UPDATE_TRANSACTION_DETAILS_FOR_COD_SUCCESS,
    status: SUCCESS,
    transactionDetails
  };
}

export function updateTransactionDetailsForCODFailure(error) {
  return {
    type: UPDATE_TRANSACTION_DETAILS_FOR_COD_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator to update Transaction Details
export function updateTransactionDetailsForCOD(paymentMode, juspayOrderID) {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  const cartId = JSON.parse(cartDetails).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(updateTransactionDetailsForCODRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/updateTransactionDetailsforCOD?access_token=${
          JSON.parse(customerCookie).access_token
        }&platformNumber=2&isPwa=true&paymentMode=${paymentMode}&juspayOrderID=${juspayOrderID}&cartGuid=${cartId}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(orderConfirmation(resultJson.orderId));
    } catch (e) {
      dispatch(updateTransactionDetailsForCODFailure(e.message));
    }
  };
}

//Actions for soft reservation for COD Payment
export function softReservationForCODPaymentRequest() {
  return {
    type: SOFT_RESERVATION_FOR_COD_PAYMENT_REQUEST,
    status: REQUESTING
  };
}

export function softReservationForCODPaymentSuccess(softReserveCODPayment) {
  return {
    type: SOFT_RESERVATION_FOR_COD_PAYMENT_SUCCESS,
    status: SUCCESS,
    softReserveCODPayment
  };
}

export function softReservationForCODPaymentFailure(error) {
  return {
    type: SOFT_RESERVATION_FOR_COD_PAYMENT_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator to soft reservation For COD Payment
export function softReservationForCODPayment(pinCode) {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    let productItems = {};
    let item = [];
    each(getState().cart.cartDetailsCNC.products, product => {
      let productDetails = {};
      productDetails.ussId = product.USSID;
      productDetails.quantity = product.qtySelectedByUser;
      productDetails.deliveryMode =
        product.pinCodeResponse.validDeliveryModes[0].type;
      productDetails.serviceableSlaves =
        product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves[0];
      productDetails.fulfillmentType = product.fullfillmentType;
      item.push(productDetails);
      productItems.item = item;
    });

    const cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    const cartId = JSON.parse(cartDetails).guid;

    dispatch(softReservationForCODPaymentRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/softReservationForPayment?access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${cartId}&pincode=${pinCode}`,
        productItems
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(updateTransactionDetailsForCOD(CASH_ON_DELIVERY, ""));
      dispatch(softReservationForCODPaymentSuccess(resultJson));
    } catch (e) {
      dispatch(softReservationForCODPaymentFailure(e.message));
    }
  };
}

// Actions for Add Product to Wish List
export function addProductToWishListRequest() {
  return {
    type: ADD_PRODUCT_TO_WISH_LIST_REQUEST,
    status: REQUESTING
  };
}
export function addProductToWishListSuccess() {
  return {
    type: ADD_PRODUCT_TO_WISH_LIST_SUCCESS,
    status: SUCCESS
  };
}

export function addProductToWishListFailure(error) {
  return {
    type: ADD_PRODUCT_TO_WISH_LIST_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator for Add Product To Wish List
export function addProductToWishList(productDetails) {
  return async (dispatch, getState, { api }) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    const cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    const cartId = JSON.parse(cartDetails).guid;
    dispatch(addProductToWishListRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/addProductInWishlist?platformNumber=2&access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&ussid=${productDetails.USSID}&productCode=${
          productDetails.productcode
        }&wishlistName=${MY_WISH_LIST}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(addProductToWishListSuccess());
    } catch (e) {
      dispatch(addProductToWishListFailure(e.message));
    }
  };
}

// Action for remove Item from Cart Logged In
export function removeItemFromCartLoggedInRequest() {
  return {
    type: REMOVE_ITEM_FROM_CART_LOGGED_IN_REQUEST,
    status: REQUESTING
  };
}
export function removeItemFromCartLoggedInSuccess() {
  return {
    type: REMOVE_ITEM_FROM_CART_LOGGED_IN_SUCCESS,
    status: SUCCESS
  };
}

export function removeItemFromCartLoggedInFailure(error) {
  return {
    type: REMOVE_ITEM_FROM_CART_LOGGED_IN_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator for remove Item from Cart Logged In
export function removeItemFromCartLoggedIn(cartListItemPosition, pinCode) {
  return async (dispatch, getState, { api }) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    const cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    const cartId = JSON.parse(cartDetails).code;
    dispatch(removeItemFromCartLoggedInRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/${cartId}/deleteEntries/${cartListItemPosition}?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(
        getCartDetails(
          JSON.parse(userDetails).userName,
          JSON.parse(customerCookie).access_token,
          cartId,
          pinCode
        )
      ).then(() => {
        dispatch(removeItemFromCartLoggedInSuccess());
      });
    } catch (e) {
      dispatch(removeItemFromCartLoggedInFailure(e.message));
    }
  };
}

// Action for remove Item from Cart Logged Out
export function removeItemFromCartLoggedOutRequest() {
  return {
    type: REMOVE_ITEM_FROM_CART_LOGGED_OUT_REQUEST,
    status: REQUESTING
  };
}
export function removeItemFromCartLoggedOutSuccess() {
  return {
    type: REMOVE_ITEM_FROM_CART_LOGGED_OUT_SUCCESS,
    status: SUCCESS
  };
}

export function removeItemFromCartLoggedOutFailure(error) {
  return {
    type: REMOVE_ITEM_FROM_CART_LOGGED_OUT_FAILURE,
    status: ERROR,
    error
  };
}

// Action Creator for remove Item from Cart Logged Out
export function removeItemFromCartLoggedOut(cartListItemPosition, pinCode) {
  return async (dispatch, getState, { api }) => {
    const cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
    const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    dispatch(removeItemFromCartLoggedOutRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/anonymous/carts/${
          JSON.parse(cartDetailsAnonymous).guid
        }/deleteEntries/${cartListItemPosition}?access_token=${
          JSON.parse(globalCookie).access_token
        }&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(
        getCartDetails(
          ANONYMOUS_USER,
          JSON.parse(globalCookie).access_token,
          JSON.parse(cartDetailsAnonymous).guid,
          pinCode
        )
      ).then(() => {
        dispatch(removeItemFromCartLoggedOutSuccess());
      });
    } catch (e) {
      dispatch(removeItemFromCartLoggedOutFailure(e.message));
    }
  };
}

// Actions for update quantity in cart
export function updateQuantityInCartLoggedInRequest() {
  return {
    type: UPDATE_QUANTITY_IN_CART_LOGGED_IN_REQUEST,
    status: REQUESTING
  };
}

export function updateQuantityInCartLoggedInSuccess(updateQuantityDetails) {
  return {
    type: UPDATE_QUANTITY_IN_CART_LOGGED_IN_SUCCESS,
    status: SUCCESS,
    updateQuantityDetails
  };
}

export function updateQuantityInCartLoggedInFailure(error) {
  return {
    type: UPDATE_QUANTITY_IN_CART_LOGGED_IN_FAILURE,
    status: ERROR,
    error
  };
}

// Action creator for update quantity in cart for LoggedIn
export function updateQuantityInCartLoggedIn(selectedItem, quantity, pinCode) {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  const cartId = JSON.parse(cartDetails).code;

  return async (dispatch, getState, { api }) => {
    dispatch(updateQuantityInCartLoggedInRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/${cartId}/updateEntries/${selectedItem}?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2&quantity=${quantity}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(
        getCartDetails(
          JSON.parse(userDetails).userName,
          JSON.parse(customerCookie).access_token,
          cartId,
          pinCode
        )
      ).then(() => {
        dispatch(updateQuantityInCartLoggedInSuccess(resultJson));
      });
    } catch (e) {
      dispatch(updateQuantityInCartLoggedInFailure(e.message));
    }
  };
}

// Actions for update quantity in cart for Logged Out
export function updateQuantityInCartLoggedOutRequest() {
  return {
    type: UPDATE_QUANTITY_IN_CART_LOGGED_OUT_REQUEST,
    status: REQUESTING
  };
}

export function updateQuantityInCartLoggedOutSuccess(updateQuantityDetails) {
  return {
    type: UPDATE_QUANTITY_IN_CART_LOGGED_OUT_SUCCESS,
    status: SUCCESS,
    updateQuantityDetails
  };
}

export function updateQuantityInCartLoggedOutFailure(error) {
  return {
    type: UPDATE_QUANTITY_IN_CART_LOGGED_OUT_FAILURE,
    status: ERROR,
    error
  };
}

// Action creator for update quantity in cart
export function updateQuantityInCartLoggedOut(selectedItem, quantity, pinCode) {
  const cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
  const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);

  return async (dispatch, getState, { api }) => {
    dispatch(updateQuantityInCartLoggedOutRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/anonymous/carts/${
          JSON.parse(cartDetailsAnonymous).guid
        }/updateEntries/${selectedItem}?access_token=${
          JSON.parse(globalCookie).access_token
        }&isPwa=true&platformNumber=2&quantity=${quantity}`
      );

      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }

      dispatch(
        getCartDetails(
          ANONYMOUS_USER,
          JSON.parse(globalCookie).access_token,
          JSON.parse(cartDetailsAnonymous).guid,
          pinCode
        )
      ).then(() => {
        dispatch(updateQuantityInCartLoggedOutSuccess(resultJson));
      });
    } catch (e) {
      dispatch(updateQuantityInCartLoggedOutFailure(e.message));
    }
  };
}
