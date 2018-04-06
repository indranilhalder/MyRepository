import { connect } from "react-redux";
import { logout } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import LogoutButton from "../components/LogoutButton";
import { generateCartIdForAnonymous } from "../../cart/actions/cart.actions";
const mapDispatchToProps = dispatch => {
  return {
    logout: async () => {
      return await dispatch(logout());
    },
    generateCartIdForAnonymous: async () => {
      return await dispatch(generateCartIdForAnonymous());
    }
  };
};
const LogoutButtonContainer = withRouter(
  connect(null, mapDispatchToProps)(LogoutButton)
);

export default LogoutButtonContainer;
