import { connect } from "react-redux";
import { addUserAddress } from "../../cart/actions/cart.actions";
import { withRouter } from "react-router-dom";
import AddDeliveryAddress from "../../cart/components/AddDeliveryAddress.js";
import {
  getPinCode,
  getPinCodeSuccess,
  resetAddAddressDetails,
  getUserDetails,
  updateProfile
} from "../actions/account.actions.js";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";
import { displayToast } from "../../general/toast.actions";
import { SUCCESS } from "../../general/header.actions";
import { SUCCESS_CAMEL_CASE } from "../../lib/constants";
const mapDispatchToProps = dispatch => {
  return {
    addUserAddress: addressDetails => {
      if (addressDetails.emailId && addressDetails.emailId !== "") {
        let userDetails = {};
        userDetails.emailid = addressDetails.emailId;
        dispatch(updateProfile(userDetails)).then(res => {
          if (res.status === SUCCESS_CAMEL_CASE) {
            dispatch(addUserAddress(addressDetails));
          } else {
            dispatch(displayToast(res.error));
          }
        });
      } else {
        dispatch(addUserAddress(addressDetails));
      }
    },
    getPinCode: pinCode => {
      dispatch(getPinCode(pinCode));
    },
    resetAutoPopulateDataForPinCode: () => {
      dispatch(getPinCodeSuccess(null));
    },
    showSecondaryLoader: () => {
      dispatch(showSecondaryLoader());
    },
    hideSecondaryLoader: () => {
      dispatch(hideSecondaryLoader());
    },
    displayToast: message => {
      dispatch(displayToast(message));
    },
    resetAddAddressDetails: () => {
      dispatch(resetAddAddressDetails());
    },
    getUserDetails: () => {
      dispatch(getUserDetails());
    }
  };
};

const mapStateToProps = state => {
  return {
    addUserAddressStatus: state.profile.addUserAddressStatus,
    getPinCodeDetails: state.profile.getPinCodeDetails,
    getPincodeStatus: state.profile.getPinCodeStatus,
    addUserAddressError: state.profile.addUserAddressError,
    loading: state.profile.loading,
    userDetails: state.profile.userDetails
  };
};

const AddAddressContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AddDeliveryAddress)
);

export default AddAddressContainer;
