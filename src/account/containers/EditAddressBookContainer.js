import { connect } from "react-redux";
import { getUserAddress } from "../../cart/actions/cart.actions";
import { editAddress } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import EditAddressPopUp from "../components/EditAddressPopUp.js";

const mapDispatchToProps = dispatch => {
  return {
    getUserAddress: () => {
      dispatch(getUserAddress());
    },
    editAddress: addressDetails => {
      dispatch(editAddress(addressDetails));
    }
  };
};

const mapStateToProps = state => {
  return {
    editAddressStatus: state.profile.editAddressStatus,
    editAddressError: state.profile.editAddressError
  };
};

const EditAddressBookContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(EditAddressPopUp)
);

export default EditAddressBookContainer;
