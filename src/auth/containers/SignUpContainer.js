import { connect } from "react-redux";
import { signUpUser } from "../actions/user.actions.js";
import { withRouter } from "react-router-dom";
import SignUp from "../components/SignUp.js";
import { displayToast } from "../../general/toast.actions";
const mapDispatchToProps = dispatch => {
  return {
    onSubmit: userSignUpDetails => {
      dispatch(signUpUser(userSignUpDetails));
    },
    displayToast: toastMessage => {
      dispatch(displayToast(toastMessage));
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
