import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
import { CUSTOMER_ACCESS_TOKEN } from "../../lib/constants";
export const PRODUCT_DESCRIPTION_REQUEST = "PRODUCT_DESCRIPTION_REQUEST";
export const PRODUCT_DESCRIPTION_SUCCESS = "PRODUCT_DESCRIPTION_SUCCESS";
export const PRODUCT_DESCRIPTION_FAILURE = "PRODUCT_DESCRIPTION_FAILURE";

export const CHECK_PRODUCT_PIN_CODE_REQUEST = "CHECK_PRODUCT_PIN_CODE_REQUEST";
export const CHECK_PRODUCT_PIN_CODE_SUCCESS = "CHECK_PRODUCT_PIN_CODE_SUCCESS";
export const CHECK_PRODUCT_PIN_CODE_FAILURE = "CHECK_PRODUCT_PIN_CODE_FAILURE";

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

export const ADD_PRODUCT_TO_BAG_REQUEST = "ADD_PRODUCT_TO_BAG_REQUEST";
export const ADD_PRODUCT_TO_BAG_SUCCESS = "ADD_PRODUCT_TO_BAG_SUCCESS";
export const ADD_PRODUCT_TO_BAG_FAILURE = "ADD_PRODUCT_TO_BAG_FAILURE";

export const PRODUCT_DETAILS_PATH = "v2/mpl/users";
export const PRODUCT_DESCRIPTION_PATH = "pdp";
const FAILURE = "FAILURE";
const CHANNEL = "channel";
const MY_WISH_LIST = "MyWishList";
const CLIENT_ID = "gauravj@dewsolutions.in";

export function getProductDescriptionRequest() {
  return {
    type: PRODUCT_DESCRIPTION_REQUEST,
    status: REQUESTING
  };
}
export function getProductDescriptionSuccess(productDescription) {
  return {
    type: PRODUCT_DESCRIPTION_SUCCESS,
    status: SUCCESS,
    productDescription
  };
}

export function getProductDescriptionFailure(error) {
  return {
    type: PRODUCT_DESCRIPTION_FAILURE,
    status: ERROR,
    error
  };
}
export function getProductDescription() {
  return async (dispatch, getState, { api }) => {
    dispatch(getProductDescriptionRequest());
    try {
      const result = await api.getMock(PRODUCT_DESCRIPTION_PATH);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(getProductDescriptionSuccess(resultJson));
    } catch (e) {
      dispatch(getProductDescriptionFailure(e.message));
    }
  };
}

export function getProductPinCodeRequest() {
  return {
    type: CHECK_PRODUCT_PIN_CODE_REQUEST,
    status: REQUESTING
  };
}
export function getProductPinCodeSuccess(productPinCode) {
  return {
    type: CHECK_PRODUCT_PIN_CODE_SUCCESS,
    status: SUCCESS,
    productPinCode
  };
}

export function getProductPinCodeFailure(error) {
  return {
    type: CHECK_PRODUCT_PIN_CODE_FAILURE,
    status: ERROR,
    error
  };
}

export function getProductPinCode(productDetails) {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(getProductPinCodeRequest());
    try {
      const result = await api.get(
        `${PRODUCT_DETAILS_PATH}/${CLIENT_ID}/checkPincode?access_token=${
          JSON.parse(customerCookie).access_token
        }&pin=${productDetails.pinCode}&productCode=${
          getState().productDescription.productListingId
        }`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(getProductPinCodeSuccess(resultJson));
    } catch (e) {
      dispatch(getProductPinCodeFailure(e.message));
    }
  };
}

export function addProductToWishListRequest() {
  return {
    type: ADD_PRODUCT_TO_WISH_LIST_REQUEST,
    status: REQUESTING
  };
}
export function addProductToWishListSuccess(productDescription) {
  return {
    type: ADD_PRODUCT_TO_WISH_LIST_SUCCESS,
    status: SUCCESS,
    productDescription
  };
}

export function addProductToWishListFailure(error) {
  return {
    type: ADD_PRODUCT_TO_WISH_LIST_FAILURE,
    status: ERROR,
    error
  };
}

export function addProductToWishList() {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(addProductToWishListRequest());
    try {
      const result = await api.post(
        `${PRODUCT_DETAILS_PATH}/${CLIENT_ID}/addProductInWishlist?access_token=${
          JSON.parse(customerCookie).access_token
        }&channel=${CHANNEL}&emailId=${getState().user.userId}&productCode=${
          getState().productDescription.productListingId
        }&ussid=${
          getState().productDescription.ussid
        }&wishlistName=${MY_WISH_LIST}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(addProductToWishListSuccess(resultJson));
    } catch (e) {
      dispatch(addProductToWishListFailure(e.message));
    }
  };
}

export function removeProductFromWishListRequest() {
  return {
    type: REMOVE_PRODUCT_FROM_WISH_LIST_REQUEST,
    status: REQUESTING
  };
}
export function removeProductFromWishListSuccess(productDescription) {
  return {
    type: REMOVE_PRODUCT_FROM_WISH_LIST_SUCCESS,
    status: SUCCESS,
    productDescription
  };
}

export function removeProductFromWishListFailure(error) {
  return {
    type: REMOVE_PRODUCT_FROM_WISH_LIST_FAILURE,
    status: ERROR,
    error
  };
}

export function removeProductFromWishList() {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(removeProductFromWishListRequest());
    try {
      const result = await api.post(
        `${PRODUCT_DETAILS_PATH}/${CLIENT_ID}/removeProductFromWishlist?USSID=${
          getState().productDescription.ussid
        }&access_token=${
          JSON.parse(customerCookie).access_token
        }&channel=${CHANNEL}&wishlistName=${MY_WISH_LIST}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(removeProductFromWishListSuccess(resultJson));
    } catch (e) {
      dispatch(removeProductFromWishListFailure(e.message));
    }
  };
}

export function addProductToBagRequest() {
  return {
    type: ADD_PRODUCT_TO_BAG_REQUEST,
    status: REQUESTING
  };
}
export function addProductToBagSuccess(productDescription) {
  return {
    type: ADD_PRODUCT_TO_BAG_SUCCESS,
    status: SUCCESS,
    productDescription
  };
}

export function addProductToBagFailure(error) {
  return {
    type: ADD_PRODUCT_TO_BAG_FAILURE,
    status: ERROR,
    error
  };
}
export function addProductToBag(products) {
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(addProductToBagRequest());
    try {
      const result = await api.post(
        `${PRODUCT_DETAILS_PATH}/${CLIENT_ID}/carts/15481123719086096-22105306/addProductToCart?USSID=${
          getState().productDescription.ussid
        }&access_token=${
          JSON.parse(customerCookie).access_token
        }&addedToCartWl=true&brandParam=&exchangeParam=&l3code=&pinParam=&productCode=${
          getState().productDescription.productListingId
        }&quantity=${products.count}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(addProductToBagSuccess(resultJson));
    } catch (e) {
      dispatch(addProductToBagFailure(e.message));
    }
  };
}
