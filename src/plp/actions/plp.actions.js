import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
export const PRODUCT_LISTINGS_REQUEST = "PRODUCT_LISTINGS_REQUEST";
export const PRODUCT_LISTINGS_SUCCESS = "PRODUCT_LISTINGS_SUCCESS";
export const PRODUCT_LISTINGS_FAILURE = "PRODUCT_LISTINGS_FAILURE";
export const SORT_PRODUCT_LISTINGS_REQUEST = "SORT_PRODUCT_LISTINGS_REQUEST";
export const SORT_PRODUCT_LISTINGS_SUCCESS = "SORT_PRODUCT_LISTINGS_SUCCESS";
export const SORT_PRODUCT_LISTINGS_FAILURE = "SORT_PRODUCT_LISTINGS_FAILURE";
export const FILTER_PRODUCT_LISTINGS_REQUEST =
  "FILTER_PRODUCT_LISTINGS_REQUEST";
export const FILTER_PRODUCT_LISTINGS_SUCCESS =
  "FILTER_PRODUCT_LISTINGS_SUCCESS";
export const FILTER_PRODUCT_LISTINGS_FAILURE =
  "FILTER_PRODUCT_LISTINGS_FAILURE";

export const PRODUCT_LISTINGS_PATH = "serpsearch";
export const SORT_PRODUCT_LISTINGS_PATH = "serpsearch";
export const FILTER_PRODUCT_LISTINGS_PATH = "serpsearch";
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

export function getSortedProductListingsRequest() {
  return {
    type: SORT_PRODUCT_LISTINGS_REQUEST,
    status: REQUESTING
  };
}
export function getSortedProductListingsSuccess(productListings) {
  return {
    type: SORT_PRODUCT_LISTINGS_SUCCESS,
    status: SUCCESS,
    productListings
  };
}

export function getSortedProductListingsFailure(error) {
  return {
    type: SORT_PRODUCT_LISTINGS_FAILURE,
    status: ERROR,
    error
  };
}
export function getSortedProductListings(val) {
  return async (dispatch, getState, { api }) => {
    dispatch(getSortedProductListingsRequest());
    try {
      const result = await api.get(SORT_PRODUCT_LISTINGS_PATH);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(getSortedProductListingsSuccess(resultJson));
    } catch (e) {
      dispatch(getSortedProductListingsFailure(e.message));
    }
  };
}

export function getFilteredProductListingsRequest() {
  return {
    type: FILTER_PRODUCT_LISTINGS_REQUEST,
    status: REQUESTING
  };
}
export function getFilteredProductListingsSuccess(productListings) {
  return {
    type: FILTER_PRODUCT_LISTINGS_SUCCESS,
    status: SUCCESS,
    productListings
  };
}

export function getFilteredProductListingsFailure(error) {
  return {
    type: FILTER_PRODUCT_LISTINGS_FAILURE,
    status: ERROR,
    error
  };
}
export function getFilteredProductListings(val) {
  return async (dispatch, getState, { api }) => {
    dispatch(getFilteredProductListingsRequest());
    try {
      const result = await api.get(FILTER_PRODUCT_LISTINGS_PATH);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(getFilteredProductListingsSuccess(resultJson));
    } catch (e) {
      dispatch(getFilteredProductListingsFailure(e.message));
    }
  };
}
