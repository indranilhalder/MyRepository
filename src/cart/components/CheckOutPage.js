import React from "react";
import PropTypes from "prop-types";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  GLOBAL_ACCESS_TOKEN,
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS,
  ANONYMOUS_USER
} from "../../lib/constants";

class CheckOutPage extends React.Component {
  state = {
    confirmAddress: false,
    deliverMode: false,
    paymentMethod: false
  };

  confirmAddress = () => {
    this.setState({ confirmAddress: !this.state.confirmAddress });
  };

  deliveryMode = () => {
    this.setState({ deliverMode: !this.state.deliverMode });
  };

  paymentMode = () => {
    this.setState({ paymentMethod: !this.state.paymentMethod });
  };

  renderConfirmAddress = () => {
    if (this.state.confirmAddress) {
      return <div> Address Expand</div>;
    }
  };

  renderDeliverModes = () => {
    if (this.state.deliverMode) {
      return <div>Deliver Modes Expand</div>;
    }
  };

  renderPaymentModes = () => {
    if (this.state.paymentMethod) {
      return <div>Payment Modes Expand</div>;
    }
  };

  componentDidMount() {
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let cartDetailsLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );
    let cartDetailsAnonymous = Cookie.getCookie(CART_DETAILS_FOR_ANONYMOUS);

    // //Method 1
    //get all Address
    this.props.getUserAddress();
    //add new Address
    this.props.addUserAddress();
    //add address to order
    this.props.addAddressToCart();
    //get Cart Details CNC
    if (userDetails) {
      this.props.getCartDetailsCNC(
        JSON.parse(userDetails).customerInfo.mobileNumber,
        JSON.parse(customerCookie).access_token,
        JSON.parse(cartDetailsLoggedInUser).code
      );
    } else {
      this.props.getCartDetailsCNC(
        ANONYMOUS_USER,
        JSON.parse(globalCookie).access_token,
        JSON.parse(cartDetailsAnonymous).guid
      );
    }

    //Method 2
    //select Deliver Modes
    if (userDetails) {
      this.props.selectDeliveryMode(
        "Click-and-Collect",
        "273564HOME0004",
        JSON.parse(cartDetailsLoggedInUser).code
      );
    } else {
      this.props.selectDeliveryMode(
        "Click-and-Collect",
        "273564HOME0004",
        JSON.parse(cartDetailsAnonymous).guid
      );
    }
    // Get All Stores
    this.props.getAllStoresCNC("560095");
    // Add Store CNC
    this.props.addStoreCNC("273564HOME0004", "800059-860059");
    // Add Pickup Person
    this.props.addPickupPersonCNC("9066002014", "SriramCG");
    //get order Summary
    this.props.getOrderSummary();

    //method 3
    //get Coupons
    this.props.getCoupons();
    //apply Coupon
    this.props.applyCoupon();
    //release coupon
    this.props.releaseCoupon();
  }

  render() {
    return (
      <div>
        <div onClick={this.confirmAddress}>Confirm Address</div>
        {this.renderConfirmAddress()}
        <div onClick={this.deliveryMode}>Delivery Mode</div>
        {this.renderDeliverModes()}
        <div onClick={this.paymentMode}>Confirm Address</div>
        {this.renderPaymentModes()}
      </div>
    );
  }
}

export default CheckOutPage;

CheckOutPage.propTypes = {
  getCartDetailsCNC: PropTypes.func,
  selectDeliveryMode: PropTypes.func,
  getAllStoresCNC: PropTypes.func,
  addStoreCNC: PropTypes.func,
  addPickupPersonCNC: PropTypes.func
};
