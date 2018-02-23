import { connect } from "react-redux";
import { withRouter } from "react-router";
import CheckoutAddress from "../components/CheckoutAddress";
import { getUserAddress } from "../actions/cart.actions";

const mapDispatchToProps = dispatch => {
  return {
    getUserAddress: () => {
      dispatch(getUserAddress());
    }
  };
};
const mapStateToProps = state => {
  console.log(state);
  return {
    user: state.user.user
  };
};
const CheckoutAddressContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CheckoutAddress)
);
export default CheckoutAddressContainer;
