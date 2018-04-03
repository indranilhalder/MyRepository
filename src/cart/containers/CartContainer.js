import { connect } from "react-redux";
import {
  getUserAddress,
  getEmiBankDetails,
  getNetBankDetails,
  getCartDetails,
  checkPinCodeServiceAvailability,
  addProductToWishList,
  removeItemFromCartLoggedIn,
  removeItemFromCartLoggedOut,
  updateQuantityInCartLoggedIn,
  updateQuantityInCartLoggedOut,
  displayCouponsForLoggedInUser,
  displayCouponsForAnonymous
} from "../actions/cart.actions.js";
import { displayToast } from "../../general/toast.actions";
import { withRouter } from "react-router-dom";
import CartPage from "../components/CartPage";
import { PRODUCT_COUPONS, showModal } from "../../general/modal.actions";
const mapDispatchToProps = dispatch => {
  return {
    displayToast: toastMessage => {
      dispatch(displayToast(toastMessage));
    },
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
    },
    displayCouponsForLoggedInUser: (userId, accessToken, guId) => {
      dispatch(displayCouponsForLoggedInUser(userId, accessToken, guId));
    },
    displayCouponsForAnonymous: (userId, accessToken) => {
      dispatch(displayCouponsForAnonymous(userId, accessToken));
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
