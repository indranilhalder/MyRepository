import {
  SUCCESS,
  SUCCESS_UPPERCASE,
  SUCCESS_CAMEL_CASE,
  REQUESTING,
  ERROR,
  FAILURE,
  HOME_FEED_FOLLOW_AND_UN_FOLLOW,
  PDP_FOLLOW_AND_UN_FOLLOW,
  MY_ACCOUNT_FOLLOW_AND_UN_FOLLOW,
  STORE_NOT_AVAILABLE_TEXT
} from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
import findIndex from "lodash.findindex";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  FAILURE_UPPERCASE,
  CART_DETAILS_FOR_ANONYMOUS,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  DEFAULT_PIN_CODE_LOCAL_STORAGE,
  GLOBAL_ACCESS_TOKEN,
  PLAT_FORM_NUMBER,
  SUCCESS_MESSAGE_IN_CANCELING_ORDER
} from "../../lib/constants";
import {
  showModal,
  GENERATE_OTP_FOR_CLIQ_CASH,
  VERIFY_OTP_FOR_CLIQ_CASH,
  GENERATE_OTP_FOR_EGV,
  hideModal,
  VERIFY_OTP,
  GIFT_CARD_MODAL,
  UPDATE_REFUND_DETAILS_POPUP
} from "../../general/modal.actions.js";
import format from "date-fns/format";
import { getPaymentModes } from "../../cart/actions/cart.actions.js";
import {
  getMcvId,
  setDataLayerForMyAccountDirectCalls,
  ADOBE_MY_ACCOUNT_ORDER_RETURN,
  setDataLayer,
  ADOBE_MY_ACCOUNT_SAVED_LIST,
  ADOBE_MY_ACCOUNT_BRANDS,
  ADOBE_MY_ACCOUNT_ORDER_HISTORY,
  ADOBE_MY_ACCOUNT_GIFT_CARD,
  ADOBE_MY_ACCOUNT_CLIQ_CASH,
  AODBE_MY_ACCOUNT_SETTINGS,
  ADOBE_MY_ACCOUNT_ORDER_DETAILS,
  setDataLayerForFollowAndUnFollowBrand,
  ADOBE_ON_FOLLOW_AND_UN_FOLLOW_BRANDS,
  ADOBE_MY_ACCOUNT_CANCEL_ORDER_SUCCESS
} from "../../lib/adobeUtils";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";
import * as ErrorHandling from "../../general/ErrorHandling.js";
import { displayToast } from "../../general/toast.actions";
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

export const RETURN_PRODUCT_DETAILS_REQUEST = "RETURN_PRODUCT_DETAILS_REQUEST";
export const RETURN_PRODUCT_DETAILS_SUCCESS = "RETURN_PRODUCT_DETAILS_SUCCESS";
export const RETURN_PRODUCT_DETAILS_FAILURE = "RETURN_PRODUCT_DETAILS_FAILURE";

export const RETURN_INITIAL_REQUEST = "RETURN_INITIAL_REQUEST";
export const RETURN_INITIAL_SUCCESS = "RETURN_INITIAL_SUCCESS";
export const RETURN_INITIAL_FAILURE = "RETURN_INITIAL_FAILURE";

export const GET_RETURN_REQUEST = "RETURN_REQEUEST";
export const GET_RETURN_REQUEST_SUCCESS = "GET_RETURN_REQUEST_SUCCESS";
export const GET_RETURN_REQUEST_FAILURE = "GET_RETURN_REQUEST_FAILURE";

export const FETCH_ORDER_DETAILS_REQUEST = "FETCH_ORDER_DETAILS_REQUEST";
export const FETCH_ORDER_DETAILS_SUCCESS = "FETCH_ORDER_DETAILS_SUCCESS";
export const FETCH_ORDER_DETAILS_FAILURE = "FETCH_ORDER_DETAILS_FAILURE";

export const GET_USER_COUPON_REQUEST = "GET_USER_COUPON_REQUEST";
export const GET_USER_COUPON_SUCCESS = "GET_USER_COUPON_SUCCESS";
export const GET_USER_COUPON_FAILURE = "GET_USER_COUPON_FAILURE";

export const GET_USER_ALERTS_REQUEST = "GET_USER_ALERTS_REQUEST";
export const GET_USER_ALERTS_SUCCESS = "GET_USER_ALERTS_SUCCESS";
export const GET_USER_ALERTS_FAILURE = "GET_USER_ALERTS_FAILURE";

export const GET_PIN_CODE_REQUEST = "GET_PIN_CODE_REQUEST";
export const GET_PIN_CODE_SUCCESS = "GET_PIN_CODE_SUCCESS";
export const GET_PIN_CODE_FAILURE = "GET_PIN_CODE_FAILURE";

export const SEND_INVOICE_REQUEST = "SEND_INVOICE_REQUEST";
export const SEND_INVOICE_SUCCESS = "SEND_INVOICE_SUCCESS";
export const SEND_INVOICE_FAILURE = "SEND_INVOICE_FAILURE";

export const REMOVE_ADDRESS_REQUEST = "REMOVE_ADDRESS_REQUEST";
export const REMOVE_ADDRESS_SUCCESS = "REMOVE_ADDRESS_SUCCESS";
export const REMOVE_ADDRESS_FAILURE = "REMOVE_ADDRESS_FAILURE";

export const EDIT_ADDRESS_REQUEST = "EDIT_ADDRESS_REQUEST";
export const EDIT_ADDRESS_SUCCESS = "EDIT_ADDRESS_SUCCESS";
export const EDIT_ADDRESS_FAILURE = "EDIT_ADDRESS_FAILURE";
export const GET_WISHLIST_REQUEST = "GET_WISHLIST_REQUEST";
export const GET_WISHLIST_SUCCESS = "GET_WISHLIST_SUCCESS";
export const GET_WISHLIST_FAILURE = "GET_WISHLIST_FAILURE";

export const GET_FOLLOWED_BRANDS_REQUEST = "GET_FOLLOWED_BRANDS_REQUEST";
export const GET_FOLLOWED_BRANDS_SUCCESS = "GET_FOLLOWED_BRANDS_SUCCESS";
export const GET_FOLLOWED_BRANDS_FAILURE = "GET_FOLLOWED_BRANDS_FAILURE";

export const QUICK_DROP_STORE_REQUEST = "QUICK_DROP_STORE_REQUEST";
export const QUICK_DROP_STORE_SUCCESS = "QUICK_DROP_STORE_SUCCESS";
export const QUICK_DROP_STORE_FAILURE = "QUICK_DROP_STORE_FAILURE";
export const NEW_RETURN_INITIATE_REQUEST = "NEW_RETURN_INITIATE_REQUEST";
export const NEW_RETURN_INITIATE_SUCCESS = "NEW_RETURN_INITIATE_SUCCESS";
export const NEW_RETURN_INITIATE_FAILURE = "NEW_RETURN_INITIATE_FAILURE";

