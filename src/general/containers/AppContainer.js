import { connect } from "react-redux";
import { showModal } from "../modal.actions.js";
import {
  facebookLogin,
  googlePlusLogin,
  getGlobalAccessToken,
  refreshToken
} from "../../auth/actions/user.actions";
import { withRouter } from "react-router-dom";
import App from "../../App.js";

const mapDispatchToProps = dispatch => {
  return {
    showModal: type => {
      dispatch(showModal(type));
    },
    facebookLogin: () => {
      dispatch(facebookLogin());
    },
    googlePlusLogin: () => {
      dispatch(googlePlusLogin());
    },
    getGlobalAccessToken: () => {
      dispatch(getGlobalAccessToken());
    },
    refreshToken: () => {
      dispatch(refreshToken());
    }
  };
};

const mapStateToProps = state => {
  return {
    modalStatus: state.modal.modalDisplayed
  };
};

const AppContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(App)
);

export default AppContainer;
