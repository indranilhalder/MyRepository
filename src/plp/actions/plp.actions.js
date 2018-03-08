import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
export const PRODUCT_LISTINGS_REQUEST = "PRODUCT_LISTINGS_REQUEST";
export const PRODUCT_LISTINGS_SUCCESS = "PRODUCT_LISTINGS_SUCCESS";
export const PRODUCT_LISTINGS_FAILURE = "PRODUCT_LISTINGS_FAILURE";

export const PRODUCT_LISTINGS_PATH = "v2/mpl/products/serpsearch";
export const PRODUCT_LISTINGS_SUFFIX = "&isPwa=true&pageSize=20&typeID=all";
export const SORT_PRODUCT_LISTINGS_PATH = "serpsearch";
export const FILTER_PRODUCT_LISTINGS_PATH = "serpsearch";
export const GET_PRODUCT_LISTINGS_PAGINATED_SUCCESS =
  "GET_PRODUCT_LISTINGS_PAGINATED_SUCCESS";

export const SET_PAGE = "SET_PAGE";

const FAILURE = "FAILURE";

export function setPage(pageNumber) {
  return {
    type: SET_PAGE,
    pageNumber
  };
}

export function getProductListingsPaginatedSuccess(productListings) {
  return {
    type: GET_PRODUCT_LISTINGS_PAGINATED_SUCCESS,
    productListings
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
export function getProductListings(suffix: null, paginated: false) {
  return async (dispatch, getState, { api }) => {
    dispatch(getProductListingsRequest());
    try {
      const searchState = getState().search;
      const pageNumber = getState().productListings.pageNumber;
      let queryString = `${PRODUCT_LISTINGS_PATH}/?searchText=${
        searchState.string
      }`;

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
      if (paginated) {
        if (resultJson.searchresult) {
          dispatch(getProductListingsPaginatedSuccess(resultJson));
        }
      } else {
        dispatch(getProductListingsSuccess(resultJson));
      }
    } catch (e) {
      dispatch(getProductListingsFailure(e.message));
    }
  };
}
