import { connect } from "react-redux";
import { displayToast, TOAST_DELAY } from "../toast.actions.js";
import { clearError } from "../error.actions.js";
import React from "react";
import delay from "lodash/delay";
import keys from "lodash/keys";
import each from "lodash/each";

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
    binValidationCODError: state.cart.binValidationCODError,
    plpError: state.productListings.error,
    wishlistError: state.wishlistItems.error,
    reviewsError: state.productDescription.reviewsError
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

// go through all of the

class ErrorDisplay extends React.Component {
  componentDidUpdate(prevProps) {
    const errorKeys = keys(this.props);
    let seenError = false;
    each(errorKeys, key => {
      const previousError = prevProps[key];
      const currentError = this.props[key];
      if (previousError !== currentError) {
        if (currentError !== "" && currentError !== null && !seenError) {
          this.props.displayToast(currentError);
          delay(() => this.props.clearError(), CLEAR_ERROR_DELAY);
          seenError = true;
        }
      }
    });
  }

  render() {
    return null;
  }
}

const ErrorContainer = connect(mapStateToProps, mapDispatchToProps)(
  ErrorDisplay
);

export default ErrorContainer;
