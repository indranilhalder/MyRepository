import {
  SUCCESS,
  SUCCESS_UPPERCASE,
  SUCCESS_CAMEL_CASE,
  REQUESTING,
  ERROR,
  FAILURE,
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  SUCCESS_FOR_ADDING_TO_WSHLIST
} from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";

export const GET_WISH_LIST_ITEMS_REQUEST = "GET_WISH_LIST_ITEMS_REQUEST";
export const GET_WISH_LIST_ITEMS_SUCCESS = "GET_WISH_LIST_ITEMS_SUCCESS";
export const GET_WISH_LIST_ITEMS_FAILURE = "GET_WISH_LIST_ITEMS_FAILURE";

export const ADD_PRODUCT_TO_WISH_LIST_REQUEST =
  "ADD_PRODUCT_TO_WISH_LIST_REQUEST";
export const ADD_PRODUCT_TO_WISH_LIST_SUCCESS =
  "ADD_PRODUCT_TO_WISH_LIST_SUCCESS";
export const ADD_PRODUCT_TO_WISH_LIST_FAILURE =
  "ADD_PRODUCT_TO_WISH_LIST_FAILURE";

export const REMOVE_PRODUCT_FROM_WISH_LIST_REQUEST =
  "REMOVE_PRODUCT_FROM_WISH_LIST_REQUEST";
export const REMOVE_PRODUCT_FROM_WISH_LIST_SUCCESS =
  "REMOVE_PRODUCT_FROM_WISH_LIST_SUCCESS";
export const REMOVE_PRODUCT_FROM_WISH_LIST_FAILURE =
  "REMOVE_PRODUCT_FROM_WISH_LIST_FAILURE";

export const PRODUCT_DETAILS_PATH = "v2/mpl/users";
const MY_WISH_LIST = "MyWishList";

export function getWishListItemsRequest() {
  return {
    type: GET_WISH_LIST_ITEMS_REQUEST,
    status: REQUESTING
  };
}
export function getWishListItemsSuccess(wishlist) {
  return {
    type: GET_WISH_LIST_ITEMS_SUCCESS,
    status: SUCCESS,
    wishlist
  };
}

export function getWishListItemsFailure(error) {
  return {
    type: GET_WISH_LIST_ITEMS_FAILURE,
    status: ERROR,
    error
  };
}

export function getWishListItems(productDetails) {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(getWishListItemsRequest());
    try {
      const result = await api.postFormData(
        `${PRODUCT_DETAILS_PATH}/${
          JSON.parse(userDetails).userName
        }/getAllWishlist?platformNumber=2&access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true`
      );
      const resultJson = await result.json();
      if (resultJson.errors) {
        throw new Error(`${resultJson.errors[0].message}`);
      }
      return dispatch(getWishListItemsSuccess(resultJson.wishList[0]));
    } catch (e) {
      return dispatch(getWishListItemsFailure(e.message));
    }
  };
}

export function addProductToWishListRequest() {
  return {
    type: ADD_PRODUCT_TO_WISH_LIST_REQUEST,
    status: REQUESTING
  };
}
export function addProductToWishListSuccess(product) {
  return {
    type: ADD_PRODUCT_TO_WISH_LIST_SUCCESS,
    status: SUCCESS,
    product
  };
}

export function addProductToWishListFailure(error) {
  return {
    type: ADD_PRODUCT_TO_WISH_LIST_FAILURE,
    status: ERROR,
    error
  };
}

export function addProductToWishList(productDetails) {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(addProductToWishListRequest());
    const productToBeAdd = new FormData();
    productToBeAdd.append("ussid", productDetails.winningUssID);
    productToBeAdd.append("productCode", productDetails.productListingId);
    productToBeAdd.append("wishlistName", MY_WISH_LIST);
    try {
      const result = await api.postFormData(
        `${PRODUCT_DETAILS_PATH}/${
          JSON.parse(userDetails).userName
        }/addProductInWishlist?platformNumber=2&access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true`,
        productToBeAdd
      );
      const resultJson = await result.json();
      if (resultJson.status === SUCCESS_FOR_ADDING_TO_WSHLIST) {
        return dispatch(addProductToWishListSuccess(productDetails));
      } else {
        throw new Error(`${resultJson.message}`);
      }
    } catch (e) {
      return dispatch(addProductToWishListFailure(e.message));
    }
  };
}

export function removeProductFromWishListRequest() {
  return {
    type: REMOVE_PRODUCT_FROM_WISH_LIST_REQUEST,
    status: REQUESTING
  };
}
export function removeProductFromWishListSuccess(product) {
  return {
    type: REMOVE_PRODUCT_FROM_WISH_LIST_SUCCESS,
    status: SUCCESS,
    product
  };
}

export function removeProductFromWishListFailure(error) {
  return {
    type: REMOVE_PRODUCT_FROM_WISH_LIST_FAILURE,
    status: ERROR,
    error
  };
}

export function removeProductFromWishList(productDetails) {
  return async (dispatch, getState, { api }) => {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    const productToBeRemove = new FormData();
    productToBeRemove.append("ussid", productDetails.USSID);
    productToBeRemove.append("wishlistName", MY_WISH_LIST);
    dispatch(removeProductFromWishListRequest());
    try {
      const result = await api.postFormData(
        `${PRODUCT_DETAILS_PATH}/${
          JSON.parse(userDetails).userName
        }/removeProductFromWishlist?&access_token=${
          JSON.parse(customerCookie).access_token
        }`,
        productToBeRemove
      );
      const resultJson = await result.json();
      if (
        resultJson.status === SUCCESS ||
        resultJson.status === SUCCESS_UPPERCASE ||
        resultJson.status === SUCCESS_CAMEL_CASE
      ) {
        return dispatch(removeProductFromWishListSuccess(productDetails));
      } else {
        throw new Error(`${resultJson.errors[0].message}`);
      }
    } catch (e) {
      return dispatch(removeProductFromWishListFailure(e.message));
    }
  };
}
