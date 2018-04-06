import { connect } from "react-redux";
import { showModal } from "../modal.actions.js";
import {
  facebookLogin,
  googlePlusLogin,
  getGlobalAccessToken,
  refreshToken
} from "../../auth/actions/user.actions";
import {
  generateCartIdForLoggedInUser,
  generateCartIdForAnonymous,
  mergeCartId
} from "../../cart/actions/cart.actions.js";
import { withRouter } from "react-router-dom";
import App from "../../App.js";
import { createWishlist } from "../../wishlist/actions/wishlist.actions.js";

const mapDispatchToProps = dispatch => {
  return {
    showModal: type => {
      dispatch(showModal(type));
    },
    facebookLogin: type => {
      dispatch(facebookLogin(type));
    },
    googlePlusLogin: async type => {
      const response = await dispatch(googlePlusLogin(type));
      if (response.code <= 400) {
        dispatch(createWishlist());
      }
    },
    getGlobalAccessToken: async () => {
      return await dispatch(getGlobalAccessToken());
    },
    refreshToken: async () => {
      return dispatch(refreshToken());
    },
    generateCartIdForLoggedInUser: async () => {
      return dispatch(generateCartIdForLoggedInUser());
    },
    generateCartIdForAnonymous: async () => {
      return dispatch(generateCartIdForAnonymous());
    }
  };
};

const mapStateToProps = state => {
  return {
    modalStatus: state.modal.modalDisplayed,
    cart: state.cart,
    globalAccessTokenStatus: state.user.globalAccessTokenStatus,
    customerAccessTokenStatus: state.user.customerAccessTokenStatus,
    refreshCustomerAccessTokenStatus:
      state.user.refreshCustomerAccessTokenStatus,
    cartIdForLoggedInUserStatus: state.cart.cartIdForLoggedInUserStatus,
    cartIdForAnonymousUserStatus: state.cart.cartIdForAnonymousUserStatus
  };
};

const AppContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(App)
);

export default AppContainer;
