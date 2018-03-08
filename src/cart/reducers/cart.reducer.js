import * as cartActions from "../actions/cart.actions";
import * as Cookies from "../../lib/Cookie";
import {
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS
} from "../../lib/constants";
const cart = (
  state = {
    status: null,
    error: null,
    loading: false,
    type: null,

    userCart: null,
    userCartStatus: null,
    userCartError: null,

    cartDetails: null,
    cartDetailsStatus: null,
    cartDetailsError: null,

    cartDetailsCNC: null,
    cartDetailsCNCStatus: null,
    cartDetailsCNCError: null,

    couponStatus: null,
    couponError: null,

    deliveryModes: null,
    userAddress: null,
    setAddress: null,
    netBankDetails: null,
    emiBankDetails: null,

    orderSummary: null,
    orderSummaryStatus: null,
    orderSummaryError: null,
    coupons: null,

    storeDetails: null,
    storeStatus: null,
    storeError: null,
    storeAdded: null,

    softReserve: null,
    softReserveStatus: null,
    softReserveError: null,

    paymentModes: null,
    paymentModesStatus: null,
    paymentModesError: null,

    bankOffer: null,
    bankOfferStatus: null,
    bankOfferError: null,

    paymentStatus: null,
    paymentStatusError: null,
    paymentDetails: null
  },
  action
) => {
  switch (action.type) {
    case cartActions.CART_DETAILS_REQUEST:
      return Object.assign({}, state, {
        cartDetailsStatus: action.status,
        loading: true
      });

    case cartActions.CART_DETAILS_SUCCESS:
      return Object.assign({}, state, {
        cartDetailsStatus: action.status,
        cartDetails: action.cartDetails,
        loading: false
      });

    case cartActions.CART_DETAILS_FAILURE:
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

    case cartActions.GET_COUPON_REQUEST:
      return Object.assign({}, state, {
        couponStatus: action.status,
        loading: true
      });

    case cartActions.GET_COUPON_SUCCESS:
      return Object.assign({}, state, {
        couponStatus: action.status,
        loading: false,
        coupons: action.coupons
      });

    case cartActions.GET_COUPON_FAILURE:
      return Object.assign({}, state, {
        couponStatus: action.status,
        couponError: action.error,
        loading: false
      });

    case cartActions.RELEASE_COUPON_REQUEST:
      return Object.assign({}, state, {
        couponStatus: action.status,
        loading: true
      });

    case cartActions.RELEASE_COUPON_SUCCESS:
      return Object.assign({}, state, {
        couponStatus: action.status,
        loading: false
      });

    case cartActions.RELEASE_COUPON_FAILURE:
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
        userAddress: action.userAddress,
        loading: false
      });

    case cartActions.GET_USER_ADDRESS_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });
    case cartActions.CART_DETAILS_CNC_REQUEST:
      return Object.assign({}, state, {
        cartDetailsCNCStatus: action.status,
        loading: true
      });
    case cartActions.CART_DETAILS_CNC_SUCCESS:
      return Object.assign({}, state, {
        cartDetailsCNCStatus: action.status,
        setAddress: action.setAddress,
        userAddress: action.cartDetailsCnc.addressDetailsList,
        cartDetailsCnc: action.cartDetailsCnc,
        loading: false
      });
    case cartActions.CART_DETAILS_CNC_FAILURE:
      return Object.assign({}, state, {
        cartDetailsCNCStatus: action.status,
        cartDetailsCNCError: action.error,
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

    case cartActions.GENERATE_CART_ID_REQUEST:
      return Object.assign({}, state, {
        status: action.status
      });

    case cartActions.GENERATE_CART_ID_FOR_LOGGED_ID_SUCCESS:
      Cookies.createCookie(
        CART_DETAILS_FOR_LOGGED_IN_USER,
        JSON.stringify(action.cartDetails)
      );
      return Object.assign({}, state, {
        status: action.status
      });

    case cartActions.GENERATE_CART_ID_BY_ANONYMOUS_SUCCESS:
      Cookies.createCookie(
        CART_DETAILS_FOR_ANONYMOUS,
        JSON.stringify(action.cartDetails)
      );
      return Object.assign({}, state, {
        status: action.status
      });

    case cartActions.GENERATE_CART_ID_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error
      });

    case cartActions.ORDER_SUMMARY_REQUEST:
      return Object.assign({}, state, {
        orderSummaryStatus: action.status
      });

    case cartActions.ORDER_SUMMARY_SUCCESS:
      return Object.assign({}, state, {
        orderSummaryStatus: action.status,
        orderSummary: action.orderSummary
      });

    case cartActions.ORDER_SUMMARY_FAILURE:
      return Object.assign({}, state, {
        orderSummaryStatus: action.status,
        orderSummaryError: action.error
      });

    case cartActions.GET_CART_ID_REQUEST:
      return Object.assign({}, state, {
        status: action.status
      });
    case cartActions.GET_CART_ID_SUCCESS:
      Cookies.createCookie(
        CART_DETAILS_FOR_LOGGED_IN_USER,
        JSON.stringify(action.cartDetails)
      );
      Cookies.deleteCookie(CART_DETAILS_FOR_ANONYMOUS);
      return Object.assign({}, state, {
        status: action.status
      });

    case cartActions.GET_CART_ID_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error
      });

    case cartActions.MERGE_CART_ID_REQUEST:
      return Object.assign({}, state, {
        status: action.status
      });

    case cartActions.MERGE_CART_ID_SUCCESS:
      Cookies.createCookie(
        CART_DETAILS_FOR_LOGGED_IN_USER,
        JSON.stringify(action.cartDetails)
      );
      Cookies.deleteCookie(CART_DETAILS_FOR_ANONYMOUS);
      return Object.assign({}, state, {
        status: action.status,
        type: action.type
      });

    case cartActions.MERGE_CART_ID_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error
      });

    case cartActions.GET_ALL_STORES_CNC_REQUEST:
      return Object.assign({}, state, {
        storeStatus: action.status,
        loading: true
      });

    case cartActions.GET_ALL_STORES_CNC_SUCCESS:
      return Object.assign({}, state, {
        storeStatus: action.status,
        storeDetails: action.storeDetails,
        loading: false
      });

    case cartActions.GET_ALL_STORES_CNC_FAILURE:
      return Object.assign({}, state, {
        storeStatus: action.status,
        storeError: action.error,
        loading: false
      });

    case cartActions.ADD_STORE_CNC_REQUEST:
      return Object.assign({}, state, {
        storeStatus: action.status,
        loading: true
      });

    case cartActions.ADD_STORE_CNC_SUCCESS:
      return Object.assign({}, state, {
        storeStatus: action.status,
        storeAdded: action.storeAdded,
        loading: false
      });

    case cartActions.ADD_STORE_CNC_FAILURE:
      return Object.assign({}, state, {
        storeStatus: action.status,
        storeError: action.error,
        loading: false
      });

    case cartActions.ADD_PICKUP_PERSON_REQUEST:
      return Object.assign({}, state, {
        cartDetailsCNCStatus: action.status,
        loading: true
      });

    case cartActions.ADD_PICKUP_PERSON_SUCCESS:
      return Object.assign({}, state, {
        cartDetailsCNCStatus: action.status,
        cartDetailsCNC: action.cartDetailsCNC,
        loading: false
      });

    case cartActions.ADD_PICKUP_PERSON_FAILURE:
      return Object.assign({}, state, {
        cartDetailsCNCStatus: action.status,
        cartDetailsCNCError: action.error,
        loading: false
      });
    case cartActions.ADD_ADDRESS_TO_CART_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case cartActions.ADD_ADDRESS_TO_CART_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        loading: false
      });

    case cartActions.ADD_ADDRESS_TO_CART_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case cartActions.SOFT_RESERVATION_REQUEST:
      return Object.assign({}, state, {
        softReserveStatus: action.status,
        loading: true
      });

    case cartActions.SOFT_RESERVATION_SUCCESS:
      return Object.assign({}, state, {
        softReserveStatus: action.status,
        softReserve: action.softReserve,
        loading: false
      });

    case cartActions.SOFT_RESERVATION_FAILURE:
      return Object.assign({}, state, {
        softReserveStatus: action.status,
        softReserveError: action.error,
        loading: false
      });

    case cartActions.GET_PAYMENT_MODES_REQUEST:
      return Object.assign({}, state, {
        paymentModesStatus: action.status,
        loading: true
      });

    case cartActions.GET_PAYMENT_MODES_SUCCESS:
      return Object.assign({}, state, {
        paymentModesStatus: action.status,
        paymentModes: action.paymentModes,
        loading: false
      });

    case cartActions.GET_PAYMENT_MODES_FAILURE:
      return Object.assign({}, state, {
        paymentModesStatus: action.status,
        paymentModesError: action.error,
        loading: false
      });

    case cartActions.APPLY_BANK_OFFER_REQUEST:
      return Object.assign({}, state, {
        bankOfferStatus: action.status,
        loading: true
      });
    case cartActions.APPLY_BANK_OFFER_SUCCESS:
      return Object.assign({}, state, {
        bankOfferStatus: action.status,
        bankOffer: action.bankOffer,
        loading: false
      });
    case cartActions.APPLY_BANK_OFFER_FAILURE:
      return Object.assign({}, state, {
        bankOfferStatus: action.status,
        bankOfferError: action.error,
        loading: false
      });
    case cartActions.RELEASE_BANK_OFFER_REQUEST:
      return Object.assign({}, state, {
        bankOfferStatus: action.status,
        loading: true
      });
    case cartActions.RELEASE_BANK_OFFER_SUCCESS:
      return Object.assign({}, state, {
        bankOfferStatus: action.status,
        loading: false
      });
    case cartActions.RELEASE_BANK_OFFER_FAILURE:
      return Object.assign({}, state, {
        bankOfferStatus: action.status,
        bankOfferError: action.error,
        loading: false
      });
    case cartActions.APPLY_CLIQ_CASH_REQUEST:
      return Object.assign({}, state, {
        paymentStatus: action.status,
        loading: true
      });

    case cartActions.APPLY_CLIQ_CASH_SUCCESS: {
      return Object.assign({}, state, {
        paymentStatus: action.status,
        paymentDetails: action.paymentDetails,
        loading: false
      });
    }

    case cartActions.APPLY_CLIQ_CASH_FAILURE:
      return Object.assign({}, state, {
        paymentStatus: action.status,
        paymentStatusError: action.error,
        loading: false
      });

    case cartActions.REMOVE_CLIQ_CASH_REQUEST:
      return Object.assign({}, state, {
        paymentStatus: action.status,
        loading: true
      });

    case cartActions.REMOVE_CLIQ_CASH_SUCCESS: {
      return Object.assign({}, state, {
        paymentStatus: action.status,
        paymentDetails: action.paymentDetails,
        loading: false
      });
    }

    case cartActions.REMOVE_CLIQ_CASH_FAILURE:
      return Object.assign({}, state, {
        paymentStatus: action.status,
        paymentStatusError: action.error,
        loading: false
      });

    default:
      return state;
  }
};

export default cart;
