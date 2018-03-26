import {
  SUCCESS,
  SUCCESS_UPPERCASE,
  SUCCESS_CAMEL_CASE,
  REQUESTING,
  ERROR,
  FAILURE
} from "../../lib/constants";
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

export const FETCH_ORDER_DETAILS_REQUEST = "FETCH_ORDER_DETAILS_REQUEST";
export const FETCH_ORDER_DETAILS_SUCCESS = "FETCH_ORDER_DETAILS_SUCCESS";
export const FETCH_ORDER_DETAILS_FAILURE = "FETCH_ORDER_DETAILS_FAILURE";

export const GET_USER_COUPON_REQUEST = "GET_USER_COUPON_REQUEST";
export const GET_USER_COUPON_SUCCESS = "GET_USER_COUPON_SUCCESS";
export const GET_USER_COUPON_FAILURE = "GET_USER_COUPON_FAILURE";

export const GET_USER_ALERTS_REQUEST = "GET_USER_ALERTS_REQUEST";
export const GET_USER_ALERTS_SUCCESS = "GET_USER_ALERTS_SUCCESS";
export const GET_USER_ALERTS_FAILURE = "GET_USER_ALERTS_FAILURE";

export const SEND_INVOICE_REQUEST = "SEND_INVOICE_REQUEST";
export const SEND_INVOICE_SUCCESS = "SEND_INVOICE_SUCCESS";
export const SEND_INVOICE_FAILURE = "SEND_INVOICE_FAILURE";

export const REMOVE_ADDRESS_REQUEST = "REMOVE_ADDRESS_REQUEST";
export const REMOVE_ADDRESS_SUCCESS = "REMOVE_ADDRESS_SUCCESS";
export const REMOVE_ADDRESS_FAILURE = "REMOVE_ADDRESS_FAILURE";

export const GET_WISHLIST_REQUEST = "GET_WISHLIST_REQUEST";
export const GET_WISHLIST_SUCCESS = "GET_WISHLIST_SUCCESS";
export const GET_WISHLIST_FAILURE = "GET_WISHLIST_FAILURE";

export const GET_FOLLOWED_BRANDS_REQUEST = "GET_FOLLOWED_BRANDS_REQUEST";
export const GET_FOLLOWED_BRANDS_SUCCESS = "GET_FOLLOWED_BRANDS_SUCCESS";
export const GET_FOLLOWED_BRANDS_FAILURE = "GET_FOLLOWED_BRANDS_FAILURE";

export const GET_GIFTCARD_REQUEST = "GET_GIFTCARD_REQUEST";
export const GET_GIFTCARD_SUCCESS = "GET_GIFTCARD_SUCCESS";
export const GET_GIFTCARD_FAILURE = "GET_GIFTCARD_FAILURE";

export const FOLLOW_AND_UN_FOLLOW_BRANDS_COMMERCE_REQUEST =
  "FOLLOW_AND_UN_FOLLOW_BRANDS_COMMERCE_REQUEST";
export const FOLLOW_AND_UN_FOLLOW_BRANDS_COMMERCE_SUCCESS =
  "FOLLOW_AND_UN_FOLLOW_BRANDS_COMMERCE_SUCCESS";
export const FOLLOW_AND_UN_FOLLOW_BRANDS_COMMERCE_FAILURE =
  "FOLLOW_AND_UN_FOLLOW_BRANDS_COMMERCE_FAILURE";

export const FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_REQUEST =
  "FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_REQUEST";
export const FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_SUCCESS =
  "FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_SUCCESS";
export const FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_FAILURE =
  "FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_FAILURE";

export const CREATE_GIFT_CARD_REQUEST = "CREATE_GIFT_CARD_REQUEST";
export const CREATE_GIFT_CARD_SUCCESS = "CREATE_GIFT_CARD_SUCCESS";
export const CREATE_GIFT_CARD_FAILURE = "CREATE_GIFT_CARD_FAILURE";

export const CURRENT_PAGE = 0;
export const PAGE_SIZE = 10;
export const PLATFORM_NUMBER = 2;
export const USER_PATH = "v2/mpl/users";
export const PRODUCT_PATH = "v2/mpl/products";
export const ROOT_URL = "v2/mpl/products";

export const MSD_ROOT_PATH = "https://ap-southeast-1-api.madstreetden.com";

const API_KEY_FOR_MSD = "8783ef14595919d35b91cbc65b51b5b1da72a5c3";
const NUMBER_OF_RESULTS_FOR_BRANDS = [25];
const WIDGETS_LIST_FOR_BRANDS = [112];
const CARD_TYPE = "BOTH";
const FOLLOW = "follow";
const UNFOLLOW = "unfollow";

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
  return async (dispatch, getState, { api }) => {
    dispatch(giftCardRequest());
    try {
      const result = await api.get(
        `${PRODUCT_PATH}/egvProductInfo?access_token=${
          JSON.parse(customerCookie).access_token
        }`
      );
      const resultJson = await result.json();

      if (
        resultJson.status === SUCCESS ||
        resultJson.status === SUCCESS_UPPERCASE ||
        resultJson.status === SUCCESS_CAMEL_CASE
      ) {
        return dispatch(giftCardSuccess(resultJson));
      } else {
        throw new Error(`${resultJson.errors[0].message}`);
      }
    } catch (e) {
      dispatch(giftCardFailure(e.message));
    }
  };
}
////create gift card

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
      console.log(resultJson);
      if (resultJson.errors) {
        throw new Error(resultJson.errors[0].message);
      }
      dispatch(createGiftCardSuccess(resultJson));
    } catch (e) {
      dispatch(createGiftCardFailure(e.message));
    }
  };
}

//////

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

