import { connect } from "react-redux";
import { loginUser } from "../actions/user.actions";
import { withRouter } from "react-router-dom";
import { showModal, RESTORE_PASSWORD } from "../../general/modal.actions.js";
import { homeFeed } from "../../home/actions/home.actions";
import Login from "../components/Login.js";
const mapDispatchToProps = dispatch => {
  return {
    onSubmit: userLoginDetails => {
      dispatch(loginUser(userLoginDetails));
    },
    onForgotPassword: () => {
      dispatch(showModal(RESTORE_PASSWORD));
    },
    homeFeed: () => {
      dispatch(homeFeed());
    }
  };
};

const mapStateToProps = state => {
  return {
    user: state.user.user
  };
};

const LoginContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Login)
);

export default LoginContainer;
