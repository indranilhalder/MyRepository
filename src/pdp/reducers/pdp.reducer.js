import * as pdpActions from "../actions/pdp.actions";
const productDescription = (
  state = {
    status: null,
    error: null,
    loading: false,
    productDetails: null
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
        productDetails: action.productDescription,
        loading: false
      });

    case pdpActions.PRODUCT_DESCRIPTION_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case pdpActions.CHECK_PRODUCT_PIN_CODE_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.CHECK_PRODUCT_PIN_CODE_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        productDetails: action.productDescription,
        loading: false
      });

    case pdpActions.CHECK_PRODUCT_PIN_CODE_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case pdpActions.ADD_PRODUCT_TO_WISH_LIST_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.ADD_PRODUCT_TO_WISH_LIST_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        productDetails: action.productDescription,
        loading: false
      });

    case pdpActions.ADD_PRODUCT_TO_WISH_LIST_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case pdpActions.REMOVE_PRODUCT_FROM_WISH_LIST_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.REMOVE_PRODUCT_FROM_WISH_LIST_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        productDetails: action.productDescription,
        loading: false
      });

    case pdpActions.REMOVE_PRODUCT_FROM_WISH_LIST_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case pdpActions.ADD_PRODUCT_TO_BAG_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.ADD_PRODUCT_TO_BAG_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        productDetails: action.productDescription,
        loading: false
      });

    case pdpActions.ADD_PRODUCT_TO_BAG_FAILURE:
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
