import { connect } from "react-redux";
import { loginUser } from "../actions/user.actions";
import { withRouter } from "react-router-dom";
import { showModal, RESTORE_PASSWORD } from "../../general/modal.actions.js";
import Login from "../components/Login.js";
const mapDispatchToProps = dispatch => {
  return {
    login: userLoginDetails => {
      dispatch(loginUser(userLoginDetails));
    },
    onForgotPassword: () => {
      dispatch(showModal(RESTORE_PASSWORD));
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
