import * as accountActions from "../actions/account.actions";
import * as cartActions from "../../cart/actions/cart.actions";
import * as Cookie from "../../lib/Cookie.js";
import {
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN
} from "../../lib/constants.js";

const account = (
  state = {
    status: null,
    error: null,
    loading: false,
    type: null,
    savedCards: null,
    orderDetails: null,
    orderDetailsStatus: null,
    orderDetailsError: null,

    fetchOrderDetails: null,
    fetchOrderDetailsStatus: null,
    fetchOrderDetailsError: null,
    loadingForFetchOrderDetails: false,

    userDetails: null,
    userDetailsStatus: null,
    userDetailsError: null,
    loadingForUserDetails: false,

    userCoupons: null,
    userCouponsStatus: null,
    userCouponsError: null,
    loadingForUserCoupons: false,

    userAlerts: null,
    userAlertsStatus: null,
    userAlertsError: null,
    loadingForUserAlerts: false,

    sendInvoice: null,
    sendInvoiceStatus: null,
    sendInvoiceError: null,
    loadingForSendInvoice: false,

    userAddress: null,
    userAddressStatus: null,
    userAddressError: null,

    removeAddressStatus: null,
    removeAddressError: null,

    updateProfileStatus: null,
    updateProfileError: null,

    changePasswordStatus: null,
    changePasswordError: null,
    changePasswordDetails: null
  },
  action
) => {
  switch (action.type) {
    case accountActions.GET_SAVED_CARD_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case accountActions.GET_SAVED_CARD_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        savedCards: action.savedCards,
        loading: false
      });

    case accountActions.GET_SAVED_CARD_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case accountActions.REMOVE_SAVED_CARD_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case accountActions.REMOVE_SAVED_CARD_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        loading: false
      });

    case accountActions.REMOVE_SAVED_CARD_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

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
    case accountActions.GET_USER_DETAILS_REQUEST:
      return Object.assign({}, state, {
        userDetailsStatus: action.status,
        loadingForUserDetails: true
      });

    case accountActions.GET_USER_DETAILS_SUCCESS:
      return Object.assign({}, state, {
        userDetailsStatus: action.status,
        userDetails: action.userDetails,
        loadingForUserDetails: false
      });

    case accountActions.GET_USER_DETAILS_FAILURE:
      return Object.assign({}, state, {
        userDetailsStatus: action.status,
        userDetailsError: action.error,
        loadingForUserDetails: false
      });

    case accountActions.GET_USER_COUPON_REQUEST:
      return Object.assign({}, state, {
        userCouponsStatus: action.status,
        loadingForUserCoupons: true
      });

    case accountActions.GET_USER_COUPON_SUCCESS:
      return Object.assign({}, state, {
        userCouponsStatus: action.status,
        userCoupons: action.userCoupons,
        loadingForUserCoupons: false
      });

    case accountActions.GET_USER_COUPON_FAILURE:
      return Object.assign({}, state, {
        userCouponsStatus: action.status,
        userCouponsError: action.error,
        loadingForUserCoupons: false
      });
    case accountActions.GET_USER_ALERTS_REQUEST:
      return Object.assign({}, state, {
        userAlertsStatus: action.status,
        loadingForUserAlerts: true
      });

    case accountActions.GET_USER_ALERTS_SUCCESS:
      return Object.assign({}, state, {
        userAlertsStatus: action.status,
        userAlerts: action.userAlerts,
        loadingForUserAlerts: false
      });

    case accountActions.GET_USER_ALERTS_FAILURE:
      return Object.assign({}, state, {
        userAlertsStatus: action.status,
        userAlertsError: action.error,
        loadingForUserAlerts: false
      });

    case accountActions.FETCH_ORDER_DETAILS_REQUEST:
      return Object.assign({}, state, {
        fetchOrderDetailsStatus: action.status,
        loadingForFetchOrderDetails: true
      });

    case accountActions.FETCH_ORDER_DETAILS_SUCCESS:
      return Object.assign({}, state, {
        fetchOrderDetailsStatus: action.status,
        fetchOrderDetails: action.fetchOrderDetails,
        loadingForFetchOrderDetails: false
      });

    case accountActions.FETCH_ORDER_DETAILS_FAILURE:
      return Object.assign({}, state, {
        fetchOrderDetailsStatus: action.status,
        fetchOrderDetailsError: action.error,
        loadingForFetchOrderDetails: false
      });

    case accountActions.SEND_INVOICE_REQUEST:
      return Object.assign({}, state, {
        sendInvoiceStatus: action.status,
        loadingForSendInvoice: true
      });

    case accountActions.SEND_INVOICE_SUCCESS:
      return Object.assign({}, state, {
        sendInvoiceStatus: action.status,
        sendInvoice: action.sendInvoice,
        loadingForSendInvoice: false
      });

    case accountActions.SEND_INVOICE_FAILURE:
      return Object.assign({}, state, {
        sendInvoiceStatus: action.status,
        sendInvoiceError: action.error,
        loadingForSendInvoice: false
      });
    case cartActions.GET_USER_ADDRESS_REQUEST:
      return Object.assign({}, state, {
        userAddressStatus: action.status,
        loading: true
      });

    case cartActions.GET_USER_ADDRESS_SUCCESS:
      return Object.assign({}, state, {
        userAddressStatus: action.status,
        userAddress: action.userAddress,
        loading: false
      });

    case cartActions.GET_USER_ADDRESS_FAILURE:
      return Object.assign({}, state, {
        userAddressStatus: action.status,
        userAddressError: action.error,
        loading: false
      });

    case accountActions.REMOVE_ADDRESS_REQUEST:
      return Object.assign({}, state, {
        removeAddressStatus: action.status,
        loading: true
      });

    case accountActions.REMOVE_ADDRESS_SUCCESS:
      return Object.assign({}, state, {
        removeAddressStatus: action.status,
        loading: false
      });

    case accountActions.REMOVE_ADDRESS_FAILURE:
      return Object.assign({}, state, {
        removeAddressStatus: action.status,
        removeAddressError: action.error,
        loading: false
      });

    case accountActions.UPDATE_PROFILE_REQUEST:
      return Object.assign({}, state, {
        updateProfileStatus: action.status,
        loading: true
      });

    case accountActions.UPDATE_PROFILE_SUCCESS:
      return Object.assign({}, state, {
        updateProfileStatus: action.status,
        userDetails: action.userDetails,
        loading: false
      });

    case accountActions.UPDATE_PROFILE_FAILURE:
      return Object.assign({}, state, {
        updateProfileStatus: action.status,
        updateProfileError: action.error,
        loading: false
      });

    case accountActions.LOG_OUT_ACCOUNT:
      Cookie.deleteCookie(LOGGED_IN_USER_DETAILS);
      Cookie.deleteCookie(CUSTOMER_ACCESS_TOKEN);
      return Object.assign({}, state, {
        type: action.type
      });

    case accountActions.CHANGE_PASSWORD_REQUEST:
      return Object.assign({}, state, {
        changePasswordStatus: action.status,
        loading: false
      });

    case accountActions.CHANGE_PASSWORD_SUCCESS:
      return Object.assign({}, state, {
        changePasswordStatus: action.status,
        changePasswordDetails: action.passwordDetails,
        loading: false
      });

    case accountActions.CHANGE_PASSWORD_FAILURE:
      return Object.assign({}, state, {
        changePasswordStatus: action.status,
        changePasswordError: action.error,
        loading: false
      });

    default:
      return state;
  }
};

export default account;
