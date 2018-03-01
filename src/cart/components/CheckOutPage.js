import React from "react";
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
    //Method 1
    //get all Address
    this.props.getUserAddress();
    //add new Address
    this.props.addUserAddress();
    //add address to order
    this.props.addAddressToCart();
    //get Cart Details CNC
    this.props.getCartDetailsCNC();

    //Method 2
    //select Deliver Modes
    this.props.selectDeliveryMode();
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
