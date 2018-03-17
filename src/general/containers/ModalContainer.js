import { connect } from "react-redux";
import ModalRoot from "../components/ModalRoot.js";
import { withRouter } from "react-router-dom";
import * as modalActions from "../modal.actions.js";
import {
  resetPassword,
  otpVerification,
  forgotPassword,
  signUpUser,
  forgotPasswordOtpVerification
} from "../../auth/actions/user.actions";

import {
  applyBankOffer,
  releaseBankOffer,
  applyUserCoupon,
  releaseUserCoupon,
  getUserAddress
} from "../../cart/actions/cart.actions";
const mapStateToProps = (state, ownProps) => {
  return {
    modalType: state.modal.modalType,
    ownProps: state.modal.ownProps,
    modalStatus: state.modal.modalDisplayed,
    user: state.user
  };
};

const mapDispatchToProps = dispatch => {
  return {
    showModal: (type, ownProps = null) => {
      dispatch(modalActions.showModal(type, ownProps));
    },
    hideModal: () => {
      dispatch(modalActions.hideModal());
    },
    otpVerification: (otpDetails, userDetails) => {
      dispatch(otpVerification(otpDetails, userDetails));
    },
    resetPassword: userDetails => {
      dispatch(resetPassword(userDetails));
    },
    forgotPassword: userDetails => {
      dispatch(forgotPassword(userDetails));
    },
    forgotPasswordOtpVerification: (otpDetails, userDetails) => {
      dispatch(forgotPasswordOtpVerification(otpDetails, userDetails));
    },
    resendOTP: userObj => {
      dispatch(signUpUser(userObj));
    },
    applyBankOffer: couponCode => {
      dispatch(applyBankOffer(couponCode));
    },
    releaseBankOffer: couponCode => {
      dispatch(releaseBankOffer(couponCode));
    },
    applyUserCoupon: couponCode => {
      dispatch(applyUserCoupon(couponCode));
    },
    releaseUserCoupon: couponCode => {
      dispatch(releaseUserCoupon(couponCode));
    },
    getUserAddress: () => {
      dispatch(getUserAddress());
    }
  };
};
const ModalContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ModalRoot)
);

export default ModalContainer;
