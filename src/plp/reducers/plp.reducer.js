import * as plpActions from "../actions/plp.actions";
import concat from "lodash.concat";
import cloneDeep from "lodash.clonedeep";
const productListings = (
  state = {
    status: null,
    error: null,
    loading: false,
    productListings: null,
    pageNumber: 0,
    paginatedLoading: false,
    isFilterOpen: false,
    urlToReturnToAfterClear: null,
    filterTabIndex: 0,
    isCategorySelected: true
  },
  action
) => {
  let existingProductListings;
  let toUpdate;
  switch (action.type) {
    case plpActions.SET_FILTER_SELECTED_DATA:
      return Object.assign({}, state, {
        filterTabIndex: action.filterTabIndex,
        isCategorySelected: action.isCategorySelected
      });
    case plpActions.RESET_FILTER_SELECTED_DATA:
      return Object.assign({}, state, {
        filterTabIndex: 0,
        isCategorySelected: true
      });
    case plpActions.SET_URL_TO_RETURN_TO_AFTER_CLEAR:
      return Object.assign({}, state, {
        urlToReturnToAfterClear: action.urlToReturnToAfterClear
      });
    case plpActions.SET_URL_TO_RETURN_TO_AFTER_CLEAR_TO_NULL:
      return Object.assign({}, state, {
        urlToReturnToAfterClear: null
      });
    case plpActions.SHOW_FILTER:
      return Object.assign({}, state, {
        isFilterOpen: true
      });
    case plpActions.HIDE_FILTER:
      return Object.assign({}, state, {
        isFilterOpen: false
      });
    case plpActions.UPDATE_FACETS:
      const productListings = cloneDeep(state.productListings);
      productListings.facetdata = action.productListings.facetdata;
      productListings.facetdatacategory =
        action.productListings.facetdatacategory;
      return Object.assign({}, state, {
        productListings,
        loading: false
      });
    case plpActions.PRODUCT_LISTINGS_REQUEST:
      toUpdate = {
        status: action.status
      };

      if (action.isPaginated) {
        toUpdate.paginatedLoading = true;
        toUpdate.loading = false;
      } else {
        toUpdate.paginatedLoading = false;
        toUpdate.loading = true;
      }
      return Object.assign({}, state, toUpdate);
    case plpActions.PRODUCT_LISTINGS_SUCCESS:
      toUpdate = {
        status: action.status
      };

      if (action.isPaginated) {
        toUpdate.paginatedLoading = true;
        toUpdate.loading = false;
      } else {
        toUpdate.paginatedLoading = false;
        toUpdate.loading = true;
      }
      return Object.assign({}, state, {
        status: action.status,
        productListings: action.productListings,
        loading: false
      });

    case plpActions.PRODUCT_LISTINGS_FAILURE:
      toUpdate = {
        status: action.status
      };

      if (action.isPaginated) {
        toUpdate.paginatedLoading = true;
        toUpdate.loading = false;
      } else {
        toUpdate.paginatedLoading = false;
        toUpdate.loading = true;
      }
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });
    case plpActions.SET_PAGE:
      return Object.assign({}, state, {
        pageNumber: action.pageNumber
      });
    case plpActions.GET_PRODUCT_LISTINGS_PAGINATED_SUCCESS:
      let searchResults = cloneDeep(state.productListings.searchresult);
      searchResults = concat(
        searchResults,
        action.productListings.searchresult
      );
      existingProductListings = cloneDeep(state.productListings);
      existingProductListings.searchresult = searchResults;
      return Object.assign({}, state, {
        productListings: existingProductListings
      });
    default:
      return state;
  }
};

export default productListings;
