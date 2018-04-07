import * as cartActions from "../actions/cart.actions";
import cloneDeep from "lodash/cloneDeep";
import * as Cookies from "../../lib/Cookie";
import { CLEAR_ERROR } from "../../general/error.actions.js";
import {
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS,
  OLD_CART_GU_ID,
  COUPON_COOKIE
} from "../../lib/constants";
import find from "lodash/find";
const IST_TIME_ZONE = "IST";
const cart = (
  state = {
    status: null,
    error: null,
    loading: false,
    type: null,
    cartIdForLoggedInUserStatus: null,
    cartIdForAnonymousUserStatus: null,
    mergeCartIdStatus: null,

    getUserAddressStatus: null,
    getUserAddressError: null,

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
    coupons: null,

    deliveryModes: null,
    userAddress: null,
    setAddress: null,

    netBankDetailsStatus: null,
    netBankDetailsError: null,
    netBankDetails: null,

    emiBankDetails: null,
    emiBankStatus: null,
    emiBankError: null,

    orderSummary: null,
    orderSummaryStatus: null,
    orderSummaryError: null,

    storeDetails: [],
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

    cliqCashPaymentStatus: null,
    cliqCashPaymentStatusError: null,
    cliqCashPaymentDetails: null,

    jusPayStatus: null,
    jusPayError: null,
    jusPayDetails: null,
    cliqCashJusPayDetails: null,

    transactionDetailsStatus: null,
    transactionDetailsError: null,
    transactionDetailsDetails: null,

    orderConfirmationDetailsStatus: null,
    orderConfirmationDetailsError: null,
    orderConfirmationDetails: null,

    justPayPaymentDetailsStatus: null,
    justPayPaymentDetailsError: null,
    justPayPaymentDetails: null,

    codEligibilityStatus: null,
    codEligibilityError: null,
    codEligibilityDetails: null,

    binValidationCODStatus: null,
    binValidationCODError: null,
    binValidationCODDetails: null,

    transactionCODStatus: null,
    transactionCODError: null,

    softReserveCODPaymentStatus: null,
    softReserveCODPayment: null,
    softReserveCODPaymentError: null,
    orderExperienceStatus: null,
    orderExperience: null,
    orderExperienceError: null,

    binValidationStatus: null,
    binValidationError: null,
    binValidationDetails: null,

    addToWishlistStatus: null,
    addToWishlistError: null,

    removeCartItemStatus: null,
    removeCartItemError: null,

    removeCartItemLoggedOutStatus: null,
    removeCartItemLoggedOutError: null,

    updateQuantityLoggedInStatus: null,
    updateQuantityLoggedInDetails: null,
    updateQuantityLoggedInError: null,

    updateQuantityLoggedOutStatus: null,
    updateQuantityLoggedOutDetails: null,
    updateQuantityLoggedOutError: null,

    AddUserAddressStatus: null,
    AddUserAddressError: null,
    addingAddress: false,
    returnCliqPiqStatus: null,
    returnCliqPiqDetails: null,
    returnCliqPiqError: null,

    softReservationForPaymentStatus: null,
    softReservationForPaymentError: null,
    softReservationForPaymentDetails: null,

    jusPayTokenizeStatus: null,
    jusPayTokenizeError: null,
    jusPayTokenizeDetails: null,

    createJusPayOrderStatus: null,
    createJusPayOrderError: null,
    createJusPayOrderDetails: null,
    jusPaymentLoader: false,
    selectDeliveryModeLoader: false,
    transactionStatus: null,
    loginFromMyBag: false
  },
  action
) => {
  let updatedCartDetailsCNC;
  switch (action.type) {
    case CLEAR_ERROR:
      return Object.assign({}, state, {
        userCartError: null,
        cartDetailsError: null,
        cartDetailsCNCError: null,
        couponError: null,
        emiBankError: null,
        softReserveError: null,
        paymentsModeError: null,
        bankOfferError: null,
        cliqCashPaymentStatusError: null,
        jusPayError: null,
        transactionDetailsError: null,
        orderConfirmationDetailsError: null,
        jusPayPaymentDetailsError: null,
        codEligibilityError: null,
        binValidationCODError: null,
        updateQuantityLoggedInError: null,
        updateQuantityLoggedOutError: null,
        returnCliqPiqError: null,
        AddUserAddressError: null,
        softReservationForPaymentError: null,
        jusPayTokenizeError: null,
        createJusPayOrderError: null,
        transactionCODError: null,
        orderSummaryError: null,
        storeError: null,
        paymentModesError: null,
        justPayPaymentDetailsError: null,
        orderExperienceError: null,
        binValidationError: null,
        addToWishlistError: null,
        removeCartItemError: null,
        removeCartItemLoggedOutError: null,
        getUserAddressError: null
      });
    case cartActions.CART_DETAILS_REQUEST:
      return Object.assign({}, state, {
        cartDetailsStatus: action.status,
        cartDetailsError: null,
        loading: true
      });

    case cartActions.CART_DETAILS_SUCCESS:
      return Object.assign({}, state, {
        cartDetailsStatus: action.status,
        cartDetails: action.cartDetails,
        cartDetailsError: null,
        loading: false
      });

    case cartActions.CART_DETAILS_FAILURE:
      return Object.assign({}, state, {
        cartDetailsStatus: action.status,
        cartDetailsError: action.error,
        loading: false
      });

    case cartActions.APPLY_USER_COUPON_REQUEST:
      return Object.assign({}, state, {
        couponStatus: action.status,
        loading: true
      });

    case cartActions.APPLY_USER_COUPON_SUCCESS:
      let couponList = cloneDeep(state.coupons.opencouponsList);

      let couponDetails = find(couponList, coupon => {
        return coupon.couponCode === action.couponCode;
      });
      let date = couponDetails.couponExpiryDate;
      let expiryTime = new Date(date.split(IST_TIME_ZONE).join());
      let expiryCouponDate = expiryTime.getTime();
      Cookies.createCookie(COUPON_COOKIE, action.couponCode, expiryCouponDate);

      let carDetailsCopy = cloneDeep(state.cartDetails);
      let cartAmount = action.couponResult.cartAmount;
      carDetailsCopy.cartAmount = cartAmount;
      return Object.assign({}, state, {
        couponStatus: action.status,
        cartDetails: carDetailsCopy,
        loading: false
      });

    case cartActions.APPLY_USER_COUPON_FAILURE:
      return Object.assign({}, state, {
        couponStatus: action.status,
        couponError: action.error,
        loading: false
      });

    case cartActions.RELEASE_USER_COUPON_REQUEST:
      return Object.assign({}, state, {
        couponStatus: action.status,
        loading: true
      });

    case cartActions.RELEASE_USER_COUPON_SUCCESS:
      Cookies.deleteCookie(COUPON_COOKIE);
      return Object.assign({}, state, {
        couponStatus: action.status,
        loading: false
      });

    case cartActions.RELEASE_USER_COUPON_FAILURE:
      return Object.assign({}, state, {
        couponStatus: action.status,
        couponError: action.error,
        loading: false
      });

    case cartActions.SELECT_DELIVERY_MODES_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        selectDeliveryModeLoader: true
      });

    case cartActions.SELECT_DELIVERY_MODES_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        deliveryModes: action.deliveryModes
      });

    case cartActions.SELECT_DELIVERY_MODES_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        selectDeliveryModeLoader: false
      });

    case cartActions.GET_USER_ADDRESS_REQUEST:
      return Object.assign({}, state, {
        getUserAddressStatus: action.status,
        loading: true
      });

    case cartActions.GET_USER_ADDRESS_SUCCESS:
      return Object.assign({}, state, {
        getUserAddressStatus: action.status,
        userAddress: action.userAddress,
        loading: false
      });

    case cartActions.GET_USER_ADDRESS_FAILURE:
      return Object.assign({}, state, {
        getUserAddressStatus: action.status,
        getUserAddressError: action.error,
        loading: false
      });
    case cartActions.CART_DETAILS_CNC_REQUEST:
      return Object.assign({}, state, {
        cartDetailsCNCStatus: action.status,
        loading: true
      });
    case cartActions.CART_DETAILS_CNC_SUCCESS: {
      return Object.assign({}, state, {
        cartDetailsCNCStatus: action.status,
        setAddress: action.setAddress,
        userAddress: action.cartDetailsCnc.addressDetailsList,
        cartDetailsCNC: action.cartDetailsCnc,
        loading: false
      });
    }
    case cartActions.CART_DETAILS_CNC_FAILURE:
      return Object.assign({}, state, {
        cartDetailsCNCStatus: action.status,
        cartDetailsCNCError: action.error,
        loading: false
      });

    case cartActions.NET_BANKING_DETAILS_REQUEST:
      return Object.assign({}, state, {
        netBankDetailsStatus: action.status,
        loading: true
      });

    case cartActions.NET_BANKING_DETAILS_SUCCESS:
      return Object.assign({}, state, {
        netBankDetailsStatus: action.status,
        netBankDetails: action.netBankDetails,
        loading: false
      });

    case cartActions.NET_BANKING_DETAILS_FAILURE:
      return Object.assign({}, state, {
        netBankDetailsStatus: action.status,
        netBankDetailsError: action.error,
        loading: false
      });

    case cartActions.EMI_BANKING_DETAILS_REQUEST:
      return Object.assign({}, state, {
        emiBankStatus: action.status,
        loading: true
      });

    case cartActions.EMI_BANKING_DETAILS_SUCCESS:
      return Object.assign({}, state, {
        emiBankStatus: action.status,
        emiBankDetails: action.emiBankDetails,
        loading: false
      });

    case cartActions.EMI_BANKING_DETAILS_FAILURE:
      return Object.assign({}, state, {
        emiBankStatus: action.status,
        emiBankError: action.error,
        loading: false
      });

    case cartActions.GENERATE_CART_ID_FOR_LOGGED_IN_USER_SUCCESS:
      let cartDetails = Cookies.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
      if (!cartDetails) {
        Cookies.createCookie(
          CART_DETAILS_FOR_LOGGED_IN_USER,
          JSON.stringify(action.cartDetails)
        );
      }
      return Object.assign({}, state, {
        cartIdForLoggedInUserStatus: action.status
      });
    case cartActions.GENERATE_CART_ID_FOR_LOGGED_IN_USER_REQUEST:
      return Object.assign({}, state, {
        cartIdForLoggedInUserStatus: action.status
      });
    case cartActions.GENERATE_CART_ID_FOR_LOGGED_IN_USER_FAILURE:
      return Object.assign({}, state, {
        cartIdForLoggedInUserStatus: action.status,
        error: action.error
      });

    case cartActions.GENERATE_CART_ID_FOR_ANONYMOUS_USER_SUCCESS:
      Cookies.createCookie(
        CART_DETAILS_FOR_ANONYMOUS,
        JSON.stringify(action.cartDetails)
      );
      return Object.assign({}, state, {
        cartIdForAnonymousUserStatus: action.status
      });
    case cartActions.GENERATE_CART_ID_FOR_ANONYMOUS_USER_REQUEST:
      return Object.assign({}, state, {
        cartIdForAnonymousUserStatus: action.status
      });
    case cartActions.GENERATE_CART_ID_FOR_ANONYMOUS_USER_FAILURE:
      return Object.assign({}, state, {
        cartIdForAnonymousUserStatus: action.status,
        error: action.error
      });

    case cartActions.ORDER_SUMMARY_REQUEST:
      return Object.assign({}, state, {
        orderSummaryStatus: action.status,
        selectDeliveryModeLoader: true
      });

    case cartActions.ORDER_SUMMARY_SUCCESS:
      return Object.assign({}, state, {
        orderSummaryStatus: action.status,
        orderSummary: action.orderSummary,
        selectDeliveryModeLoader: false
      });

    case cartActions.ORDER_SUMMARY_FAILURE:
      return Object.assign({}, state, {
        orderSummaryStatus: action.status,
        orderSummaryError: action.error,
        selectDeliveryModeLoader: false
      });

    case cartActions.GET_CART_ID_REQUEST:
      return Object.assign({}, state, {
        status: action.status
      });
    case cartActions.GET_CART_ID_SUCCESS:
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
        mergeCartIdStatus: action.status
      });

    case cartActions.MERGE_CART_ID_SUCCESS:
      Cookies.createCookie(
        CART_DETAILS_FOR_LOGGED_IN_USER,
        JSON.stringify(action.cartDetails)
      );
      Cookies.deleteCookie(CART_DETAILS_FOR_ANONYMOUS);
      return Object.assign({}, state, {
        mergeCartIdStatus: action.status,
        type: action.type
      });

    case cartActions.MERGE_CART_ID_FAILURE:
      return Object.assign({}, state, {
        mergeCartIdStatus: action.status,
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
      const currentCartDetailsCNC = cloneDeep(state.cartDetails);
      updatedCartDetailsCNC = Object.assign({}, action.cartDetailsCNC, {
        cartAmount: currentCartDetailsCNC.cartAmount
      });
      return Object.assign({}, state, {
        cartDetailsCNCStatus: action.status,
        cartDetailsCNC: updatedCartDetailsCNC,
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
        selectDeliveryModeLoader: true
      });

    case cartActions.SOFT_RESERVATION_SUCCESS:
      return Object.assign({}, state, {
        softReserveStatus: action.status,
        softReserve: action.softReserve
      });

    case cartActions.SOFT_RESERVATION_FAILURE:
      return Object.assign({}, state, {
        softReserveStatus: action.status,
        softReserveError: action.error,
        selectDeliveryModeLoader: false
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
        cliqCashPaymentStatus: action.status,
        loading: true
      });

    case cartActions.APPLY_CLIQ_CASH_SUCCESS: {
      return Object.assign({}, state, {
        cliqCashPaymentStatus: action.status,
        cliqCashPaymentDetails: action.paymentDetails,
        loading: false
      });
    }

    case cartActions.APPLY_CLIQ_CASH_FAILURE:
      return Object.assign({}, state, {
        cliqCashPaymentStatus: action.status,
        cliqCashPaymentStatusError: action.error,
        loading: false
      });

    case cartActions.REMOVE_CLIQ_CASH_REQUEST:
      return Object.assign({}, state, {
        cliqCashPaymentStatus: action.status,
        loading: true
      });

    case cartActions.REMOVE_CLIQ_CASH_SUCCESS: {
      return Object.assign({}, state, {
        cliqCashPaymentStatus: action.status,
        cliqCashPaymentDetails: action.paymentDetails,
        loading: false
      });
    }

    case cartActions.REMOVE_CLIQ_CASH_FAILURE:
      return Object.assign({}, state, {
        cliqCashPaymentStatus: action.status,
        cliqCashPaymentStatusError: action.error,
        loading: false
      });

    case cartActions.CREATE_JUS_PAY_ORDER_REQUEST:
      return Object.assign({}, state, {
        jusPayStatus: action.status,
        jusPaymentLoader: true
      });

    case cartActions.CREATE_JUS_PAY_ORDER_SUCCESS: {
      return Object.assign({}, state, {
        jusPayStatus: action.status,
        jusPayDetails: action.jusPayDetails
      });
    }
    case cartActions.CREATE_JUS_PAY_ORDER_FOR_CLIQ_CASH_SUCCESS: {
      const cartDetails = Cookies.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
      const cartDetailsGuid = JSON.parse(cartDetails).guid;
      localStorage.setItem(OLD_CART_GU_ID, cartDetailsGuid);

      // here is where I need to destroy the cart details
      Cookies.deleteCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
      return Object.assign({}, state, {
        jusPayStatus: action.status,
        cliqCashJusPayDetails: action.cliqCashJusPayDetails,
        jusPaymentLoader: false
      });
    }

    case cartActions.CREATE_JUS_PAY_ORDER_FAILURE:
      return Object.assign({}, state, {
        jusPayStatus: action.status,
        jusPayError: action.error,
        jusPaymentLoader: false
      });

    case cartActions.BIN_VALIDATION_REQUEST:
      return Object.assign({}, state, {
        binValidationStatus: action.status,
        loading: true
      });

    case cartActions.BIN_VALIDATION_SUCCESS: {
      return Object.assign({}, state, {
        binValidationStatus: action.status,
        binValidationDetails: action.jusPayDetails,
        loading: false
      });
    }

    case cartActions.BIN_VALIDATION_FAILURE:
      return Object.assign({}, state, {
        binValidationStatus: action.status,
        binValidationError: action.error,
        loading: false
      });

    case cartActions.UPDATE_TRANSACTION_DETAILS_REQUEST:
      return Object.assign({}, state, {
        transactionStatus: action.status,
        jusPaymentLoader: true
      });

    case cartActions.UPDATE_TRANSACTION_DETAILS_SUCCESS: {
      localStorage.removeItem(OLD_CART_GU_ID);
      return Object.assign({}, state, {
        jusPayDetails: action.jusPayDetails
      });
    }

    case cartActions.UPDATE_TRANSACTION_DETAILS_FAILURE:
      localStorage.removeItem(OLD_CART_GU_ID);
      return Object.assign({}, state, {
        transactionStatus: action.status,
        jusPayError: action.error,
        jusPaymentLoader: false
      });

    case cartActions.ORDER_CONFIRMATION_REQUEST:
      return Object.assign({}, state, {
        orderConfirmationDetailsStatus: action.status
      });

    case cartActions.ORDER_CONFIRMATION_SUCCESS: {
      return Object.assign({}, state, {
        orderConfirmationDetailsStatus: action.status,
        orderConfirmationDetails: action.confirmedOrderDetails,
        transactionStatus: action.status,
        jusPaymentLoader: false
      });
    }

    case cartActions.ORDER_CONFIRMATION_FAILURE:
      return Object.assign({}, state, {
        orderConfirmationDetailsStatus: action.status,
        transactionStatus: action.status,
        orderConfirmationDetailsError: action.error,
        jusPaymentLoader: false
      });

    case cartActions.CLEAR_ORDER_EXPERIENCE_CAPTURE:
      return Object.assign({}, state, {
        orderConfirmationDetailsStatus: null
      });

    case cartActions.JUS_PAY_PAYMENT_METHOD_TYPE_REQUEST:
      return Object.assign({}, state, {
        justPayPaymentDetailsStatus: action.status,
        jusPaymentLoader: true
      });

    case cartActions.JUS_PAY_PAYMENT_METHOD_TYPE_SUCCESS: {
      const cartDetails = Cookies.getCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
      const cartDetailsGuid = JSON.parse(cartDetails).guid;
      localStorage.setItem(OLD_CART_GU_ID, cartDetailsGuid);

      // here is where I need to destroy the cart details
      Cookies.deleteCookie(CART_DETAILS_FOR_LOGGED_IN_USER);
      return Object.assign({}, state, {
        justPayPaymentDetailsStatus: action.status,
        justPayPaymentDetails: action.justPayPaymentDetails,
        jusPaymentLoader: false
      });
    }

    case cartActions.JUS_PAY_PAYMENT_METHOD_TYPE_FOR_GIFT_CARD_SUCCESS: {
      localStorage.setItem(OLD_CART_GU_ID, action.guId);
      return Object.assign({}, state, {
        justPayPaymentDetailsStatus: action.status,
        justPayPaymentDetails: action.justPayPaymentDetails,
        jusPaymentLoader: false
      });
    }

    case cartActions.JUS_PAY_PAYMENT_METHOD_TYPE_FAILURE:
      return Object.assign({}, state, {
        justPayPaymentDetailsStatus: action.status,
        justPayPaymentDetailsError: action.error,
        jusPaymentLoader: false
      });

    case cartActions.GET_COD_ELIGIBILITY_REQUEST:
      return Object.assign({}, state, {
        codEligibilityStatus: action.status,
        loading: true
      });

    case cartActions.GET_COD_ELIGIBILITY_SUCCESS: {
      return Object.assign({}, state, {
        codEligibilityStatus: action.status,
        codEligibilityDetails: action.codEligibilityDetails,
        loading: false
      });
    }

    case cartActions.ORDER_EXPERIENCE_CAPTURE_REQUEST:
      return Object.assign({}, state, {
        orderExperienceStatus: action.status
      });

    case cartActions.ORDER_EXPERIENCE_CAPTURE_SUCCESS: {
      return Object.assign({}, state, {
        orderExperienceStatus: action.status,
        orderExperience: action.orderExperience
      });
    }

    case cartActions.ORDER_EXPERIENCE_CAPTURE_FAILURE:
      return Object.assign({}, state, {
        orderExperienceStatus: action.status,
        orderExperienceError: action.error
      });

    case cartActions.GET_COD_ELIGIBILITY_FAILURE:
      return Object.assign({}, state, {
        codEligibilityStatus: action.status,
        codEligibilityError: action.error,
        loading: false
      });

    case cartActions.BIN_VALIDATION_COD_REQUEST:
      return Object.assign({}, state, {
        binValidationCODStatus: action.status,
        loading: false
      });

    case cartActions.BIN_VALIDATION_COD_SUCCESS: {
      return Object.assign({}, state, {
        binValidationCODStatus: action.status,
        binValidationCODDetails: action.binValidationCODDetails,
        loading: false
      });
    }

    case cartActions.BIN_VALIDATION_COD_FAILURE:
      return Object.assign({}, state, {
        binValidationCODStatus: action.status,
        binValidationCODError: action.error,
        loading: false
      });

    case cartActions.UPDATE_TRANSACTION_DETAILS_FOR_COD_REQUEST:
      return Object.assign({}, state, {
        transactionDetailsStatus: action.status,
        loading: true
      });

    case cartActions.UPDATE_TRANSACTION_DETAILS_FOR_COD_SUCCESS:
      return Object.assign({}, state, {
        transactionDetailsStatus: action.status,
        transactionDetailsDetails: action.transactionDetails,
        loading: false
      });

    case cartActions.UPDATE_TRANSACTION_DETAILS_FOR_COD_FAILURE:
      return Object.assign({}, state, {
        transactionDetailsStatus: action.status,
        transactionDetailsError: action.error,
        loading: false
      });

    case cartActions.SOFT_RESERVATION_FOR_COD_PAYMENT_REQUEST:
      return Object.assign({}, state, {
        softReserveCODPaymentStatus: action.status,
        loading: true
      });

    case cartActions.SOFT_RESERVATION_FOR_COD_PAYMENT_SUCCESS:
      return Object.assign({}, state, {
        softReserveCODPaymentStatus: action.status,
        softReserveCODPayment: action.softReserveCODPayment,
        loading: false
      });

    case cartActions.SOFT_RESERVATION_FOR_COD_PAYMENT_FAILURE:
      return Object.assign({}, state, {
        softReserveCODPaymentStatus: action.status,
        softReserveCODPaymentError: action.error,
        loading: false
      });

    case cartActions.ADD_PRODUCT_TO_WISH_LIST_REQUEST:
      return Object.assign({}, state, {
        addToWishlistStatus: action.status,
        loading: true
      });

    case cartActions.ADD_PRODUCT_TO_WISH_LIST_SUCCESS:
      return Object.assign({}, state, {
        addToWishlistStatus: action.status,
        loading: false
      });

    case cartActions.ADD_PRODUCT_TO_WISH_LIST_FAILURE:
      return Object.assign({}, state, {
        addToWishlistStatus: action.status,
        addToWishlistError: action.error,
        loading: false
      });

    case cartActions.REMOVE_ITEM_FROM_CART_LOGGED_IN_REQUEST:
      return Object.assign({}, state, {
        removeCartItemStatus: action.status,
        loading: true
      });

    case cartActions.REMOVE_ITEM_FROM_CART_LOGGED_IN_SUCCESS:
      return Object.assign({}, state, {
        removeCartItemStatus: action.status,
        loading: false
      });

    case cartActions.REMOVE_ITEM_FROM_CART_LOGGED_IN_FAILURE:
      return Object.assign({}, state, {
        removeCartItemStatus: action.status,
        removeCartItemError: action.error,
        loading: false
      });

    case cartActions.REMOVE_ITEM_FROM_CART_LOGGED_OUT_REQUEST:
      return Object.assign({}, state, {
        removeCartItemLoggedOutStatus: action.status,
        loading: true
      });

    case cartActions.REMOVE_ITEM_FROM_CART_LOGGED_OUT_SUCCESS:
      return Object.assign({}, state, {
        removeCartItemLoggedOutStatus: action.status,
        loading: false
      });

    case cartActions.REMOVE_ITEM_FROM_CART_LOGGED_OUT_FAILURE:
      return Object.assign({}, state, {
        removeCartItemLoggedOutStatus: action.status,
        removeCartItemLoggedOutError: action.error,
        loading: false
      });

    case cartActions.UPDATE_QUANTITY_IN_CART_LOGGED_IN_REQUEST:
      return Object.assign({}, state, {
        updateQuantityLoggedInStatus: action.status,
        loading: true
      });

    case cartActions.UPDATE_QUANTITY_IN_CART_LOGGED_IN_SUCCESS:
      return Object.assign({}, state, {
        updateQuantityLoggedInStatus: action.status,
        updateQuantityLoggedInDetails: action.updateQuantityDetails,
        loading: false
      });

    case cartActions.UPDATE_QUANTITY_IN_CART_LOGGED_IN_FAILURE:
      return Object.assign({}, state, {
        updateQuantityLoggedInStatus: action.status,
        updateQuantityLoggedInError: action.error,
        loading: false
      });

    case cartActions.UPDATE_QUANTITY_IN_CART_LOGGED_OUT_REQUEST:
      return Object.assign({}, state, {
        updateQuantityLoggedOutStatus: action.status,
        loading: true
      });

    case cartActions.UPDATE_QUANTITY_IN_CART_LOGGED_OUT_SUCCESS:
      return Object.assign({}, state, {
        updateQuantityLoggedOutStatus: action.status,
        updateQuantityLoggedOutDetails: action.updateQuantityDetails,
        loading: false
      });

    case cartActions.UPDATE_QUANTITY_IN_CART_LOGGED_OUT_FAILURE:
      return Object.assign({}, state, {
        updateQuantityLoggedOutStatus: action.status,
        updateQuantityLoggedOutError: action.error,
        loading: false
      });

    case cartActions.ADD_USER_ADDRESS_REQUEST:
      return Object.assign({}, state, {
        AddUserAddressStatus: action.status,
        loading: true
      });
    case cartActions.ADD_USER_ADDRESS_SUCCESS:
      return Object.assign({}, state, {
        AddUserAddressStatus: action.status,
        loading: false
      });

    case cartActions.ADD_USER_ADDRESS_FAILURE:
      return Object.assign({}, state, {
        AddUserAddressStatus: action.status,
        loading: false
      });
    case cartActions.DISPLAY_COUPON_REQUEST:
      return Object.assign({}, state, {
        couponStatus: action.status,
        loading: true
      });

    case cartActions.DISPLAY_COUPON_SUCCESS:
      return Object.assign({}, state, {
        couponStatus: action.status,
        coupons: action.couponDetails,
        loading: false
      });

    case cartActions.DISPLAY_COUPON_FAILURE:
      return Object.assign({}, state, {
        couponStatus: action.status,
        couponError: action.error,
        loading: false
      });

    case cartActions.SOFT_RESERVATION_FOR_PAYMENT_REQUEST:
      return Object.assign({}, state, {
        softReservationForPaymentStatus: action.status,
        jusPaymentLoader: true
      });

    case cartActions.SOFT_RESERVATION_FOR_PAYMENT_SUCCESS:
      return Object.assign({}, state, {
        softReservationForPaymentStatus: action.status,
        softReservationForPaymentDetails: action.orderDetails
      });

    case cartActions.SOFT_RESERVATION_FOR_PAYMENT_FAILURE:
      return Object.assign({}, state, {
        softReservationForPaymentStatus: action.status,
        softReservationForPaymentError: action.error,
        jusPaymentLoader: false
      });

    case cartActions.JUS_PAY_TOKENIZE_REQUEST:
      return Object.assign({}, state, {
        jusPayTokenizeStatus: action.status,
        jusPaymentLoader: true
      });

    case cartActions.JUS_PAY_TOKENIZE_SUCCESS:
      return Object.assign({}, state, {
        jusPayTokenizeStatus: action.status,
        jusPayTokenizeDetails: action.jusPayToken
      });

    case cartActions.JUS_PAY_TOKENIZE_FAILURE:
      return Object.assign({}, state, {
        jusPayTokenizeStatus: action.status,
        jusPayTokenizeError: action.error,
        jusPaymentLoader: false
      });

    default:
      return state;
  }
};

export default cart;
