import { connect } from "react-redux";
import { getCustomerProfileRequest } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import AccountSettingsPage from "../components/AccountSettingsPage";

const mapDispatchToProps = dispatch => {
  return {
    getCustomerProfile: () => {
      dispatch(getCustomerProfileRequest());
    }
  };
};

const mapStateToProps = state => {
  console.log(state);
  return {
    profile: state.profile
  };
};

const EditProfileContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AccountSettingsPage)
);

export default EditProfileContainer;
