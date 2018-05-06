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
import {
  redeemCliqVoucher,
  removeAddress
} from "../../account/actions/account.actions";
import {
  SUCCESS,
  FAILURE,
  CART_DETAILS_FOR_ANONYMOUS,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  ERROR_MESSAGE_FOR_VERIFY_OTP
} from "../../lib/constants";
import { updateProfile } from "../../account/actions/account.actions.js";
import { setUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";
import * as Cookies from "../../lib/Cookie";

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
  releaseUserCoupon,
  removeNoCostEmi
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
import { displayToast } from "../../general/toast.actions";
const mapStateToProps = (state, ownProps) => {
  return {
    modalType: state.modal.modalType,
    ownProps: state.modal.ownProps,
    modalStatus: state.modal.modalDisplayed,
    user: state.user,
    loadingForGetOtpToActivateWallet:
      state.profile.loadingForGetOtpToActivateWallet,
    loadingForVerifyWallet: state.profile.loadingForverifyWallet
  };
};

const mapDispatchToProps = dispatch => {
  return {
    displayToast: toastMessage => {
      dispatch(displayToast(toastMessage));
    },
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
              Cookies.deleteCookie(CART_DETAILS_FOR_ANONYMOUS);
              Cookies.createCookie(
                CART_DETAILS_FOR_LOGGED_IN_USER,
                JSON.stringify(createdCartVal.cartDetails)
              );
              dispatch(setIfAllAuthCallsHaveSucceeded());
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
    applyBankOffer: async couponCode => {
      return await dispatch(applyBankOffer(couponCode));
    },
    releaseBankOffer: (previousCouponCode, newCouponCode) => {
      return dispatch(releaseBankOffer(previousCouponCode, newCouponCode));
    },
    applyUserCouponForAnonymous: couponCode => {
      return dispatch(applyUserCouponForAnonymous(couponCode));
    },
    releaseCouponForAnonymous: (oldCouponCode, newCouponCode) => {
      return dispatch(releaseCouponForAnonymous(oldCouponCode, newCouponCode));
    },
    applyUserCouponForLoggedInUsers: couponCode => {
      return dispatch(applyUserCouponForLoggedInUsers(couponCode));
    },
    releaseUserCoupon: (oldCouponCode, newCouponCode) => {
      return dispatch(releaseUserCoupon(oldCouponCode, newCouponCode));
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

    verifyWallet: async (customerDetailsWithOtp, isFromCliqCash) => {
      const createdCartVal = await dispatch(
        verifyWallet(customerDetailsWithOtp, isFromCliqCash)
      );
      if (createdCartVal.error !== ERROR_MESSAGE_FOR_VERIFY_OTP) {
        dispatch(modalActions.hideModal());
      }
    },

    submitSelfCourierReturnInfo: returnDetails => {
      dispatch(submitSelfCourierReturnInfo(returnDetails));
    },
    redeemCliqVoucher: (cliqCashDetails, fromCheckOut) => {
      dispatch(redeemCliqVoucher(cliqCashDetails, fromCheckOut));
    },
    setUrlToRedirectToAfterAuth: url => {
      dispatch(setUrlToRedirectToAfterAuth(url));
    },
    removeNoCostEmi: (couponCode, cartGuid, cartId) => {
      return dispatch(removeNoCostEmi(couponCode, cartGuid, cartId));
    }
  };
};
const ModalContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ModalRoot)
);

export default ModalContainer;
