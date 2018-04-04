import { connect } from "react-redux";
import { logout } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import LogoutButton from "../components/LogoutButton";
const mapDispatchToProps = dispatch => {
  return {
    logout: () => {
      dispatch(logout());
    }
  };
};
const LogoutButtonContainer = withRouter(
  connect(null, mapDispatchToProps)(LogoutButton)
);

export default LogoutButtonContainer;
