import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import each from "lodash/each";
import { SET_FILTERS } from "../../search/actions/search.actions";
export const PRODUCT_LISTINGS_REQUEST = "PRODUCT_LISTINGS_REQUEST";
export const PRODUCT_LISTINGS_SUCCESS = "PRODUCT_LISTINGS_SUCCESS";
export const PRODUCT_LISTINGS_FAILURE = "PRODUCT_LISTINGS_FAILURE";

export const PRODUCT_LISTINGS_PATH = "v2/mpl/products/serpsearch";
export const PRODUCT_LISTINGS_SUFFIX = "&isPwa=true&pageSize=20&typeID=all";
export const SORT_PRODUCT_LISTINGS_PATH = "serpsearch";
export const FILTER_PRODUCT_LISTINGS_PATH = "serpsearch";

export const SET_PAGE = "SET_PAGE";

const FAILURE = "FAILURE";

export function setPage(pageNumber) {
  return {
    type: SET_PAGE,
    pageNumber
  };
}
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
export function getProductListings(suffix: null) {
  return async (dispatch, getState, { api }) => {
    dispatch(getProductListingsRequest());
    try {
      const searchState = getState().search;
      const pageNumber = getState().productListings.pageNumber;
      let searchString = searchState.string;
      const filters = searchState.filters;

      // TODO brand or category filters must always come first.
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

        if (sortString.length > 0) {
          queryString = `${queryString}:${sortString}`;
        }
        if (filterString.length > 0) {
          queryString = `${queryString}:${filterString}`;
        }
      }

      if (suffix) {
        queryString = `${queryString}${suffix}`;
      }
      queryString = `${queryString}&page=${pageNumber}`;
      queryString = `${queryString}${PRODUCT_LISTINGS_SUFFIX}`;

      const result = await api.get(queryString);
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