export const RETURN_PIN_CODE_REQUEST = "RETURN_PIN_CODE_REQUEST";
export const RETURN_PIN_CODE_SUCCESS = "RETURN_PIN_CODE_SUCCESS";
export const RETURN_PIN_CODE_FAILURE = "RETURN_PIN_CODE_FAILURE";

export const GET_GIFTCARD_REQUEST = "GET_GIFTCARD_REQUEST";
export const GET_GIFTCARD_SUCCESS = "GET_GIFTCARD_SUCCESS";
export const GET_GIFTCARD_FAILURE = "GET_GIFTCARD_FAILURE";

export const FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_REQUEST =
  "FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_REQUEST";
export const FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_SUCCESS =
  "FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_SUCCESS";
export const FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_FAILURE =
  "FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_FAILURE";

export const FOLLOW_AND_UN_FOLLOW_BRANDS_IN_HOME_FEED_SUCCESS =
  "FOLLOW_AND_UN_FOLLOW_BRANDS_IN_HOME_FEED_SUCCESS";
export const FOLLOW_AND_UN_FOLLOW_BRANDS_IN_PDP_SUCCESS =
  "FOLLOW_AND_UN_FOLLOW_BRANDS_IN_PDP_SUCCESS";
export const FOLLOW_AND_UN_FOLLOW_BRANDS_IN_MY_ACCOUNT_SUCCESS =
  "FOLLOW_AND_UN_FOLLOW_BRANDS_IN_MY_ACCOUNT_SUCCESS";

export const GET_USER_CLIQ_CASH_DETAILS_REQUEST =
  "GET_USER_CLIQ_CASH_DETAILS_REQUEST";
export const GET_USER_CLIQ_CASH_DETAILS_SUCCESS =
  "GET_USER_CLIQ_CASH_DETAILS_SUCCESS";
export const GET_USER_CLIQ_CASH_DETAILS_FAILURE =
  "GET_USER_CLIQ_CASH_DETAILS_FAILURE";

export const REDEEM_CLIQ_VOUCHER_REQUEST = "REDEEM_CLIQ_VOUCHER_REQUEST";
export const REDEEM_CLIQ_VOUCHER_SUCCESS = "REDEEM_CLIQ_VOUCHER_SUCCESS";
export const REDEEM_CLIQ_VOUCHER_FAILURE = "REDEEM_CLIQ_VOUCHER_FAILURE";

export const CREATE_GIFT_CARD_REQUEST = "CREATE_GIFT_CARD_REQUEST";
export const CREATE_GIFT_CARD_SUCCESS = "CREATE_GIFT_CARD_SUCCESS";
export const CREATE_GIFT_CARD_FAILURE = "CREATE_GIFT_CARD_FAILURE";

export const GET_OTP_TO_ACTIVATE_WALLET_REQUEST =
  "GET_OTP_TO_ACTIVATE_WALLET_REQUEST";
export const GET_OTP_TO_ACTIVATE_WALLET_SUCCESS =
  "GET_OTP_TO_ACTIVATE_WALLET_SUCCESS";
export const GET_OTP_TO_ACTIVATE_WALLET_FAILURE =
  "GET_OTP_TO_ACTIVATE_WALLET_FAILURE";

export const VERIFY_WALLET_REQUEST = "VERIFY_WALLET_REQUEST";
export const VERIFY_WALLET_SUCCESS = "VERIFY_WALLET_SUCCESS";
export const VERIFY_WALLET_FAILURE = "VERIFY_WALLET_FAILURE";

export const SUBMIT_SELF_COURIER_INFO_REQUEST =
  "SUBMIT_SELF_COURIER_INFO_REQUEST";
export const SUBMIT_SELF_COURIER_INFO_SUCCESS =
  "SUBMIT_SELF_COURIER_INFO_SUCCESS";
export const SUBMIT_SELF_COURIER_INFO_FAILURE =
  "SUBMIT_SELF_COURIER_INFO_FAILURE";

export const CANCEL_PRODUCT_REQUEST = "CANCEL_PRODUCT_REQUEST";
export const CANCEL_PRODUCT_SUCCESS = "CANCEL_PRODUCT_SUCCESS";
export const CANCEL_PRODUCT_FAILURE = "CANCEL_PRODUCT_FAILURE";

export const GET_CANCEL_PRODUCT_DETAILS_REQUEST =
  "GET_CANCEL_PRODUCT_DETAILS_REQUEST";
export const GET_CANCEL_PRODUCT_DETAILS_SUCCESS =
  "GET_CANCEL_PRODUCT_DETAILS_SUCCESS";
export const GET_CANCEL_PRODUCT_DETAILS_FAILURE =
  "GET_CANCEL_PRODUCT_DETAILS_FAILURE";

export const UPDATE_PROFILE_REQUEST = "UPDATE_PROFILE_REQUEST";
export const UPDATE_PROFILE_SUCCESS = "UPDATE_PROFILE_SUCCESS";
export const UPDATE_PROFILE_FAILURE = "UPDATE_PROFILE_FAILURE";
export const LOG_OUT_ACCOUNT_USING_MOBILE_NUMBER =
  "LOG_OUT_ACCOUNT_USING_MOBILE_NUMBER";
export const UPDATE_PROFILE_OTP_VERIFICATION = "UpdateProfileOtpVerification";
export const CHANGE_PASSWORD_REQUEST = "CHANGE_PASSWORD_REQUEST";
export const CHANGE_PASSWORD_SUCCESS = "CHANGE_PASSWORD_SUCCESS";
export const CHANGE_PASSWORD_FAILURE = "CHANGE_PASSWORD_FAILURE";
export const Clear_ORDER_DATA = "Clear_ORDER_DATA";
export const RE_SET_ADD_ADDRESS_DETAILS = "RE_SET_ADD_ADDRESS_DETAILS";
export const CLEAR_CHANGE_PASSWORD_DETAILS = "CLEAR_CHANGE_PASSWORD_DETAILS";
export const CLEAR_PIN_CODE_STATUS = "CLEAR_PIN_CODE_STATUS";
export const CURRENT_PAGE = 0;
export const PAGE_SIZE = 10;
export const PLATFORM_NUMBER = 2;
export const USER_PATH = "v2/mpl/users";
export const PRODUCT_PATH = "v2/mpl/products";

export const PIN_PATH = "v2/mpl";
export const PATH = "v2/mpl";

