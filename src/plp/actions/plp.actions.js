import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
export const PRODUCT_LISTINGS_REQUEST = "PRODUCT_LISTINGS_REQUEST";
export const PRODUCT_LISTINGS_SUCCESS = "PRODUCT_LISTINGS_SUCCESS";
export const PRODUCT_LISTINGS_FAILURE = "PRODUCT_LISTINGS_FAILURE";
export const PRODUCT_LISTINGS_PATH = "serpsearch";
const FAILURE = "FAILURE";
export function productListingsRequest() {
  return {
    type: PRODUCT_LISTINGS_REQUEST,
    status: REQUESTING
  };
}
export function productListingsSuccess(products) {
  return {
    type: PRODUCT_LISTINGS_SUCCESS,
    status: SUCCESS,
    products
  };
}

export function productListingsFailure(error) {
  return {
    type: PRODUCT_LISTINGS_FAILURE,
    status: ERROR,
    error
  };
}
export function getProducts() {
  return async (dispatch, getState, { api }) => {
    dispatch(productListingsRequest());
    try {
      const result = await api.get(PRODUCT_LISTINGS_PATH);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(productListingsSuccess(resultJson));
    } catch (e) {
      dispatch(productListingsFailure(e.message));
    }
  };
}
