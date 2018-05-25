import { connect } from "react-redux";
import {
  loginUser,
  customerAccessToken,
  refreshToken
} from "../actions/user.actions";
import {
  mergeCartId,
  generateCartIdForLoggedInUser,
  getCartId
} from "../../cart/actions/cart.actions";
import * as Cookies from "../../lib/Cookie";

import { withRouter } from "react-router-dom";
import { showModal, RESTORE_PASSWORD } from "../../general/modal.actions.js";
import { getFeed } from "../../home/actions/home.actions";
import Login from "../components/Login.js";
import {
  SUCCESS,
  CART_DETAILS_FOR_ANONYMOUS,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  ERROR,
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  DEFAULT_PIN_CODE_LOCAL_STORAGE
} from "../../lib/constants";
import { displayToast } from "../../general/toast.actions";
import { clearUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";

import {
  singleAuthCallHasFailed,
  setIfAllAuthCallsHaveSucceeded
} from "../../auth/actions/auth.actions";
import {
  setDataLayerForLogin,
  ADOBE_DIRECT_CALL_FOR_LOGIN_SUCCESS,
  ADOBE_DIRECT_CALL_FOR_LOGIN_FAILURE
} from "../../lib/adobeUtils";
import { getCartDetails } from "../../cart/actions/cart.actions.js";
import {
  getWishListItems,
  createWishlist
} from "../../wishlist/actions/wishlist.actions";
export const OTP_VERIFICATION_REQUIRED_MESSAGE = "OTP VERIFICATION REQUIRED";

const mapDispatchToProps = dispatch => {
  return {
    displayToast: toastMessage => {
      dispatch(displayToast(toastMessage));
    },
    onForgotPassword: () => {
      dispatch(showModal(RESTORE_PASSWORD));
    },
    homeFeed: () => {
      dispatch(getFeed());
    },
    clearUrlToRedirectToAfterAuth: () => {
      dispatch(clearUrlToRedirectToAfterAuth());
    },
    onSubmit: async userDetails => {
      const userDetailsResponse = await dispatch(
        customerAccessToken(userDetails)
      );
      // checking condition for the failure customer access token api
      if (userDetailsResponse.status === ERROR) {
        dispatch(singleAuthCallHasFailed(userDetailsResponse.error));
      } else if (userDetailsResponse.status === SUCCESS) {
        const loginUserResponse = await dispatch(loginUser(userDetails));
        if (loginUserResponse.status === SUCCESS) {
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
            const newCartIdObj = await dispatch(
              generateCartIdForLoggedInUser()
            );

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
      }
    },
    refreshToken: sessionData => {
      dispatch(refreshToken(sessionData));
    }
  };
};

const mapStateToProps = state => {
  return {
    authCallsInProcess: state.auth.authCallsInProcess,
    authCallsIsSucceed: state.auth.authCallsIsSucceed,
    redirectToAfterAuthUrl: state.auth.redirectToAfterAuthUrl
  };
};

const LoginContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Login)
);

export default LoginContainer;
