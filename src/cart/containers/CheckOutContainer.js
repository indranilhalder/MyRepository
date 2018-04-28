import { connect } from "react-redux";
import { withRouter } from "react-router";
import CheckOutPage from "../components/CheckOutPage";
import {
  getCartDetailsCNC,
  addUserAddress,
  addAddressToCart,
  getUserAddress,
  selectDeliveryMode,
  getOrderSummary,
  applyUserCoupon,
  releaseUserCoupon,
  getAllStoresCNC,
  addStoreCNC,
  addPickupPersonCNC,
  softReservation,
  getPaymentModes,
  applyBankOffer,
  releaseBankOffer,
  getNetBankDetails,
  getEmiBankDetails,
  applyCliqCash,
  removeCliqCash,
  binValidation,
  softReservationForPayment,
  updateTransactionDetails,
  getCODEligibility,
  binValidationForCOD,
  updateTransactionDetailsForCOD,
  softReservationForCODPayment,
  captureOrderExperience,
  binValidationForNetBanking,
  softReservationPaymentForNetBanking,
  softReservationPaymentForSavedCard,
  orderConfirmation,
  softReservationForCliqCash,
  jusPayTokenizeForGiftCard,
  createJusPayOrderForGiftCardNetBanking,
  createJusPayOrderForGiftCardFromSavedCards,
  clearCaptureOrderExperience,
  applyUserCouponForAnonymous,
  getEmiEligibility,
  getBankAndTenureDetails,
  getEmiTermsAndConditionsForBank,
  applyNoCostEmi,
  removeNoCostEmi,
  getItemBreakUpDetails,
  getPaymentFailureOrderDetails,
  createJusPayOrderForSavedCards,
  createJusPayOrderForCliqCash,
  clearCartDetails
} from "../actions/cart.actions";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";
import {
  showModal,
  BANK_OFFERS,
  GIFT_CARD_MODAL
} from "../../general/modal.actions";
import {
  getPinCode,
  getUserDetails,
  getPinCodeSuccess
} from "../../account/actions/account.actions.js";
import { displayToast } from "../../general/toast.actions";
import { SUCCESS } from "../../lib/constants";
import { setHeaderText } from "../../general/header.actions.js";
import {
  setDataLayerForCheckoutDirectCalls,
  ADOBE_ADD_NEW_ADDRESS_ON_CHECKOUT_PAGE,
  ADOBE_FINAL_PAYMENT_MODES,
  ADOBE_CALL_FOR_SEE_ALL_BANK_OFFER
} from "../../lib/adobeUtils";

