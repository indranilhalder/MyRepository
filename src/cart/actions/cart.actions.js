import {
  SUCCESS,
  REQUESTING,
  ERROR,
  SUCCESS_CAMEL_CASE,
  SUCCESS_UPPERCASE,
  JUS_PAY_AUTHENTICATION_FAILED,
  NO,
  BANK_COUPON_COOKIE,
  PAYMENT_MODE_TYPE,
  SELECTED_BANK_NAME,
  EMI,
  NO_COST_EMI_COUPON,
  CLIQ_CASH
} from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
import each from "lodash.foreach";
import * as ErrorHandling from "../../general/ErrorHandling.js";
import {
  showModal,
  EMI_ITEM_LEVEL_BREAKAGE,
  EMI_BANK_TERMS_AND_CONDITIONS,
  INVALID_BANK_COUPON_POPUP
} from "../../general/modal.actions";
import {
  CUSTOMER_ACCESS_TOKEN,
  GLOBAL_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  FAILURE,
  FAILURE_UPPERCASE,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS,
  DEFAULT_PIN_CODE_LOCAL_STORAGE,
  JUS_PAY_PENDING,
  JUS_PAY_CHARGED,
  FAILURE_LOWERCASE,
  SOFT_RESERVATION_ITEM,
  ADDRESS_DETAILS_FOR_PAYMENT,
  CART_BAG_DETAILS
} from "../../lib/constants";
import queryString, { parse } from "query-string";
import { setBagCount } from "../../general/header.actions";

import {
  setDataLayer,
  ADOBE_CART_TYPE,
  setDataLayerForCartDirectCalls,
  ADOBE_REMOVE_ITEM,
  ADOBE_ORDER_CONFIRMATION,
  ADOBE_CHECKOUT_TYPE,
  ADOBE_CALLS_FOR_CHANGE_QUANTITY,
  ADOBE_CALLS_FOR_APPLY_COUPON_SUCCESS,
  ADOBE_CALLS_FOR_APPLY_COUPON_FAIL,
  setDataLayerForOrderConfirmationDirectCalls,
  ADOBE_DIRECT_CALLS_FOR_ORDER_CONFIRMATION_SUCCESS,
  ADOBE_DIRECT_CALLS_FOR_ORDER_CONFIRMATION_FAILURE,
  setDataLayerForCheckoutDirectCalls,
  ADOBE_ADD_ADDRESS_TO_ORDER,
  ADOBE_CALL_FOR_LANDING_ON_PAYMENT_MODE,
  ADOBE_CALL_FOR_SELECT_DELIVERY_MODE,
  ADOBE_CALL_FOR_APPLY_COUPON_FAILURE,
  ADOBE_CALL_FOR_APPLY_COUPON_SUCCESS,
  ADOBE_ADD_NEW_ADDRESS_ON_CHECKOUT_PAGE,
  ADOBE_FINAL_PAYMENT_MODES,
  ADOBE_CALL_FOR_CLIQ_CASH_TOGGLE_ON,
  ADOBE_CALL_FOR_CLIQ_CASH_TOGGLE_OFF,
  ADOBE_MY_ACCOUNT_ADDRESS_BOOK
} from "../../lib/adobeUtils";
export const CLEAR_CART_DETAILS = "CLEAR_CART_DETAILS";
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
export const ADD_NEW_ADDRESS_FOR_USER_ADDRESS_SUCCESS =
  "ADD_NEW_ADDRESS_FOR_USER_ADDRESS_SUCCESS";
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

export const GENERATE_CART_ID_FOR_ANONYMOUS_USER_REQUEST =
  "GENERATE_CART_ID_FOR_ANONYMOUS_USER_REQUEST";
export const GENERATE_CART_ID_FOR_ANONYMOUS_USER_SUCCESS =
  "GENERATE_CART_ID_FOR_ANONYMOUS_USER_SUCCESS";
export const GENERATE_CART_ID_FOR_ANONYMOUS_USER_FAILURE =
  "GENERATE_CART_ID_FOR_ANONYMOUS_USER_FAILURE";

export const GENERATE_CART_ID_FOR_LOGGED_IN_USER_REQUEST =
  "GENERATE_CART_ID_FOR_LOGGED_IN_USER_REQUEST";
export const GENERATE_CART_ID_FOR_LOGGED_IN_USER_SUCCESS =
  "GENERATE_CART_ID_FOR_LOGGED_IN_USER_SUCCESS";
export const GENERATE_CART_ID_FOR_LOGGED_IN_USER_FAILURE =
  "GENERATE_CART_ID_FOR_LOGGED_IN_USER_FAILURE";

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
export const CREATE_JUS_PAY_ORDER_FOR_CLIQ_CASH_SUCCESS =
  "CREATE_JUS_PAY_ORDER_FOR_CLIQ_CASH_SUCCESS";
export const CREATE_JUS_PAY_ORDER_FAILURE = "CREATE_JUS_PAY_ORDER_FAILURE";

export const JUS_PAY_PAYMENT_METHOD_TYPE_REQUEST =
  "JUS_PAY_PAYMENT_METHOD_TYPE_REQUEST";
export const JUS_PAY_PAYMENT_METHOD_TYPE_SUCCESS =
  "JUS_PAY_PAYMENT_METHOD_TYPE_SUCCESS";
export const JUS_PAY_PAYMENT_METHOD_TYPE_FOR_GIFT_CARD_SUCCESS =
  "JUS_PAY_PAYMENT_METHOD_TYPE_FOR_GIFT_CARD_SUCCESS";
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

export const CLEAR_ORDER_EXPERIENCE_CAPTURE = "CLEAR_ORDER_EXPERIENCE_CAPTURE";

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

export const DISPLAY_COUPON_REQUEST = "DISPLAY_COUPON_REQUEST";
export const DISPLAY_COUPON_SUCCESS = "DISPLAY_COUPON_SUCCESS";
export const DISPLAY_COUPON_FAILURE = "DISPLAY_COUPON_FAILURE";

export const ELIGIBILITY_OF_NO_COST_EMI_REQUEST =
  "ELIGIBILITY_OF_NO_COST_EMI_REQUEST";
export const ELIGIBILITY_OF_NO_COST_EMI_SUCCESS =
  "ELIGIBILITY_OF_NO_COST_EMI_SUCCESS";
export const ELIGIBILITY_OF_NO_COST_EMI_FAILURE =
  "ELIGIBILITY_OF_NO_COST_EMI_FAILURE";

export const BANK_AND_TENURE_DETAILS_REQUEST =
  "BANK_AND_TENURE_DETAILS_REQUEST";
export const BANK_AND_TENURE_DETAILS_SUCCESS =
  "BANK_AND_TENURE_DETAILS_SUCCESS";
export const BANK_AND_TENURE_DETAILS_FAILURE =
  "BANK_AND_TENURE_DETAILS_FAILURE";

export const EMI_TERMS_AND_CONDITIONS_FOR_BANK_REQUEST =
  "EMI_TERMS_AND_CONDITIONS_FOR_BANK_REQUEST";
export const EMI_TERMS_AND_CONDITIONS_FOR_BANK_SUCCESS =
  "EMI_TERMS_AND_CONDITIONS_FOR_BANK_SUCCESS";
export const EMI_TERMS_AND_CONDITIONS_FOR_BANK_FAILURE =
  "EMI_TERMS_AND_CONDITIONS_FOR_BANK_FAILURE";

export const APPLY_NO_COST_EMI_REQUEST = "APPLY_NO_COST_EMI_REQUEST";
export const APPLY_NO_COST_EMI_SUCCESS = "APPLY_NO_COST_EMI_SUCCESS";
export const APPLY_NO_COST_EMI_FAILURE = "APPLY_NO_COST_EMI_FAILURE";

export const REMOVE_NO_COST_EMI_REQUEST = "REMOVE_NO_COST_EMI_REQUEST";
export const REMOVE_NO_COST_EMI_SUCCESS = "REMOVE_NO_COST_EMI_SUCCESS";
export const REMOVE_NO_COST_EMI_FAILURE = "REMOVE_NO_COST_EMI_FAILURE";

export const EMI_ITEM_BREAK_UP_DETAILS_REQUEST =
  "EMI_ITEM_BREAK_UP_DETAILS_REQUEST";
export const EMI_ITEM_BREAK_UP_DETAILS_SUCCESS =
  "EMI_ITEM_BREAK_UP_DETAILS_SUCCESS";
export const EMI_ITEM_BREAK_UP_DETAILS_FAILURE =
  "EMI_ITEM_BREAK_UP_DETAILS_FAILURE";

export const PAYMENT_FAILURE_ORDER_DETAILS_REQUEST =
  "PAYMENT_FAILURE_ORDER_DETAILS_REQUEST";
export const PAYMENT_FAILURE_ORDER_DETAILS_SUCCESS =
  "PAYMENT_FAILURE_ORDER_DETAILS_SUCCESS";
export const PAYMENT_FAILURE_ORDER_DETAILS_FAILURE =
  "PAYMENT_FAILURE_ORDER_DETAILS_FAILURE";
