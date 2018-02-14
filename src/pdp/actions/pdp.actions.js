import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";

import { CUSTOMER_ACCESS_TOKEN, FAILURE } from "../../lib/constants";
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

export const PRODUCT_SIZE_GUIDE_REQUEST = "PRODUCT_SIZE_GUIDE_REQUEST";
export const PRODUCT_SIZE_GUIDE_SUCCESS = "PRODUCT_SIZE_GUIDE_SUCCESS";
export const PRODUCT_SIZE_GUIDE_FAILURE = "PRODUCT_SIZE_GUIDE_FAILURE";

export const PRODUCT_PDP_EMI_REQUEST = "PRODUCT_PDP_EMI_REQUEST";
export const PRODUCT_PDP_EMI_SUCCESS = "PRODUCT_PDP_EMI_SUCCESS";
export const PRODUCT_PDP_EMI_FAILURE = "PRODUCT_PDP_EMI_FAILURE";

export const PRODUCT_WISH_LIST_REQUEST = "PRODUCT_WISH_LIST_REQUEST";
export const PRODUCT_WISH_LIST_SUCCESS = "PRODUCT_WISH_LIST_SUCCESS";
export const PRODUCT_WISH_LIST_FAILURE = "PRODUCT_WISH_LIST_FAILURE";
export const PRODUCT_WISH_LIST_PATH = "wishList";

export const PRODUCT_SPECIFICATION_REQUEST = "PRODUCT_SPECIFICATION_REQUEST";
export const PRODUCT_SPECIFICATION_SUCCESS = "PRODUCT_SPECIFICATION_SUCCESS";
export const PRODUCT_SPECIFICATION_FAILURE = "PRODUCT_SPECIFICATION_FAILURE";

export const PRODUCT_DETAILS_PATH = "v2/mpl/users";
export const PIN_CODE_AVAILABILITY_PATH = "pincodeserviceability";
export const PRODUCT_DESCRIPTION_PATH = "pdp";
export const PRODUCT_SIZE_GUIDE_PATH = "sizeGuide";
export const PRODUCT_PDP_EMI_PATH = "pdpEMI";
const CHANNEL = "channel";
const MY_WISH_LIST = "MyWishList";
const CLIENT_ID = "gauravj@dewsolutions.in";
const ADD_PRODUCT_TO_WISH_LIST = "addToWishListInPDP";
const ADD_PRODUCT_TO_CART = "addProductToCart";
const REMOVE_FROM_WISH_LIST = "removeFromWl";
const PRODUCT_SPECIFICATION_PATH = "marketplacewebservices/v2/mpl/products";

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
  return async (dispatch, getState, { api }) => {
    dispatch(getProductPinCodeRequest());
    try {
      const result = await api.getMock(PIN_CODE_AVAILABILITY_PATH);
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

export function addProductToWishList(productDetails) {
  return async (dispatch, getState, { api }) => {
    dispatch(addProductToWishListRequest());
    try {
      const result = await api.postMock(ADD_PRODUCT_TO_WISH_LIST);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }

      // TODO: dispatch a modal here
      dispatch(addProductToWishListSuccess());
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
export function removeProductFromWishListSuccess() {
  return {
    type: REMOVE_PRODUCT_FROM_WISH_LIST_SUCCESS,
    status: SUCCESS
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
    dispatch(removeProductFromWishListRequest());
    try {
      const result = await api.postMock(REMOVE_FROM_WISH_LIST);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(removeProductFromWishListSuccess());
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
export function addProductToBagSuccess() {
  return {
    type: ADD_PRODUCT_TO_BAG_SUCCESS,
    status: SUCCESS
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
  return async (dispatch, getState, { api }) => {
    dispatch(addProductToBagRequest());
    try {
      const result = await api.postMock(ADD_PRODUCT_TO_CART);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(addProductToBagSuccess());
    } catch (e) {
      dispatch(addProductToBagFailure(e.message));
    }
  };
}

export function getProductSizeGuideRequest() {
  return {
    type: PRODUCT_SIZE_GUIDE_REQUEST,
    status: REQUESTING
  };
}
export function getProductSizeGuideSuccess(sizeGuide) {
  return {
    type: PRODUCT_SIZE_GUIDE_SUCCESS,
    status: SUCCESS,
    sizeGuide
  };
}

export function getProductSizeGuideFailure(error) {
  return {
    type: PRODUCT_SIZE_GUIDE_FAILURE,
    status: ERROR,
    error
  };
}
export function getProductSizeGuide() {
  return async (dispatch, getState, { api }) => {
    dispatch(getProductSizeGuideRequest());
    try {
      const result = await api.postMock(PRODUCT_SIZE_GUIDE_PATH);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(getProductSizeGuideSuccess(resultJson));
    } catch (e) {
      dispatch(getProductSizeGuideFailure(e.message));
    }
  };
}

export function getPdpEmiRequest() {
  return {
    type: PRODUCT_PDP_EMI_REQUEST,
    status: REQUESTING
  };
}
export function getPdpEmiSuccess(emiResult) {
  return {
    type: PRODUCT_PDP_EMI_SUCCESS,
    status: SUCCESS,
    emiResult
  };
}

export function getPdpEmiFailure(error) {
  return {
    type: PRODUCT_PDP_EMI_FAILURE,
    status: ERROR,
    error
  };
}
export function getPdpEmi() {
  return async (dispatch, getState, { api }) => {
    dispatch(getPdpEmiRequest());
    try {
      const result = await api.postMock(PRODUCT_PDP_EMI_PATH);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(getPdpEmiSuccess(resultJson));
    } catch (e) {
      dispatch(getPdpEmiFailure(e.message));
    }
  };
}

export function getProductWishListRequest() {
  return {
    type: PRODUCT_WISH_LIST_REQUEST,
    status: REQUESTING
  };
}
export function getProductWishListSuccess(wishList) {
  return {
    type: PRODUCT_WISH_LIST_SUCCESS,
    status: SUCCESS,
    wishList
  };
}

export function getProductWishListFailure(error) {
  return {
    type: PRODUCT_WISH_LIST_FAILURE,
    status: ERROR,
    error
  };
}
export function getProductWishList() {
  return async (dispatch, getState, { api }) => {
    dispatch(getProductWishListRequest());
    try {
      const result = await api.postMock(PRODUCT_WISH_LIST_PATH);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(getProductWishListSuccess(resultJson));
    } catch (e) {
      dispatch(getProductWishListFailure(e.message));
    }
  };
}

export function getProductSpecificationRequest() {
  return {
    type: PRODUCT_SPECIFICATION_REQUEST,
    status: REQUESTING
  };
}
export function getProductSpecificationSuccess(productDetails) {
  return {
    type: PRODUCT_SPECIFICATION_SUCCESS,
    status: SUCCESS,
    productDetails
  };
}

export function getProductSpecificationFailure(error) {
  return {
    type: PRODUCT_SPECIFICATION_FAILURE,
    status: ERROR,
    error
  };
}
export function getProductSpecification(ProductId) {
  return async (dispatch, getState, { api }) => {
    dispatch(getProductSpecificationRequest());
    try {
      const result = await api.getMock(
        `${PRODUCT_SPECIFICATION_PATH}/${ProductId}`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(getProductSpecificationSuccess(resultJson));
    } catch (e) {
      dispatch(getProductSpecificationFailure(e.message));
    }
  };
}
