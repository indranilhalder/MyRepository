import { connect } from "react-redux";
import { withRouter } from "react-router";
import CheckoutAddress from "../components/CheckoutAddress";
import {
  getUserAddress,
  addUserAddress,
  addAddressToCart
} from "../actions/cart.actions";

const mapDispatchToProps = dispatch => {
  return {
    getUserAddress: () => {
      dispatch(getUserAddress());
    },
    addUserAddress: userAddress => {
      dispatch(addUserAddress(userAddress));
    },
    addAddressToCart: addressId => {
      dispatch(addAddressToCart(addressId));
    }
  };
};
const mapStateToProps = state => {
  console.log(state);
  return {
    cart: state.cart
    // cart: {
    //   userAddress: {
    //     addresses: [
    //       {
    //         addressType: "Home",
    //         city: "Mumba",
    //         country: {
    //           isocode: "IN"
    //         },
    //         defaultAddress: true,
    //         firstName: "rhcb",
    //         id: "8815459074071",
    //         landmark: "Balshet Madurkar\n Marg",
    //         lastName: "cbncd",
    //         line1: "Cbff",
    //         phone: "5485855845",
    //         postalCode: "400013",
    //         state: "Maharashtra",
    //         town: "Mumba"
    //       },
    //       {
    //         addressType: "Office",
    //         city: "Kormangala",
    //         country: {
    //           isocode: "IN"
    //         },
    //         defaultAddress: false,
    //         firstName: "Aakarsh Yadav",
    //         id: "8815491809303",
    //         landmark: "Aegis tower",
    //         line1: "Xelpmoc design",
    //         phone: "9456888501",
    //         postalCode: "229001",
    //         state: "Kr",
    //         town: "Kormangala"
    //       },
    //       {
    //         addressType: "Office",
    //         city: "Bangalore",
    //         country: {
    //           isocode: "IN"
    //         },
    //         defaultAddress: false,
    //         firstName: "Aakarsh Yadav",
    //         id: "8815459106839",
    //         landmark: "Kormangal",
    //         line1: "Xelpmoc",
    //         phone: "9456888501",
    //         postalCode: "229001",
    //         state: "Kr",
    //         town: "Bangalore"
    //       },
    //       {
    //         addressType: "Office",
    //         city: "Bangalore",
    //         country: {
    //           isocode: "IN"
    //         },
    //         defaultAddress: false,
    //         firstName: "Aakarsh Yadav",
    //         id: "8815491776535",
    //         landmark: "Kormangal",
    //         line1: "Xelpmoc",
    //         phone: "9456888501",
    //         postalCode: "229001",
    //         state: "Kr",
    //         town: "Bangalore"
    //       }
    //     ],
    //     status: "Success"
    //   }
  };
};

const CheckoutAddressContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(CheckoutAddress)
);
export default CheckoutAddressContainer;
