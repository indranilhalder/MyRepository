import { connect } from "react-redux";
import { loginUser } from "../actions/user.actions";
import { withRouter } from "react-router-dom";
import App from "../../App";
const mapDispatchToProps = dispatch => {
  return {
    login: userLoginDetails => {
      dispatch(loginUser(userLoginDetails));
    }
  };
};

const mapStateToProps = state => {
  return {
    status: state.user.status,
    user: state.user.user,
    error: state.user.error,
    loading: state.user.loading
  };
};

const LoginContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(App)
);

export default LoginContainer;
