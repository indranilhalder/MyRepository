import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import { FAILURE } from "../../lib/constants";
export const PRODUCT_CART_REQUEST = "PRODUCT_CART_REQUEST";
export const PRODUCT_CART_SUCCESS = "PRODUCT_CART_SUCCESS";
export const PRODUCT_CART_FAILURE = "PRODUCT_CART_FAILURE";
export const PRODUCT_CART_PATH = "1f1ob9";

export function getProductCartRequest() {
  return {
    type: PRODUCT_CART_REQUEST,
    status: REQUESTING
  };
}
export function getProductCartSuccess(cartDetails) {
  return {
    type: PRODUCT_CART_SUCCESS,
    status: SUCCESS,
    cartDetails
  };
}

export function getProductCartFailure(error) {
  return {
    type: PRODUCT_CART_FAILURE,
    status: ERROR,
    error
  };
}
export function getProductCart() {
  return async (dispatch, getState, { api }) => {
    dispatch(getProductCartRequest());
    try {
      const result = await api.getMockFromMyJson(PRODUCT_CART_PATH);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(getProductCartSuccess(resultJson));
    } catch (e) {
      dispatch(getProductCartFailure(e.message));
    }
  };
}
