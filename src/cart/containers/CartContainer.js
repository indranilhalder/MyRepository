import { connect } from "react-redux";
import {
  getUserAddress,
  getCoupons,
  getEmiBankDetails,
  getNetBankDetails,
  getCartDetails,
  checkPinCodeServiceAvailability,
  addProductToWishList,
  removeItemFromCartLoggedIn
} from "../actions/cart.actions.js";
import { withRouter } from "react-router-dom";
import CartPage from "../components/CartPage";
import { PRODUCT_COUPONS, showModal } from "../../general/modal.actions";
const mapDispatchToProps = dispatch => {
  return {
    getUserAddress: () => {
      dispatch(getUserAddress());
    },
    getNetBankDetails: () => {
      dispatch(getNetBankDetails());
    },
    getEmiBankDetails: cartDetails => {
      dispatch(getEmiBankDetails(cartDetails));
    },
    getCartDetails: (cartId, userId, accessToken, pinCode) => {
      dispatch(getCartDetails(cartId, userId, accessToken, pinCode));
    },
    showCouponModal: data => {
      dispatch(showModal(PRODUCT_COUPONS, data));
    },
    checkPinCodeServiceAvailability: (userName, accessToken, pinCode) => {
      dispatch(checkPinCodeServiceAvailability(userName, accessToken, pinCode));
    },
    getCoupons: () => {
      dispatch(getCoupons());
    },
    addProductToWishList: productDetails => {
      dispatch(addProductToWishList(productDetails));
    },
    removeItemFromCartLoggedIn: (cartListItemPosition, pinCode) => {
      dispatch(removeItemFromCartLoggedIn(cartListItemPosition, pinCode));
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
