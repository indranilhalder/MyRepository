import { connect } from "react-redux";
import {
  getUserAddress,
  getEmiBankDetails,
  getNetBankDetails,
  getCartDetails,
  checkPinCodeServiceAvailability,
  removeItemFromCartLoggedIn,
  removeItemFromCartLoggedOut,
  updateQuantityInCartLoggedIn,
  updateQuantityInCartLoggedOut,
  displayCouponsForLoggedInUser,
  displayCouponsForAnonymous,
  clearCartDetails
} from "../actions/cart.actions.js";
import { displayToast } from "../../general/toast.actions";
import { withRouter } from "react-router-dom";
import CartPage from "../components/CartPage";
import { setHeaderText } from "../../general/header.actions";
import { setUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";

import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";
import {
  PRODUCT_COUPONS,
  showModal,
  ADDRESS
} from "../../general/modal.actions";
import { SUCCESS } from "../../lib/constants";
import {
  setDataLayerForCartDirectCalls,
  ADOBE_DIRECT_CALL_FOR_PINCODE_SUCCESS,
  ADOBE_DIRECT_CALL_FOR_PINCODE_FAILURE
} from "../../lib/adobeUtils";
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
    getCartDetails: async (
      cartId,
      userId,
      accessToken,
      pinCode,
      setDataLayerForPincode: false
    ) => {
      const cartDetailsObj = await dispatch(
        getCartDetails(cartId, userId, accessToken, pinCode)
      );
      // here we are setting data layer for pincode change on cart page
      if (setDataLayerForPincode) {
        if (cartDetailsObj.status === SUCCESS) {
          setDataLayerForCartDirectCalls(
            ADOBE_DIRECT_CALL_FOR_PINCODE_SUCCESS,
            pinCode
          );
        } else {
          setDataLayerForCartDirectCalls(
            ADOBE_DIRECT_CALL_FOR_PINCODE_FAILURE,
            pinCode
          );
        }
      }
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    },

    setUrlToRedirectToAfterAuth: url => {
      dispatch(setUrlToRedirectToAfterAuth(url));
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
    },
    showSecondaryLoader: () => {
      dispatch(showSecondaryLoader());
    },
    hideSecondaryLoader: () => {
      dispatch(hideSecondaryLoader());
    },
    clearCartDetails: () => {
      dispatch(clearCartDetails());
    },
    addressModal: pinCodeObj => {
      dispatch(showModal(ADDRESS, pinCodeObj));
    }
  };
};

const mapStateToProps = state => {
  return {
    cart: state.cart,
    user: state.user,
    loginFromMyBag: state.cart.loginFromMyBag
  };
};
const CartContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CartPage)
);

export default CartContainer;
