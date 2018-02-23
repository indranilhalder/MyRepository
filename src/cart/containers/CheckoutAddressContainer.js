import { connect } from "react-redux";
import { withRouter } from "react-router";
import CheckoutAddress from "../components/CheckoutAddress";
import { getUserAddress, addUserAddress } from "../actions/cart.actions";

const mapDispatchToProps = dispatch => {
  return {
    getUserAddress: () => {
      dispatch(getUserAddress());
    },
    addUserAddress: userAddress => {
      dispatch(addUserAddress(userAddress));
    }
  };
};
const mapStateToProps = state => {
  console.log(state);
  return {
    cart: state.cart
  };
};
const CheckoutAddressContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CheckoutAddress)
);
export default CheckoutAddressContainer;
