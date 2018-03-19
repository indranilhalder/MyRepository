import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  FAILURE_UPPERCASE
} from "../../lib/constants";
export const USER_CART_PATH = "v2/mpl/users";

export const GET_ALL_ORDERS_REQUEST = "GET_ALL_ORDERS_REQUEST";
export const GET_ALL_ORDERS_SUCCESS = "GET_ALL_ORDERS_SUCCESS";
export const GET_ALL_ORDERS_FAILURE = "GET_ALL_ORDERS_FAILURE";

export const GET_ALL_WISHLIST_REQUEST = "GET_ALL_WISHLIST_REQUEST";
export const GET_ALL_WISHLIST_SUCCESS = "GET_ALL_WISHLIST_SUCCESS";
export const GET_ALL_WISHLIST_FAILURE = "GET_ALL_WISHLIST_FAILURE";

export const CURRENT_PAGE = 0;
export const PAGE_SIZE = 10;

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

export function getAllWishlistRequest() {
  return {
    type: GET_ALL_WISHLIST_REQUEST,
    status: REQUESTING
  };
}
export function getAllWishlistSuccess(wishlist) {
  return {
    type: GET_ALL_WISHLIST_SUCCESS,
    status: SUCCESS,
    wishlist
  };
}

export function getAllWishlistFailure(error) {
  return {
    type: GET_ALL_WISHLIST_FAILURE,
    status: ERROR,
    error
  };
}

export function getWishList() {
  return async (dispatch, getState, { api }) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    dispatch(getAllWishlistRequest());
    try {
      const result = await api.post(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).userName
        }/getAllWishlist?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(getAllWishlistSuccess(resultJson.wishList[0]));
    } catch (e) {
      dispatch(getAllWishlistFailure(e.message));
    }
  };
}
