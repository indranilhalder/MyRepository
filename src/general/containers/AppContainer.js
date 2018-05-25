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
  generateCartIdForAnonymous
} from "../../cart/actions/cart.actions.js";
import { withRouter } from "react-router-dom";
import App from "../../App.js";
import { createWishlist } from "../../wishlist/actions/wishlist.actions.js";
import { clearUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";
import { getFeed } from "../../home/actions/home.actions.js";
const mapDispatchToProps = dispatch => {
  return {
    homeFeed: () => {
      dispatch(getFeed());
    },
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
    clearUrlToRedirectToAfterAuth: () => {
      dispatch(clearUrlToRedirectToAfterAuth());
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
    scrollPosition: state.modal.scrollPosition,
    cartLoading: state.cart.loading,
    globalAccessTokenStatus: state.user.globalAccessTokenStatus,
    customerAccessTokenStatus: state.user.customerAccessTokenStatus,
    refreshCustomerAccessTokenStatus:
      state.user.refreshCustomerAccessTokenStatus,
    cartIdForLoggedInUserStatus: state.cart.cartIdForLoggedInUserStatus,
    cartIdForAnonymousUserStatus: state.cart.cartIdForAnonymousUserStatus,
    redirectToAfterAuthUrl: state.auth.redirectToAfterAuthUrl
  };
};

const AppContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(App)
);

export default AppContainer;
