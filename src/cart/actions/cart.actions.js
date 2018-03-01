import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  GLOBAL_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  FAILURE,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS
} from "../../lib/constants";
export const USER_CART_PATH = "v2/mpl/users";

export const APPLY_COUPON_REQUEST = "APPLY_COUPON_REQUEST";
export const APPLY_COUPON_SUCCESS = "APPLY_COUPON_SUCCESS";
export const APPLY_COUPON_FAILURE = "APPLY_COUPON_FAILURE";

export const GET_COUPON_REQUEST = "GET_COUPON_REQUEST";
export const GET_COUPON_SUCCESS = "GET_COUPON_SUCCESS";
export const GET_COUPON_FAILURE = "GET_COUPON_FAILURE";

export const RELEASE_COUPON_REQUEST = "RELEASE_COUPON_REQUEST";
export const RELEASE_COUPON_SUCCESS = "RELEASE_COUPON_SUCCESS";
export const RELEASE_COUPON_FAILURE = "RELEASE_COUPON_FAILURE";

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


const pincode = 229001;

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
export function cartDetailsCNCRequest() {
  return {
    type: CART_DETAILS_REQUEST,
    status: REQUESTING
  };
}
export function cartDetailsCNCSuccess(cartDetails) {
  return {
    type: CART_DETAILS_SUCCESS,
    status: SUCCESS,
    cartDetails
  };
}

export function cartDetailsCNCFailure(error) {
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
export function getCartDetailsCNC(userId, accessToken, cartId) {
  return async (dispatch, getState, { api }) => {
    dispatch(cartDetailsCNCRequest());

    try {
      const result = await api.get(
        `${USER_CART_PATH}/${userId}/carts/${cartId}/cartDetailsCNC?access_token=${accessToken}&isPwa=true`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
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

export function getCoupons() {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(getCouponsRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).customerInfo.mobileNumber
        }/getCoupons?access_token=${
          JSON.parse(customerCookie).access_token
        }&currentPage=0&usedCoupon=N&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(getCouponsSuccess(resultJson));
    } catch (e) {
      dispatch(getCouponsFailure(e.message));
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

export function releaseCouponRequest() {
  return {
    type: RELEASE_COUPON_REQUEST,
    status: REQUESTING
  };
}
export function releaseCouponSuccess() {
  return {
    type: RELEASE_COUPON_SUCCESS,
    status: SUCCESS
  };
}

export function releaseCouponFailure(error) {
  return {
    type: RELEASE_COUPON_FAILURE,
    status: ERROR,
    error
  };
}
export function releaseCoupon(carId) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(releaseCouponRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).customerInfo.mobileNumber
        }/carts/carId/releaseCoupons?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(releaseCouponSuccess());
    } catch (e) {
      dispatch(releaseCouponFailure(e.message));
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
export function selectDeliveryMode(deliveryMode, ussId, cartId) {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(userAddressRequest());
    try {
      const result = await api.post(`${USER_CART_PATH}/${
        getState().user.user.customerInfo.mobileNumber
      }/carts/${cartId}/selectDeliveryMode?access_token=${
        JSON.parse(customerCookie).access_token
      }&deliverymodeussId={"${ussId}":"${deliveryMode}"}&
      removeExchange=0`);
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

export function addAddressToCartRequest(error) {
  return {
    type: ADD_ADDRESS_TO_CART_REQUEST,
    status: REQUESTING
  };
}

export function addAddressToCartSuccess(userAddress) {
  return {
    type: ADD_ADDRESS_TO_CART_SUCCESS,
    status: SUCCESS,
    userAddress
  };
}

export function addAddressToCartFailure(error) {
  return {
    type: ADD_ADDRESS_TO_CART_FAILURE,
    status: ERROR,
    error
  };
}

export function addAddressToCart(addressId) {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  return async (dispatch, getState, { api }) => {
    dispatch(userAddressRequest());
    try {
      let userId = getState().user.user.customerInfo.mobileNumber;
      let access_token = JSON.parse(customerCookie).access_token;
      let cartId = JSON.parse(cartDetails).code;
      const result = await api.post(
        `${USER_CART_PATH}/${userId}/addAddressToOrder?channel=mobile&access_token=${access_token}&addressId=${addressId}&cartId=${cartId}&removeExchangeFromCart=`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // this is going to be call when we will wiringup the final checkout page
      // dispatch(getCartDetailsCNC(userId, access_token, cartId));
      dispatch(addAddressToCartSuccess(resultJson));
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

export function getOrderSummary() {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetails = Cookie.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
  return async (dispatch, getState, { api }) => {
    dispatch(orderSummaryRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          getState().user.user.customerInfo.mobileNumber
        }/carts/${
          JSON.parse(cartDetails).code
        }/displayOrderSummary?access_token=${
          JSON.parse(customerCookie).access_token
        }&pincode=400083&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
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
  let cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
  return async (dispatch, getState, { api }) => {
    dispatch(getCartIdRequest());

    try {
      const result = await api.get(
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
      if (cartDetailsAnonymous) {
        dispatch(mergeCartId(resultJson));
      } else {
        dispatch(getCartIdSuccess(resultJson));
      }
    } catch (e) {
      dispatch(getCartIdFailure(e.message));
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

export function mergeCartId(cartDetails) {
  let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  let cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);
  return async (dispatch, getState, { api }) => {
    dispatch(mergeCardIdRequest());

    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).customerInfo.mobileNumber
        }/carts?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&&platformNumber=2&userId=${
          JSON.parse(userDetails).customerInfo.mobileNumber
        }&oldCartId=${JSON.parse(cartDetailsAnonymous).guid}&toMergeCartGuid=${
          cartDetails.guid
        }`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(resultJson.message);
      }

      dispatch(mergeCartIdSuccess(resultJson));
    } catch (e) {
      dispatch(mergeCartIdFailure(e.message));
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
  pinCode
) {
  return async (dispatch, getState, { api }) => {
    dispatch(checkPinCodeServiceAvailabilityRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${userName}/checkPincode?access_token=${accessToken}&productCode=MP000000000165621&pin=${pinCode}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(resultJson.message);
      }
      dispatch(checkPinCodeServiceAvailabilitySuccess(resultJson));
    } catch (e) {
      dispatch(checkPinCodeServiceAvailabilityFailure(e.message));

    }
  };
}
