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
import { logout } from "../../account/actions/account.actions.js";
import { SUCCESS, ERROR, FAILURE } from "../../lib/constants";
import { createWishlist } from "../../wishlist/actions/wishlist.actions.js";
import { displayToast } from "../../general/toast.actions.js";
import { clearUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";

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
        dispatch(logout());
        return;
      }
      console.log("FACEBOOK RESPONSE");
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
          dispatch(logout());
          return false;
        }
        if (signUpResponse.status === SUCCESS) {
          dispatch(createWishlist());
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

      console.log("CUSTOMER ACCESS TOKEN RESPONSE");
      console.log(customerAccessTokenActionResponse);

      // now I need to actually login
      if (customerAccessTokenActionResponse.status === SUCCESS) {
        const loginUserResponse = await dispatch(
          socialMediaLogin(
            facebookResponse.email,
            FACEBOOK_PLATFORM,
            customerAccessTokenActionResponse.customerAccessTokenDetails
              .access_token
          )
        );

        console.log("LOGIN USER RESPONSE");
        console.log(loginUserResponse);

        if (loginUserResponse.status === SUCCESS) {
          const cartVal = await dispatch(getCartId());

          console.log("CART VAL");
          console.log(cartVal);
          if (
            cartVal.status === SUCCESS &&
            cartVal.cartDetails.guid &&
            cartVal.cartDetails.code
          ) {
            const mergeCartResponse = await dispatch(
              mergeCartId(cartVal.cartDetails.guid)
            );

            console.log("MERGE CART RESPONSE");
            console.log(mergeCartResponse);

            if (mergeCartResponse.status === SUCCESS) {
              console.log("AUTH CALLS SUCCEEDED");
              dispatch(setIfAllAuthCallsHaveSucceeded());
            } else {
              dispatch(singleAuthCallHasFailed(mergeCartResponse.error));
              dispatch(logout());
            }
          } else {
            const createdCartVal = await dispatch(
              generateCartIdForLoggedInUser()
            );

            console.log("CREATED CART VAl");
            console.log(createdCartVal);
            if (
              createdCartVal.status === ERROR ||
              createdCartVal.status === FAILURE
            ) {
              dispatch(singleAuthCallHasFailed(createdCartVal.error));
              dispatch(logout());
            } else {
              const mergeCartResponse = await dispatch(
                mergeCartId(createdCartVal.cartDetails.guid)
              );
              if (mergeCartResponse.status === SUCCESS) {
                console.log("AUTH CALLS SUCCEEDED");

                dispatch(setIfAllAuthCallsHaveSucceeded());
              }
            }
          }
        } else {
          dispatch(singleAuthCallHasFailed(loginUserRequest.error));
          dispatch(logout());
        }
      } else {
        dispatch(
          singleAuthCallHasFailed(customerAccessTokenActionResponse.error)
        );
        dispatch(logout());
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

      // console.log("LOAD GOOGLE SDK");
      const googlePlusResponse = await dispatch(googlePlusLogin(isSignUp));
      if (googlePlusResponse.status && googlePlusResponse.status !== SUCCESS) {
        dispatch(singleAuthCallHasFailed());
        dispatch(logout());
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
          dispatch(logout());

          return;
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
        // console.log("CUSTOMER ACCESS TOKEN SUCCESS");
        const loginUserResponse = await dispatch(
          socialMediaLogin(
            googlePlusResponse.email,
            GOOGLE_PLUS_PLATFORM,
            customerAccessTokenActionResponse.customerAccessTokenDetails
              .access_token
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
              dispatch(singleAuthCallHasFailed(mergeCartResponse.error));
              dispatch(logout());
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
              dispatch(logout());
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
          dispatch(singleAuthCallHasFailed(loginUserRequest.error));
          dispatch(logout());
        }
      } else {
        dispatch(
          singleAuthCallHasFailed(customerAccessTokenActionResponse.error)
        );
        dispatch(logout());
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