export const MSD_ROOT_PATH = "https://ap-southeast-1-api.madstreetden.com";
export const LOGOUT = "LOGOUT";
export const CLEAR_GIFT_CARD_STATUS = "CLEAR_GIFT_CARD_STATUS";
export const CLEAR_ACCOUNT_UPDATE_TYPE = "CLEAR_ACCOUNT_UPDATE_TYPE";
const API_KEY_FOR_MSD = "8783ef14595919d35b91cbc65b51b5b1da72a5c3";
const NUMBER_OF_RESULTS_FOR_BRANDS = [25];
const WIDGETS_LIST_FOR_BRANDS = [112];
const CARD_TYPE = "BOTH";
const FOLLOW = "follow";
const UNFOLLOW = "unfollow";
const DATE_FORMAT_TO_UPDATE_PROFILE = "DD/MM/YYYY";
const MOBILE_PATTERN = /^[7,8,9]{1}[0-9]{9}$/;
const CART_GU_ID = "cartGuid";
// cencel product

export function getDetailsOfCancelledProductRequest() {
  return {
    type: GET_CANCEL_PRODUCT_DETAILS_REQUEST,
    status: REQUESTING
  };
}
export function getDetailsOfCancelledProductSuccess(
  getDetailsOfCancelledProduct
) {
  return {
    type: GET_CANCEL_PRODUCT_DETAILS_SUCCESS,
    status: SUCCESS,
    getDetailsOfCancelledProduct
  };
}
export function getDetailsOfCancelledProductFailure(error) {
  return {
    type: GET_CANCEL_PRODUCT_DETAILS_FAILURE,
    status: ERROR,
    error
  };
}
export function getDetailsOfCancelledProduct(cancelProductDetails) {
  let cancelProductObject = new FormData();
  cancelProductObject.append(
    "transactionId",
    cancelProductDetails.transactionId
  );
  cancelProductObject.append("orderCode", cancelProductDetails.orderCode);
  cancelProductObject.append("USSID", cancelProductDetails.USSID);
  cancelProductObject.append(
    "returnCancelFlag",
    cancelProductDetails.returnCancelFlag
  );
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  return async (dispatch, getState, { api }) => {
    dispatch(getDetailsOfCancelledProductRequest());
    try {
      const result = await api.postFormData(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/returnProductDetails?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2`,
        cancelProductObject
      );
      const resultJson = await result.json();

      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      return dispatch(getDetailsOfCancelledProductSuccess(resultJson));
    } catch (e) {
      dispatch(getDetailsOfCancelledProductFailure(e.message));
    }
  };
}

//cancel final order

export function cancelProductRequest() {
  return {
    type: CANCEL_PRODUCT_REQUEST,
    status: REQUESTING
  };
}
export function cancelProductSuccess(cancelProduct) {
  return {
    type: CANCEL_PRODUCT_SUCCESS,
    status: SUCCESS,
    cancelProduct
  };
}
export function cancelProductFailure(error) {
  return {
    type: CANCEL_PRODUCT_FAILURE,
    status: ERROR,
    error
  };
}
export function cancelProduct(cancelProductDetails) {
  let cancelProductObject = new FormData();
  cancelProductObject.append(
    "transactionId",
    cancelProductDetails.transactionId
  );
  cancelProductObject.append("orderCode", cancelProductDetails.orderCode);
  cancelProductObject.append("ussid", cancelProductDetails.USSID);
  cancelProductObject.append(
    "ticketTypeCode",
    cancelProductDetails.ticketTypeCode
  );
  cancelProductObject.append("reasonCode", cancelProductDetails.reasonCode);
  cancelProductObject.append("refundType", cancelProductDetails.refundType);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  return async (dispatch, getState, { api }) => {
    dispatch(cancelProductRequest());
    try {
      const result = await api.postFormData(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/initiateRefund?access_token=${
          JSON.parse(customerCookie).access_token
        }&login=${JSON.parse(userDetails).userName}&isPwa=true`,
        cancelProductObject
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      dispatch(displayToast(SUCCESS_MESSAGE_IN_CANCELING_ORDER));
      setDataLayerForMyAccountDirectCalls(
        ADOBE_MY_ACCOUNT_CANCEL_ORDER_SUCCESS,
        cancelProductDetails
      );
      return dispatch(cancelProductSuccess(resultJson));
    } catch (e) {
      return dispatch(cancelProductFailure(e.message));
    }
  };
}

export function returnProductDetailsRequest() {
  return {
    type: RETURN_PRODUCT_DETAILS_REQUEST,
    status: REQUESTING
  };
}

export function returnProductDetailsSuccess(returnProductDetails) {
  return {
    type: RETURN_PRODUCT_DETAILS_SUCCESS,
    status: SUCCESS,
    returnProductDetails
  };
}

export function returnProductDetailsFailure(error) {
  return {
    type: RETURN_PRODUCT_DETAILS_FAILURE,
    error,
    status: FAILURE
  };
}

export function returnProductDetails(productDetails) {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    let returnProductFormData = new FormData();
    returnProductFormData.append("transactionId", productDetails.transactionId);
    returnProductFormData.append(
      "returnCancelFlag",
      productDetails.returnCancelFlag
    );
    returnProductFormData.append("orderCode", productDetails.orderCode);

    dispatch(returnProductDetailsRequest());
    try {
      const result = await api.postFormData(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/newReturnProductDetails?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true`,
        returnProductFormData
      );

      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(returnProductDetailsSuccess(resultJson));
    } catch (e) {
      dispatch(returnProductDetailsFailure(e.message));
    }
  };
}

// This is a crappy name, but the api is called getReturnRequest and that conflicts with our pattern
// Let's keep the name, because it fits our convention and deal with the awkwardness.
export function getReturnRequestRequest() {
  return {
    type: GET_RETURN_REQUEST,
    status: REQUESTING
  };
}

export function getReturnRequestSuccess(returnRequest) {
  return {
    type: GET_RETURN_REQUEST_SUCCESS,
    returnRequest,
    status: SUCCESS
  };
}

export function getReturnRequestFailure(error) {
  return {
    type: GET_RETURN_REQUEST_FAILURE,
    error,
    status: FAILURE
  };
}

export function getReturnRequest(orderCode, transactionId) {
  return async (dispatch, getState, { api }) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    dispatch(getReturnRequestRequest());

    try {
      const result = await api.get(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/returnRequest?access_token=${
          JSON.parse(customerCookie).access_token
        }&channel=mobile&loginId=${
          JSON.parse(userDetails).userName
        }&orderCode=${orderCode}&transactionId=${transactionId}`
      );

      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(getReturnRequestSuccess(resultJson));
    } catch (e) {
      dispatch(getReturnRequestFailure(e.message));
    }
  };
}

export function newReturnInitiateRequest() {
  return {
    type: NEW_RETURN_INITIATE_REQUEST,
    status: REQUESTING
  };
}

export function newReturnInitiateSuccess(returnDetails) {
  return {
    type: NEW_RETURN_INITIATE_SUCCESS,
    returnDetails,
    status: SUCCESS
  };
}

export function newReturnInitiateFailure(error) {
  return {
    type: NEW_RETURN_INITIATE_FAILURE,
    error,
    status: FAILURE
  };
}

