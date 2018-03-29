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
  getCoupons,
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
  createJusPayOrderForGiftCardFromSavedCards
} from "../actions/cart.actions";
import { showModal, BANK_OFFERS } from "../../general/modal.actions";
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
          isSoftReservation
        )
      );
    },
    getUserAddress: () => {
      dispatch(getUserAddress());
    },
    addUserAddress: (userAddress, getCartDetailCNCObj) => {
      dispatch(addUserAddress(userAddress)).then(() =>
        dispatch(
          getCartDetailsCNC(
            getCartDetailCNCObj.userId,
            getCartDetailCNCObj.accessToken,
            getCartDetailCNCObj.cartId,
            getCartDetailCNCObj.pinCode,
            getCartDetailCNCObj.isSoftReservation
          )
        )
      );
    },
    addAddressToCart: (addressId, pinCode) => {
      dispatch(addAddressToCart(addressId, pinCode));
    },
    getOrderSummary: pinCode => {
      dispatch(getOrderSummary(pinCode));
    },
    getCoupons: () => {
      dispatch(getCoupons());
    },
    applyUserCoupon: () => {
      dispatch(applyUserCoupon());
    },
    releaseUserCoupon: () => {
      dispatch(releaseUserCoupon());
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
      dispatch(addPickupPersonCNC(personMobile, personName));
    },
    softReservation: (pinCode, payload) => {
      dispatch(softReservation(pinCode, payload));
    },
    getPaymentModes: guIdDetails => {
      dispatch(getPaymentModes(guIdDetails));
    },
    showCouponModal: data => {
      dispatch(showModal(BANK_OFFERS, data));
    },
    applyBankOffer: couponCode => {
      dispatch(applyBankOffer(couponCode));
    },
    releaseBankOffer: couponCode => {
      dispatch(releaseBankOffer(couponCode));
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
    captureOrderExperience: (orderId, Rating) => {
      dispatch(captureOrderExperience(orderId, Rating));
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
    softReservationPaymentForSavedCard: (cardDetails, address, paymentMode) => {
      dispatch(
        softReservationPaymentForSavedCard(cardDetails, address, paymentMode)
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
    }
  };
};
const mapStateToProps = state => {
  return {
    cart: state.cart
  };
};

const CheckoutAddressContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CheckOutPage)
);
export default CheckoutAddressContainer;
