import { connect } from "react-redux";
import { signUpUser } from "../actions/user.actions.js";
import { withRouter } from "react-router-dom";
import { displayToast } from "../../general/toast.actions.js";
import SignUp from "../components/SignUp.js";
const mapDispatchToProps = dispatch => {
  return {
    onSubmit: userSignUpDetails => {
      dispatch(signUpUser(userSignUpDetails));
    },
    displayToast: message => {
      dispatch(displayToast(message));
    }
  };
};

const mapStateToProps = state => {
  return {
    authCallsInProcess: state.auth.authCallsInProcess,
    authCallsIsSucceed: state.auth.authCallsIsSucceed
  };
};

const SignUpContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(SignUp)
);
export default SignUpContainer;
