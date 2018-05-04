import { connect } from "react-redux";
import SocialButtons from "../components/SocialButtons.js";
import {
  facebookLogin,
  googlePlusLogin,
  generateCustomerLevelAccessTokenForSocialMedia,
  socialMediaLogin,
  FACEBOOK_PLATFORM,
  SOCIAL_CHANNEL_FACEBOOK,
  GOOGLE_PLUS_PLATFORM,
  SOCIAL_CHANNEL_GOOGLE_PLUS,
  loginUser,
  loginUserRequest,
  customerAccessToken,
  socialMediaRegistration,
  loadGoogleSignInApi
} from "../../auth/actions/user.actions";
import * as Cookies from "../../lib/Cookie";

import {
  singleAuthCallHasFailed,
  setIfAllAuthCallsHaveSucceeded,
  authCallsAreInProgress
} from "../../auth/actions/auth.actions";
import {
  mergeCartId,
  generateCartIdForLoggedInUser,
  getCartId
} from "../../cart/actions/cart.actions";
import {
  SUCCESS,
  ERROR,
  FAILURE,
  CART_DETAILS_FOR_ANONYMOUS,
  CART_DETAILS_FOR_LOGGED_IN_USER
} from "../../lib/constants";
import { createWishlist } from "../../wishlist/actions/wishlist.actions.js";
import { displayToast } from "../../general/toast.actions.js";
import { clearUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";
import {
  setDataLayerForLogin,
  ADOBE_DIRECT_CALL_FOR_LOGIN_SUCCESS,
  ADOBE_DIRECT_CALL_FOR_LOGIN_FAILURE
} from "../../lib/adobeUtils";

const mapDispatchToProps = dispatch => {
  return {
    clearUrlToRedirectToAfterAuth: () => {
      dispatch(clearUrlToRedirectToAfterAuth());
    },
    facebookLogin: async isSignUp => {
      dispatch(authCallsAreInProgress());
      const facebookResponse = await dispatch(facebookLogin(isSignUp));
      if (facebookResponse.status === ERROR) {
        dispatch(singleAuthCallHasFailed(facebookResponse.error));
        return;
      }
      // if user doesn't have any email id linked to their fb account then
      // we have to show toast that
      if (!facebookResponse.email) {
        dispatch(
          singleAuthCallHasFailed(
            "Something went wrong. Please try with different account"
          )
        );
        return;
      }
      console.log(facebookResponse);
      if (isSignUp) {
        const signUpResponse = await dispatch(
          socialMediaRegistration(
            facebookResponse.email,
            facebookResponse.id,
            facebookResponse.accessToken,
            FACEBOOK_PLATFORM,
            SOCIAL_CHANNEL_FACEBOOK
          )
        );

        if (signUpResponse.status !== SUCCESS) {
          dispatch(singleAuthCallHasFailed(signUpResponse.error));
          return;
        }
        if (signUpResponse.status === SUCCESS) {
          const wishListResponse = await dispatch(createWishlist());
          if (wishListResponse.status === ERROR) {
            dispatch(singleAuthCallHasFailed(signUpResponse.error));
            return;
          }
        }
      }

      const customerAccessTokenActionResponse = await dispatch(
        generateCustomerLevelAccessTokenForSocialMedia(
          facebookResponse.email,
          facebookResponse.id,
          facebookResponse.accessToken,
          FACEBOOK_PLATFORM,
          SOCIAL_CHANNEL_FACEBOOK
        )
      );

      // now I need to actually login
      let profileImage;
      if (
        facebookResponse &&
        facebookResponse.profileImage &&
        facebookResponse.profileImage.data
      ) {
        profileImage = facebookResponse.profileImage.data.url;
      }
      if (customerAccessTokenActionResponse.status === SUCCESS) {
        const loginUserResponse = await dispatch(
          socialMediaLogin(
            facebookResponse.email,
            FACEBOOK_PLATFORM,
            customerAccessTokenActionResponse.customerAccessTokenDetails
              .access_token,
            {
              profileImage,
              firstName: facebookResponse.firstName,
              lastName: facebookResponse.lastName
            }
          )
        );

        if (loginUserResponse.status === SUCCESS) {
          setDataLayerForLogin(ADOBE_DIRECT_CALL_FOR_LOGIN_SUCCESS);
          const cartVal = await dispatch(getCartId());

          if (
            cartVal.status === SUCCESS &&
            cartVal.cartDetails.guid &&
            cartVal.cartDetails.code
          ) {
            const mergeCartResponse = await dispatch(
              mergeCartId(cartVal.cartDetails.guid)
            );

            if (mergeCartResponse.status === SUCCESS) {
              dispatch(setIfAllAuthCallsHaveSucceeded());
            } else {
              Cookies.deleteCookie(CART_DETAILS_FOR_ANONYMOUS);
              Cookies.createCookie(
                CART_DETAILS_FOR_LOGGED_IN_USER,
                JSON.stringify(cartVal.cartDetails)
              );
              dispatch(setIfAllAuthCallsHaveSucceeded());
            }
          } else {
            const createdCartVal = await dispatch(
              generateCartIdForLoggedInUser()
            );

            if (
              createdCartVal.status === ERROR ||
              createdCartVal.status === FAILURE
            ) {
              dispatch(singleAuthCallHasFailed(createdCartVal.error));
            } else {
              const mergeCartResponse = await dispatch(
                mergeCartId(createdCartVal.cartDetails.guid)
              );
              if (mergeCartResponse.status === SUCCESS) {
                dispatch(setIfAllAuthCallsHaveSucceeded());
              }
            }
          }
        } else {
          setDataLayerForLogin(ADOBE_DIRECT_CALL_FOR_LOGIN_FAILURE);
          dispatch(singleAuthCallHasFailed(loginUserRequest.error));
        }
      } else {
        dispatch(
          singleAuthCallHasFailed(customerAccessTokenActionResponse.error)
        );
      }
    },
    googlePlusLogin: async isSignUp => {
      dispatch(authCallsAreInProgress());

      const loadGoogleSdkResponse = await loadGoogleSignInApi();
      if (loadGoogleSdkResponse.status === ERROR) {
        dispatch(singleAuthCallHasFailed(loadGoogleSdkResponse.description));
        // as loading the google sign in api has nothing with redux state
        // we manually trigger the toast error here
        dispatch(displayToast("SDK Failed to load, check Google Client ID"));
        return;
      }

      const googlePlusResponse = await dispatch(googlePlusLogin(isSignUp));
      if (googlePlusResponse.status && googlePlusResponse.status !== SUCCESS) {
        dispatch(singleAuthCallHasFailed());
        return;
      }

      if (isSignUp) {
        const signUpResponse = await dispatch(
          socialMediaRegistration(
            googlePlusResponse.email,
            googlePlusResponse.email,
            googlePlusResponse.id,
            GOOGLE_PLUS_PLATFORM,
            SOCIAL_CHANNEL_GOOGLE_PLUS
          )
        );

        if (signUpResponse.status !== SUCCESS) {
          dispatch(singleAuthCallHasFailed(signUpResponse.error));
          return;
        }

        if (signUpResponse.status === SUCCESS) {
          const wishListResponse = await dispatch(createWishlist());
          if (wishListResponse.status === ERROR) {
            dispatch(singleAuthCallHasFailed(signUpResponse.error));
            return;
          }
        }
      }

      const customerAccessTokenActionResponse = await dispatch(
        generateCustomerLevelAccessTokenForSocialMedia(
          googlePlusResponse.email,
          googlePlusResponse.id,
          googlePlusResponse.accessToken,
          GOOGLE_PLUS_PLATFORM,
          SOCIAL_CHANNEL_GOOGLE_PLUS
        )
      );

      if (customerAccessTokenActionResponse.status === SUCCESS) {
        console.log(googlePlusResponse);
        const loginUserResponse = await dispatch(
          socialMediaLogin(
            googlePlusResponse.email,
            GOOGLE_PLUS_PLATFORM,
            customerAccessTokenActionResponse.customerAccessTokenDetails
              .access_token,
            {
              profileImage: googlePlusResponse.profileImage,
              firstName: googlePlusResponse.firstName,
              lastName: googlePlusResponse.lastName
            }
          )
        );
        if (loginUserResponse.status === SUCCESS) {
          const cartVal = await dispatch(getCartId());
          if (
            cartVal.status === SUCCESS &&
            cartVal.cartDetails &&
            cartVal.cartDetails.guid &&
            cartVal.cartDetails.code
          ) {
            const mergeCartResponse = await dispatch(
              mergeCartId(cartVal.cartDetails.guid)
            );

            if (mergeCartResponse.status === SUCCESS) {
              dispatch(setIfAllAuthCallsHaveSucceeded());
            } else {
              Cookies.deleteCookie(CART_DETAILS_FOR_ANONYMOUS);
              Cookies.createCookie(
                CART_DETAILS_FOR_LOGGED_IN_USER,
                JSON.stringify(cartVal.cartDetails)
              );
              dispatch(setIfAllAuthCallsHaveSucceeded());

              // merge cart has failed, so all I need to do is remove the cart details for anonymous
              // and use the cart as a logged in cart.
            }
          } else {
            const createdCartVal = await dispatch(
              generateCartIdForLoggedInUser()
            );

            if (
              createdCartVal.status === ERROR ||
              createdCartVal.status === FAILURE
            ) {
              dispatch(singleAuthCallHasFailed(createdCartVal.error));
            } else {
              const mergeCartResponse = await dispatch(
                mergeCartId(createdCartVal.cartDetails.guid)
              );
              if (mergeCartResponse.status === SUCCESS) {
                dispatch(setIfAllAuthCallsHaveSucceeded());
              } else {
                // ignore the anonymous cart
                // use the generated cart
                Cookies.deleteCookie(CART_DETAILS_FOR_ANONYMOUS);
                Cookies.createCookie(
                  CART_DETAILS_FOR_LOGGED_IN_USER,
                  JSON.stringify(createdCartVal.cartDetails)
                );
                dispatch(setIfAllAuthCallsHaveSucceeded());
              }
            }
          }
        } else {
          dispatch(singleAuthCallHasFailed(loginUserRequest.error));
        }
      } else {
        dispatch(
          singleAuthCallHasFailed(customerAccessTokenActionResponse.error)
        );
      }
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    signUp: ownProps.isSignUp,
    redirectToAfterAuthUrl: state.auth.redirectToAfterAuthUrl
  };
};

const SocialButtonsContainer = connect(mapStateToProps, mapDispatchToProps)(
  SocialButtons
);

export default SocialButtonsContainer;
