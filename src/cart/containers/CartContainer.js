import { connect } from "react-redux";
import {
  applyCoupon,
  getUserAddress,
  selectDeliveryModes,
  getEmiBankDetails,
  getNetBankDetails,
  getCartDetails
} from "../actions/cart.actions.js";
import { withRouter } from "react-router-dom";
import CartPage from "../components/CartPage";
import { PRODUCT_COUPONS, showModal } from "../../general/modal.actions";
const mapDispatchToProps = dispatch => {
  return {
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
    },
    getCartDetails: (cartId, userId, accessToken) => {
      dispatch(getCartDetails(cartId, userId, accessToken));
    },
    showCouponModal: data => {
      dispatch(showModal(PRODUCT_COUPONS, data));
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
