import * as cartActions from "../actions/cart.actions";
const cart = (
  state = {
    status: null,
    error: null,
    loading: false,
    cartDetails: null
  },
  action
) => {
  switch (action.type) {
    case cartActions.PRODUCT_CART_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case cartActions.PRODUCT_CART_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        cartDetails: action.cartDetails,
        loading: false
      });

    case cartActions.PRODUCT_CART_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });
    default:
      return state;
  }
};

export default cart;