export function newReturnInitial(returnDetails, product = null) {
  return async (dispatch, getState, { api }) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    dispatch(newReturnInitiateRequest());

    try {
      const result = await api.post(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/newReturnInitiate?access_token=${
          JSON.parse(customerCookie).access_token
        }&channel=mobile`,
        returnDetails
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      if (product) {
        setDataLayerForMyAccountDirectCalls(
          ADOBE_MY_ACCOUNT_ORDER_RETURN,
          product,
          returnDetails
        );
      }
      return dispatch(newReturnInitiateSuccess(resultJson));
    } catch (e) {
      return dispatch(newReturnInitiateFailure(e.message));
    }
  };
}

export function returnPInCodeRequest() {
  return {
    type: RETURN_PIN_CODE_REQUEST,
    status: REQUESTING
  };
}

export function returnPInCodeSuccess(pinCodeDetails) {
  return {
    type: RETURN_PIN_CODE_SUCCESS,
    pinCodeDetails,
    status: SUCCESS
  };
}

export function returnPinCodeFailure(error, pinCodeDetails) {
  return {
    type: RETURN_PIN_CODE_FAILURE,
    error,
    status: FAILURE,
    pinCodeDetails
  };
}

export function returnPinCode(productDetails) {
  return async (dispatch, getState, { api }) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    dispatch(returnPInCodeRequest());
    let resultJson;
    try {
      const result = await api.post(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/returnPincode?access_token=${
          JSON.parse(customerCookie).access_token
        }&&orderCode=${productDetails.orderCode}&pincode=${
          productDetails.pinCode
        }&transactionId=${productDetails.transactionId}`
      );
      resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        let message = resultJsonStatus.message;
        if (resultJsonStatus.message === FAILURE_UPPERCASE) {
          message =
            "Sorry! pick up is not available for your area. You can still return the item by dropping in store or by self shipping the product";
        }
        throw new Error(message);
      }

      dispatch(returnPInCodeSuccess(resultJson));
    } catch (e) {
      dispatch(returnPinCodeFailure(e.message, resultJson));
    }
  };
}

export function quickDropStoreRequest() {
  return {
    type: QUICK_DROP_STORE_REQUEST,
    status: REQUESTING
  };
}

export function quickDropStoreSuccess(addresses) {
  return {
    type: QUICK_DROP_STORE_SUCCESS,
    addresses,
    status: SUCCESS
  };
}

export function quickDropStoreFailure(error) {
  return {
    type: QUICK_DROP_STORE_FAILURE,
    error,
    status: FAILURE
  };
}

export function quickDropStore(pincode, ussId) {
  return async (dispatch, getState, { api }) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    dispatch(quickDropStoreRequest());

    try {
      const result = await api.get(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/quickDropStores?access_token=${
          JSON.parse(customerCookie).access_token
        }&pincode=${pincode}&&ussid=${ussId}`
      );

      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (
        resultJsonStatus.status ||
        resultJson.status === "Store Not available"
      ) {
        let errorMessage = resultJsonStatus.message;
        if (resultJson.status === "Store Not available") {
          errorMessage = "Store Not available";
        }

        throw new Error(errorMessage);
      }
      return dispatch(quickDropStoreSuccess(resultJson.returnStoreDetailsList));
    } catch (e) {
      return dispatch(quickDropStoreFailure(e.message));
    }
  };
}

//get egv product info
export function giftCardRequest() {
  return {
    type: GET_GIFTCARD_REQUEST,
    status: REQUESTING
  };
}
export function giftCardSuccess(giftCards) {
  return {
    type: GET_GIFTCARD_SUCCESS,
    status: SUCCESS,
    giftCards
  };
}
export function giftCardFailure(error) {
  return {
    type: GET_GIFTCARD_FAILURE,
    status: ERROR,
    error
  };
}
export function getGiftCardDetails() {
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  return async (dispatch, getState, { api }) => {
    dispatch(giftCardRequest());
    try {
      const result = await api.get(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/giftCard/egvProductInfo?access_token=${
          JSON.parse(customerCookie).access_token
        }`
      );
      const resultJson = await result.json();

      if (
        resultJson.status === SUCCESS ||
        resultJson.status === SUCCESS_UPPERCASE ||
        resultJson.status === SUCCESS_CAMEL_CASE
      ) {
        if (!resultJson.isWalletCreated && !resultJson.isWalletOtpVerified) {
          dispatch(showModal(GENERATE_OTP_FOR_EGV));
        }
        setDataLayer(ADOBE_MY_ACCOUNT_GIFT_CARD);
        return dispatch(giftCardSuccess(resultJson));
      } else {
        throw new Error(`${resultJson.errors[0].message}`);
      }
    } catch (e) {
      dispatch(giftCardFailure(e.message));
    }
  };
}

//create gift card

export function createGiftCardRequest() {
  return {
    type: CREATE_GIFT_CARD_REQUEST,
    status: REQUESTING
  };
}
export function createGiftCardSuccess(giftCardDetails) {
  return {
    type: CREATE_GIFT_CARD_SUCCESS,
    status: SUCCESS,
    giftCardDetails
  };
}

export function createGiftCardFailure(error) {
  return {
    type: CREATE_GIFT_CARD_FAILURE,
    status: ERROR,
    error
  };
}

export function createGiftCardDetails(giftCardDetails) {
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  return async (dispatch, getState, { api }) => {
    dispatch(createGiftCardRequest());
    try {
      const result = await api.post(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/createElectronicsGiftCardCartGuid?access_token=${
          JSON.parse(customerCookie).access_token
        }`,
        giftCardDetails
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      return dispatch(createGiftCardSuccess(resultJson));
    } catch (e) {
      dispatch(createGiftCardFailure(e.message));
    }
  };
}
//get otp to activate wallet

export function getOtpToActivateWalletRequest() {
  return {
    type: GET_OTP_TO_ACTIVATE_WALLET_REQUEST,
    status: REQUESTING
  };
}
export function getOtpToActivateWalletSuccess(getOtpToActivateWallet) {
  return {
    type: GET_OTP_TO_ACTIVATE_WALLET_SUCCESS,
    status: SUCCESS,
    getOtpToActivateWallet
  };
}

export function getOtpToActivateWalletFailure(error) {
  return {
    type: GET_OTP_TO_ACTIVATE_WALLET_FAILURE,
    status: ERROR,
    error
  };
}

export function getOtpToActivateWallet(customerDetails, isFromCliqCash) {
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  return async (dispatch, getState, { api }) => {
    dispatch(getOtpToActivateWalletRequest());
    try {
      const result = await api.post(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/checkWalletMobileNumber?access_token=${
          JSON.parse(customerCookie).access_token
        }&isUpdateProfile=false`,
        customerDetails
      );
      const resultJson = await result.json();

      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      if (isFromCliqCash) {
        dispatch(showModal(VERIFY_OTP_FOR_CLIQ_CASH));
      } else {
        dispatch(showModal(VERIFY_OTP));
      }
      return dispatch(getOtpToActivateWalletSuccess(resultJson));
    } catch (e) {
      return dispatch(getOtpToActivateWalletFailure(e.message));
    }
  };
}

