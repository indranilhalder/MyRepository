import { connect } from "react-redux";
import { addUserAddress } from "../../cart/actions/cart.actions";
import { withRouter } from "react-router-dom";
import AddDeliveryAddress from "../../cart/components/AddDeliveryAddress.js";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";
const mapDispatchToProps = dispatch => {
  return {
    addUserAddress: addressDetails => {
      dispatch(addUserAddress(addressDetails));
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
    addUserAddressError: state.profile.addUserAddressError,
    loading: state.profile.loading
  };
};

const AddAddressContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AddDeliveryAddress)
);

export default AddAddressContainer;
