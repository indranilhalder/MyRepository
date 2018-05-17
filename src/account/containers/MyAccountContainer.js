import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import MyAccount from "../components/MyAccount";
import { setHeaderText } from "../../general/header.actions";
import { setUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";
import { displayToast } from "../../general/toast.actions.js";
const mapDispatchToProps = dispatch => {
  return {
    displayToast: message => {
      dispatch(displayToast(message));
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },
    setUrlToRedirectToAfterAuth: url => {
      dispatch(setUrlToRedirectToAfterAuth(url));
    }
  };
};
const mapStateToProps = state => {
  return {
    userDetails: state.profile.userDetails
  };
};
const MyAccountContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(MyAccount)
);
export default MyAccountContainer;
