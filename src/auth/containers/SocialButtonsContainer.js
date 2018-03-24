import { connect } from "react-redux";
import SocialButtons from "../components/SocialButtons.js";
import {
  facebookLogin,
  googlePlusLogin,
  generateCustomerLevelAccessTokenForSocialMedia,
  socialMediaLogin,
  FACEBOOK_PLATFORM,
  SOCIAL_CHANNEL_FACEBOOK,
  loginUser,
  loginUserRequest,
  customerAccessToken
} from "../../auth/actions/user.actions";
import {
  mergeCartId,
  generateCartIdForLoggedInUser,
  getCartId
} from "../../cart/actions/cart.actions";
import { SUCCESS } from "../../lib/constants";

const mapDispatchToProps = dispatch => {
  return {
    facebookLogin: async type => {
      const facebookResponse = await dispatch(facebookLogin(type));
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
        console.log(
          customerAccessTokenActionResponse.customerAccessTokenDetails
            .access_token
        );
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
            console.log("MERGE CART ID IF");
            dispatch(mergeCartId(cartVal.cartDetails.guid));
          } else {
            console.log("GENERATE CART ID FOR LOGGED IN USER");
            dispatch(generateCartIdForLoggedInUser());
          }
        }
      }
    },
    googlePlusLogin: type => {
      dispatch(googlePlusLogin(type));
    }
  };
};

const SocialButtonsContainer = connect(null, mapDispatchToProps)(SocialButtons);

export default SocialButtonsContainer;
