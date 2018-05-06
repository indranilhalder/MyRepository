import { connect } from "react-redux";
import {
  getUserDetails,
  updateProfile,
  changePassword,
  clearAccountUpdateType
} from "../actions/account.actions";
import { setHeaderText } from "../../general/header.actions";
import { withRouter } from "react-router-dom";
import EditAccountDetails from "../components/EditAccountDetails.js";
import { displayToast } from "../../general/toast.actions";
import { SUCCESS } from "../../lib/constants.js";
const UPDATE_PROFILE_SUCCESS = "Profile Updated Successfully";
const UPDATE_PASSWORD = "Password Updated Successfully";
const mapDispatchToProps = dispatch => {
  return {
    getUserDetails: addressId => {
      dispatch(getUserDetails(addressId));
    },

    updateProfile: async accountDetails => {
      const response = await dispatch(updateProfile(accountDetails));

      if (response && response.status === SUCCESS) {
        dispatch(getUserDetails());
        dispatch(displayToast(UPDATE_PROFILE_SUCCESS));
      }
    },
    changePassword: async passwordDetails => {
      const response = await dispatch(changePassword(passwordDetails));
      if (response && response.status === SUCCESS) {
        dispatch(displayToast(UPDATE_PASSWORD));
      }
    },
    clearAccountUpdateType: () => {
      dispatch(clearAccountUpdateType());
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },
    displayToast: message => {
      dispatch(displayToast(message));
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
