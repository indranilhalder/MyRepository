import { connect } from "react-redux";
import { displayToast, TOAST_DELAY } from "../toast.actions.js";
import { clearError, CLEAR_ERROR } from "../error.actions.js";
import React from "react";
import delay from "lodash/delay";

const CLEAR_ERROR_DELAY = TOAST_DELAY + 500;

const mapStateToProps = state => {
  return {
    userError: state.user.error,
    pdpError: state.productDescription.error,
    userCartError: state.cart.userCartError,
    cartDetailsError: state.cart.cartDetailsError,
    cartDetailsCNCError: state.cart.cartDetailsCNCError,
    couponError: state.cart.couponError,
    emiBankError: state.cart.emiBankError,
    softReserveError: state.cart.softReserveError,
    paymentsModeError: state.cart.paymentsModeError,
    bankOfferError: state.cart.bankOfferError,
    cliqCashPaymentStatusError: state.cart.cliqCashPaymentStatusError,
    jusPayError: state.cart.jusPayError,
    transactionDetailsError: state.cart.transactionDetailsError,
    orderConfirmationDetailsError: state.cart.orderConfirmationDetailsError,
    jusPayPaymentDetailsError: state.cart.jusPayPaymentDetailsError,
    codEligibilityError: state.cart.codEligibilityError,
    binValidationCODError: state.cart.binValidationCODError
  };
};

const mapDispatchToProps = dispatch => {
  return {
    displayToast: message => {
      dispatch(displayToast(message));
    },
    clearError: () => {
      dispatch(clearError());
    }
  };
};

// should clear error clear EVERYTHING?

class ErrorDisplay extends React.Component {
  componentDidUpdate(prevProps) {
    if (prevProps.userError !== this.props.userError) {
      if (this.props.userError !== "" && this.props.userError !== null) {
        this.props.displayToast(this.props.userError);
      }
    }

    if (prevProps.pdpError !== this.props.pdpError) {
      if (this.props.pdpError !== "" && this.props.pdpError !== null) {
        this.props.displayToast(this.props.pdpError);
      }
    }
    delay(() => this.props.clearError(), CLEAR_ERROR_DELAY);
  }

  render() {
    return null;
  }
}

const ErrorContainer = connect(mapStateToProps, mapDispatchToProps)(
  ErrorDisplay
);

export default ErrorContainer;
