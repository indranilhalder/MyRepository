import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";
import {
  setDataLayer,
  ADOBE_PLP_TYPE,
  ADOBE_INTERNAL_SEARCH_CALL_ON_GET_PRODUCT,
  ADOBE_INTERNAL_SEARCH_CALL_ON_GET_NULL
} from "../../lib/adobeUtils";
export const PRODUCT_LISTINGS_REQUEST = "PRODUCT_LISTINGS_REQUEST";
export const PRODUCT_LISTINGS_SUCCESS = "PRODUCT_LISTINGS_SUCCESS";
export const PRODUCT_LISTINGS_FAILURE = "PRODUCT_LISTINGS_FAILURE";

export const PRODUCT_LISTINGS_PATH = "v2/mpl/products/searchProducts";
export const PRODUCT_LISTINGS_SUFFIX = "&isPwa=true&pageSize=20&typeID=all";
export const SORT_PRODUCT_LISTINGS_PATH = "searchProducts";
export const FILTER_PRODUCT_LISTINGS_PATH = "searchProducts";
export const GET_PRODUCT_LISTINGS_PAGINATED_SUCCESS =
  "GET_PRODUCT_LISTINGS_PAGINATED_SUCCESS";

export const SHOW_FILTER = "SHOW_FILTER";
export const HIDE_FILTER = "HIDE_FILTER";

export const SET_FILTER_SELECTED_DATA = "SET_FILTER_SELECTED_DATA";
export const RESET_FILTER_SELECTED_DATA = "RESET_FILTER_SELECTED_DATA";

export const UPDATE_FACETS = "UPDATE_FACETS";

export const SET_PAGE = "SET_PAGE";

export const FILTER_HAS_BEEN_CLICKED = "FILTER_HAS_BEEN_CLICKED";
export const SORT_HAS_BEEN_CLICKED = "SORT_HAS_BEEN_CLICKED";

export const IS_GO_BACK_FROM_PDP = "IS_GO_BACK_FROM_PDP";
export const IS_NOT_GO_BACK_FROM_PDP = "IS_NOT_GO_BACK_FROM_PDP";

export const SET_PRODUCT_MODULE_REF = "SET_PRODUCT_MODULE_REF";
export const CLEAR_PRODUCT_MODULE_REF = "CLEAR_PRODUCT_MODULE_REF";

export function setProductModuleRef(ref) {
  return {
    type: SET_PRODUCT_MODULE_REF,
    ref
  };
}

export function clearProductModuleRef() {
  return {
    type: CLEAR_PRODUCT_MODULE_REF
  };
}
export function setIsGoBackFromPDP() {
  return {
    type: IS_GO_BACK_FROM_PDP
  };
}

export function setIsNotGoBackFromPDP() {
  return {
    type: IS_NOT_GO_BACK_FROM_PDP
  };
}

export function setIfSortHasBeenClicked() {
  return {
    type: SORT_HAS_BEEN_CLICKED
  };
}

export function setIfFilterHasBeenClicked() {
  return {
    type: FILTER_HAS_BEEN_CLICKED
  };
}

export function setFilterSelectedData(isCategorySelected, filterTabIndex) {
  return {
    type: SET_FILTER_SELECTED_DATA,
    isCategorySelected,
    filterTabIndex
  };
}

export function resetFilterSelectedData() {
  return {
    type: RESET_FILTER_SELECTED_DATA
  };
}

export function showFilter() {
  return {
    type: SHOW_FILTER
  };
}

export function hideFilter() {
  return {
    type: HIDE_FILTER
  };
}

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
export function getProductListingsRequest(paginated: false, isFilter: false) {
  return {
    type: PRODUCT_LISTINGS_REQUEST,
    status: REQUESTING,
    isFilter,
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
    dispatch(getProductListingsRequest(paginated, isFilter));
    dispatch(showSecondaryLoader());
    try {
      const searchState = getState().search;
      const pageNumber = getState().productListings.pageNumber;
      const encodedString =
        searchState.string.includes("%3A") || searchState.string.includes("%20")
          ? searchState.string
          : encodeURI(searchState.string);

      let queryString = `${PRODUCT_LISTINGS_PATH}/?searchText=${encodedString}`;

      if (suffix) {
        queryString = `${queryString}${suffix}`;
      }
      queryString = `${queryString}&page=${pageNumber}`;
      queryString = `${queryString}${PRODUCT_LISTINGS_SUFFIX}`;
      const result = await api.getMiddlewareUrl(queryString);
      const resultJson = await result.json();
      if (resultJson.error) {
        if (
          resultJson &&
          resultJson.currentQuery &&
          resultJson.currentQuery.searchQuery
        ) {
          setDataLayer(
            ADOBE_INTERNAL_SEARCH_CALL_ON_GET_NULL,
            resultJson,
            getState().icid.value,
            getState().icid.icidType
          );
        }
        throw new Error(`${resultJson.error}`);
      }
      if (
        resultJson &&
        resultJson.currentQuery &&
        resultJson.currentQuery.searchQuery
      ) {
        setDataLayer(
          ADOBE_INTERNAL_SEARCH_CALL_ON_GET_PRODUCT,
          resultJson,
          getState().icid.value,
          getState().icid.icidType
        );
      } else {
        if (
          window.digitalData &&
          window.digitalData.page &&
          window.digitalData.page.pageInfo &&
          window.digitalData.page.pageInfo.pageName !== "product grid"
        ) {
          setDataLayer(
            ADOBE_PLP_TYPE,
            resultJson,
            getState().icid.value,
            getState().icid.icidType
          );
        }
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
