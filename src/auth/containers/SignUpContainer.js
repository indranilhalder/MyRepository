import { connect } from "react-redux";
import { signUpUser } from "../actions/user.actions.js";
import { withRouter } from "react-router-dom";
import { displayToast } from "../../general/toast.actions.js";
import { clearUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";

import SignUp from "../components/SignUp.js";
const mapDispatchToProps = dispatch => {
  return {
    onSubmit: userSignUpDetails => {
      dispatch(signUpUser(userSignUpDetails));
    },
    displayToast: message => {
      dispatch(displayToast(message));
    },
    clearUrlToRedirectToAfterAuth: () => {
      dispatch(clearUrlToRedirectToAfterAuth());
    }
  };
};

const mapStateToProps = state => {
  return {
    authCallsInProcess: state.auth.authCallsInProcess,
    authCallsIsSucceed: state.auth.authCallsIsSucceed,
    redirectToAfterAuthUrl: state.auth.redirectToAfterAuthUrl
  };
};

const SignUpContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(SignUp)
);
export default SignUpContainer;
