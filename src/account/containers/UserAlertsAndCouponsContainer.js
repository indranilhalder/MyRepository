import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import UserAlertsAndCoupons from "../components/UserAlertsAndCoupons";
import { getUserCoupons, getUserAlerts } from "../actions/account.actions";
import { setHeaderText } from "../../general/header.actions";
const mapDispatchToProps = dispatch => {
  return {
    getUserCoupons: () => {
      dispatch(getUserCoupons());
    },
    getUserAlerts: () => {
      dispatch(getUserAlerts());
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    }
  };
};
const mapStateToProps = state => {
  return {
    userCoupons: state.profile.userCoupons,
    userAlerts: state.profile.userAlerts,
    loadingForUserCoupons: state.profile.loadingForUserCoupons,
    loadingForUserAlerts: state.profile.loadingForUserAlerts
  };
};
const UserAlertsAndCouponsContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(UserAlertsAndCoupons)
);
export default UserAlertsAndCouponsContainer;
