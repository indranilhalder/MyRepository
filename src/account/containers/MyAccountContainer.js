import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import MyAccount from "../components/MyAccount";
import { getUserCoupons, getUserAlerts } from "../actions/account.actions";
import { setHeaderText } from "../../general/header.actions";
import { setUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";
import { displayToast } from "../../general/toast.actions.js";
const mapDispatchToProps = dispatch => {
  return {
    getUserCoupons: () => {
      dispatch(getUserCoupons());
    },
    getUserAlerts: () => {
      dispatch(getUserAlerts());
    },
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
    userDetails: state.profile.userDetails,
    userCoupons: state.profile.userCoupons,
    userAlerts: state.profile.userAlerts
  };
};
const MyAccountContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(MyAccount)
);
export default MyAccountContainer;