export const RESET_IS_SOFT_RESERVATION_FAILED =
  "RESET_IS_SOFT_RESERVATION_FAILED";

export const PAYMENT_MODE = "credit card";
const PAYMENT_EMI = "EMI";
const CASH_ON_DELIVERY = "COD";
const ERROR_CODE_FOR_BANK_OFFER_INVALID_1 = "B9078";
const ERROR_CODE_FOR_BANK_OFFER_INVALID_2 = "B6009";
const INVALID_COUPON_ERROR_MESSAGE = "invalid coupon";
export const CART_ITEM_COOKIE = "cartItems";
export const ADDRESS_FOR_PLACE_ORDER = "orderAddress";
export const ANONYMOUS_USER = "anonymous";

const ERROR_MESSAGE_FOR_CREATE_JUS_PAY_CALL = "Something went wrong";
export function displayCouponRequest() {
  return {
    type: DISPLAY_COUPON_REQUEST,
    status: REQUESTING
  };
}
export function displayCouponSuccess(couponDetails) {
  return {
    type: DISPLAY_COUPON_SUCCESS,
    status: SUCCESS,
    couponDetails
  };
}

export function displayCouponFailure(error) {
  return {
    type: DISPLAY_COUPON_FAILURE,
    status: ERROR,
    error
  };
}

export function displayCouponsForLoggedInUser(userId, accessToken, cartId) {
  return async (dispatch, getState, { api }) => {
    dispatch(displayCouponRequest());

    try {
      const result = await api.get(
        `${USER_CART_PATH}/${userId}/displayCouponOffers?access_token=${accessToken}&cartGuid=${cartId}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      dispatch(displayCouponSuccess(resultJson));
    } catch (e) {
      dispatch(displayCouponFailure(e.message));
    }
  };
}

export function displayCouponsForAnonymous(userId, accessToken) {
  return async (dispatch, getState, { api }) => {
    dispatch(displayCouponRequest());

    try {
      const result = await api.get(
        `${USER_CART_PATH}/${userId}/displayOpenCouponOffers?access_token=${accessToken}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(displayCouponSuccess(resultJson));
    } catch (e) {
      dispatch(displayCouponFailure(e.message));
    }
  };
}

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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      setDataLayer(
        ADOBE_CART_TYPE,
        resultJson,
        getState().icid.value,
        getState().icid.icidType
      );

      //set the local storage
      //set local storage
      localStorage.setItem(CART_BAG_DETAILS, []);
      let cartProducts = [];
      resultJson &&
        each(resultJson.products, product => {
          if (product.isGiveAway === NO) {
            cartProducts.push(product.USSID);
          }
        });
      localStorage.setItem(CART_BAG_DETAILS, JSON.stringify(cartProducts));
      dispatch(setBagCount(cartProducts.length));
      return dispatch(cartDetailsSuccess(resultJson));
    } catch (e) {
      return dispatch(cartDetailsFailure(e.message));
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
  isSoftReservation,
  isSetDataLayer: false
) {
  return async (dispatch, getState, { api }) => {
    dispatch(cartDetailsCNCRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${userId}/carts/${cartId}/cartDetailsCNC?access_token=${accessToken}&isPwa=true&platformNumber=2&pincode=${pinCode}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }

      if (isSoftReservation) {
        let productItems = {};
        let item = [];
        each(resultJson.products, product => {
          if (product.isGiveAway === NO) {
            let productDetails = {};
            productDetails.ussId = product.USSID;
            productDetails.quantity = product.qtySelectedByUser;
            productDetails.fulfillmentType = product.fullfillmentType;

            if (
              product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves
            ) {
              productDetails.deliveryMode =
                product.pinCodeResponse.validDeliveryModes[0].type;
              productDetails.serviceableSlaves =
                product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves;
            } else if (
              product.pinCodeResponse.validDeliveryModes[0]
                .CNCServiceableSlavesData
            ) {
              productDetails.deliveryMode =
                product.pinCodeResponse.validDeliveryModes[0].type;
              productDetails.serviceableSlaves =
                product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData[0].serviceableSlaves;
            }
            item.push(productDetails);
            productItems.item = item;
          }
        });

        dispatch(softReservation(pinCode, productItems));
      }
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      // setting data layer only for first time
      if (isSetDataLayer) {
        setDataLayer(
          ADOBE_CHECKOUT_TYPE,
          resultJson,
          getState().icid.value,
          getState().icid.icidType
        );
      }
      dispatch(cartDetailsCNCSuccess(resultJson));
    } catch (e) {
      dispatch(cartDetailsCNCFailure(e.message));
    }
  };
}

export function applyUserCouponRequest() {
  return {
    type: APPLY_USER_COUPON_REQUEST,
    status: REQUESTING
  };
}
export function applyUserCouponSuccess(couponResult, couponCode) {
  return {
    type: APPLY_USER_COUPON_SUCCESS,
    status: SUCCESS,
    couponResult,
    couponCode
  };
}

export function applyUserCouponFailure(error) {
  return {
    type: APPLY_USER_COUPON_FAILURE,
    status: ERROR,
    error
  };
}

export function applyUserCouponForAnonymous(couponCode) {
  const cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
  const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  let cartId = JSON.parse(cartDetailsAnonymous).guid;
  return async (dispatch, getState, { api }) => {
    dispatch(applyUserCouponRequest());
    let couponObject = new FormData();
    couponObject.append("access_token", JSON.parse(globalCookie).access_token);
    couponObject.append("couponCode", couponCode);
    couponObject.append("channel", "Mobile");

    try {
      const result = await api.postFormData(
        `${USER_CART_PATH}/anonymous/carts/${cartId}/applyCouponsAnonymous?`,
        couponObject
      );

      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        setDataLayerForCartDirectCalls(
          ADOBE_CALLS_FOR_APPLY_COUPON_FAIL,
          couponCode
        );
        throw new Error(resultJsonStatus.message);
      }
      setDataLayerForCartDirectCalls(
        ADOBE_CALLS_FOR_APPLY_COUPON_SUCCESS,
        couponCode
      );
      return dispatch(applyUserCouponSuccess(resultJson, couponCode));
    } catch (e) {
      return dispatch(applyUserCouponFailure(e.message));
    }
  };
}

export function applyUserCouponForLoggedInUsers(couponCode) {
  return async (dispatch, getState, { api }) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    let cartGuId = JSON.parse(cartDetails).guid;

    dispatch(applyUserCouponRequest());
    let couponObject = new FormData();

    couponObject.append("couponCode", couponCode);
    couponObject.append("cartGuid", cartGuId);

    try {
      const result = await api.postFormData(
        `${USER_CART_PATH}/${JSON.parse(userDetails).userName}/carts/${
          JSON.parse(cartDetails).code
        }/applyCoupons?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2`,
        couponObject
      );

      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        setDataLayerForCartDirectCalls(
          ADOBE_CALLS_FOR_APPLY_COUPON_FAIL,
          couponCode
        );
        throw new Error(resultJsonStatus.message);
      }
      setDataLayerForCartDirectCalls(
        ADOBE_CALLS_FOR_APPLY_COUPON_SUCCESS,
        couponCode
      );
      return dispatch(applyUserCouponSuccess(resultJson, couponCode));
    } catch (e) {
      return dispatch(applyUserCouponFailure(e.message));
    }
  };
}

export function releaseUserCouponRequest() {
  return {
    type: RELEASE_USER_COUPON_REQUEST,
    status: REQUESTING
  };
}
export function releaseUserCouponSuccess(couponResult) {
  return {
    type: RELEASE_USER_COUPON_SUCCESS,
    status: SUCCESS,
    couponResult
  };
}

export function releaseUserCouponFailure(error) {
  return {
    type: RELEASE_USER_COUPON_FAILURE,
    status: ERROR,
    error
  };
}

