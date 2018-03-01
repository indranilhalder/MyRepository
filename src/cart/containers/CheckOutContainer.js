import { connect } from "react-redux";
import { withRouter } from "react-router";
import CheckOutPage from "../components/CheckOutPage";
import {
  getCartDetailsCNC,
  addUserAddress,
  addAddressToCart,
  getUserAddress,
  selectDeliveryMode,
  getOrderSummary,
  getCoupons,
  applyCoupon,
  releaseCoupon
} from "../actions/cart.actions";

const mapDispatchToProps = dispatch => {
  return {
    getCartDetailsCNC: (cartId, userId, accessToken) => {
      dispatch(getCartDetailsCNC(cartId, userId, accessToken));
    },
    getUserAddress: () => {
      dispatch(getUserAddress());
    },
    addUserAddress: userAddress => {
      dispatch(addUserAddress(userAddress));
    },
    addAddressToCart: addressId => {
      dispatch(addAddressToCart(addressId));
    },
    selectDeliveryMode: (code, ussId, cartId) => {
      dispatch(selectDeliveryMode(code, ussId, cartId));
    },
    getOrderSummary: () => {
      dispatch(getOrderSummary());
    },
    getCoupons: () => {
      dispatch(getCoupons());
    },
    applyCoupon: () => {
      dispatch(applyCoupon());
    },
    releaseCoupon: () => {
      dispatch(releaseCoupon());
    }
  };
};
const mapStateToProps = state => {
  return {
    cart: state.cart
  };
};

const CheckoutAddressContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CheckOutPage)
);
export default CheckoutAddressContainer;
