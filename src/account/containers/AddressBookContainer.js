import { connect } from "react-redux";
import { getUserAddress } from "../../cart/actions/cart.actions";
import { removeAddress } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import AddressBook from "../components/AddressBook.js";
import { setHeaderText } from "../../general/header.actions";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";

const mapDispatchToProps = dispatch => {
  return {
    getUserAddress: () => {
      dispatch(getUserAddress(true));
    },
    removeAddress: addressId => {
      dispatch(removeAddress(addressId));
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
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
    userAddress: state.profile.userAddress,
    loading: state.profile.loading,
    removeAddressStatus: state.profile.removeAddressStatus
  };
};

const AddressBookContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(AddressBook)
);

export default AddressBookContainer;
