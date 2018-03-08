import { connect } from "react-redux";
import {
  getCartDetailsCNC,
  selectDeliveryMode
} from "../actions/cart.actions.js";
import { withRouter } from "react-router-dom";
import CheckoutDeliveryMode from "../components/CheckoutDeliveryMode";

const mapDispatchToProps = dispatch => {
  return {
    getCartDetailsCNC: (cartId, userId, accessToken) => {
      dispatch(getCartDetailsCNC(cartId, userId, accessToken));
    },
    selectDeliveryMode: (code, ussid, cartId) => {
      dispatch(selectDeliveryMode(code, ussid, cartId));
    }
  };
};

const mapStateToProps = state => {
  return {
    cart: state.cart,
    user: state.user
  };
};
const DeliveryModesContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CheckoutDeliveryMode)
);
export default DeliveryModesContainer;
