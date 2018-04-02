import { connect } from "react-redux";
import { displayToast, TOAST_DELAY } from "../toast.actions.js";
import { clearError } from "../error.actions.js";
import React from "react";
import delay from "lodash/delay";
import keys from "lodash/keys";
import each from "lodash/each";

const CLEAR_ERROR_DELAY = TOAST_DELAY + 500;

// The errors for user, pdp and plp are universal errors
// This means that they need to be dealt with separately here (meaning that the entire reducer has an error key)
// The other types of errors (like state.cart.userCartError) NEED to have the same key

const mapStateToProps = state => {
  return {
    userError: state.user.error,
    pdpError: state.productDescription.error,
    plpError: state.productListings.error,
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

    if (prevProps.userError !== this.props.userError) {
      if (this.props.userError !== "" && this.props.userError !== null) {
        this.displayError(this.props.userError);
        return;
      }
    }

    if (prevProps.plpError !== this.props.plpError) {
      if (this.props.plpError !== "" && this.props.plpError !== null) {
        this.displayError(this.props.plpError);
        return;
      }
    }

    if (prevProps.pdpError !== this.props.pdpError) {
      if (this.props.pdpError !== "" && this.props.pdpError !== null) {
        this.displayError(this.props.pdpError);
        return;
      }
    }

    each(errorKeys, key => {
      const previousError = prevProps[key];
      const currentError = this.props[key];
      if (previousError !== currentError) {
        if (currentError !== "" && currentError !== null && !seenError) {
          this.displayError(currentError);
          seenError = true;
        }
      }
    });
  }

  displayError(message) {
    this.props.displayToast(message);
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
