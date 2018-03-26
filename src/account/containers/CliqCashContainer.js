import { connect } from "react-redux";
import { getUserAddress } from "../../cart/actions/cart.actions";
import { removeAddress } from "../actions/account.actions";
import { withRouter } from "react-router-dom";
import CliqAndCash from "../components/CliqAndCash.js";

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

const CliqCashContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CliqAndCash)
);

export default CliqCashContainer;
