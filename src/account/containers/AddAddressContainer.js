import { connect } from "react-redux";
import { addUserAddress } from "../../cart/actions/cart.actions";
import { withRouter } from "react-router-dom";
import AddDeliveryAddress from "../../cart/components/AddDeliveryAddress.js";
import { getPinCode, getPinCodeSuccess } from "../actions/account.actions.js";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";
const mapDispatchToProps = dispatch => {
  return {
    addUserAddress: addressDetails => {
      dispatch(addUserAddress(addressDetails));
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
    }
  };
};

const mapStateToProps = state => {
  return {
    addUserAddressStatus: state.profile.addUserAddressStatus,
    getPinCodeDetails: state.profile.getPinCodeDetails,
    addUserAddressError: state.profile.addUserAddressError,
    loading: state.profile.loading
  };
};

const AddAddressContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AddDeliveryAddress)
);

export default AddAddressContainer;
