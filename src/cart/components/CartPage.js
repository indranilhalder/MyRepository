import React, { Component } from "react";

class CartPage extends Component {
  componentWillMount() {
    this.props.getUserCart();
    this.props.getEmiBankDetails();
    this.props.getNetBankDetails();
  }
  render() {
    return <div />;
  }
}

export default CartPage;
