import * as pdpActions from "../actions/pdp.actions";
const productDescription = (
  state = {
    status: null,
    error: null,
    loading: false,
    productDescription: null
  },
  action
) => {
  switch (action.type) {
    case pdpActions.PRODUCT_DESCRIPTION_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.PRODUCT_DESCRIPTION_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        productDescription: action.productDescription,
        loading: false
      });

    case pdpActions.PRODUCT_DESCRIPTION_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    default:
      return state;
  }
};

export default productDescription;
