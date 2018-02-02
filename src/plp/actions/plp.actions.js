import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
export const PRODUCT_LISTINGS_REQUEST = "PRODUCT_LISTINGS_REQUEST";
export const PRODUCT_LISTINGS_SUCCESS = "PRODUCT_LISTINGS_SUCCESS";
export const PRODUCT_LISTINGS_FAILURE = "PRODUCT_LISTINGS_FAILURE";
export const PRODUCT_LISTINGS_PATH = "serpsearch";
const FAILURE = "FAILURE";
export function getProductListingsRequest() {
  return {
    type: PRODUCT_LISTINGS_REQUEST,
    status: REQUESTING
  };
}
export function getProductListingsSuccess(productListings) {
  return {
    type: PRODUCT_LISTINGS_SUCCESS,
    status: SUCCESS,
    productListings
  };
}

export function getProductListingsFailure(error) {
  return {
    type: PRODUCT_LISTINGS_FAILURE,
    status: ERROR,
    error
  };
}
export function getProductListings() {
  return async (dispatch, getState, { api }) => {
    dispatch(getProductListingsRequest());
    try {
      const result = await api.get(PRODUCT_LISTINGS_PATH);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(getProductListingsSuccess(resultJson));
    } catch (e) {
      dispatch(getProductListingsFailure(e.message));
    }
  };
}
