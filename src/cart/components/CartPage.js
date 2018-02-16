import React, { Component } from "react";

class CartPage extends Component {
  componentWillMount() {
    this.props.getUserCart();
    this.props.applyCoupon();
    this.props.getUserAddress();
    this.props.selectDeliveryModes();

    let cartValues = {};
    cartValues.total = 9000.0;
    this.props.getEmiBankDetails(cartValues);

    this.props.getNetBankDetails();
  }
  render() {
    return <div />;
  }
}

export default CartPage;
