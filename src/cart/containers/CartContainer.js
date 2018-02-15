import { connect } from "react-redux";
import { getProductCart } from "../actions/cart.actions.js";
import { withRouter } from "react-router-dom";
import CartPage from "../components/CartPage";
const mapDispatchToProps = dispatch => {
  return {
    getProductCart: () => {
      dispatch(getProductCart());
    }
  };
};

const mapStateToProps = state => {
  return {
    cart: state.cart
  };
};

const CartContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CartPage)
);

export default CartContainer;