export function releaseCouponForAnonymous(oldCouponCode, newCouponCode) {
  const cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
  const globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  let cartId = JSON.parse(cartDetailsAnonymous).guid;

  return async (dispatch, getState, { api }) => {
    dispatch(releaseUserCouponRequest());
    let couponObject = new FormData();
    couponObject.append("access_token", JSON.parse(globalCookie).access_token);
    couponObject.append("couponCode", oldCouponCode);

    try {
      const result = await api.postFormData(
        `${USER_CART_PATH}/anonymous/carts/${cartId}/releaseCouponsAnonymous?`,
        couponObject
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      if (newCouponCode) {
        return dispatch(applyUserCouponForAnonymous(newCouponCode));
      }
      return dispatch(releaseUserCouponSuccess(resultJson));
    } catch (e) {
      return dispatch(releaseUserCouponFailure(e.message));
    }
  };
}

export function releaseUserCoupon(oldCouponCode, newCouponCode) {
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
        }&isPwa=true&platformNumber=2&couponCode=${oldCouponCode}&cartGuid=${cartGuId}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      if (newCouponCode) {
        return dispatch(applyUserCouponForLoggedInUsers(newCouponCode));
      }
      return dispatch(releaseUserCouponSuccess(resultJson));
    } catch (e) {
      return dispatch(releaseUserCouponFailure(e.message));
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

export function getUserAddress(setDataLayerForMyAccount: false) {
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      if (setDataLayerForMyAccount) {
        setDataLayer(ADOBE_MY_ACCOUNT_ADDRESS_BOOK);
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
export function addUserAddressSuccess() {
  return {
    type: ADD_USER_ADDRESS_SUCCESS,
    status: SUCCESS
  };
}

export function addNewAddressForUserSuccess(userAddress) {
  return {
    type: ADD_NEW_ADDRESS_FOR_USER_ADDRESS_SUCCESS,
    status: SUCCESS
  };
}

export function addUserAddressFailure(error) {
  return {
    type: ADD_USER_ADDRESS_FAILURE,
    status: ERROR,
    error
  };
}

export function addUserAddress(userAddress, fromAccount) {
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
        }&firstName=${userAddress.firstName}&lastName=${userAddress.lastName}
        &postalCode=${userAddress.postalCode}&line1=${
          userAddress.line1
        }&state=${userAddress.state}&line2=${userAddress.line2}&line3=${
          userAddress.line3
        }&town=${userAddress.town}&landmark=${
          userAddress.landmark ? userAddress.landmark : ""
        }&defaultFlag=${userAddress.defaultFlag}&emailId=${
          JSON.parse(userDetails).userName
        }`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      setDataLayerForCheckoutDirectCalls(
        ADOBE_ADD_NEW_ADDRESS_ON_CHECKOUT_PAGE
      );
      return dispatch(addUserAddressSuccess());
    } catch (e) {
      return dispatch(addUserAddressFailure(e.message));
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
    dispatch(selectDeliveryModeRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${JSON.parse(userDetails).userName}/carts/${
          JSON.parse(cartDetails).code
        }/selectDeliveryMode?access_token=${
          JSON.parse(customerCookie).access_token
        }&deliverymodeussId=${JSON.stringify(deliveryUssId)}&removeExchange=0`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
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
      // setting data layer after selecting delivery mode success
      setDataLayerForCheckoutDirectCalls(ADOBE_CALL_FOR_SELECT_DELIVERY_MODE);
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
    dispatch(addAddressToCartRequest());
    try {
      let userId = JSON.parse(userDetails).userName;
      let access_token = JSON.parse(customerCookie).access_token;
      let cartId = JSON.parse(cartDetails).code;
      const result = await api.post(
        `${USER_CART_PATH}/${userId}/addAddressToOrder?channel=mobile&access_token=${access_token}&addressId=${addressId}&cartId=${cartId}&removeExchangeFromCart=0`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(getCartDetailsCNC(userId, access_token, cartId, pinCode, false));
      dispatch(addAddressToCartSuccess());
      setDataLayerForCheckoutDirectCalls(ADOBE_ADD_ADDRESS_TO_ORDER);
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      dispatch(emiBankingDetailsSuccess(resultJson));
    } catch (e) {
      dispatch(emiBankingDetailsFailure(e.message));
    }
  };
}

export function generateCartidForLoggedInUserRequest() {
  return {
    type: GENERATE_CART_ID_FOR_LOGGED_IN_USER_REQUEST,
    status: REQUESTING
  };
}

export function generateCartIdForLoggedInUserFailure(error) {
  return {
    type: GENERATE_CART_ID_FOR_LOGGED_IN_USER_FAILURE,
    status: FAILURE,
    error
  };
}
export function generateCartIdForLoggedInUserSuccess(cartDetails) {
  return {
    type: GENERATE_CART_ID_FOR_LOGGED_IN_USER_SUCCESS,
    status: SUCCESS,
    cartDetails
  };
}

export function generateCartIdForLoggedInUser() {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(generateCartidForLoggedInUserRequest());

    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true`
      );
      const resultJson = await result.json();

      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      return dispatch(generateCartIdForLoggedInUserSuccess(resultJson));
    } catch (e) {
      return dispatch(generateCartIdForLoggedInUserFailure(e.message));
    }
  };
}

export function generateCartIdForAnonymousUserSuccess(cartDetails) {
  return {
    type: GENERATE_CART_ID_FOR_ANONYMOUS_USER_SUCCESS,
    status: SUCCESS,
    cartDetails
  };
}

export function generateCartdIdForAnonymousUserRequest() {
  return {
    type: GENERATE_CART_ID_FOR_ANONYMOUS_USER_REQUEST,
    status: REQUESTING
  };
}

export function generateCartIdForAnonymousUserFailure(error) {
  return {
    type: GENERATE_CART_ID_FOR_ANONYMOUS_USER_FAILURE,
    error,
    status: FAILURE
  };
}

export function generateCartIdForAnonymous() {
  let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(generateCartdIdForAnonymousUserRequest());

    try {
      const result = await api.post(
        `${USER_CART_PATH}/anonymous/carts?access_token=${
          JSON.parse(globalCookie).access_token
        }&isPwa=true`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      return dispatch(generateCartIdForAnonymousUserSuccess(resultJson));
    } catch (e) {
      return dispatch(generateCartIdForAnonymousUserFailure(e.message));
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
  let cartId = JSON.parse(cartDetails).code;
  return async (dispatch, getState, { api }) => {
    dispatch(orderSummaryRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/${cartId}/displayOrderSummary?access_token=${
          JSON.parse(customerCookie).access_token
        }&pincode=${pincode}&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();

      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(getPaymentModes(resultJson.cartGuid));
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      dispatch(
        getCartDetailsCNC(
          JSON.parse(userDetails).userName,
          JSON.parse(customerCookie).access_token,
          cartId,
          localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE),
          false
        )
      );
      return dispatch(addPickUpPersonSuccess(resultJson));
    } catch (e) {
      return dispatch(addPickUpPersonFailure(e.message));
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

      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(getOrderSummary(pinCode));
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
export function getPaymentModes(guId) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(paymentModesRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/getPaymentModes?access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${guId}&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      // here  we are setting data layer for when user lands on the payment modes
      // page

      dispatch(paymentModesSuccess(resultJson));
      setDataLayerForCheckoutDirectCalls(
        ADOBE_CALL_FOR_LANDING_ON_PAYMENT_MODE
      );
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

  let cartId;
  const parsedQueryString = queryString.parse(window.location.search);
  const value = parsedQueryString.value;
  if (value) {
    cartId = value;
  } else {
    cartId = JSON.parse(cartDetails).guid;
  }

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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        setDataLayerForCheckoutDirectCalls(
          ADOBE_CALL_FOR_APPLY_COUPON_FAILURE,
          couponCode
        );
        if (resultJson.couponMessage) {
          throw new Error(resultJson.couponMessage);
        } else {
          throw new Error(resultJsonStatus.message);
        }
      }
      localStorage.setItem(BANK_COUPON_COOKIE, couponCode);
      setDataLayerForCheckoutDirectCalls(
        ADOBE_CALL_FOR_APPLY_COUPON_SUCCESS,
        couponCode
      );
      return dispatch(applyBankOfferSuccess(resultJson));
    } catch (e) {
      return dispatch(applyBankOfferFailure(e.message));
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
export function releaseBankOfferSuccess(bankOffer) {
  return {
    type: RELEASE_BANK_OFFER_SUCCESS,
    status: SUCCESS,
    bankOffer
  };
}
export function releaseBankOfferFailure(error) {
  return {
    type: RELEASE_BANK_OFFER_FAILURE,
    status: ERROR,
    error
  };
}

export function releaseBankOffer(previousCouponCode, newCouponCode: null) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId;
  const parsedQueryString = queryString.parse(window.location.search);
  const value = parsedQueryString.value;
  if (value) {
    cartId = value;
  } else {
    cartId = JSON.parse(cartDetails).guid;
  }
  return async (dispatch, getState, { api }) => {
    dispatch(releaseBankOfferRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/releaseCartCoupons?access_token=${
          JSON.parse(customerCookie).access_token
        }&paymentMode=${PAYMENT_MODE}&couponCode=${previousCouponCode}&cartGuid=${cartId}&isPwa=true`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      localStorage.removeItem(BANK_COUPON_COOKIE);
      if (newCouponCode) {
        return dispatch(applyBankOffer(newCouponCode));
      }
      return dispatch(releaseBankOfferSuccess(resultJson));
    } catch (e) {
      return dispatch(releaseBankOfferFailure(e.message));
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
        }&cartGuid=${cartId}&isPwa=true`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(applyCliqCashSuccess(resultJson));
      setDataLayerForCheckoutDirectCalls(ADOBE_CALL_FOR_CLIQ_CASH_TOGGLE_ON);
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
        }&cartGuid=${cartId}&isPwa=true`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      setDataLayerForCheckoutDirectCalls(ADOBE_CALL_FOR_CLIQ_CASH_TOGGLE_OFF);
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
export function binValidation(paymentMode, binNo, cartGuId) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  if (!cartGuId) {
    let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    cartGuId = JSON.parse(cartDetails).guid;
  }

  return async (dispatch, getState, { api }) => {
    let paymentTypeObject = new FormData();
    paymentTypeObject.append("cartGuid", cartGuId);
    paymentTypeObject.append("binNo", binNo);
    dispatch(binValidationRequest());
    try {
      const result = await api.postFormData(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/binValidation?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2&paymentMode=${paymentMode}`,
        paymentTypeObject
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      if (resultJson.bankName) {
        localStorage.setItem(SELECTED_BANK_NAME, resultJson.bankName);
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
    let paymentTypeObject = new FormData();
    paymentTypeObject.append("cartGuid", cartId);
    paymentTypeObject.append("binNo", "");
    dispatch(binValidationRequest());
    try {
      const result = await api.postFormData(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/binValidation?channel=mobile&access_token=${
          JSON.parse(customerCookie).access_token
        }&bankName=${bankName}&paymentMode=${paymentMode}`,
        paymentTypeObject
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      localStorage.setItem(SELECTED_BANK_NAME, bankName);

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
export function softReservationForPayment(cardDetails, address) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const paymentMode = localStorage.getItem(PAYMENT_MODE_TYPE);
  const pinCode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);
  return async (dispatch, getState, { api }) => {
    let productItems = {};
    let item = [];
    each(getState().cart.cartDetailsCNC.products, product => {
      if (product.isGiveAway === NO) {
        let productDetails = {};
        productDetails.ussId = product.USSID;
        productDetails.quantity = product.qtySelectedByUser;
        productDetails.fulfillmentType = product.fullfillmentType;
        if (product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves) {
          productDetails.deliveryMode =
            product.pinCodeResponse.validDeliveryModes[0].type;
          productDetails.serviceableSlaves =
            product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves;
        } else if (
          product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData
        ) {
          productDetails.deliveryMode =
            product.pinCodeResponse.validDeliveryModes[0].type;
          productDetails.serviceableSlaves =
            product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData[0].serviceableSlaves;
        }
        item.push(productDetails);
        productItems.item = item;
      }
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
        }&cartGuid=${cartId}&pincode=${pinCode}&type=payment&paymentMode=${
          paymentMode ? paymentMode : ""
        }&isPwa=true`,
        productItems
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      setDataLayerForCheckoutDirectCalls(ADOBE_FINAL_PAYMENT_MODES);
      dispatch(softReservationForPaymentSuccess(resultJson));
      if (localStorage.getItem(PAYMENT_MODE_TYPE) === EMI) {
        dispatch(
          createJusPayOrder(
            "",
            productItems,
            address,
            cardDetails,
            paymentMode,
            false
          )
        );
      } else {
        dispatch(
          jusPayTokenize(cardDetails, address, productItems, paymentMode, false)
        );
      }
    } catch (e) {
      dispatch(softReservationForPaymentFailure(e.message));
    }
  };
}

export function softReservationPaymentForNetBanking(
  paymentMethodType,
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
      if (product.isGiveAway === NO) {
        let productDetails = {};
        productDetails.ussId = product.USSID;
        productDetails.quantity = product.qtySelectedByUser;
        productDetails.fulfillmentType = product.fullfillmentType;
        if (product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves) {
          productDetails.deliveryMode =
            product.pinCodeResponse.validDeliveryModes[0].type;
          productDetails.serviceableSlaves =
            product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves;
        } else if (
          product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData
        ) {
          productDetails.deliveryMode =
            product.pinCodeResponse.validDeliveryModes[0].type;
          productDetails.serviceableSlaves =
            product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData[0].serviceableSlaves;
        }
        item.push(productDetails);
        productItems.item = item;
      }
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      dispatch(
        createJusPayOrderForNetBanking(
          paymentMethodType,
          productItems,
          bankName,
          pinCode
        )
      );
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
      if (product.isGiveAway === NO) {
        let productDetails = {};
        productDetails.ussId = product.USSID;
        productDetails.quantity = product.qtySelectedByUser;
        productDetails.fulfillmentType = product.fullfillmentType;
        if (product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves) {
          productDetails.deliveryMode =
            product.pinCodeResponse.validDeliveryModes[0].type;
          productDetails.serviceableSlaves =
            product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves;
        } else if (
          product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData
        ) {
          productDetails.deliveryMode =
            product.pinCodeResponse.validDeliveryModes[0].type;
          productDetails.serviceableSlaves =
            product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData[0].serviceableSlaves;
        }
        item.push(productDetails);
        productItems.item = item;
      }
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
        }&type=payment&cartGuid=${cartId}&pincode=${localStorage.getItem(
          DEFAULT_PIN_CODE_LOCAL_STORAGE
        )}`,
        productItems
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      setDataLayerForCheckoutDirectCalls(ADOBE_FINAL_PAYMENT_MODES);
      dispatch(createJusPayOrderForSavedCards(cardDetails, productItems));
    } catch (e) {
      dispatch(softReservationForPaymentFailure(e.message));
    }
  };
}

export function softReservationForCliqCash(pinCode) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    let productItems = {};
    let item = [];
    each(getState().cart.cartDetailsCNC.products, product => {
      if (product.isGiveAway === NO) {
        let productDetails = {};
        productDetails.ussId = product.USSID;
        productDetails.quantity = product.qtySelectedByUser;
        productDetails.fulfillmentType = product.fullfillmentType;

        if (product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves) {
          productDetails.deliveryMode =
            product.pinCodeResponse.validDeliveryModes[0].type;
          productDetails.serviceableSlaves =
            product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves;
        } else if (
          product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData
        ) {
          productDetails.deliveryMode =
            product.pinCodeResponse.validDeliveryModes[0].type;
          productDetails.serviceableSlaves =
            product.pinCodeResponse.validDeliveryModes[0].CNCServiceableSlavesData[0].serviceableSlaves;
        }
        item.push(productDetails);

        productItems.item = item;
      }
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
        }&cartGuid=${cartId}&pincode=${pinCode}`,
        productItems
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      setDataLayerForCheckoutDirectCalls(ADOBE_FINAL_PAYMENT_MODES);
      dispatch(softReservationForPaymentSuccess(resultJson));
      dispatch(createJusPayOrderForCliqCash(pinCode, productItems));
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
  cartItem,
  paymentMode,
  isPaymentFailed
) {
  if (!isPaymentFailed) {
    localStorage.setItem(CART_ITEM_COOKIE, JSON.stringify(cartItem));
  }
  return async (dispatch, getState, { api }) => {
    dispatch(jusPayTokenizeRequest());
    let cardObject = new FormData();
    cardObject.append("card_exp_month", cardDetails.monthValue);
    cardObject.append("card_exp_year", cardDetails.yearValue);
    cardObject.append("card_number", cardDetails.cardNumber);
    cardObject.append("card_security_code", cardDetails.cvvNumber);
    cardObject.append("merchant_id", getState().cart.paymentModes.merchantID);
    cardObject.append("name_on_card", cardDetails.cardName);
    try {
      const result = await api.postJusPay(`card/tokenize?`, cardObject);
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(jusPayTokenizeSuccess(resultJson.token));
      dispatch(
        createJusPayOrder(
          resultJson.token,
          cartItem,
          address,
          cardDetails,
          paymentMode,
          isPaymentFailed
        )
      );
    } catch (e) {
      dispatch(jusPayTokenizeFailure(e.message));
    }
  };
}

export function jusPayTokenizeForGiftCard(cardDetails, paymentMode, guId) {
  return async (dispatch, getState, { api }) => {
    let cardObject = new FormData();
    cardObject.append("card_exp_month", cardDetails.monthValue);
    cardObject.append("card_exp_year", cardDetails.yearValue);
    cardObject.append("card_number", cardDetails.cardNumber);
    cardObject.append("card_security_code", cardDetails.cvvNumber);
    cardObject.append("merchant_id", getState().cart.paymentModes.merchantID);
    cardObject.append("name_on_card", cardDetails.cardName);
    dispatch(jusPayTokenizeRequest());
    try {
      const result = await api.postJusPay(`card/tokenize?`, cardObject);
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(
        createJusPayOrderForGiftCard(
          resultJson.token,
          cardDetails,
          paymentMode,
          guId
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

export function createJusPayOrderSuccessForCliqCash(cliqCashJusPayDetails) {
  return {
    type: CREATE_JUS_PAY_ORDER_FOR_CLIQ_CASH_SUCCESS,
    status: SUCCESS,
    cliqCashJusPayDetails
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
  paymentMode,
  isPaymentFailed
) {
  const jusPayUrl = `${
    window.location.origin
  }/checkout/payment-method/cardPayment`;
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartId;
  let url = queryString.parse(window.location.search);

  if (url && url.value) {
    cartId = url && url.value;
  } else {
    localStorage.setItem(CART_ITEM_COOKIE, JSON.stringify(cartItem));
    let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    cartId = JSON.parse(cartDetails).guid;
  }
  const currentSelectedPaymentMode = localStorage.getItem(PAYMENT_MODE_TYPE);
  const bankName = localStorage.getItem(SELECTED_BANK_NAME);
  return async (dispatch, getState, { api }) => {
    dispatch(createJusPayOrderRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/createJuspayOrder?access_token=${
          JSON.parse(customerCookie).access_token
        }&firstName=${address.firstName}&lastName=${
          address.lastName
        }&addressLine1=${address.line1 ? address.line1 : ""}&addressLine2=${
          address.line2 ? address.line2 : ""
        }&addressLine3=${address.line3 ? address.line3 : ""}&country=${
          address.country.isocode
        }&city=${address.city ? address.city : ""}&state=${
          address.state ? address.state : ""
        }&pincode=${
          address.postalCode
        }&cardSaved=true&sameAsShipping=true&cartGuid=${cartId}&token=${token}&isPwa=true&platformNumber=2&juspayUrl=${encodeURIComponent(
          jusPayUrl
        )}&bankName=${
          bankName ? bankName : ""
        }&paymentMode=${currentSelectedPaymentMode}`,
        cartItem
      );
      const resultJson = await result.json();

      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        if (
          resultJson.errorCode === ERROR_CODE_FOR_BANK_OFFER_INVALID_1 ||
          resultJson.errorCode === ERROR_CODE_FOR_BANK_OFFER_INVALID_2
        ) {
          dispatch(createJusPayOrderFailure(INVALID_COUPON_ERROR_MESSAGE));
          return dispatch(
            showModal(INVALID_BANK_COUPON_POPUP, {
              result: resultJson
            })
          );
        } else {
          throw new Error(resultJsonStatus.message);
        }
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

export function createJusPayOrderForGiftCard(
  token,
  cardDetails,
  paymentMode,
  guId
) {
  const jusPayUrl = `${
    window.location.origin
  }/checkout/payment-method/cardPayment`;
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId = JSON.parse(cartDetails).guid;
  const currentSelectedPaymentMode = localStorage.getItem(PAYMENT_MODE_TYPE);
  const bankName = localStorage.getItem(SELECTED_BANK_NAME);
  return async (dispatch, getState, { api }) => {
    let orderDetails = getState().cart.cartDetailsCNC;
    dispatch(createJusPayOrderRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/createJuspayOrder?access_token=${
          JSON.parse(customerCookie).access_token
        }&firstName=&lastName=&addressLine1=&addressLine2=&addressLine3=&country=&city=&state=&pincode=&cardSaved=true&sameAsShipping=true&cartGuid=${guId}&token=${token}&isPwa=true&platformNumber=2&juspayUrl=${encodeURIComponent(
          jusPayUrl
        )}&paymentMode=${currentSelectedPaymentMode}&bankName=${
          bankName ? bankName : ""
        }`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(
        jusPayPaymentMethodTypeForGiftCard(
          resultJson.juspayOrderId,
          cardDetails,
          paymentMode,
          guId
        )
      );
    } catch (e) {
      dispatch(createJusPayOrderFailure(e.message));
    }
  };
}

export function createJusPayOrderForNetBanking(
  paymentMethodType,
  cartItem,
  bankName,
  pinCode
) {
  const jusPayUrl = `${
    window.location.origin
  }/checkout/payment-method/cardPayment`;
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  let cartId;
  const parsedQueryString = queryString.parse(window.location.search);
  if (parsedQueryString.value) {
    cartId = parsedQueryString.value;
  } else {
    localStorage.setItem(CART_ITEM_COOKIE, JSON.stringify(cartItem));
    cartId = JSON.parse(cartDetails).guid;
  }
  const currentSelectedPaymentMode = localStorage.getItem(PAYMENT_MODE_TYPE);
  return async (dispatch, getState, { api }) => {
    dispatch(createJusPayOrderRequest());

    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/createJuspayOrder?state=&addressLine2=&lastName=&firstName=&bankName=${bankName}&addressLine3=&sameAsShipping=true&cardSaved=false&cardFingerPrint=&platform=2&pincode=${pinCode}&city=&cartGuid=${cartId}&token=&cardRefNo=&country=&addressLine1=&access_token=${
          JSON.parse(customerCookie).access_token
        }&juspayUrl=${encodeURIComponent(
          jusPayUrl
        )}&paymentMode=${currentSelectedPaymentMode}&isPwa=true`,
        cartItem
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        if (
          resultJson.errorCode === ERROR_CODE_FOR_BANK_OFFER_INVALID_1 ||
          resultJson.errorCode === ERROR_CODE_FOR_BANK_OFFER_INVALID_2
        ) {
          dispatch(createJusPayOrderFailure(INVALID_COUPON_ERROR_MESSAGE));
          return dispatch(
            showModal(INVALID_BANK_COUPON_POPUP, {
              result: resultJson
            })
          );
        } else {
          throw new Error(resultJsonStatus.message);
        }
      }
      dispatch(
        jusPayPaymentMethodTypeForNetBanking(
          paymentMethodType,
          resultJson.juspayOrderId,
          bankName
        )
      );
    } catch (e) {
      dispatch(createJusPayOrderFailure(e.message));
    }
  };
}

export function createJusPayOrderForGiftCardNetBanking(guId) {
  const jusPayUrl = `${
    window.location.origin
  }/checkout/payment-method/cardPayment`;
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const currentSelectedPaymentMode = localStorage.getItem(PAYMENT_MODE_TYPE);
  const bankName = localStorage.getItem(SELECTED_BANK_NAME);
  return async (dispatch, getState, { api }) => {
    dispatch(createJusPayOrderRequest());

    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/createJuspayOrder?state=&addressLine2=&lastName=&firstName=&bankName=${bankName}&addressLine3=&sameAsShipping=true&cardSaved=false&bankName=&cardFingerPrint=&platform=2&pincode=&city=&cartGuid=${guId}&token=&cardRefNo=&country=&addressLine1=&access_token=${
          JSON.parse(customerCookie).access_token
        }&juspayUrl=${encodeURIComponent(
          jusPayUrl
        )}$paymentMode=${currentSelectedPaymentMode}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(
        jusPayPaymentMethodTypeForGiftCardNetBanking(
          resultJson.juspayOrderId,
          bankName,
          guId
        )
      );
    } catch (e) {
      dispatch(createJusPayOrderFailure(e.message));
    }
  };
}

export function createJusPayOrderForSavedCards(
  cardDetails,
  cartItemObj,
  isPaymentFailed
) {
  let cartItem = cartItemObj;
  if (!isPaymentFailed) {
    localStorage.setItem(CART_ITEM_COOKIE, JSON.stringify(cartItem));
  }

  const jusPayUrl = `${
    window.location.origin
  }/checkout/multi/payment-method/cardPayment`;
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartId;
  if (isPaymentFailed) {
    let url = queryString.parse(window.location.search);
    cartId = url && url.value;
  } else {
    let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    cartId = JSON.parse(cartDetails).guid;
  }
  const currentSelectedPaymentMode = localStorage.getItem(PAYMENT_MODE_TYPE);
  const bankName = localStorage.getItem(SELECTED_BANK_NAME);
  return async (dispatch, getState, { api }) => {
    dispatch(createJusPayOrderRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/createJuspayOrder?state=&addressLine2=&lastName=&firstName=&addressLine3=&sameAsShipping=null&cardSaved=false&bankName=${
          cardDetails.cardIssuer
        }&
        cardFingerPrint=${
          cardDetails.cardFingerprint
        }&platform=2&pincode=${localStorage.getItem(
          DEFAULT_PIN_CODE_LOCAL_STORAGE
        )}&city=&cartGuid=${cartId}&token=&cardRefNo=${
          cardDetails.cardReferenceNumber
        }&country=&addressLine1=&access_token=${
          JSON.parse(customerCookie).access_token
        }&juspayUrl=${encodeURIComponent(
          jusPayUrl
        )}$paymentMode=${currentSelectedPaymentMode}$bankName=${
          bankName ? bankName : ""
        }`,
        cartItem
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        if (
          resultJson.errorCode === ERROR_CODE_FOR_BANK_OFFER_INVALID_1 ||
          resultJson.errorCode === ERROR_CODE_FOR_BANK_OFFER_INVALID_2
        ) {
          dispatch(createJusPayOrderFailure(INVALID_COUPON_ERROR_MESSAGE));
          return dispatch(
            showModal(INVALID_BANK_COUPON_POPUP, {
              result: resultJson
            })
          );
        } else {
          throw new Error(resultJsonStatus.message);
        }
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

export function createJusPayOrderForGiftCardFromSavedCards(cardDetails, guId) {
  const jusPayUrl = `${
    window.location.origin
  }/checkout/payment-method/cardPayment`;
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const currentSelectedPaymentMode = localStorage.getItem(PAYMENT_MODE_TYPE);
  const bankName = localStorage.getItem(SELECTED_BANK_NAME);
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
        }&city=&cartGuid=${guId}&token=&cardRefNo=${
          cardDetails.cardReferenceNumber
        }&country=&addressLine1=&access_token=${
          JSON.parse(customerCookie).access_token
        }&juspayUrl=${encodeURIComponent(
          jusPayUrl
        )}&paymentMode=${currentSelectedPaymentMode}&bankName=${
          bankName ? bankName : ""
        }`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(
        jusPayPaymentMethodTypeForGiftCardFromSavedCards(
          resultJson.juspayOrderId,
          cardDetails,
          guId
        )
      );
    } catch (e) {
      dispatch(createJusPayOrderFailure(e.message));
    }
  };
}

export function createJusPayOrderForCliqCash(
  pinCode,
  cartItemObj,
  isPaymentFailed = false
) {
  let cartItem;

  if (localStorage.getItem(CART_ITEM_COOKIE)) {
    cartItem = JSON.parse(localStorage.getItem(CART_ITEM_COOKIE));
  } else {
    cartItem = cartItemObj;
    localStorage.setItem(CART_ITEM_COOKIE, JSON.stringify(cartItem));
  }

  const jusPayUrl = `${
    window.location.origin
  }/checkout/payment-method/cardPayment`;
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartId;
  if (isPaymentFailed) {
    let url = queryString.parse(window.location.search);
    cartId = url && url.value;
  } else {
    let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    cartId = JSON.parse(cartDetails).guid;
  }

  return async (dispatch, getState, { api }) => {
    dispatch(createJusPayOrderRequest());

    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/createJuspayOrder?state=&addressLine2=&lastName=&firstName=&addressLine3=&sameAsShipping=true&cardSaved=false&bankName=&cardFingerPrint=&platform=2&pincode=${pinCode}&city=&cartGuid=${cartId}&token=&cardRefNo=&country=&addressLine1=&access_token=${
          JSON.parse(customerCookie).access_token
        }&juspayUrl=${encodeURIComponent(jusPayUrl)}&paymentMode=${CLIQ_CASH}`,
        cartItem
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        if (
          resultJson.errorCode === ERROR_CODE_FOR_BANK_OFFER_INVALID_1 ||
          resultJson.errorCode === ERROR_CODE_FOR_BANK_OFFER_INVALID_2
        ) {
          dispatch(createJusPayOrderFailure(INVALID_COUPON_ERROR_MESSAGE));
          return dispatch(
            showModal(INVALID_BANK_COUPON_POPUP, {
              result: resultJson
            })
          );
        } else {
          throw new Error(resultJsonStatus.message);
        }
      }
      dispatch(createJusPayOrderSuccessForCliqCash(resultJson));
      dispatch(setBagCount(0));
      localStorage.setItem(CART_BAG_DETAILS, []);
      dispatch(generateCartIdForLoggedInUser());
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
  // Here we need to dispatch an action to cre
  return {
    type: JUS_PAY_PAYMENT_METHOD_TYPE_SUCCESS,
    status: SUCCESS,
    justPayPaymentDetails
  };
}

export function jusPayPaymentMethodTypeForGiftCardSuccess(
  justPayPaymentDetails,
  guId
) {
  // Here we need to dispatch an action to cre
  return {
    type: JUS_PAY_PAYMENT_METHOD_TYPE_FOR_GIFT_CARD_SUCCESS,
    status: SUCCESS,
    justPayPaymentDetails,
    guId
  };
}

export function jusPayPaymentMethodTypeFailure(error) {
  return {
    type: JUS_PAY_PAYMENT_METHOD_TYPE_FAILURE,
    status: ERROR,
    error: ERROR_MESSAGE_FOR_CREATE_JUS_PAY_CALL
  };
}

export function jusPayPaymentMethodTypeForGiftCard(
  juspayOrderId,
  cardDetails,
  paymentMode,
  guId
) {
  return async (dispatch, getState, { api }) => {
    dispatch(jusPayPaymentMethodTypeRequest());
    try {
      let cardObject = new FormData();
      cardObject.append("payment_method_type", "CARD");
      cardObject.append("redirect_after_payment", "true");
      cardObject.append("format", "json");
      cardObject.append("card_exp_month", cardDetails.monthValue);
      cardObject.append("card_exp_year", cardDetails.yearValue);
      cardObject.append("card_number", cardDetails.cardNumber);
      cardObject.append("card_security_code", cardDetails.cvvNumber);
      cardObject.append("merchant_id", getState().cart.paymentModes.merchantID);
      cardObject.append("name_on_card", cardDetails.cardName);
      cardObject.append("order_id", juspayOrderId);
      cardObject.append("save_to_locker", true);
      if (localStorage.getItem(NO_COST_EMI_COUPON)) {
        cardObject.append("emi_bank", cardDetails.emi_bank);
        cardObject.append("emi_tenure", cardDetails.emi_tenure);
        cardObject.append("is_emi", cardDetails.is_emi);
      }
      const result = await api.postJusPay(`txns?`, cardObject);
      const resultJson = await result.json();

      if (
        resultJson.status === JUS_PAY_PENDING ||
        resultJson.status === SUCCESS ||
        resultJson.status === SUCCESS_CAMEL_CASE ||
        resultJson.status === SUCCESS_UPPERCASE ||
        resultJson.status === JUS_PAY_CHARGED
      ) {
        dispatch(jusPayPaymentMethodTypeForGiftCardSuccess(resultJson, guId));
      } else {
        throw new Error(resultJson.error_message);
      }
    } catch (e) {
      dispatch(jusPayPaymentMethodTypeFailure(e.message));
    }
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
      let cardObject = new FormData();
      cardObject.append("payment_method_type", "CARD");
      cardObject.append("redirect_after_payment", "true");
      cardObject.append("format", "json");
      cardObject.append("card_exp_month", cardDetails.monthValue);
      cardObject.append("card_exp_year", cardDetails.yearValue);
      cardObject.append("card_number", cardDetails.cardNumber);
      cardObject.append("card_security_code", cardDetails.cvvNumber);
      cardObject.append("merchant_id", getState().cart.paymentModes.merchantID);
      cardObject.append("name_on_card", cardDetails.cardName);
      cardObject.append("order_id", juspayOrderId);
      cardObject.append("save_to_locker", "1");
      if (localStorage.getItem(NO_COST_EMI_COUPON)) {
        cardObject.append("emi_bank", cardDetails.emi_bank);
        cardObject.append("emi_tenure", cardDetails.emi_tenure);
        cardObject.append("is_emi", cardDetails.is_emi);
      }
      const result = await api.postJusPay(`txns?`, cardObject);
      const resultJson = await result.json();

      if (
        resultJson.status === JUS_PAY_PENDING ||
        resultJson.status === SUCCESS ||
        resultJson.status === SUCCESS_CAMEL_CASE ||
        resultJson.status === SUCCESS_UPPERCASE ||
        resultJson.status === JUS_PAY_CHARGED
      ) {
        dispatch(jusPayPaymentMethodTypeSuccess(resultJson));
        dispatch(setBagCount(0));
        localStorage.setItem(CART_BAG_DETAILS, []);
        dispatch(generateCartIdForLoggedInUser());
      } else {
        throw new Error(resultJson.error_message);
      }
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
    let cardObject = new FormData();
    cardObject.append("payment_method_type", "CARD");
    cardObject.append("redirect_after_payment", "true");
    cardObject.append("format", "json");
    cardObject.append("card_security_code", cardDetails.cvvNumber);
    cardObject.append("merchant_id", getState().cart.paymentModes.merchantID);
    cardObject.append("card_token", cardDetails.cardToken);
    cardObject.append("order_id", juspayOrderId);

    try {
      const result = await api.postJusPay(`txns?`, cardObject);
      const resultJson = await result.json();

      if (
        resultJson.status === JUS_PAY_PENDING ||
        resultJson.status === SUCCESS ||
        resultJson.status === SUCCESS_CAMEL_CASE ||
        resultJson.status === SUCCESS_UPPERCASE ||
        resultJson.status === JUS_PAY_CHARGED
      ) {
        dispatch(jusPayPaymentMethodTypeSuccess(resultJson));
        dispatch(setBagCount(0));
        localStorage.setItem(CART_BAG_DETAILS, []);
        dispatch(generateCartIdForLoggedInUser());
      } else {
        throw new Error(resultJson.error_message);
      }
    } catch (e) {
      dispatch(jusPayPaymentMethodTypeFailure(e.message));
    }
  };
}

export function jusPayPaymentMethodTypeForGiftCardFromSavedCards(
  juspayOrderId,
  cardDetails,
  guId
) {
  return async (dispatch, getState, { api }) => {
    let cardObject = new FormData();
    cardObject.append("payment_method_type", "CARD");
    cardObject.append("redirect_after_payment", "true");
    cardObject.append("format", "json");
    cardObject.append("card_security_code", cardDetails.cvvNumber);
    cardObject.append("merchant_id", getState().cart.paymentModes.merchantID);
    cardObject.append("card_token", cardDetails.cardToken);
    cardObject.append("order_id", juspayOrderId);
    dispatch(jusPayPaymentMethodTypeRequest());

    try {
      const result = await api.postJusPay(`txns?`, cardObject);
      const resultJson = await result.json();

      if (
        resultJson.status === JUS_PAY_PENDING ||
        resultJson.status === SUCCESS ||
        resultJson.status === SUCCESS_CAMEL_CASE ||
        resultJson.status === SUCCESS_UPPERCASE ||
        resultJson.status === JUS_PAY_CHARGED
      ) {
        dispatch(jusPayPaymentMethodTypeForGiftCardSuccess(resultJson, guId));
      } else {
        throw new Error(resultJson.error_message);
      }
    } catch (e) {
      dispatch(jusPayPaymentMethodTypeFailure(e.message));
    }
  };
}

export function jusPayPaymentMethodTypeForNetBanking(
  paymentMethodType,
  juspayOrderId,
  bankName
) {
  return async (dispatch, getState, { api }) => {
    let cardObject = new FormData();
    cardObject.append("payment_method_type", paymentMethodType);
    cardObject.append("redirect_after_payment", "true");
    cardObject.append("format", "json");
    cardObject.append("merchant_id", getState().cart.paymentModes.merchantID);
    cardObject.append("order_id", juspayOrderId);
    cardObject.append("payment_method", bankName);

    dispatch(jusPayPaymentMethodTypeRequest());
    dispatch(jusPayPaymentMethodTypeRequest());
    try {
      const result = await api.postJusPay(`txns?`, cardObject);
      const resultJson = await result.json();

      if (
        resultJson.status === JUS_PAY_PENDING ||
        resultJson.status === SUCCESS ||
        resultJson.status === SUCCESS_CAMEL_CASE ||
        resultJson.status === SUCCESS_UPPERCASE ||
        resultJson.status === JUS_PAY_CHARGED
      ) {
        dispatch(jusPayPaymentMethodTypeSuccess(resultJson));
        dispatch(setBagCount(0));
        localStorage.setItem(CART_BAG_DETAILS, []);
        dispatch(generateCartIdForLoggedInUser());
      } else {
        throw new Error(resultJson.error_message);
      }
    } catch (e) {
      dispatch(jusPayPaymentMethodTypeFailure(e.message));
    }
  };
}

export function jusPayPaymentMethodTypeForGiftCardNetBanking(
  juspayOrderId,
  bankName,
  guId
) {
  return async (dispatch, getState, { api }) => {
    let cardObject = new FormData();
    cardObject.append("payment_method_type", "NB");
    cardObject.append("redirect_after_payment", "true");
    cardObject.append("format", "json");
    cardObject.append("merchant_id", getState().cart.paymentModes.merchantID);
    cardObject.append("order_id", juspayOrderId);
    cardObject.append("payment_method", bankName);
    dispatch(jusPayPaymentMethodTypeRequest());
    try {
      const result = await api.postJusPay(`txns?`, cardObject);
      const resultJson = await result.json();

      if (
        resultJson.status === JUS_PAY_PENDING ||
        resultJson.status === SUCCESS ||
        resultJson.status === SUCCESS_CAMEL_CASE ||
        resultJson.status === SUCCESS_UPPERCASE ||
        resultJson.status === JUS_PAY_CHARGED
      ) {
        dispatch(jusPayPaymentMethodTypeForGiftCardSuccess(resultJson, guId));
      } else {
        throw new Error(resultJson.error_message);
      }
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
    let paymentObject = new FormData();
    paymentObject.append("paymentMode", paymentMode);
    paymentObject.append("juspayOrderID", juspayOrderID);
    paymentObject.append("cartGuid", cartId);

    dispatch(updateTransactionDetailsRequest());
    try {
      const result = await api.postFormData(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/updateTransactionDetailsforCard?access_token=${
          JSON.parse(customerCookie).access_token
        }&platformNumber=2&isPwa=true`,
        paymentObject
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        setDataLayerForOrderConfirmationDirectCalls(
          ADOBE_DIRECT_CALLS_FOR_ORDER_CONFIRMATION_FAILURE,
          resultJsonStatus.message
        );
        throw new Error(resultJsonStatus.message);
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
        }&platformNumber=2&isPwa=true`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      setDataLayer(
        ADOBE_ORDER_CONFIRMATION,
        resultJson,
        getState().icid.value,
        getState().icid.icidType
      );

      setDataLayerForOrderConfirmationDirectCalls(
        ADOBE_DIRECT_CALLS_FOR_ORDER_CONFIRMATION_SUCCESS,
        resultJson.orderRefNo
      );

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

export function clearCaptureOrderExperience() {
  return {
    type: CLEAR_ORDER_EXPERIENCE_CAPTURE
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      return dispatch(captureOrderExperienceSuccess(resultJson));
    } catch (e) {
      return dispatch(captureOrderExperienceFailure(e.message));
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
export function getCODEligibility(isPaymentFailed) {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartId;
  if (isPaymentFailed) {
    let url = queryString.parse(window.location.search);
    cartId = url && url.value;
  } else {
    const cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    cartId = JSON.parse(cartDetails).guid;
  }
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
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
    let paymentTypeObject = new FormData();
    paymentTypeObject.append("cartGuid", cartId);

    dispatch(binValidationForCODRequest());
    try {
      const result = await api.postFormData(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/binValidation?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2&paymentMode=${paymentMode}`,
        paymentTypeObject
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      localStorage.setItem(SELECTED_BANK_NAME, "");
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
  const parsedQueryString = queryString.parse(window.location.search);
  let cartId;
  if (parsedQueryString.value) {
    cartId = parsedQueryString.value;
  } else {
    cartId = JSON.parse(cartDetails).guid;
  }

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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        if (
          resultJson.errorCode === ERROR_CODE_FOR_BANK_OFFER_INVALID_1 ||
          resultJson.errorCode === ERROR_CODE_FOR_BANK_OFFER_INVALID_2
        ) {
          dispatch(createJusPayOrderFailure(INVALID_COUPON_ERROR_MESSAGE));
          return dispatch(
            showModal(INVALID_BANK_COUPON_POPUP, {
              result: resultJson
            })
          );
        } else {
          throw new Error(resultJsonStatus.message);
        }
      }

      const oldUrl = window.location.href;
      if (oldUrl.includes(JUS_PAY_AUTHENTICATION_FAILED)) {
        let newUrl = oldUrl.replace(
          JUS_PAY_AUTHENTICATION_FAILED,
          JUS_PAY_CHARGED
        );
        window.location.href = newUrl;
      }
      dispatch(setBagCount(0));
      localStorage.setItem(CART_BAG_DETAILS, []);
      dispatch(orderConfirmation(resultJson.orderId));
      dispatch(updateTransactionDetailsForCODSuccess(resultJson));
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
      if (product.isGiveAway === NO) {
        let productDetails = {};
        productDetails.ussId = product.USSID;
        productDetails.quantity = product.qtySelectedByUser;
        productDetails.deliveryMode =
          product.pinCodeResponse.validDeliveryModes[0].type;
        productDetails.serviceableSlaves =
          product.pinCodeResponse.validDeliveryModes[0].serviceableSlaves;
        productDetails.fulfillmentType = product.fullfillmentType;
        item.push(productDetails);
        productItems.item = item;
      }
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      setDataLayerForCheckoutDirectCalls(ADOBE_FINAL_PAYMENT_MODES);
      dispatch(updateTransactionDetailsForCOD(CASH_ON_DELIVERY, ""));
      dispatch(softReservationForCODPaymentSuccess(resultJson));
    } catch (e) {
      dispatch(softReservationForCODPaymentFailure(e.message));
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      dispatch(
        getCartDetails(
          JSON.parse(userDetails).userName,
          JSON.parse(customerCookie).access_token,
          cartId,
          pinCode
        )
      ).then(cartDetails => {
        if (cartDetails.status === SUCCESS) {
          setDataLayerForCartDirectCalls(
            ADOBE_REMOVE_ITEM,
            cartDetails.cartDetails
          );
          dispatch(removeItemFromCartLoggedInSuccess());
        }
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(
        getCartDetails(
          ANONYMOUS_USER,
          JSON.parse(globalCookie).access_token,
          JSON.parse(cartDetailsAnonymous).guid,
          pinCode
        )
      ).then(cartDetails => {
        if (cartDetails.status === SUCCESS) {
          setDataLayerForCartDirectCalls(
            ADOBE_REMOVE_ITEM,
            cartDetails.cartDetails
          );
          dispatch(removeItemFromCartLoggedOutSuccess());
        }
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(
        getCartDetails(
          JSON.parse(userDetails).userName,
          JSON.parse(customerCookie).access_token,
          cartId,
          pinCode
        )
      ).then(cartDetails => {
        if (cartDetails.status === SUCCESS) {
          setDataLayerForCartDirectCalls(ADOBE_CALLS_FOR_CHANGE_QUANTITY);
          dispatch(updateQuantityInCartLoggedInSuccess(resultJson));
        }
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      dispatch(
        getCartDetails(
          ANONYMOUS_USER,
          JSON.parse(globalCookie).access_token,
          JSON.parse(cartDetailsAnonymous).guid,
          pinCode
        )
      ).then(cartDetails => {
        if (cartDetails.status === SUCCESS) {
          setDataLayerForCartDirectCalls(ADOBE_CALLS_FOR_CHANGE_QUANTITY);
          dispatch(updateQuantityInCartLoggedOutSuccess(resultJson));
        }
      });
    } catch (e) {
      dispatch(updateQuantityInCartLoggedOutFailure(e.message));
    }
  };
}

export function clearCartDetails() {
  return {
    type: CLEAR_CART_DETAILS
  };
}

export function getEligibilityOfNoCostEmiRequest() {
  return {
    type: ELIGIBILITY_OF_NO_COST_EMI_REQUEST,
    status: REQUESTING
  };
}

export function getEligibilityOfNoCostEmiSuccess(emiEligibility) {
  return {
    type: ELIGIBILITY_OF_NO_COST_EMI_SUCCESS,
    status: SUCCESS,
    emiEligibility
  };
}

export function getEligibilityOfNoCostEmiFailure(error) {
  return {
    type: ELIGIBILITY_OF_NO_COST_EMI_FAILURE,
    status: ERROR,
    error
  };
}

export function getEmiEligibility(cartGuId) {
  return async (dispatch, getState, { api }) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    const cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);

    const cartId = JSON.parse(cartDetails).guid;
    dispatch(getEligibilityOfNoCostEmiRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/noCostEmiCheck?platformNumber=2&access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${cartGuId}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(getEligibilityOfNoCostEmiSuccess(resultJson));
    } catch (e) {
      dispatch(getEligibilityOfNoCostEmiFailure(e.message));
    }
  };
}

export function getBankAndTenureDetailsRequest() {
  return {
    type: BANK_AND_TENURE_DETAILS_REQUEST,
    status: REQUESTING
  };
}

export function getBankAndTenureDetailsSuccess(bankAndTenureDetails) {
  return {
    type: BANK_AND_TENURE_DETAILS_SUCCESS,
    status: SUCCESS,
    bankAndTenureDetails
  };
}

export function getBankAndTenureDetailsFailure(error) {
  return {
    type: BANK_AND_TENURE_DETAILS_FAILURE,
    status: ERROR,
    error
  };
}

export function getBankAndTenureDetails() {
  return async (dispatch, getState, { api }) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    const cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    const cartId = JSON.parse(cartDetails).guid;
    dispatch(getBankAndTenureDetailsRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/noCostEmiTenureList?access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${cartId}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(getBankAndTenureDetailsSuccess(resultJson));
    } catch (e) {
      dispatch(getBankAndTenureDetailsFailure(e.message));
    }
  };
}

export function getEmiTermsAndConditionsForBankRequest() {
  return {
    type: EMI_TERMS_AND_CONDITIONS_FOR_BANK_REQUEST,
    status: REQUESTING
  };
}

export function getEmiTermsAndConditionsForBankSuccess(termsAndConditions) {
  return {
    type: EMI_TERMS_AND_CONDITIONS_FOR_BANK_SUCCESS,
    status: SUCCESS,
    termsAndConditions
  };
}

export function getEmiTermsAndConditionsForBankFailure(error) {
  return {
    type: EMI_TERMS_AND_CONDITIONS_FOR_BANK_FAILURE,
    status: ERROR,
    error
  };
}

export function getEmiTermsAndConditionsForBank(code, bankName) {
  return async (dispatch, getState, { api }) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    dispatch(getEmiTermsAndConditionsForBankRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/${code}/noCostEmiTnc?access_token=${
          JSON.parse(customerCookie).access_token
        }`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(getEmiTermsAndConditionsForBankSuccess(resultJson));
      resultJson.bankName = bankName;
      dispatch(showModal(EMI_BANK_TERMS_AND_CONDITIONS, resultJson));
    } catch (e) {
      dispatch(getEmiTermsAndConditionsForBankFailure(e.message));
    }
  };
}

export function applyNoCostEmiRequest() {
  return {
    type: APPLY_NO_COST_EMI_REQUEST,
    status: REQUESTING
  };
}

export function applyNoCostEmiSuccess(noCostEmiResult, couponCode) {
  return {
    type: APPLY_NO_COST_EMI_SUCCESS,
    status: SUCCESS,
    noCostEmiResult,
    couponCode
  };
}

export function applyNoCostEmiFailure(error) {
  return {
    type: APPLY_NO_COST_EMI_FAILURE,
    status: ERROR,
    error
  };
}

export function applyNoCostEmi(couponCode, cartGuId, cartId) {
  return async (dispatch, getState, { api }) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

    dispatch(applyNoCostEmiRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/${cartId}/applyNoCostEMI?couponCode=${couponCode}&access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${cartGuId}&isPwa=true`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      return dispatch(applyNoCostEmiSuccess(resultJson, couponCode));
    } catch (e) {
      return dispatch(applyNoCostEmiFailure(e.message));
    }
  };
}

