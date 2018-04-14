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
import { SUCCESS, FAILURE } from "../../lib/constants";
import { updateProfile } from "../../account/actions/account.actions.js";

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
import { createWishlist } from "../../wishlist/actions/wishlist.actions";
import {
  singleAuthCallHasFailed,
  setIfAllAuthCallsHaveSucceeded
} from "../../auth/actions/auth.actions.js";
const mapStateToProps = (state, ownProps) => {
  return {
    modalType: state.modal.modalType,
    ownProps: state.modal.ownProps,
    modalStatus: state.modal.modalDisplayed,
    user: state.user,
    loadingForGetOtpToActivateWallet:
      state.profile.loadingForGetOtpToActivateWallet,
    loadingForverifyWallet: state.profile.loadingForverifyWallet
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
      if (otpResponse.status === SUCCESS) {
        const customerAccessResponse = await dispatch(
          customerAccessToken(userDetails)
        );
        if (customerAccessResponse.status === SUCCESS) {
          const createdCartVal = await dispatch(
            generateCartIdForLoggedInUser()
          );
          if (createdCartVal.status === SUCCESS) {
            await dispatch(createWishlist());
            const mergeCartIdResponse = await dispatch(
              mergeCartId(createdCartVal.cartDetails.guid)
            );
            if (mergeCartIdResponse.status === SUCCESS) {
              dispatch(setIfAllAuthCallsHaveSucceeded());
            } else {
              dispatch(singleAuthCallHasFailed(mergeCartIdResponse.error));
            }
          } else if (createdCartVal.status === FAILURE) {
            dispatch(singleAuthCallHasFailed(otpResponse.error));
          }
        } else if (customerAccessResponse.status === FAILURE) {
          dispatch(singleAuthCallHasFailed(otpResponse.error));
        }
      } else if (otpResponse.status === FAILURE) {
        dispatch(singleAuthCallHasFailed(otpResponse.error));
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

    updateProfile: (accountDetails, otp) => {
      dispatch(updateProfile(accountDetails, otp));
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
