import * as plpActions from "../actions/plp.actions";
import concat from "lodash/concat";
import cloneDeep from "lodash/cloneDeep";
const productListings = (
  state = {
    status: null,
    error: null,
    loading: false,
    productListings: null,
    pageNumber: 0
  },
  action
) => {
  let existingProductListings;

  switch (action.type) {
    case plpActions.PRODUCT_LISTINGS_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });
    case plpActions.PRODUCT_LISTINGS_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        productListings: action.productListings,
        loading: false
      });

    case plpActions.PRODUCT_LISTINGS_FAILURE:
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
