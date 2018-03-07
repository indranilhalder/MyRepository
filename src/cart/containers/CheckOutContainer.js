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
  releaseCoupon,
  getAllStoresCNC,
  addStoreCNC,
  addPickupPersonCNC,
  applyCliqCash,
  removeCliqCash
} from "../actions/cart.actions";

const mapDispatchToProps = dispatch => {
  return {
    getCartDetailsCNC: (userId, accessToken, cartId) => {
      dispatch(getCartDetailsCNC(userId, accessToken, cartId));
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
    },
    selectDeliveryMode: (code, ussId, cartId) => {
      dispatch(selectDeliveryMode(code, ussId, cartId));
    },
    getAllStoresCNC: pinCode => {
      dispatch(getAllStoresCNC(pinCode));
    },
    addStoreCNC: (ussId, slaveId) => {
      dispatch(addStoreCNC(ussId, slaveId));
    },
    addPickupPersonCNC: (personMobile, personName) => {
      dispatch(addPickupPersonCNC(personMobile, personName));
    },
    applyCliqCash: cartGuid => {
      dispatch(applyCliqCash(cartGuid));
    },
    removeCliqCash(cartGuid) {
      dispatch(removeCliqCash(cartGuid));
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
