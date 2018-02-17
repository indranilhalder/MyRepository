import * as cartActions from "../actions/cart.actions";
const cart = (
  state = {
    status: null,
    error: null,
    loading: false,

    userCart: null,
    userCartStatus: null,
    userCartError: null,

    cartDetails: null,
    cartDetailsStatus: null,
    cartDetailsError: null,

    couponStatus: null,
    couponError: null,

    deliveryModes: null,
    userAddress: null,
    netBankDetails: null,
    emiBankDetails: null
  },
  action
) => {
  switch (action.type) {
    case cartActions.PRODUCT_CART_DETAILS_REQUEST:
      return Object.assign({}, state, {
        cartDetailsStatus: action.status,
        loading: true
      });

    case cartActions.PRODUCT_CART_DETAILS_SUCCESS:
      return Object.assign({}, state, {
        cartDetailsStatus: action.status,
        cartDetails: action.cartDetails,
        loading: false
      });

    case cartActions.PRODUCT_CART_DETAILS_FAILURE:
      return Object.assign({}, state, {
        cartDetailsStatus: action.status,
        cartDetailsError: action.error,
        loading: false
      });

    case cartActions.USER_CART_REQUEST:
      return Object.assign({}, state, {
        userCartStatus: action.status,
        loading: true
      });

    case cartActions.USER_CART_SUCCESS:
      return Object.assign({}, state, {
        userCartStatus: action.status,
        userCart: action.userCart,
        loading: false
      });

    case cartActions.USER_CART_FAILURE:
      return Object.assign({}, state, {
        userCartStatus: action.status,
        userCartError: action.error,
        loading: false
      });
    case cartActions.PRODUCT_CART_REQUEST:
      return Object.assign({}, state, {
        cartDetailsStatus: action.status,
        loading: true
      });

    case cartActions.PRODUCT_CART_SUCCESS:
      return Object.assign({}, state, {
        cartDetailsStatus: action.status,
        cartDetails: action.cartDetails,
        loading: false
      });

    case cartActions.PRODUCT_CART_FAILURE:
      return Object.assign({}, state, {
        cartDetailsStatus: action.status,
        cartDetailsError: action.error,
        loading: false
      });

    case cartActions.APPLY_COUPON_REQUEST:
      return Object.assign({}, state, {
        couponStatus: action.status,
        loading: true
      });

    case cartActions.APPLY_COUPON_SUCCESS:
      return Object.assign({}, state, {
        couponStatus: action.status,
        loading: false
      });

    case cartActions.APPLY_COUPON_FAILURE:
      return Object.assign({}, state, {
        couponStatus: action.status,
        couponError: action.error,
        loading: false
      });

    case cartActions.SELECT_DELIVERY_MODES_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case cartActions.SELECT_DELIVERY_MODES_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        deliveryModes: action.deliveryModes,
        loading: false
      });

    case cartActions.SELECT_DELIVERY_MODES_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case cartActions.GET_USER_ADDRESS_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case cartActions.GET_USER_ADDRESS_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        userAddress: action.userDeliveryAddress,
        loading: false
      });

    case cartActions.GET_USER_ADDRESS_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case cartActions.NET_BANKING_DETAILS_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case cartActions.NET_BANKING_DETAILS_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        netBankDetails: action.netBankDetails,
        loading: false
      });

    case cartActions.NET_BANKING_DETAILS_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case cartActions.EMI_BANKING_DETAILS_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case cartActions.EMI_BANKING_DETAILS_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        emiBankDetails: action.emiBankDetails,
        loading: false
      });

    case cartActions.EMI_BANKING_DETAILS_FAILURE:
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
