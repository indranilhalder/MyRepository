import cloneDeep from "lodash.clonedeep";
import * as accountActions from "../actions/account.actions";
import * as cartActions from "../../cart/actions/cart.actions";
import * as Cookie from "../../lib/Cookie.js";
import {
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN
} from "../../lib/constants.js";
import findIndex from "lodash.findindex";
import { SUCCESS } from "../../lib/constants";
import { CLEAR_ERROR } from "../../general/error.actions";
import * as Cookies from "../../lib/Cookie";
const account = (
  state = {
    status: null,
    error: null,
    loading: false,
    type: null,
    loadingForClearOrderDetails: true,
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

    updateProfileStatus: null,
    updateProfileError: null,

    changePasswordStatus: null,
    changePasswordError: null,
    changePasswordDetails: null,

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

    cliqCashUserDetailsStatus: null,
    cliqCashUserDetailsError: null,
    cliqCashUserDetails: null,

    cliqCashVoucherDetailsStatus: null,
    cliqCashVoucherDetailsError: null,
    cliqCashVoucherDetails: null,

    returnPinCodeStatus: null,
    returnPinCodeValues: null,
    returnPinCodeError: null,

    returnInitiateStatus: null,
    returnInitiateError: null,
    returnInitiate: null,
    giftCards: null,
    giftCardStatus: null,
    giftCardsError: null,
    loadingForGiftCard: false,

    giftCardDetails: null,
    giftCardDetailsStatus: null,
    giftCardDetailsError: null,
    loadingForGiftCardDetails: false,

    getOtpToActivateWallet: null,
    getOtpToActivateWalletStatus: null,
    getOtpToActivateWalletError: null,
    loadingForGetOtpToActivateWallet: false,

    verifyWallet: null,
    verifyWalletStatus: null,
    verifyWalletError: null,
    loadingForverifyWallet: false,

    getPinCodeDetails: null,
    getPinCodeStatus: null,
    getPinCodeError: null,

    updateReturnDetails: null,
    updateReturnDetailsStatus: null,
    updateReturnDetailsError: null,
    loadingForUpdateReturnDetails: null,

    cancelProductDetails: null,
    cancelProductDetailsStatus: null,
    cancelProductDetailsError: null,
    loadingForCancelProductDetails: false,

    cancelProduct: null,
    cancelProductStatus: null,
    cancelProductError: null,
    loadingForCancelProduct: false
  },
  action
) => {
  let currentReturnRequest;
  switch (action.type) {
    case CLEAR_ERROR:
      return Object.assign({}, state, {
        error: null,
        orderDetailsError: null,
        fetchOrderDetailsError: null,
        userDetailsError: null,
        userCouponsError: null,
        userAlertsError: null,
        sendInvoiceError: null,
        userAddressError: null,
        removeAddressError: null,
        editAddressError: null,
        addUserAddressError: null,
        followedBrandsError: null,
        cliqCashUserDetailsError: null,
        cliqCashVoucherDetailsError: null,
        returnPinCodeError: null,
        giftCardsError: null,
        giftCardDetailsError: null,
        getOtpToActivateWalletError: null,
        getPinCodeError: null,
        updateReturnDetailsError: null,
        cancelProductDetailsError: null,
        cancelProductError: null,
        verifyWalletError: null,
        wishlistError: null,
        updateProfileError: null,
        changePasswordError: null
      });
    case accountActions.GET_RETURN_REQUEST:
    case accountActions.RETURN_PRODUCT_DETAILS_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true,
        error: null
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
        status: action.status,
        error: action.error
      });

    case accountActions.QUICK_DROP_STORE_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true,
        error: null
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
        status: action.status,
        error: action.error
      });

    case accountActions.GET_GIFTCARD_REQUEST:
      return Object.assign({}, state, {
        giftCardStatus: action.status,
        loadingForGiftCard: true
      });

    case accountActions.GET_GIFTCARD_SUCCESS:
      return Object.assign({}, state, {
        giftCardStatus: action.status,
        giftCards: action.giftCards,
        loadingForGiftCard: false
      });

    case accountActions.GET_GIFTCARD_FAILURE:
      return Object.assign({}, state, {
        giftCardStatus: action.status,
        giftCardsError: action.error,
        loadingForGiftCard: false
      });

    case accountActions.CREATE_GIFT_CARD_REQUEST:
      return Object.assign({}, state, {
        giftCardDetailsStatus: action.status,
        loadingForGiftCardDetails: true
      });

    case accountActions.CREATE_GIFT_CARD_SUCCESS:
      return Object.assign({}, state, {
        giftCardDetailsStatus: action.status,
        giftCardDetails: action.giftCardDetails,
        loadingForGiftCardDetails: false
      });

    case accountActions.CREATE_GIFT_CARD_FAILURE:
      return Object.assign({}, state, {
        giftCardDetailsStatus: action.status,
        giftCardDetailsError: action.error,
        loadingForGiftCardDetails: false
      });
    case accountActions.GET_OTP_TO_ACTIVATE_WALLET_REQUEST:
      return Object.assign({}, state, {
        getOtpToActivateWalletStatus: action.status,
        loadingForGetOtpToActivateWallet: true
      });

    case accountActions.GET_OTP_TO_ACTIVATE_WALLET_SUCCESS:
      return Object.assign({}, state, {
        getOtpToActivateWalletStatus: action.status,
        getOtpToActivateWallet: action.getOtpToActivateWallet,
        loadingForGetOtpToActivateWallet: false
      });

    case accountActions.GET_OTP_TO_ACTIVATE_WALLET_FAILURE:
      return Object.assign({}, state, {
        getOtpToActivateWalletStatus: action.status,
        getOtpToActivateWalletError: action.error,
        loadingForGetOtpToActivateWallet: false
      });

    case accountActions.VERIFY_WALLET_REQUEST:
      return Object.assign({}, state, {
        verifyWalletStatus: action.status,
        loadingForverifyWallet: true
      });

    case accountActions.VERIFY_WALLET_SUCCESS:
      return Object.assign({}, state, {
        verifyWalletStatus: action.status,
        verifyWallet: action.verifyWallet,
        loadingForverifyWallet: false
      });

    case accountActions.VERIFY_WALLET_FAILURE:
      return Object.assign({}, state, {
        verifyWalletStatus: action.status,
        verifyWalletError: action.error,
        loadingForverifyWallet: false
      });

    case accountActions.SUBMIT_SELF_COURIER_INFO_REQUEST:
      return Object.assign({}, state, {
        updateReturnDetailsStatus: action.status,
        loadingForUpdateReturnDetails: true
      });

    case accountActions.SUBMIT_SELF_COURIER_INFO_SUCCESS:
      return Object.assign({}, state, {
        updateReturnDetailsStatus: action.status,
        updateReturnDetails: action.updateReturnDetails,
        loadingForUpdateReturnDetails: false
      });

    case accountActions.SUBMIT_SELF_COURIER_INFO_FAILURE:
      return Object.assign({}, state, {
        updateReturnDetailsStatus: action.status,
        updateReturnDetailsError: action.error,
        loadingForUpdateReturnDetails: false
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
      let currentOrderDetailObj = state.orderDetails
        ? cloneDeep(state.orderDetails)
        : {};
      if (
        action.isPaginated &&
        currentOrderDetailObj &&
        currentOrderDetailObj.orderData
      ) {
        currentOrderDetailObj.orderData = currentOrderDetailObj.orderData.concat(
          action.orderDetails.orderData
        );
        currentOrderDetailObj.currentPage =
          currentOrderDetailObj.currentPage + 1;
      } else {
        currentOrderDetailObj = action.orderDetails;
        Object.assign(currentOrderDetailObj, {
          currentPage: 0
        });
      }
      return Object.assign({}, state, {
        orderDetailsStatus: action.status,
        orderDetails: currentOrderDetailObj,
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
        loadingForWishlist: false
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
    case accountActions.FOLLOW_AND_UN_FOLLOW_BRANDS_IN_MY_ACCOUNT_SUCCESS:
      const currentBrands = cloneDeep(state.followedBrands);
      const indexToBeRemoved = findIndex(currentBrands, brand => {
        return brand.id === action.brandId;
      });
      currentBrands.splice(indexToBeRemoved, 1);
      return Object.assign({}, state, {
        followedBrands: currentBrands
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
      const indexOfAddressToBeRemove = findIndex(
        currentAddresses.addresses,
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

    case accountActions.UPDATE_PROFILE_REQUEST:
      return Object.assign({}, state, {
        updateProfileStatus: action.status,
        loading: true
      });

    case accountActions.UPDATE_PROFILE_SUCCESS:
      const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);

      //deletre cookie
      Cookie.deleteCookie(LOGGED_IN_USER_DETAILS);
      //assign firstName and Last Name
      let updateUserDetails = JSON.parse(userDetails);
      updateUserDetails.firstName =
        action.userDetails.firstName !== undefined &&
        action.userDetails.firstName !== "undefined"
          ? action.userDetails.firstName
          : "";
      updateUserDetails.lastName =
        action.userDetails.lastName !== undefined &&
        action.userDetails.lastName !== "undefined"
          ? action.userDetails.lastName
          : "";
      Cookies.createCookie(
        LOGGED_IN_USER_DETAILS,
        JSON.stringify(updateUserDetails)
      );
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

    case accountActions.LOG_OUT_ACCOUNT_USING_MOBILE_NUMBER:
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

    case accountActions.GET_USER_CLIQ_CASH_DETAILS_REQUEST:
      return Object.assign({}, state, {
        cliqCashUserDetailsStatus: action.status,
        loading: true
      });

    case accountActions.GET_USER_CLIQ_CASH_DETAILS_SUCCESS:
      return Object.assign({}, state, {
        cliqCashUserDetailsStatus: action.status,
        cliqCashUserDetails: action.cliqCashDetails,
        loading: false
      });

    case accountActions.GET_USER_CLIQ_CASH_DETAILS_FAILURE:
      return Object.assign({}, state, {
        cliqCashUserDetailsStatus: action.status,
        cliqCashUserDetailsError: action.error,
        loading: false
      });

    case accountActions.REDEEM_CLIQ_VOUCHER_REQUEST:
      return Object.assign({}, state, {
        cliqCashVoucherDetailsStatus: action.status,
        loading: true
      });

    case accountActions.REDEEM_CLIQ_VOUCHER_SUCCESS:
      return Object.assign({}, state, {
        cliqCashVoucherDetailsStatus: action.status,
        cliqCashVoucherDetails: action.cliqCashVoucherDetails,
        loading: false
      });

    case accountActions.REDEEM_CLIQ_VOUCHER_FAILURE:
      return Object.assign({}, state, {
        cliqCashVoucherDetailsStatus: action.status,
        cliqCashVoucherDetailsError: action.error,
        loading: false,
        error: action.error
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
      let updatedReturnProductDetails = cloneDeep(state.returnProductDetails);
      updatedReturnProductDetails.returnModes =
        action.pinCodeDetails && action.pinCodeDetails.returnModes;
      return Object.assign({}, state, {
        returnPinCodeStatus: action.status,
        returnPinCodeError: action.error,
        returnProductDetails: updatedReturnProductDetails,
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
    case accountActions.GET_CANCEL_PRODUCT_DETAILS_REQUEST:
      return Object.assign({}, state, {
        cancelProductDetailsStatus: action.status,
        loadingForCancelProductDetails: true
      });

    case accountActions.GET_CANCEL_PRODUCT_DETAILS_SUCCESS:
      return Object.assign({}, state, {
        cancelProductDetailsStatus: action.status,
        cancelProductDetails: action.getDetailsOfCancelledProduct,
        loadingForCancelProductDetails: false
      });

    case accountActions.GET_CANCEL_PRODUCT_DETAILS_FAILURE:
      return Object.assign({}, state, {
        cancelProductDetailsStatus: action.status,
        cancelProductDetailsError: action.error,
        loadingForCancelProductDetails: false
      });

    case accountActions.CANCEL_PRODUCT_REQUEST:
      return Object.assign({}, state, {
        cancelProductStatus: action.status,
        loadingForCancelProduct: true
      });

    case accountActions.CANCEL_PRODUCT_SUCCESS:
      return Object.assign({}, state, {
        cancelProductStatus: action.status,
        cancelProduct: action.cancelProduct,
        loadingForCancelProduct: false
      });
    case accountActions.CANCEL_PRODUCT_FAILURE:
      return Object.assign({}, state, {
        cancelProductStatus: action.status,
        cancelProductError: action.error,
        loadingForCancelProduct: false
      });
    case accountActions.CLEAR_GIFT_CARD_STATUS: {
      return Object.assign({}, state, {
        giftCardDetails: null,
        giftCardDetailsStatus: null
      });
    }
    case accountActions.CLEAR_ACCOUNT_UPDATE_TYPE: {
      return Object.assign({}, state, {
        type: null,
        status: null
      });
    }
    case accountActions.Clear_ORDER_DATA: {
      return Object.assign({}, state, {
        type: null,
        status: null,
        orderDetails: null,
        loadingForClearOrderDetails: false
      });
    }
    case accountActions.RE_SET_ADD_ADDRESS_DETAILS: {
      return Object.assign({}, state, {
        addUserAddressStatus: null,
        addUserAddressError: null
      });
    }
    case accountActions.CLEAR_CHANGE_PASSWORD_DETAILS: {
      return Object.assign({}, state, {
        changePasswordStatus: null,
        changePasswordError: null
      });
    }
    case accountActions.CLEAR_PIN_CODE_STATUS: {
      return Object.assign({}, state, {
        getPinCodeStatus: null,
        getPinCodeError: null
      });
    }

    default:
      return state;
  }
};

export default account;
