import React from "react";
import CheckoutFrame from "./CheckoutFrame";
import ConfirmAddress from "./ConfirmAddress";
import AddDeliveryAddress from "./AddDeliveryAddress";
import DummyTab from "./DummyTab";
import DeliveryModeSet from "./DeliveryModeSet";
export default class CheckoutAddress extends React.Component {
  constructor(props) {
    super(props);
    this.state = { defaultAddress: true };
  }
  componentDidMount() {
    this.props.getUserAddress();
  }
  onChange(val) {
    this.setState(val);
  }
  onCancel() {
    console.log(this.state);
    this.props.addUserAddress(this.state);
  }
  onSelectAddress(addressId) {
    console.log(addressId);
    this.props.addAddressToCart(addressId[0]);
  }
  render() {
    console.log(this.props);
    return (
      <CheckoutFrame onSubmit={() => this.onCancel()}>
        {this.props.cart.userAddress && (
          <ConfirmAddress
            address={this.props.cart.userAddress.addresses.map(address => {
              return {
                addressTitle: address.id,
                addressDescription: `${address.line1} ${address.town} ${
                  address.city
                }, ${address.state} ${address.postalCode}`
              };
            })}
            onSelectAddress={addressId => this.onSelectAddress(addressId)}
          />
        )}
        {!this.props.cart.userAddress && (
          <AddDeliveryAddress
            {...this.state}
            onChange={val => this.onChange(val)}
          />
        )}

        {!this.props.deliveryModeSet && (
          <DummyTab number="2" title="Delivery Mode" />
        )}
        {!this.props.deliveryModeSet && (
          <DeliveryModeSet
            productDelivery={[
              {
                productName: "Apple iPhone 7 upgraded vrson with gorila glass",
                deliveryWay: " Express Shipping"
              },
              {
                productName: "Apple iPhone 7 upgraded vrson with gorila glass",
                deliveryWay: "Free home delivery"
              }
            ]}
          />
        )}

        <DummyTab number="3" title="Payment Method" />
      </CheckoutFrame>
    );
  }
}
CheckoutAddress.defaultProps = {
  addressSet: false,
  deliveryModeSet: false
};
