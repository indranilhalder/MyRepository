import { connect } from "react-redux";
import { getUserAddress } from "../../cart/actions/cart.actions";
import { removeAddress } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import AddressBook from "../components/AddressBook.js";

const mapDispatchToProps = dispatch => {
  return {
    getUserAddress: () => {
      dispatch(getUserAddress());
    },
    removeAddress: addressId => {
      dispatch(removeAddress(addressId));
    }
  };
};

const mapStateToProps = state => {
  return {
    userAddress: state.profile.userAddress,
    removeAddressStatus: state.profile.removeAddressStatus
  };
};

const AddressBookContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AddressBook)
);

export default AddressBookContainer;
