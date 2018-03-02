import * as plpActions from "../actions/plp.actions";
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
    default:
      return state;
  }
};

export default productListings;