export function removeNoCostEmiRequest() {
  return {
    type: REMOVE_NO_COST_EMI_REQUEST,
    status: REQUESTING
  };
}

export function removeNoCostEmiSuccess(noCostEmiResult, couponCode) {
  return {
    type: REMOVE_NO_COST_EMI_SUCCESS,
    status: SUCCESS,
    noCostEmiResult,
    couponCode
  };
}

export function removeNoCostEmiFailure(error) {
  return {
    type: REMOVE_NO_COST_EMI_FAILURE,
    status: ERROR,
    error
  };
}

export function removeNoCostEmi(couponCode, cartGuId, cartId) {
  return async (dispatch, getState, { api }) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (!cartGuId) {
      const cartDetails = JSON.parse(
        Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER)
      );
      if (cartDetails) {
        cartGuId = cartDetails.guid;
      }
    }
    if (!cartId) {
      const cartDetails = JSON.parse(
        Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER)
      );
      if (cartDetails) {
        cartId = cartDetails.code;
      }
    }
    dispatch(removeNoCostEmiRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/carts/${cartId}/releaseNoCostEMI?couponCode=${couponCode}&access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${cartGuId}&isPwa=true`
      );
      const resultJson = await result.json();

      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      return dispatch(removeNoCostEmiSuccess(resultJson, couponCode));
    } catch (e) {
      return dispatch(removeNoCostEmiFailure(e.message));
    }
  };
}

export function getItemBreakUpDetailsRequest() {
  return {
    type: EMI_ITEM_BREAK_UP_DETAILS_REQUEST,
    status: REQUESTING
  };
}

export function getItemBreakUpDetailsSuccess(noCostEmiResult) {
  return {
    type: EMI_ITEM_BREAK_UP_DETAILS_SUCCESS,
    status: SUCCESS,
    noCostEmiResult
  };
}

export function getItemBreakUpDetailsFailure(error) {
  return {
    type: EMI_ITEM_BREAK_UP_DETAILS_FAILURE,
    status: ERROR,
    error
  };
}

export function getItemBreakUpDetails(
  couponCode,
  cartGuId,
  noCostEmiText,
  noCostEmiProductCount
) {
  return async (dispatch, getState, { api }) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (!cartGuId) {
      const cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
      cartGuId = JSON.parse(cartDetails).guid;
    }

    dispatch(getItemBreakUpDetailsRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/noCostEmiItemBreakUp?couponCode=${couponCode}&access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${cartGuId}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      let noCostEmiResult = Object.assign({}, resultJson, {
        noCostEmiText: noCostEmiText,
        noCostEmiProductCount: noCostEmiProductCount
      });
      dispatch(getItemBreakUpDetailsSuccess(resultJson));
      dispatch(showModal(EMI_ITEM_LEVEL_BREAKAGE, noCostEmiResult));
    } catch (e) {
      dispatch(getItemBreakUpDetailsFailure(e.message));
    }
  };
}

export function getPaymentFailureOrderDetailsRequest() {
  return {
    type: PAYMENT_FAILURE_ORDER_DETAILS_REQUEST,
    status: REQUESTING
  };
}

export function getPaymentFailureOrderDetailsSuccess(
  paymentFailureOrderDetails
) {
  return {
    type: PAYMENT_FAILURE_ORDER_DETAILS_SUCCESS,
    status: SUCCESS,
    paymentFailureOrderDetails
  };
}

export function getPaymentFailureOrderDetailsFailure(error) {
  return {
    type: PAYMENT_FAILURE_ORDER_DETAILS_FAILURE,
    status: ERROR,
    error
  };
}

export function getPaymentFailureOrderDetails() {
  return async (dispatch, getState, { api }) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

    let url = queryString.parse(window.location.search);
    const cartGuId = url && url.value;

    dispatch(getPaymentFailureOrderDetailsRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/failedorderdetails?access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=${cartGuId}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(getPaymentFailureOrderDetailsSuccess(resultJson));
    } catch (e) {
      dispatch(getPaymentFailureOrderDetailsFailure(e.message));
    }
  };
}

export function resetIsSoftReservationFailed() {
  return {
    type: RESET_IS_SOFT_RESERVATION_FAILED
  };
}