//verify wallet

export function verifyWalletRequest() {
  return {
    type: VERIFY_WALLET_REQUEST,
    status: REQUESTING
  };
}
export function verifyWalletSuccess(verifyWallet) {
  return {
    type: VERIFY_WALLET_SUCCESS,
    status: SUCCESS,
    verifyWallet
  };
}

export function verifyWalletFailure(error) {
  return {
    type: VERIFY_WALLET_FAILURE,
    status: ERROR,
    error
  };
}

export function verifyWallet(customerDetailsWithOtp, isFromCliqCash) {
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  return async (dispatch, getState, { api }) => {
    dispatch(verifyWalletRequest());
    try {
      const result = await api.post(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/verifyWalletOtp?access_token=${
          JSON.parse(customerCookie).access_token
        }&otp=${customerDetailsWithOtp.otp}&firstName=${
          customerDetailsWithOtp.firstName
        }&lastName=${customerDetailsWithOtp.lastName}&mobileNumber=${
          customerDetailsWithOtp.mobileNumber
        }`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(hideModal());
      if (isFromCliqCash) {
        dispatch(getCliqCashDetails());
      } else {
        dispatch(getGiftCardDetails());
      }

      return dispatch(verifyWalletSuccess(resultJson));
    } catch (e) {
      return dispatch(verifyWalletFailure(e.message));
    }
  };
}
//  update refund details

export function submitSelfCourierReturnInfoRequest() {
  return {
    type: SUBMIT_SELF_COURIER_INFO_REQUEST,
    status: REQUESTING
  };
}
export function submitSelfCourierReturnInfoSuccess(updateReturnDetails) {
  return {
    type: SUBMIT_SELF_COURIER_INFO_SUCCESS,
    status: SUCCESS,
    updateReturnDetails
  };
}

export function submitSelfCourierReturnInfoFailure(error) {
  return {
    type: SUBMIT_SELF_COURIER_INFO_FAILURE,
    status: ERROR,
    error
  };
}
export function submitSelfCourierReturnInfo(returnDetails) {
  let returnDetailsObject = new FormData(returnDetails.file);
  returnDetailsObject.append("awbNumber", returnDetails.awbNumber);
  returnDetailsObject.append("lpname", returnDetails.lpname);
  returnDetailsObject.append("amount", returnDetails.amount);
  returnDetailsObject.append("orderId", returnDetails.orderId);
  returnDetailsObject.append("transactionId", returnDetails.transactionId);
  returnDetailsObject.append("file", returnDetails.file);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  return async (dispatch, getState, { api }) => {
    dispatch(submitSelfCourierReturnInfoRequest());
    try {
      const result = await api.postFormData(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/submitSelfCourierRetrunInfo?channel=mobile&access_token=${
          JSON.parse(customerCookie).access_token
        }`,
        returnDetailsObject
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      dispatch(hideModal(UPDATE_REFUND_DETAILS_POPUP));
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      dispatch(submitSelfCourierReturnInfoSuccess(resultJson));
    } catch (e) {
      dispatch(submitSelfCourierReturnInfoFailure(e.message));
    }
  };
}

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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      setDataLayer(ADOBE_MY_ACCOUNT_SAVED_LIST);
      dispatch(getSavedCardSuccess(resultJson));
    } catch (e) {
      dispatch(getSavedCardFailure(e.message));
    }
  };
}
export function getPinCodeRequest() {
  return {
    type: GET_PIN_CODE_REQUEST,
    status: REQUESTING
  };
}
export function getPinCodeSuccess(pinCode) {
  return {
    type: GET_PIN_CODE_SUCCESS,
    status: SUCCESS,
    pinCode
  };
}

export function getPinCodeFailure(error) {
  return {
    type: GET_PIN_CODE_FAILURE,
    status: ERROR,
    error
  };
}

