import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import each from "lodash/each";
export const PRODUCT_LISTINGS_REQUEST = "PRODUCT_LISTINGS_REQUEST";
export const PRODUCT_LISTINGS_SUCCESS = "PRODUCT_LISTINGS_SUCCESS";
export const PRODUCT_LISTINGS_FAILURE = "PRODUCT_LISTINGS_FAILURE";

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
      const searchState = getState().search;
      let searchString = searchState.string;
      const filters = searchState.filters;
      const sortString = searchState.sort;
      let filterString = "";
      let queryString = `${PRODUCT_LISTINGS_PATH}`;
      each(filters, filterObj => {
        const key = filterObj.key;
        const filterValues = filterObj.filters;
        each(filterValues, filterValue => {
          if (filterString.length === 0) {
            filterString = `${filterString}${key}:${filterValue}`;
          } else {
            filterString = `${filterString}:${key}:${filterValue}`;
          }
        });
      });
      if (
        sortString.length !== 0 ||
        filterString.length !== 0 ||
        searchString.length !== 0
      ) {
        queryString = `${queryString}?searchText=`;
        if (searchString.length > 0) {
          queryString = `${queryString}${searchString}`;
        }
        if (filterString.length > 0) {
          queryString = `${queryString}:${filterString}`;
        }
        if (sortString.length > 0) {
          queryString = `${queryString}:${sortString}`;
        }
      }

      const result = await api.getMock(queryString);
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
