import { connect } from "react-redux";
import { logout } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import LogoutButton from "../components/LogoutButton";
import { generateCartIdForAnonymous } from "../../cart/actions/cart.actions";
import { setFalseForAllAuthCallHasSucceedFlag } from "../../auth/actions/auth.actions";
const mapDispatchToProps = dispatch => {
  return {
    logout: async () => {
      return await dispatch(logout());
    },
    generateCartIdForAnonymous: async () => {
      return await dispatch(generateCartIdForAnonymous());
    },
    setFalseForAllAuthCallHasSucceedFlag: () => {
      dispatch(setFalseForAllAuthCallHasSucceedFlag());
    }
  };
};
const LogoutButtonContainer = withRouter(
  connect(null, mapDispatchToProps)(LogoutButton)
);

export default LogoutButtonContainer;