export function getPinCode(pinCode) {
  return async (dispatch, getState, { api }) => {
    const globalAccessToken = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    dispatch(getPinCodeRequest());
    try {
      const result = await api.get(
        `${PIN_PATH}/getPincodeData?pincode=${pinCode}&access_token=${
          JSON.parse(globalAccessToken).access_token
        }`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        let errorMessage = resultJsonStatus.message;
        if (errorMessage === FAILURE_UPPERCASE) {
          errorMessage = "Pincode is not serviceable";
        }
        throw new Error(errorMessage);
      }
      dispatch(getPinCodeSuccess(resultJson));
    } catch (e) {
      dispatch(getPinCodeFailure(e.message));
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

export function removeSavedCardDetails(cardToken) {
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  return async (dispatch, getState, { api }) => {
    dispatch(removeSavedCardRequest());
    try {
      const result = await api.post(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/payments/removeSavedCards?access_token=${
          JSON.parse(customerCookie).access_token
        }&cardToken=${cardToken}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(removeSavedCardSuccess(resultJson));
      dispatch(
        getSavedCardDetails(
          JSON.parse(userDetails).userName,
          JSON.parse(customerCookie).access_token
        )
      );
    } catch (e) {
      dispatch(removeSavedCardFailure(e.message));
    }
  };
}
export function getAllOrdersRequest(paginated: false) {
  return {
    type: GET_ALL_ORDERS_REQUEST,
    status: REQUESTING
  };
}
export function getAllOrdersSuccess(orderDetails, isPaginated: false) {
  return {
    type: GET_ALL_ORDERS_SUCCESS,
    status: SUCCESS,
    orderDetails,
    isPaginated
  };
}

export function getAllOrdersFailure(error, isPaginated) {
  return {
    type: GET_ALL_ORDERS_FAILURE,

    status: ERROR,
    error,
    isPaginated
  };
}
export function getAllOrdersDetails(
  suffix: null,
  paginated: false,
  isSetDataLayer: true
) {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(getAllOrdersRequest(paginated));
    dispatch(showSecondaryLoader());
    let currentPage = 0;
    if (getState().profile.orderDetails) {
      currentPage = getState().profile.orderDetails.currentPage + 1;
    }
    try {
      const result = await api.get(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/orderhistorylist?currentPage=${currentPage}&access_token=${
          JSON.parse(customerCookie).access_token
        }&pageSize=${PAGE_SIZE}&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      if (isSetDataLayer) {
        setDataLayer(ADOBE_MY_ACCOUNT_ORDER_HISTORY);
      }
      if (paginated) {
        dispatch(getAllOrdersSuccess(resultJson, paginated));
        dispatch(hideSecondaryLoader());
      } else {
        dispatch(getAllOrdersSuccess(resultJson, paginated));
        dispatch(hideSecondaryLoader());
      }
    } catch (e) {
      dispatch(hideSecondaryLoader());
      dispatch(getAllOrdersFailure(e.message, paginated));
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

export function getUserDetails(isSetDataLayer) {
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      if (isSetDataLayer) {
        setDataLayer(AODBE_MY_ACCOUNT_SETTINGS);
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
    dispatch(showSecondaryLoader());
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(getUserCouponsSuccess(resultJson));
      dispatch(hideSecondaryLoader());
    } catch (e) {
      dispatch(hideSecondaryLoader());

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
    dispatch(showSecondaryLoader());

    try {
      const result = await api.get(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/getOrderTrackingNotifications?access_token=${
          JSON.parse(customerCookie).access_token
        }&emailId=${JSON.parse(userDetails).userName}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(getUserAlertsSuccess(resultJson));
      dispatch(hideSecondaryLoader());
    } catch (e) {
      dispatch(hideSecondaryLoader());
      dispatch(getUserAlertsFailure(e.message));
    }
  };
}

export function removeAddressRequest() {
  return {
    type: REMOVE_ADDRESS_REQUEST,
    status: REQUESTING
  };
}
export function removeAddressSuccess(addressId) {
  return {
    type: REMOVE_ADDRESS_SUCCESS,
    status: SUCCESS,
    addressId
  };
}

export function removeAddressFailure(error) {
  return {
    type: REMOVE_ADDRESS_FAILURE,
    status: ERROR,
    error
  };
}

export function removeAddress(addressId) {
  return async (dispatch, getState, { api }) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let addressObject = new FormData();

    addressObject.append("addressId", addressId);
    addressObject.append("emailId", "");

    dispatch(removeAddressRequest());
    try {
      const result = await api.postFormData(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/removeAddress?isPwa=true&platformNumber=2&access_token=${
          JSON.parse(customerCookie).access_token
        }`,
        addressObject
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(removeAddressSuccess(addressId));
    } catch (e) {
      dispatch(removeAddressFailure(e.message));
    }
  };
}

export function editAddressRequest() {
  return {
    type: EDIT_ADDRESS_REQUEST,
    status: REQUESTING
  };
}
export function editAddressSuccess(addressDetails) {
  return {
    type: EDIT_ADDRESS_SUCCESS,
    status: SUCCESS,
    addressDetails
  };
}

export function editAddressFailure(error) {
  return {
    type: EDIT_ADDRESS_FAILURE,
    status: ERROR,
    error
  };
}

export function fetchOrderDetailsRequest() {
  return {
    type: FETCH_ORDER_DETAILS_REQUEST,
    status: REQUESTING
  };
}
export function fetchOrderDetailsSuccess(fetchOrderDetails) {
  return {
    type: FETCH_ORDER_DETAILS_SUCCESS,
    status: SUCCESS,
    fetchOrderDetails
  };
}

export function fetchOrderDetailsFailure(error) {
  return {
    type: FETCH_ORDER_DETAILS_FAILURE,

    status: ERROR,
    error
  };
}

export function editAddress(addressDetails) {
  return async (dispatch, getState, { api }) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    dispatch(editAddressRequest());
    let addressObject = new FormData();
    addressObject.append("countryIso", addressDetails.countryIso);
    addressObject.append("addressType", addressDetails.addressType);
    if (addressDetails.phone) {
      addressObject.append("phone", addressDetails.phone);
    }
    if (addressDetails.firstName) {
      addressObject.append("firstName", addressDetails.firstName);
    }
    if (addressDetails.lastName) {
      addressObject.append("lastName", addressDetails.lastName);
    }
    addressObject.append("postalCode", addressDetails.postalCode);
    if (addressDetails.line1) {
      addressObject.append("line1", addressDetails.line1);
    } else {
      addressObject.append("line1", "");
    }
    if (addressDetails.line2) {
      addressObject.append("line2", addressDetails.line2);
    } else {
      addressObject.append("line2", "");
    }
    if (addressDetails.line3) {
      addressObject.append("line3", addressDetails.line3);
    } else {
      addressObject.append("line3", "");
    }
    addressObject.append("state", addressDetails.state);
    addressObject.append("town", addressDetails.town);
    addressObject.append("defaultFlag", addressDetails.defaultFlag);
    addressObject.append("addressId", addressDetails.addressId);
    if (addressDetails.landmark) {
      addressObject.append("landmark", addressDetails.landmark);
    } else {
      addressObject.append("landmark", "");
    }
    addressObject.append("emailId", " ");
    try {
      const result = await api.postFormData(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/editAddress?access_token=${
          JSON.parse(customerCookie).access_token
        }&pageSize=${PAGE_SIZE}&isPwa=true&platformNumber=${PLAT_FORM_NUMBER}`,
        addressObject
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      return dispatch(editAddressSuccess(resultJson));
    } catch (e) {
      return dispatch(editAddressFailure(e.message));
    }
  };
}

export function fetchOrderDetails(orderId) {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(fetchOrderDetailsRequest());
    try {
      const result = await api.get(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/getSelectedOrder/${orderId}?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      setDataLayer(ADOBE_MY_ACCOUNT_ORDER_DETAILS);
      dispatch(fetchOrderDetailsSuccess(resultJson));
    } catch (e) {
      dispatch(fetchOrderDetailsFailure(e.message));
    }
  };
}

export function sendInvoiceRequest() {
  return {
    type: SEND_INVOICE_REQUEST,
    status: REQUESTING
  };
}
export function sendInvoiceSuccess(sendInvoice) {
  return {
    type: SEND_INVOICE_SUCCESS,
    status: SUCCESS,
    sendInvoice
  };
}

export function sendInvoiceFailure(error) {
  return {
    type: SEND_INVOICE_FAILURE,
    status: ERROR,
    error
  };
}

export function sendInvoice(lineID, orderNumber) {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(sendInvoiceRequest());
    try {
      const result = await api.get(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/sendInvoice?access_token=${
          JSON.parse(customerCookie).access_token
        }&orderNumber=${orderNumber}&lineID=${lineID}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(sendInvoiceSuccess(resultJson));
    } catch (e) {
      dispatch(sendInvoiceFailure(e.message));
    }
  };
}

export function updateProfileRequest() {
  return {
    type: UPDATE_PROFILE_REQUEST,
    status: REQUESTING
  };
}

export function logoutUserByMobileNumber() {
  return {
    type: LOG_OUT_ACCOUNT_USING_MOBILE_NUMBER
  };
}

export function updateProfileSuccess(userDetails) {
  return {
    type: UPDATE_PROFILE_SUCCESS,
    status: SUCCESS,
    userDetails
  };
}

export function updateProfileFailure(error) {
  return {
    type: UPDATE_PROFILE_FAILURE,
    status: ERROR,
    error
  };
}

export function updateProfile(accountDetails, otp) {
  let dateOfBirth = format(
    accountDetails.dateOfBirth,
    DATE_FORMAT_TO_UPDATE_PROFILE
  );
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(updateProfileRequest());
    let updateProfileUrl;
    let requestUrl = `isPwa=true&access_token=${
      JSON.parse(customerCookie).access_token
    }&ProfileDataRequired=true`;
    if (accountDetails.firstName) {
      requestUrl = requestUrl + `&firstName=${accountDetails.firstName}`;
    }
    if (accountDetails.lastName) {
      requestUrl = requestUrl + `&lastName=${accountDetails.lastName}`;
    }
    if (accountDetails.dateOfBirth) {
      requestUrl = requestUrl + `&dateOfBirth=${dateOfBirth}`;
    }
    if (accountDetails.mobileNumber) {
      requestUrl = requestUrl + `&mobilenumber=${accountDetails.mobileNumber}`;
    }
    if (accountDetails.gender) {
      requestUrl = requestUrl + `&gender=${accountDetails.gender}`;
    }
    if (accountDetails.emailId) {
      requestUrl = requestUrl + `&emailid=${accountDetails.emailId}`;
    }
    try {
      updateProfileUrl = `${USER_PATH}/${
        JSON.parse(userDetails).userName
      }/updateprofile?${requestUrl}`;
      if (otp) {
        updateProfileUrl = `${updateProfileUrl}&otp=${otp}`;
      }
      const result = await api.post(updateProfileUrl);
      const resultJson = await result.json();

      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      if (resultJson.status === "OTP SENT TO MOBILE NUMBER: PLEASE VALIDATE") {
        dispatch(showModal(UPDATE_PROFILE_OTP_VERIFICATION, accountDetails));
      } else if (
        resultJson.emailId !== JSON.parse(userDetails).userName &&
        !MOBILE_PATTERN.test(JSON.parse(userDetails).userName)
      ) {
        dispatch(logoutUserByMobileNumber());
      } else {
        if (otp) {
          if (
            (resultJson.status === SUCCESS ||
              resultJson.status === SUCCESS_CAMEL_CASE ||
              resultJson.status === SUCCESS_UPPERCASE) &&
            (resultJson.mobileNumber !== JSON.parse(userDetails).userName &&
              MOBILE_PATTERN.test(JSON.parse(userDetails).userName))
          ) {
            dispatch(logoutUserByMobileNumber());
          }
        } else {
          return dispatch(updateProfileSuccess(resultJson));
        }
      }
    } catch (e) {
      return dispatch(updateProfileFailure(e.message));
    }
  };
}

export function getFollowedBrandsRequest() {
  return {
    type: GET_FOLLOWED_BRANDS_REQUEST,
    status: REQUESTING
  };
}
export function getFollowedBrandsSuccess(followedBrands) {
  return {
    type: GET_FOLLOWED_BRANDS_SUCCESS,
    status: SUCCESS,
    followedBrands
  };
}

export function getFollowedBrandsFailure(error) {
  return {
    type: GET_FOLLOWED_BRANDS_FAILURE,
    status: ERROR,
    error
  };
}

export function getFollowedBrands(isSetDataLayer) {
  return async (dispatch, getState, { api }) => {
    const mcvId = await getMcvId();

    dispatch(getFollowedBrandsRequest());
    let msdFormData = new FormData();
    msdFormData.append("api_key", API_KEY_FOR_MSD);
    msdFormData.append("num_results", NUMBER_OF_RESULTS_FOR_BRANDS);
    msdFormData.append("mad_uuid", mcvId);
    msdFormData.append("details", true);
    msdFormData.append("widget_list", WIDGETS_LIST_FOR_BRANDS);
    try {
      const result = await api.postMsd(`${MSD_ROOT_PATH}/widgets`, msdFormData);
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      if (isSetDataLayer) {
        setDataLayer(ADOBE_MY_ACCOUNT_BRANDS);
      }
      dispatch(getFollowedBrandsSuccess(resultJson.data[0]));
    } catch (e) {
      dispatch(getFollowedBrandsFailure(e.message));
    }
  };
}

export function followAndUnFollowBrandRequest() {
  return {
    type: FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_REQUEST,
    status: REQUESTING
  };
}

// this reducer we need to handle in home reducer
export function followAndUnFollowBrandSuccessForHomeFeed(
  brandId,
  followStatus,
  positionInFeed
) {
  return {
    type: FOLLOW_AND_UN_FOLLOW_BRANDS_IN_HOME_FEED_SUCCESS,
    status: SUCCESS,
    brandId,
    followStatus,
    positionInFeed
  };
}

// this reducer we need to catch in pdp reducer
export function followAndUnFollowBrandSuccessForPdp(brandId, followStatus) {
  return {
    type: FOLLOW_AND_UN_FOLLOW_BRANDS_IN_PDP_SUCCESS,
    status: SUCCESS,
    brandId,
    followStatus
  };
}

// this reducer we need to catch in account reducer
export function followAndUnFollowBrandSuccessForMyAccount(
  brandId,
  followStatus
) {
  return {
    type: FOLLOW_AND_UN_FOLLOW_BRANDS_IN_MY_ACCOUNT_SUCCESS,
    status: SUCCESS,
    brandId,
    followStatus
  };
}
export function followAndUnFollowBrandFailure(error) {
  return {
    type: FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_FAILURE,
    status: ERROR,
    error
  };
}

export function followAndUnFollowBrand(
  brandId,
  followStatus,
  pageType: null,
  positionInFeed: null
) {
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  const followedText =
    followStatus === "true" || followStatus === true ? UNFOLLOW : FOLLOW;
  //here sometimes  we are getting isFollowingStatus type of string "true" or "false"
  // so here we are converting it in to bool
  const updatedFollowedStatus = !(
    followStatus === "true" || followStatus === true
  );
  return async (dispatch, getState, { api }) => {
    dispatch(followAndUnFollowBrandRequest());
    const mcvId = await getMcvId();
    const updatedBrandObj = {
      api_key: API_KEY_FOR_MSD,
      mad_uuid: mcvId,
      data: [
        {
          fields: "brand",
          values: [brandId],
          action: followedText
        }
      ]
    };
    try {
      const followInFeedBackApiResult = await api.postMsdRowData(
        `feedback`,
        updatedBrandObj
      );
      const followInFeedBackApiResultJson = await followInFeedBackApiResult.json();
      const followInFeedBackApiResultJsonStatus = ErrorHandling.getFailureResponse(
        followInFeedBackApiResultJson
      );
      if (!followInFeedBackApiResultJsonStatus.status) {
        // here we are hitting call for update follow brand on p2 and we don;t have to
        // wait for this response . we just need to wait for msd follow and un follow brand
        // api response if it success then we have to update our reducer with success
        if (customerCookie) {
          await api.post(
            `${PRODUCT_PATH}/${
              JSON.parse(customerCookie).access_token
            }/updateFollowedBrands?brands=${brandId}&follow=${updatedFollowedStatus}&isPwa=true`
          );
        }
        // dispatch success for following brand on the basis of page type
        if (pageType === HOME_FEED_FOLLOW_AND_UN_FOLLOW) {
          const clonedComponent = getState().feed.homeFeed[positionInFeed];
          const indexOfBrand = findIndex(clonedComponent.data, item => {
            return item.id === brandId;
          });
          let brandName = clonedComponent.data[indexOfBrand].brandName;
          setDataLayerForFollowAndUnFollowBrand(
            ADOBE_ON_FOLLOW_AND_UN_FOLLOW_BRANDS,
            { followStatus: updatedFollowedStatus, brandName }
          );
          return dispatch(
            followAndUnFollowBrandSuccessForHomeFeed(
              brandId,
              updatedFollowedStatus,
              positionInFeed
            )
          );
        } else if (pageType === PDP_FOLLOW_AND_UN_FOLLOW) {
          const brandObj = getState().productDescription.aboutTheBrand;
          const brandName = brandObj.brandName;
          setDataLayerForFollowAndUnFollowBrand(
            ADOBE_ON_FOLLOW_AND_UN_FOLLOW_BRANDS,
            { followStatus: updatedFollowedStatus, brandName }
          );
          return dispatch(
            followAndUnFollowBrandSuccessForPdp(brandId, updatedFollowedStatus)
          );
        } else if (pageType === MY_ACCOUNT_FOLLOW_AND_UN_FOLLOW) {
          const currentBrands = getState().profile.followedBrands;
          const brandObj = currentBrands.find(item => item.id === brandId);
          let brandName = brandObj.brandName;
          setDataLayerForFollowAndUnFollowBrand(
            ADOBE_ON_FOLLOW_AND_UN_FOLLOW_BRANDS,
            { followStatus: updatedFollowedStatus, brandName }
          );

          return dispatch(
            followAndUnFollowBrandSuccessForMyAccount(
              brandId,
              updatedFollowedStatus
            )
          );
        }
      } else {
        throw new Error(`Error in following Brand for feedback Api`);
      }
    } catch (e) {
      return dispatch(followAndUnFollowBrandFailure(e.message));
    }
  };
}
export function changePasswordRequest() {
  return {
    type: CHANGE_PASSWORD_REQUEST,
    status: REQUESTING
  };
}
export function changePasswordSuccess(passwordDetails) {
  return {
    type: CHANGE_PASSWORD_SUCCESS,
    status: SUCCESS,
    passwordDetails
  };
}

export function changePasswordFailure(error) {
  return {
    type: CHANGE_PASSWORD_FAILURE,
    status: ERROR,
    error
  };
}

export function getCliqCashRequest() {
  return {
    type: GET_USER_CLIQ_CASH_DETAILS_REQUEST,
    status: REQUESTING
  };
}
export function getCliqCashSuccess(cliqCashDetails) {
  return {
    type: GET_USER_CLIQ_CASH_DETAILS_SUCCESS,
    status: SUCCESS,
    cliqCashDetails
  };
}

export function getCliqCashFailure(error) {
  return {
    type: GET_USER_CLIQ_CASH_DETAILS_FAILURE,
    status: ERROR,
    error
  };
}

export function changePassword(passwordDetails) {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(changePasswordRequest());
    try {
      const result = await api.post(
        `${PATH}/forgottenpasswordtokens/${
          JSON.parse(userDetails).userName
        }/resetCustomerPassword?isPwa=true&access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&old=${passwordDetails.oldPassword}&newPassword=${
          passwordDetails.newPassword
        }`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      return dispatch(changePasswordSuccess(resultJson));
    } catch (e) {
      return dispatch(changePasswordFailure(e.message));
    }
  };
}

export function getCliqCashDetails() {
  return async (dispatch, getState, { api }) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    dispatch(getCliqCashRequest());
    try {
      const result = await api.post(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/cliqcash/getUserCliqCashDetails?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=${PLATFORM_NUMBER}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      setDataLayer(ADOBE_MY_ACCOUNT_CLIQ_CASH);
      if (!resultJson.isWalletCreated && !resultJson.isWalletOtpVerified) {
        dispatch(showModal(GENERATE_OTP_FOR_CLIQ_CASH));
      }

      dispatch(getCliqCashSuccess(resultJson));
    } catch (e) {
      dispatch(getCliqCashFailure(e.message));
    }
  };
}

export function redeemCliqVoucherRequest() {
  return {
    type: REDEEM_CLIQ_VOUCHER_REQUEST,
    status: REQUESTING
  };
}
export function redeemCliqVoucherSuccess(cliqCashVoucherDetails) {
  return {
    type: REDEEM_CLIQ_VOUCHER_SUCCESS,
    status: SUCCESS,
    cliqCashVoucherDetails
  };
}

export function redeemCliqVoucherFailure(error) {
  return {
    type: REDEEM_CLIQ_VOUCHER_FAILURE,
    status: FAILURE,
    error
  };
}

export function redeemCliqVoucher(cliqCashDetails, fromCheckout) {
  return async (dispatch, getState, { api }) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
    dispatch(redeemCliqVoucherRequest());
    try {
      const result = await api.postFormData(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/cliqcash/redeemCliqVoucher?access_token=${
          JSON.parse(customerCookie).access_token
        }&cartGuid=&couponCode=${cliqCashDetails.cardNumber}&passKey=${
          cliqCashDetails.pinNumber
        }`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      if (fromCheckout) {
        dispatch(hideModal(GIFT_CARD_MODAL));

        dispatch(getPaymentModes(JSON.parse(cartDetails).guid));
      }
      return dispatch(redeemCliqVoucherSuccess(resultJson));
    } catch (e) {
      return dispatch(redeemCliqVoucherFailure(e.message));
    }
  };
}
export function logout() {
  return {
    type: LOGOUT,
    status: SUCCESS
  };
}

export function clearGiftCardStatus() {
  return {
    type: CLEAR_GIFT_CARD_STATUS
  };
}
export function clearAccountUpdateType() {
  return {
    type: CLEAR_ACCOUNT_UPDATE_TYPE
  };
}
export function clearOrderDetails() {
  return {
    type: Clear_ORDER_DATA
  };
}

export function resetAddAddressDetails() {
  return {
    type: RE_SET_ADD_ADDRESS_DETAILS
  };
}
export function clearChangePasswordDetails() {
  return {
    type: CLEAR_CHANGE_PASSWORD_DETAILS
  };
}

export function clearPinCodeStatus() {
  return {
    type: CLEAR_PIN_CODE_STATUS
  };
}
