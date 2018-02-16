import { connect } from "react-redux";
import {
  getUserCart,
  applyCoupon,
  getUserAddress,
  selectDeliveryModes,
  getEmiBankDetails,
  getNetBankDetails
} from "../actions/cart.actions.js";
import { withRouter } from "react-router-dom";
import CartPage from "../components/CartPage";
const mapDispatchToProps = dispatch => {
  return {
    getUserCart: () => {
      dispatch(getUserCart());
    },
    applyCoupon: couponDetails => {
      dispatch(applyCoupon(couponDetails));
    },
    getUserAddress: () => {
      dispatch(getUserAddress());
    },
    selectDeliveryModes: deliverModes => {
      dispatch(selectDeliveryModes(deliverModes));
    },
    getNetBankDetails: () => {
      dispatch(getNetBankDetails());
    },
    getEmiBankDetails: cartDetails => {
      dispatch(getEmiBankDetails(cartDetails));
    }
  };
};

const mapStateToProps = state => {
  return {
    cart: state.cart,
    user: state.user
  };
};

const CartContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CartPage)
);

export default CartContainer;
