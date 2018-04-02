import { connect } from "react-redux";
import { signUpUser } from "../actions/user.actions.js";
import { withRouter } from "react-router-dom";
import SignUp from "../components/SignUp.js";

const mapDispatchToProps = dispatch => {
  return {
    onSubmit: userSignUpDetails => {
      dispatch(signUpUser(userSignUpDetails));
    }
  };
};

const mapStateToProps = state => {
  return {
    user: state.user
  };
};

const SignUpContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(SignUp)
);

export default SignUpContainer;
