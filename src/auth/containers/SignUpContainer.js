import { connect } from "react-redux";
import { signUpUser, otpVerification } from "../actions/user.actions.js";
import { withRouter } from "react-router-dom";
import App from "../../App";

const mapDispatchToProps = dispatch => {
  return {
    signUp: userSignUpDetails => {
      dispatch(signUpUser(userSignUpDetails));
    },
    otpVerification: userDetails => {
      dispatch(otpVerification(userDetails));
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

const SignUpContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(App)
);

export default SignUpContainer;
