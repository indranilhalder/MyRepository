import { connect } from "react-redux";
import { getUserDetails, updateProfile } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import EditAccountDetails from "../components/EditAccountDetails.js";

const mapDispatchToProps = dispatch => {
  return {
    getUserDetails: addressId => {
      dispatch(getUserDetails(addressId));
    },
    updateProfile: accountDetails => {
      dispatch(updateProfile(accountDetails));
    }
  };
};

const mapStateToProps = state => {
  return {
    userDetails: state.profile.userDetails,
    type: state.profile.type
  };
};

const UpdateProfileContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(EditAccountDetails)
);

export default UpdateProfileContainer;
