import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";
export const PRODUCT_LISTINGS_REQUEST = "PRODUCT_LISTINGS_REQUEST";
export const PRODUCT_LISTINGS_SUCCESS = "PRODUCT_LISTINGS_SUCCESS";
export const PRODUCT_LISTINGS_FAILURE = "PRODUCT_LISTINGS_FAILURE";

export const PRODUCT_LISTINGS_PATH = "v2/mpl/products/searchProducts";
export const PRODUCT_LISTINGS_SUFFIX = "&isPwa=true&pageSize=20&typeID=all";
export const SORT_PRODUCT_LISTINGS_PATH = "searchProducts";
export const FILTER_PRODUCT_LISTINGS_PATH = "searchProducts";
export const GET_PRODUCT_LISTINGS_PAGINATED_SUCCESS =
  "GET_PRODUCT_LISTINGS_PAGINATED_SUCCESS";

export const UPDATE_FACETS = "UPDATE_FACETS";

export const SET_PAGE = "SET_PAGE";

const FAILURE = "FAILURE";

export function setPage(pageNumber) {
  return {
    type: SET_PAGE,
    pageNumber
  };
}

export function updateFacets(productListings) {
  return {
    type: UPDATE_FACETS,
    status: SUCCESS,
    productListings
  };
}

export function getProductListingsPaginatedSuccess(productListings) {
  return {
    type: GET_PRODUCT_LISTINGS_PAGINATED_SUCCESS,
    productListings
  };
}
export function getProductListingsRequest(paginated: false) {
  return {
    type: PRODUCT_LISTINGS_REQUEST,
    status: REQUESTING,

    isPaginated: paginated
  };
}
export function getProductListingsSuccess(productListings, isPaginated: false) {
  return {
    type: PRODUCT_LISTINGS_SUCCESS,
    status: SUCCESS,
    productListings,
    isPaginated
  };
}

export function getProductListingsFailure(error, isPaginated) {
  return {
    type: PRODUCT_LISTINGS_FAILURE,
    status: ERROR,
    error,
    isPaginated
  };
}
export function getProductListings(
  suffix: null,
  paginated: false,
  isFilter: false
) {
  return async (dispatch, getState, { api }) => {
    dispatch(getProductListingsRequest(paginated));
    dispatch(showSecondaryLoader());
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
      if (resultJson.error) {
        throw new Error(`${resultJson.error}`);
      }
      if (paginated) {
        if (resultJson.searchresult) {
          dispatch(getProductListingsPaginatedSuccess(resultJson, true));
          dispatch(hideSecondaryLoader());
        }
      } else if (isFilter) {
        dispatch(updateFacets(resultJson));
        dispatch(hideSecondaryLoader());
      } else {
        dispatch(getProductListingsSuccess(resultJson, paginated));
        dispatch(hideSecondaryLoader());
      }
    } catch (e) {
      dispatch(getProductListingsFailure(e.message, paginated));
      dispatch(hideSecondaryLoader());
    }
  };
}
