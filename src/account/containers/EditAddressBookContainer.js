import { connect } from "react-redux";
import { getUserAddress } from "../../cart/actions/cart.actions";
import { editAddress, getPinCode } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import EditAddressPopUp from "../components/EditAddressPopUp.js";

const mapDispatchToProps = dispatch => {
  return {
    editAddress: addressDetails => {
      dispatch(editAddress(addressDetails));
    },
    getPinCode: pinCode => {
      dispatch(getPinCode(pinCode));
    }
  };
};

const mapStateToProps = state => {
  return {
    editAddressStatus: state.profile.editAddressStatus,
    editAddressError: state.profile.editAddressError,
    getPinCodeStatus: state.profile
  };
};

const EditAddressBookContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(EditAddressPopUp)
);

export default EditAddressBookContainer;
