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
import { SUCCESS, ERROR, FAILURE } from "../../lib/constants";
import { createWishlist } from "../../wishlist/actions/wishlist.actions.js";

const mapDispatchToProps = dispatch => {
  return {
    facebookLogin: async isSignUp => {
      const facebookResponse = await dispatch(facebookLogin(isSignUp));
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
          //TODO dispatch toast here.
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

        if (loginUserResponse.status === SUCCESS) {
          const cartVal = await dispatch(getCartId());
          if (
            cartVal.status === SUCCESS &&
            cartVal.cartDetails.guid &&
            cartVal.cartDetails.code
          ) {
            dispatch(mergeCartId(cartVal.cartDetails.guid));
          } else {
            const createdCartVal = await dispatch(
              generateCartIdForLoggedInUser()
            );
            if (isSignUp) {
              dispatch(mergeCartId(createdCartVal.cartDetails.guid));
            }
          }
        }
      }
    },
    googlePlusLogin: async isSignUp => {
      dispatch(authCallsAreInProgress());

      const loadGoogleSdkResponse = await loadGoogleSignInApi();
      if (loadGoogleSdkResponse.status === ERROR) {
        dispatch(singleAuthCallHasFailed(loadGoogleSdkResponse.description));
        return;
      }

      // console.log("LOAD GOOGLE SDK");
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

        console.log("SIGN UP RESPONSE");
        console.log(signUpResponse);

        if (signUpResponse.status !== SUCCESS) {
          dispatch(singleAuthCallHasFailed(signUpResponse.error));
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
    signUp: ownProps.isSignUp
  };
};

const SocialButtonsContainer = connect(mapStateToProps, mapDispatchToProps)(
  SocialButtons
);

export default SocialButtonsContainer;
