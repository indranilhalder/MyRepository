import cloneDeep from "lodash/cloneDeep";
import * as accountActions from "../actions/account.actions";
import cloneDeep from "lodash/cloneDeep";
import * as cartActions from "../../cart/actions/cart.actions";
import { SUCCESS } from "../../lib/constants";

const account = (
  state = {
    status: null,
    error: null,
    loading: false,
    savedCards: null,
    orderDetails: null,
    orderDetailsStatus: null,
    orderDetailsError: null,

    fetchOrderDetails: null,
    fetchOrderDetailsStatus: null,
    fetchOrderDetailsError: null,
    loadingForFetchOrderDetails: false,

    wishlist: null,
    wishlistStatus: null,
    wishlistError: null,
    loadingForWishlist: false,

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

    returnProductDetails: null,
    returnRequest: null,
    editAddressStatus: null,
    editAddressError: null,

    addUserAddressStatus: null,
    addUserAddressError: null,

    followedBrands: null,
    followedBrandsStatus: null,
    followedBrandsError: null,
    loadingForFollowedBrands: false,

    returnPinCodeStatus: null,
    returnPinCodeValues: null,
    returnPinCodeError: null,

    returnInitiateStatus: null,
    returnInitiateError: null,
    returnInitiate: null,
    getPinCodeDetails: null,
    getPinCodeStatus: null,
    getPinCodeError: null

  },
  action
) => {
  let currentReturnRequest;
  switch (action.type) {
    case accountActions.GET_RETURN_REQUEST:
    case accountActions.RETURN_PRODUCT_DETAILS_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });
    case accountActions.GET_RETURN_REQUEST_SUCCESS:
      return Object.assign({}, state, {
        status: SUCCESS,
        loading: false,
        returnRequest: action.returnRequest
      });
    case accountActions.GET_RETURN_REQUEST_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        loading: false,
        error: action.error
      });
    case accountActions.RETURN_PRODUCT_DETAILS_SUCCESS:
      return Object.assign({}, state, {
        loading: false,
        status: action.state,
        returnProductDetails: action.returnProductDetails
      });
    case accountActions.RETURN_PRODUCT_DETAILS_FAILURE:
      return Object.assign({}, state, {
        loading: false,
        status: action.status
      });

    case accountActions.QUICK_DROP_STORE_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true,
        error: action.error
      });
    case accountActions.QUICK_DROP_STORE_SUCCESS:
      currentReturnRequest = cloneDeep(state.returnRequest);
      Object.assign(currentReturnRequest, {
        returnStoreDetailsList: action.addresses
      });

      return Object.assign({}, state, {
        loading: false,
        status: action.state,
        returnRequest: currentReturnRequest
      });
    case accountActions.QUICK_DROP_STORE_FAILURE:
      return Object.assign({}, state, {
        loading: false,
        status: action.status
      });

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
    case accountActions.GET_WISHLIST_REQUEST:
      return Object.assign({}, state, {
        wishlistStatus: action.status,
        loadingForWishlist: true
      });

    case accountActions.GET_WISHLIST_SUCCESS:
      return Object.assign({}, state, {
        wishlistStatus: action.status,
        wishlist: action.wishlist,
        loadingForWishlist: false
      });

    case accountActions.GET_WISHLIST_FAILURE:
      return Object.assign({}, state, {
        wishlistStatus: action.status,
        wishlistError: action.error,
        loaloadingForWishlistding: false
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
    case accountActions.GET_FOLLOWED_BRANDS_REQUEST:
      return Object.assign({}, state, {
        followedBrandsStatus: action.status,
        loadingForFollowedBrands: true
      });

    case accountActions.GET_FOLLOWED_BRANDS_SUCCESS:
      return Object.assign({}, state, {
        followedBrandsStatus: action.status,
        followedBrands: action.followedBrands,
        loadingForFollowedBrands: false
      });

    case accountActions.GET_FOLLOWED_BRANDS_FAILURE:
      return Object.assign({}, state, {
        followedBrandsStatus: action.status,
        followedBrandsError: action.error,
        loadingForFollowedBrands: false
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
      const currentAddresses = cloneDeep(state.userAddress);
      const indexOfAddressToBeRemove = currentAddresses.addresses.findIndex(
        address => {
          return address.id === action.addressId;
        }
      );
      currentAddresses.addresses.splice(indexOfAddressToBeRemove, 1);
      return Object.assign({}, state, {
        removeAddressStatus: action.status,
        userAddress: currentAddresses,
        loading: false
      });

    case accountActions.REMOVE_ADDRESS_FAILURE:
      return Object.assign({}, state, {
        removeAddressStatus: action.status,
        removeAddressError: action.error,
        loading: false
      });

    case accountActions.NEW_RETURN_INITIATE_REQUEST:
      return Object.assign({}, state, {
        returnInitiateStatus: action.status,
        loading: true
      });
    case accountActions.NEW_RETURN_INITIATE_SUCCESS:
      return Object.assign({}, state, {
        returnInitiateStatus: action.status,
        returnInitiate: action.returnDetails,
        loading: false
      });

    case accountActions.NEW_RETURN_INITIATE_FAILURE:
      return Object.assign({}, state, {
        returnInitiateStatus: action.status,
        returnInitiateError: action.error,
        loading: false
      });

    case accountActions.RETURN_PIN_CODE_REQUEST:
      return Object.assign({}, state, {
        returnPinCodeStatus: action.status,
        loading: true
      });
    case accountActions.RETURN_PIN_CODE_SUCCESS:
      return Object.assign({}, state, {
        returnPinCodeStatus: action.status,
        returnPinCodeValues: action.returnDetails,
        loading: false
      });

    case accountActions.RETURN_PIN_CODE_FAILURE:
      return Object.assign({}, state, {
        returnPinCodeStatus: action.status,
        returnPinCodeError: action.error,
        loading: false
      });

    case accountActions.EDIT_ADDRESS_REQUEST:
      return Object.assign({}, state, {
        editAddressStatus: action.status,
        loading: true
      });

    case accountActions.EDIT_ADDRESS_SUCCESS:
      return Object.assign({}, state, {
        editAddressStatus: action.status,
        loading: false
      });

    case accountActions.EDIT_ADDRESS_FAILURE:
      return Object.assign({}, state, {
        editAddressStatus: action.status,
        editAddressError: action.error,
        loading: false
      });

    case cartActions.ADD_USER_ADDRESS_REQUEST:
      return Object.assign({}, state, {
        addUserAddressStatus: action.status,
        loading: true
      });

    case cartActions.ADD_USER_ADDRESS_SUCCESS:
      return Object.assign({}, state, {
        addUserAddressStatus: action.status,
        loading: false
      });

    case cartActions.ADD_USER_ADDRESS_FAILURE:
      return Object.assign({}, state, {
        addUserAddressStatus: action.status,
        addUserAddressError: action.error,
        loading: false
      });

    case accountActions.GET_PIN_CODE_REQUEST:
      return Object.assign({}, state, {
        getPinCodeStatus: action.status
      });

    case accountActions.GET_PIN_CODE_SUCCESS:
      return Object.assign({}, state, {
        getPinCodeStatus: action.status,
        getPinCodeDetails: action.pinCode
      });

    case accountActions.GET_PIN_CODE_FAILURE:
      return Object.assign({}, state, {
        getPinCodeStatus: action.status,
        getPinCodeError: action.error
      });
    default:
      return state;
  }
};

export default account;