const mapDispatchToProps = dispatch => {
  return {
    getCartDetailsCNC: (
      userId,
      accessToken,
      cartId,
      pinCode,
      isSoftReservation
    ) => {
      dispatch(
        getCartDetailsCNC(
          userId,
          accessToken,
          cartId,
          pinCode,
          isSoftReservation,
          true // this is using to setting data layer for first time when page loads
        )
      );
    },
    getUserAddress: () => {
      dispatch(getUserAddress());
    },
    addUserAddress: (userAddress, getCartDetailCNCObj) => {
      dispatch(addUserAddress(userAddress)).then(() => {
        dispatch(
          getCartDetailsCNC(
            getCartDetailCNCObj.userId,
            getCartDetailCNCObj.accessToken,
            getCartDetailCNCObj.cartId,
            getCartDetailCNCObj.pinCode,
            getCartDetailCNCObj.isSoftReservation
          )
        );
      });
    },
    addAddressToCart: (addressId, pinCode) => {
      dispatch(addAddressToCart(addressId, pinCode));
    },
    getOrderSummary: pinCode => {
      dispatch(getOrderSummary(pinCode));
    },

    applyUserCouponForAnonymous: couponCode => {
      dispatch(applyUserCouponForAnonymous(couponCode));
    },
    releaseUserCoupon: () => {
      dispatch(releaseUserCoupon());
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },
    selectDeliveryMode: (deliveryUssId, pinCode) => {
      dispatch(selectDeliveryMode(deliveryUssId, pinCode));
    },
    getAllStoresCNC: pinCode => {
      dispatch(getAllStoresCNC(pinCode));
    },
    addStoreCNC: (ussId, slaveId) => {
      dispatch(addStoreCNC(ussId, slaveId));
    },
    addPickupPersonCNC: (personMobile, personName) => {
      return dispatch(addPickupPersonCNC(personMobile, personName));
    },
    softReservation: (pinCode, payload) => {
      dispatch(softReservation(pinCode, payload));
    },
    getPaymentModes: guIdDetails => {
      dispatch(getPaymentModes(guIdDetails));
    },
    showCouponModal: data => {
      setDataLayerForCheckoutDirectCalls(ADOBE_CALL_FOR_SEE_ALL_BANK_OFFER);
      dispatch(showModal(BANK_OFFERS, data));
    },
    applyBankOffer: couponCode => {
      return dispatch(applyBankOffer(couponCode));
    },
    releaseBankOffer: couponCode => {
      return dispatch(releaseBankOffer(couponCode));
    },
    getNetBankDetails: () => {
      dispatch(getNetBankDetails());
    },
    getEmiBankDetails: cartTotalProducts => {
      dispatch(getEmiBankDetails(cartTotalProducts));
    },
    applyCliqCash: pinCode => {
      dispatch(applyCliqCash(pinCode));
    },
    removeCliqCash: pinCode => {
      dispatch(removeCliqCash(pinCode));
    },
    binValidation: (paymentMode, binNo) => {
      dispatch(binValidation(paymentMode, binNo));
    },
    softReservationForPayment: (cardDetails, address, paymentMode) => {
      dispatch(softReservationForPayment(cardDetails, address, paymentMode));
    },
    updateTransactionDetails: (paymentMode, juspayOrderID, cartId) => {
      dispatch(updateTransactionDetails(paymentMode, juspayOrderID, cartId));
    },
    getCODEligibility: () => {
      dispatch(getCODEligibility());
    },
    binValidationForCOD: paymentMode => {
      dispatch(binValidationForCOD(paymentMode));
    },
    updateTransactionDetailsForCOD: (paymentMode, juspayOrderID) => {
      dispatch(updateTransactionDetailsForCOD(paymentMode, juspayOrderID));
    },
    softReservationForCODPayment: pinCode => {
      dispatch(softReservationForCODPayment(pinCode));
    },
    captureOrderExperience: async (orderId, Rating) => {
      const response = await dispatch(captureOrderExperience(orderId, Rating));
      if (response.status === SUCCESS) {
        dispatch(displayToast(response.orderExperience.message));
      }
    },
    binValidationForNetBanking: (paymentMode, binNo) => {
      dispatch(binValidationForNetBanking(paymentMode, binNo));
    },
    softReservationPaymentForNetBanking: (
      paymentMethodType,
      paymentMode,
      bankName,
      pinCode
    ) => {
      dispatch(
        softReservationPaymentForNetBanking(
          paymentMethodType,
          paymentMode,
          bankName,
          pinCode
        )
      );
    },
    softReservationPaymentForSavedCard: (
      cardDetails,
      address,
      paymentMode,
      isPaymentFailed
    ) => {
      dispatch(
        softReservationPaymentForSavedCard(
          cardDetails,
          address,
          paymentMode,
          isPaymentFailed
        )
      );
    },
    softReservationForCliqCash: pinCode => {
      dispatch(softReservationForCliqCash(pinCode));
    },
    jusPayTokenizeForGiftCard: (cardDetails, paymentMode, guId) => {
      dispatch(jusPayTokenizeForGiftCard(cardDetails, paymentMode, guId));
    },
    createJusPayOrderForGiftCardNetBanking: (bankName, guId) => {
      dispatch(createJusPayOrderForGiftCardNetBanking(bankName, guId));
    },
    createJusPayOrderForGiftCardFromSavedCards: (cardDetails, guId) => {
      dispatch(createJusPayOrderForGiftCardFromSavedCards(cardDetails, guId));
    },
    addGiftCard: () => {
      dispatch(showModal(GIFT_CARD_MODAL));
    },
    displayToast: message => {
      dispatch(displayToast(message));
    },
    clearCaptureOrderExperience: () => {
      dispatch(clearCaptureOrderExperience());
    },
    showSecondaryLoader: () => {
      dispatch(showSecondaryLoader());
    },
    hideSecondaryLoader: () => {
      dispatch(hideSecondaryLoader());
    },

    getEmiEligibility: () => {
      dispatch(getEmiEligibility());
    },
    getBankAndTenureDetails: () => {
      dispatch(getBankAndTenureDetails());
    },
    getEmiTermsAndConditionsForBank: (code, bankName) => {
      dispatch(getEmiTermsAndConditionsForBank(code, bankName));
    },
    applyNoCostEmi: couponCode => {
      dispatch(applyNoCostEmi(couponCode));
    },
    removeNoCostEmi: couponCode => {
      dispatch(removeNoCostEmi(couponCode));
    },
    getItemBreakUpDetails: couponCode => {
      dispatch(getItemBreakUpDetails(couponCode));
    },
    getPinCode: pinCode => {
      dispatch(getPinCode(pinCode));
    },

    getUserDetails: () => {
      dispatch(getUserDetails());
    },
    resetAutoPopulateDataForPinCode: () => {
      dispatch(getPinCodeSuccess(null));
    },
    getPaymentFailureOrderDetails: () => {
      dispatch(getPaymentFailureOrderDetails());
    },
    createJusPayOrderForSavedCards: (
      cardDetails,
      cartItem,
      isPaymentFailed
    ) => {
      dispatch(
        createJusPayOrderForSavedCards(cardDetails, cartItem, isPaymentFailed)
      );
    },
    createJusPayOrderForCliqCash: (pinCode, cartItem, isPaymentFailed) => {
      dispatch(
        createJusPayOrderForCliqCash(pinCode, cartItem, isPaymentFailed)
      );
    },
    clearCartDetails: () => {
      dispatch(clearCartDetails());
    }
  };
};
const mapStateToProps = state => {
  return {
    cart: state.cart,
    getPinCodeDetails: state.profile.getPinCodeDetails,
    userDetails: state.profile.userDetails
  };
};

const CheckoutAddressContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CheckOutPage)
);
export default CheckoutAddressContainer;
