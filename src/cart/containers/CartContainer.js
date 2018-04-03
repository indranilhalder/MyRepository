import { connect } from "react-redux";
import {
  getUserAddress,
  getCoupons,
  getEmiBankDetails,
  getNetBankDetails,
  getCartDetails,
  checkPinCodeServiceAvailability,
  addProductToWishList,
  removeItemFromCartLoggedIn,
  removeItemFromCartLoggedOut,
  updateQuantityInCartLoggedIn,
  updateQuantityInCartLoggedOut
} from "../actions/cart.actions.js";
import { withRouter } from "react-router-dom";
import CartPage from "../components/CartPage";
import { setHeaderText } from "../../general/header.actions";
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
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },
    showCouponModal: data => {
      dispatch(showModal(PRODUCT_COUPONS, data));
    },
    checkPinCodeServiceAvailability: (
      userName,
      accessToken,
      pinCode,
      productCode
    ) => {
      dispatch(
        checkPinCodeServiceAvailability(
          userName,
          accessToken,
          pinCode,
          productCode
        )
      );
    },
    getCoupons: () => {
      dispatch(getCoupons());
    },
    addProductToWishList: productDetails => {
      dispatch(addProductToWishList(productDetails));
    },
    removeItemFromCartLoggedIn: (cartListItemPosition, pinCode) => {
      dispatch(removeItemFromCartLoggedIn(cartListItemPosition, pinCode));
    },
    removeItemFromCartLoggedOut: (cartListItemPosition, pinCode) => {
      dispatch(removeItemFromCartLoggedOut(cartListItemPosition, pinCode));
    },
    updateQuantityInCartLoggedIn: (selectedItem, quantity, pinCode) => {
      dispatch(updateQuantityInCartLoggedIn(selectedItem, quantity, pinCode));
    },
    updateQuantityInCartLoggedOut: (selectedItem, quantity, pinCode) => {
      dispatch(updateQuantityInCartLoggedOut(selectedItem, quantity, pinCode));
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
