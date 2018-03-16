import * as accountActions from "../actions/account.actions";
const cart = (
  state = {
    status: null,
    error: null,
    loading: false,
    type: null,

    orderDetails: null,
    orderDetailsStatus: null,
    orderDetailsError: null
  },
  action
) => {
  switch (action.type) {
    case accountActions.GET_ALL_ORDERS_REQUEST:
      return Object.assign({}, state, {
        orderDetailsStatus: action.status,
        loading: true
      });

    case accountActions.GET_ALL_ORDERS_SUCCESS:
      return Object.assign({}, state, {
        orderDetailsStatus: action.status,
        orderDetails: action.orderDetails,
        loading: false
      });

    case accountActions.GET_ALL_ORDERS_FAILURE:
      return Object.assign({}, state, {
        orderDetailsStatus: action.status,
        orderDetailsError: action.error,
        loading: false
      });
    default:
      return state;
  }
};

export default cart;
