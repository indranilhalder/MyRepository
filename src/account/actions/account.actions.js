import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS
} from "../../lib/constants";
export const GET_SAVED_CARD_REQUEST = "GET_SAVED_CARD_REQUEST";
export const GET_SAVED_CARD_SUCCESS = "GET_SAVED_CARD_SUCCESS";
export const GET_SAVED_CARD_FAILURE = "GET_SAVED_CARD_FAILURE";

export const REMOVE_SAVED_CARD_REQUEST = "REMOVE_SAVED_CARD_REQUEST";
export const REMOVE_SAVED_CARD_SUCCESS = "REMOVE_SAVED_CARD_SUCCESS";
export const REMOVE_SAVED_CARD_FAILURE = "REMOVE_SAVED_CARD_FAILURE";

export const GET_ALL_ORDERS_REQUEST = "GET_ALL_ORDERS_REQUEST";
export const GET_ALL_ORDERS_SUCCESS = "GET_ALL_ORDERS_SUCCESS";
export const GET_ALL_ORDERS_FAILURE = "GET_ALL_ORDERS_FAILURE";

export const CURRENT_PAGE = 0;
export const PAGE_SIZE = 10;
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
export function removeSavedCardSuccess(savedCards) {
  return {
    type: REMOVE_SAVED_CARD_SUCCESS,
    status: SUCCESS,
    savedCards
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
  return async (dispatch, getState, { api }) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
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
