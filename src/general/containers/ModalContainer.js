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
  removeAddress,
  cancelProduct
} from "../../account/actions/account.actions";
import {
  SUCCESS,
  FAILURE,
  CART_DETAILS_FOR_ANONYMOUS,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  ERROR_MESSAGE_FOR_VERIFY_OTP,
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  DEFAULT_PIN_CODE_LOCAL_STORAGE,
  MY_ACCOUNT,
  MY_ACCOUNT_ORDERS_PAGE,
  ERROR
} from "../../lib/constants";

import { updateProfile } from "../../account/actions/account.actions.js";
import { setUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";
import * as Cookies from "../../lib/Cookie";
import { setDataLayerForMyAccountDirectCalls } from "../../lib/adobeUtils";
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
  removeNoCostEmi,
  getCartDetails
} from "../../cart/actions/cart.actions";
import {
  getOtpToActivateWallet,
  verifyWallet,
  submitSelfCourierReturnInfo
} from "../../account/actions/account.actions";
import {
  createWishlist,
  getWishListItems
} from "../../wishlist/actions/wishlist.actions";
import {
  singleAuthCallHasFailed,
  setIfAllAuthCallsHaveSucceeded
} from "../../auth/actions/auth.actions.js";
import { displayToast } from "../../general/toast.actions";
import {
  setDataLayerForLogin,
  ADOBE_DIRECT_CALL_FOR_LOGIN_SUCCESS,
  ADOBE_DIRECT_CALL_FOR_LOGIN_FAILURE
} from "../../lib/adobeUtils";
const ERROR_MESSAGE_IN_CANCELING_ORDER = "Error in Canceling order";
const mapStateToProps = (state, ownProps) => {
  return {
    modalType: state.modal.modalType,
    ownProps: state.modal.ownProps,
    modalStatus: state.modal.modalDisplayed,
    user: state.user,
    loadingForGetOtpToActivateWallet:
      state.profile.loadingForGetOtpToActivateWallet,
    loadingForVerifyWallet: state.profile.loadingForverifyWallet,
    loadingForCancelProduct: state.profile.loadingForCancelProduct,
    loading: state.profile.loading
  };
};

const mapDispatchToProps = (dispatch, ownProps) => {
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
        setDataLayerForLogin(ADOBE_DIRECT_CALL_FOR_LOGIN_SUCCESS);
        const cartVal = await dispatch(getCartId());
        if (
          cartVal.status === SUCCESS &&
          cartVal.cartDetails.guid &&
          cartVal.cartDetails.code
        ) {
          // if get old cart id then just merge it with anonymous cart id
          const mergeCartIdWithOldOneResponse = await dispatch(
            mergeCartId(cartVal.cartDetails.guid)
          );

          if (mergeCartIdWithOldOneResponse.status === SUCCESS) {
            const customerCookie = Cookies.getCookie(CUSTOMER_ACCESS_TOKEN);

            const userDetails = Cookies.getCookie(LOGGED_IN_USER_DETAILS);
            const cartDetailsLoggedInUser = Cookies.getCookie(
              CART_DETAILS_FOR_LOGGED_IN_USER
            );
            dispatch(
              getCartDetails(
                JSON.parse(userDetails).userName,
                JSON.parse(customerCookie).access_token,
                JSON.parse(cartDetailsLoggedInUser).code,
                localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
              )
            );
            const existingWishList = await dispatch(getWishListItems());

            if (!existingWishList || !existingWishList.wishlist) {
              dispatch(createWishlist());
            }
            dispatch(setIfAllAuthCallsHaveSucceeded());
          } else if (mergeCartIdWithOldOneResponse.status === ERROR) {
            Cookies.deleteCookie(CART_DETAILS_FOR_ANONYMOUS);
            Cookies.createCookie(
              CART_DETAILS_FOR_LOGGED_IN_USER,
              JSON.stringify(cartVal.cartDetails)
            );
            dispatch(setIfAllAuthCallsHaveSucceeded());
          }
          //end of  merge old cart id with anonymous cart id
        } else {
          // generating new cart if if wont get any existing cartId
          const newCartIdObj = await dispatch(generateCartIdForLoggedInUser());

          if (newCartIdObj.status === SUCCESS) {
            const mergeCartIdResponse = await dispatch(
              mergeCartId(newCartIdObj.cartDetails.guid)
            );
            // merging cart id with new cart id
            if (mergeCartIdResponse.status === SUCCESS) {
              const customerCookie = Cookies.getCookie(CUSTOMER_ACCESS_TOKEN);

              const userDetails = Cookies.getCookie(LOGGED_IN_USER_DETAILS);
              const cartDetailsLoggedInUser = Cookies.getCookie(
                CART_DETAILS_FOR_LOGGED_IN_USER
              );
              dispatch(
                getCartDetails(
                  JSON.parse(userDetails).userName,
                  JSON.parse(customerCookie).access_token,
                  JSON.parse(cartDetailsLoggedInUser).code,
                  localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
                )
              );
              const existingWishList = await dispatch(getWishListItems());
              if (!existingWishList || !existingWishList.wishlist) {
                dispatch(createWishlist());
              }
              dispatch(setIfAllAuthCallsHaveSucceeded());
            } else if (mergeCartIdResponse.status === ERROR) {
              Cookies.deleteCookie(CART_DETAILS_FOR_ANONYMOUS);
              Cookies.createCookie(
                CART_DETAILS_FOR_LOGGED_IN_USER,
                JSON.stringify(newCartIdObj.cartDetails)
              );
              dispatch(setIfAllAuthCallsHaveSucceeded());
            }
            // end of merging cart id with new cart id
          } else if (newCartIdObj.status === ERROR) {
            dispatch(singleAuthCallHasFailed(newCartIdObj.error));
          }
          // end of generating new cart if if wont get any existing cartId
        }
      } else {
        setDataLayerForLogin(ADOBE_DIRECT_CALL_FOR_LOGIN_FAILURE);
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
              const customerCookie = Cookies.getCookie(CUSTOMER_ACCESS_TOKEN);

              const userDetails = Cookies.getCookie(LOGGED_IN_USER_DETAILS);
              const cartDetailsLoggedInUser = Cookies.getCookie(
                CART_DETAILS_FOR_LOGGED_IN_USER
              );
              dispatch(
                getCartDetails(
                  JSON.parse(userDetails).userName,
                  JSON.parse(customerCookie).access_token,
                  JSON.parse(cartDetailsLoggedInUser).code,
                  localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
                )
              );
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
    },
    cancelProduct: async (cancelProductDetails, productDetials) => {
      const cancelOrderDetails = await dispatch(
        cancelProduct(cancelProductDetails)
      );
      if (cancelOrderDetails.status === SUCCESS) {
        setDataLayerForMyAccountDirectCalls(productDetials);
        ownProps.history.goBack();
      } else {
        dispatch(displayToast(ERROR_MESSAGE_IN_CANCELING_ORDER));
      }
    }
  };
};
const ModalContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ModalRoot)
);

export default ModalContainer;
