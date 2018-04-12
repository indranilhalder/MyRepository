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
    paginatedLoading: false
  },
  action
) => {
  let existingProductListings;
  let toUpdate;
  switch (action.type) {
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
