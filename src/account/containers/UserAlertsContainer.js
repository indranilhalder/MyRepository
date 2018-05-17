import { getUserAlerts } from "../actions/account.actions";
import { connect } from "react-redux";
import UserAlerts from "../components/UserAlerts";

const mapDispatchToProps = dispatch => {
  return {
    getUserAlerts: () => {
      dispatch(getUserAlerts());
    }
  };
};

const mapStateToProps = state => {
  return {
    userAlerts: state.profile.userAlerts
  };
};

const UserAlertsContainer = connect(mapStateToProps, mapDispatchToProps)(
  UserAlerts
);

export default UserAlertsContainer;
