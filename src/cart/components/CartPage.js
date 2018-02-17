import React, { Component } from "react";

class CartPage extends Component {
  componentWillMount() {
    this.props.getCartDetails();
  }

  render() {
    return <div />;
  }
}

export default CartPage;
