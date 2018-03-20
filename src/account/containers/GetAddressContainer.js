import { connect } from "react-redux";
import { getUserAddress } from "../../cart/actions/cart.actions";
import { withRouter } from "react-router-dom";
import CartPage from "../components/CartPage";

const mapDispatchToProps = dispatch => {
  return {
    getUserAddress: () => {
      dispatch(getUserAddress());
    }
  };
};

const mapStateToProps = state => {
  return {
    userAddress: state.cart.userAddress
  };
};

const GetAddressContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CartPage)
);

export default GetAddressContainer;
