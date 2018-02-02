import * as plpActions from "../actions/plp.actions";
const products = (
  state = {
    status: null,
    error: null,
    loading: false,
    products: null
  },
  action
) => {
  switch (action.type) {
    case plpActions.PRODUCT_LISTING_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case plpActions.PRODUCT_LISTING_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        products: action.products,
        loading: false
      });

    case plpActions.PRODUCT_LISTING_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    default:
      return state;
  }
};

export default products;
