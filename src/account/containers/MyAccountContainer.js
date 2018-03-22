import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import MyAccount from "../components/MyAccount";
import {
  getUserDetails,
  getUserCoupons,
  getUserAlerts
} from "../actions/account.actions";
const mapDispatchToProps = dispatch => {
  return {
    getUserDetails: () => {
      dispatch(getUserDetails());
    },
    getUserCoupons: () => {
      dispatch(getUserCoupons());
    },
    getUserAlerts: () => {
      dispatch(getUserAlerts());
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
