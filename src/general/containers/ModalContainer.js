import { connect } from "react-redux";
import ModalRoot from "../components/ModalRoot.js";
import { withRouter } from "react-router-dom";
import * as modalActions from "../modal.actions.js";
import {
  resetPassword,
  otpVerification,
  forgotPassword,
  signUpUser,
  forgotPasswordOtpVerification,
  loginUser,
  loginUserRequest,
  customerAccessToken
} from "../../auth/actions/user.actions";
import { redeemCliqVoucher } from "../../account/actions/account.actions";
import { SUCCESS } from "../../lib/constants";

import {
  applyBankOffer,
  releaseBankOffer,
  applyUserCouponForAnonymous,
  getUserAddress,
  mergeCartId,
  generateCartIdForLoggedInUser,
  getCartId,
  applyUserCouponForLoggedInUsers,
  releaseCouponForAnonymous,
  releaseUserCoupon
} from "../../cart/actions/cart.actions";
import {
  getOtpToActivateWallet,
  verifyWallet,
  submitSelfCourierReturnInfo
} from "../../account/actions/account.actions";

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
    loginUser: async userDetails => {
      const loginResponse = await dispatch(loginUser(userDetails));
      if (loginResponse.status === SUCCESS) {
        dispatch(modalActions.hideModal());
      }
    },
    otpVerification: async (otpDetails, userDetails) => {
      const otpResponse = await dispatch(
        otpVerification(otpDetails, userDetails)
      );
      const customerAccessResponse = await dispatch(
        customerAccessToken(userDetails)
      );
      if (customerAccessResponse.status === SUCCESS) {
        if (otpResponse.status === SUCCESS) {
          const loginUserResponse = await dispatch(loginUser(userDetails));
          if (loginUserResponse.status === SUCCESS) {
            const cartVal = await dispatch(getCartId());
            if (
              cartVal.status === SUCCESS &&
              cartVal.cartDetails.guid &&
              cartVal.cartDetails.code
            ) {
              // This is the anonymous case
              // And I have an existing cart that needs to be merged.
              dispatch(mergeCartId(cartVal.cartDetails.guid));
            } else {
              const createdCartVal = await dispatch(
                generateCartIdForLoggedInUser()
              );
              dispatch(mergeCartId(createdCartVal.cartDetails.guid));
            }
          }
        }
      }
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
    applyUserCouponForAnonymous: couponCode => {
      dispatch(applyUserCouponForAnonymous(couponCode));
    },
    releaseCouponForAnonymous: (oldCouponCode, newCouponCode) => {
      dispatch(releaseCouponForAnonymous(oldCouponCode, newCouponCode));
    },
    applyUserCouponForLoggedInUsers: couponCode => {
      dispatch(applyUserCouponForLoggedInUsers(couponCode));
    },
    releaseUserCoupon: (oldCouponCode, newCouponCode) => {
      dispatch(releaseUserCoupon(oldCouponCode, newCouponCode));
    },
    getUserAddress: () => {
      dispatch(getUserAddress());
    },

    getOtpToActivateWallet: (customerDetails, isFromCliqCash) => {
      dispatch(getOtpToActivateWallet(customerDetails, isFromCliqCash));
    },

    verifyWallet: (customerDetailsWithOtp, isFromCliqCash) => {
      dispatch(verifyWallet(customerDetailsWithOtp, isFromCliqCash));
    },

    submitSelfCourierReturnInfo: returnDetails => {
      dispatch(submitSelfCourierReturnInfo(returnDetails));
    },
    redeemCliqVoucher: (cliqCashDetails, fromCheckOut) => {
      dispatch(redeemCliqVoucher(cliqCashDetails, fromCheckOut));
    }
  };
};
const ModalContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ModalRoot)
);

export default ModalContainer;
