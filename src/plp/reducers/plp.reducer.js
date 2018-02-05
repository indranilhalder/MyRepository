import * as plpActions from "../actions/plp.actions";
const productListings = (
  state = {
    status: null,
    error: null,
    loading: false,
    productListings: null
  },
  action
) => {
  switch (action.type) {
    case plpActions.PRODUCT_LISTINGS_REQUEST:
    case plpActions.SORT_PRODUCT_LISTINGS_REQUEST:
    case plpActions.FILTER_PRODUCT_LISTINGS_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });
    case plpActions.PRODUCT_LISTINGS_SUCCESS:
    case plpActions.SORT_PRODUCT_LISTINGS_SUCCESS:
    case plpActions.FILTER_PRODUCT_LISTINGS_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        productListings: action.productListings,
        loading: false
      });

    case plpActions.PRODUCT_LISTINGS_FAILURE:
    case plpActions.SORT_PRODUCT_LISTINGS_FAILURE:
    case plpActions.FILTER_PRODUCT_LISTINGS_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });
    default:
      return state;
  }
};

export default productListings;
