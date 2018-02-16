import React, { Component } from "react";

class CartPage extends Component {
  componentWillMount() {
    this.props.getProductCart();
  }
  render() {
    return <div />;
  }
}

export default CartPage;
