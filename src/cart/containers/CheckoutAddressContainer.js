import { connect } from "react-redux";
import { withRouter } from "react-router";
import CheckoutAddress from "../components/CheckoutAddress";
import {
  getUserAddress,
  addUserAddress,
  addAddressToCart
} from "../actions/cart.actions";

const mapDispatchToProps = dispatch => {
  return {
    getUserAddress: () => {
      dispatch(getUserAddress());
    },
    addUserAddress: userAddress => {
      dispatch(addUserAddress(userAddress));
    },
    addAddressToCart: addressId => {
      dispatch(addAddressToCart(addressId));
    }
  };
};
const mapStateToProps = state => {
  return {
    cart: state.cart
  };
};

const CheckoutAddressContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CheckoutAddress)
);
export default CheckoutAddressContainer;
