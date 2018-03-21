import { connect } from "react-redux";
import {
  loginUser,
  customerAccessToken,
  refreshToken,
  loginUserRequest
} from "../actions/user.actions";
import {
  mergeCartId,
  generateCartIdForLoggedInUser,
  getCartId
} from "../../cart/actions/cart.actions";
import { withRouter } from "react-router-dom";
import {
  showModal,
  RESTORE_PASSWORD,
  OTP_LOGIN_MODAL
} from "../../general/modal.actions.js";
import { homeFeed } from "../../home/actions/home.actions";
import Login from "../components/Login.js";
import { SUCCESS } from "../../lib/constants";

export const OTP_VERIFICATION_REQUIRED_MESSAGE = "OTP VERIFICATION REQUIRED";

const mapDispatchToProps = dispatch => {
  return {
    onForgotPassword: () => {
      dispatch(showModal(RESTORE_PASSWORD));
    },
    homeFeed: () => {
      dispatch(homeFeed());
    },
    onSubmit: async userDetails => {
      const userDetailsResponse = await dispatch(
        customerAccessToken(userDetails)
      );
      if (userDetailsResponse.status === SUCCESS) {
        const loginUserResponse = await dispatch(loginUser(userDetails));
        console.log("LOGIN USER RESPONSE");
        console.log(loginUserResponse);
        console.log("USER DETAILS");
        console.log(userDetails);
        if (loginUserResponse.status === SUCCESS) {
          const cartVal = await dispatch(getCartId());
          if (
            cartVal.status === SUCCESS &&
            cartVal.cartDetails.guid &&
            cartVal.cartDetails.code
          ) {
            dispatch(mergeCartId(cartVal.cartDetails.guid));
          } else {
            dispatch(generateCartIdForLoggedInUser());
          }
        } else if (
          loginUserResponse.error === OTP_VERIFICATION_REQUIRED_MESSAGE
        ) {
          console.log("RIGHT IF BRANCH");
          dispatch(showModal(OTP_LOGIN_MODAL, userDetails));
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
    user: state.user,
    cart: state.cart
  };
};

const LoginContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Login)
);

export default LoginContainer;
