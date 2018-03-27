import { connect } from "react-redux";
import { addUserAddress } from "../../cart/actions/cart.actions";
import { withRouter } from "react-router-dom";
import AddDeliveryAddress from "../../cart/components/AddDeliveryAddress.js";

const mapDispatchToProps = dispatch => {
  return {
    addUserAddress: addressDetails => {
      dispatch(addUserAddress(addressDetails));
    }
  };
};

const mapStateToProps = state => {
  return {
    addUserAddressStatus: state.profile.addUserAddressStatus,
    addUserAddressError: state.profile.addUserAddressError
  };
};

const AddAddressContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AddDeliveryAddress)
);

export default AddAddressContainer;