export function removeAddressRequest() {
  return {
    type: REMOVE_ADDRESS_REQUEST,
    status: REQUESTING
  };
}
export function removeAddressSuccess() {
  return {
    type: REMOVE_ADDRESS_SUCCESS,
    status: SUCCESS
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
      if (resultJson.errors) {
        throw new Error(`${resultJson.errors[0].message}`);
      }
      dispatch(removeAddressSuccess());
    } catch (e) {
      dispatch(removeAddressFailure(e.message));
    }
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
        }`
      );
      const resultJson = await result.json();
      if (
        resultJson.errors ||
        resultJson.status === FAILURE_UPPERCASE ||
        resultJson.status === FAILURE
      ) {
        throw new Error(`${resultJson.errors[0].message}`);
      }
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
      if (
        resultJson.errors ||
        resultJson.status === FAILURE_UPPERCASE ||
        resultJson.status === FAILURE
      ) {
        throw new Error(`${resultJson.errors[0].message}`);
      }
      dispatch(sendInvoiceSuccess(resultJson));
    } catch (e) {
      dispatch(sendInvoiceFailure(e.message));
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

export function getFollowedBrands() {
  const mcvId = window._satellite.getVisitorId().getMarketingCloudVisitorID();

  return async (dispatch, getState, { api }) => {
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
      if (
        resultJson.errors ||
        resultJson.status === FAILURE_UPPERCASE ||
        resultJson.status === FAILURE
      ) {
        throw new Error(`${resultJson.errors[0].message}`);
      }

      dispatch(getFollowedBrandsSuccess(resultJson.data[0]));
    } catch (e) {
      dispatch(getFollowedBrandsFailure(e.message));
    }
  };
}

// this follow and unfollow function is for just follow and unfollow brand we don't need to
// update any reducer data for brand following status. because after follow and un follow we need to hit
// all brands for user that will return again followed brands
export function followAndUnFollowBrandInCommerceRequest() {
  return {
    type: FOLLOW_AND_UN_FOLLOW_BRANDS_COMMERCE_REQUEST,
    status: REQUESTING
  };
}

export function followAndUnFollowBrandInCommerceSuccess() {
  return {
    type: FOLLOW_AND_UN_FOLLOW_BRANDS_COMMERCE_SUCCESS,
    status: SUCCESS
  };
}
export function followAndUnFollowBrandInCommerceFailure(error) {
  return {
    type: FOLLOW_AND_UN_FOLLOW_BRANDS_COMMERCE_FAILURE,
    status: ERROR,
    error
  };
}

export function followAndUnFollowBrandInCommerce(brandId, followStatus) {
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(followAndUnFollowBrandInCommerceRequest());
    try {
      const updatedFollowStatus = !followStatus;
      const result = await api.post(
        `${PRODUCT_PATH}/${
          JSON.parse(customerCookie).access_token
        }/updateFollowedBrands?brands=${brandId}&follow=${updatedFollowStatus}&isPwa=true`
      );
      const resultJson = await result.json();

      if (
        resultJson.status === SUCCESS ||
        resultJson.status === SUCCESS_UPPERCASE ||
        resultJson.status === SUCCESS_CAMEL_CASE
      ) {
        return dispatch(followAndUnFollowBrandInCommerceSuccess());
      } else {
        throw new Error(`${resultJson.message}`);
      }
    } catch (e) {
      return dispatch(followAndUnFollowBrandInCommerceFailure(e.message));
    }
  };
}

export function followAndUnFollowBrandInFeedBackRequest() {
  return {
    type: FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_REQUEST,
    status: REQUESTING
  };
}

export function followAndUnFollowBrandInFeedBackSuccess() {
  return {
    type: FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_SUCCESS,
    status: SUCCESS
  };
}
export function followAndUnFollowBrandInFeedBackFailure(error) {
  return {
    type: FOLLOW_AND_UN_FOLLOW_BRANDS_IN_FEEDBACK_FAILURE,
    status: ERROR,
    error
  };
}

export function followAndUnFollowBrandInFeedBackInCommerceApi(
  brandId,
  followStatus
) {
  const mcvId = window._satellite.getVisitorId().getMarketingCloudVisitorID();

  const followedText = followStatus ? UNFOLLOW : FOLLOW;
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
  return async (dispatch, getState, { api }) => {
    dispatch(followAndUnFollowBrandInFeedBackRequest());
    try {
      const result = await api.postMsdRowData(
        `${MSD_ROOT_PATH}/feedback`,
        updatedBrandObj
      );
      const resultJson = await result.json();

      if (
        resultJson.status === SUCCESS ||
        resultJson.status === SUCCESS_UPPERCASE ||
        resultJson.status === SUCCESS_CAMEL_CASE
      ) {
        return dispatch(followAndUnFollowBrandInFeedBackSuccess());
      } else {
        throw new Error(`${resultJson.errors[0].message}`);
      }
    } catch (e) {
      return dispatch(followAndUnFollowBrandInFeedBackFailure(e.message));
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
      const result = await api.postFormData(
        `${USER_PATH}/${
          JSON.parse(userDetails).userName
        }/getAllWishlist?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=${PLATFORM_NUMBER}`
      );
      const resultJson = await result.json();
      if (
        resultJson.status === SUCCESS ||
        resultJson.status === SUCCESS_UPPERCASE ||
        resultJson.status === SUCCESS_CAMEL_CASE
      ) {
        return dispatch(getWishlistSuccess(resultJson.wishList[0])); //we sre getting response wishlit[0]
      } else {
        throw new Error(`${resultJson.errors[0].message}`);
      }
    } catch (e) {
      return dispatch(getWishlistFailure(e.message));
    }
  };
}
