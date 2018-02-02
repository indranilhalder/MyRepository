import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
export const PRODUCT_LISTING_REQUEST = "PRODUCT_LISTING_REQUEST";
export const PRODUCT_LISTING_SUCCESS = "PRODUCT_LISTING_SUCCESS";
export const PRODUCT_LISTING_FAILURE = "PRODUCT_LISTING_FAILURE";
export const PRODUCT_LISTING_PATH = "serpsearch";
const FAILURE = "FAILURE";
export function productListingRequest() {
  return {
    type: PRODUCT_LISTING_REQUEST,
    status: REQUESTING
  };
}
export function productListingSuccess(productListings) {
  return {
    type: PRODUCT_LISTING_SUCCESS,
    status: SUCCESS,
    productListings
  };
}

export function productListingFailure(error) {
  return {
    type: PRODUCT_LISTING_FAILURE,
    status: ERROR,
    error
  };
}
export function getProducts() {
  return async (dispatch, getState, { api }) => {
    dispatch(productListingRequest());
    try {
      const result = await api.get(PRODUCT_LISTING_PATH);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(productListingSuccess(resultJson));
    } catch (e) {
      dispatch(productListingFailure(e.message));
    }
  };
}
