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
import * as ErrorHandling from "../../general/ErrorHandling.js";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions.js";
import {
  setDataLayerForPdpDirectCalls,
  ADOBE_DIRECT_CALL_FOR_SAVE_ITEM_ON_CART,
  setDataLayerForCartDirectCalls,
  SET_DATA_LAYER_FOR_SAVE_PRODUCT_EVENT_ON_PDP
} from "../../lib/adobeUtils";
export const GET_WISH_LIST_ITEMS_REQUEST = "GET_WISH_LIST_ITEMS_REQUEST";
export const GET_WISH_LIST_ITEMS_SUCCESS = "GET_WISH_LIST_ITEMS_SUCCESS";
export const GET_WISH_LIST_ITEMS_FAILURE = "GET_WISH_LIST_ITEMS_FAILURE";

export const CREATE_WISHLIST_REQUEST = "CREATE_WISHLIST_REQUEST";
export const CREATE_WISHLIST_SUCCESS = "CREATE_WISHLIST_SUCCESS";
export const CREATE_WISHLIST_FAILURE = "CREATE_WISHLIST_FAILURE";

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

export function getWishListItems() {
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

      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      const currentWishlist = resultJson.wishList.filter(wishlist => {
        return wishlist.name === MY_WISH_LIST;
      });

      return dispatch(getWishListItemsSuccess(currentWishlist[0]));
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

export function addProductToWishList(productDetails, setDataLayerType: null) {
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      if (setDataLayerType === SET_DATA_LAYER_FOR_SAVE_PRODUCT_EVENT_ON_PDP) {
        setDataLayerForPdpDirectCalls(
          SET_DATA_LAYER_FOR_SAVE_PRODUCT_EVENT_ON_PDP
        );
      } else if (setDataLayerType === ADOBE_DIRECT_CALL_FOR_SAVE_ITEM_ON_CART) {
        setDataLayerForCartDirectCalls(ADOBE_DIRECT_CALL_FOR_SAVE_ITEM_ON_CART);
      }
      return dispatch(addProductToWishListSuccess(productDetails));
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
    productToBeRemove.append("USSID", productDetails.ussId);
    productToBeRemove.append("wishlistName", MY_WISH_LIST);
    dispatch(removeProductFromWishListRequest());
    dispatch(showSecondaryLoader());
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
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(hideSecondaryLoader());
      dispatch(getWishListItems());
      return dispatch(removeProductFromWishListSuccess(productDetails));
    } catch (e) {
      dispatch(hideSecondaryLoader());

      return dispatch(removeProductFromWishListFailure(e.message));
    }
  };
}
export function createWishlistRequest() {
  return {
    type: CREATE_WISHLIST_REQUEST,
    status: REQUESTING
  };
}
export function createWishlistSuccess() {
  return {
    type: CREATE_WISHLIST_SUCCESS,
    status: SUCCESS
  };
}

export function createWishlistFailure(error) {
  return {
    type: CREATE_WISHLIST_FAILURE,
    status: ERROR,
    error
  };
}

export function createWishlist(productDetails) {
  const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
  const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(createWishlistRequest());
    const createWishlistObj = new FormData();
    createWishlistObj.append("wishlistName", MY_WISH_LIST);
    try {
      const result = await api.postFormData(
        `${PRODUCT_DETAILS_PATH}/${
          JSON.parse(userDetails).userName
        }/CreateWishlist?channel=mobile&access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true`,
        createWishlistObj
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      return dispatch(createWishlistSuccess());
    } catch (e) {
      return dispatch(createWishlistFailure(e.message));
    }
  };
}
