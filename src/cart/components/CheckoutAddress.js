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
  }
  render() {
    return (
      <CheckoutFrame onSubmit={() => this.onCancel()}>
        {this.props.user &&
          this.props.user.addressSet && (
            <ConfirmAddress
              address={[
                {
                  addressTitle: "Home",
                  addressDescription:
                    "Lal Bahadur Shastri Marg, Chandan Nagar, Vikhori West"
                },
                {
                  addressTitle: "Office",
                  addressDescription:
                    "Homi Modi St, Kala Ghoda, Fort Mumbai, Maharashtra 400023"
                },
                {
                  addressTitle: "Other1",
                  addressDescription:
                    "Tagore Nagar, Vikhroli East, Mumbai, Maharashtra 400012"
                },
                {
                  addressTitle: "Other2",
                  addressDescription:
                    "Homi Modi St, Kala Ghoda, Fort Mumbai, Maharashtra 400023"
                },
                {
                  addressTitle: "Other3",
                  addressDescription:
                    "Homi Modi St, Kala Ghoda, Fort Mumbai, Maharashtra 400023"
                }
              ]}
            />
          )}
        {!this.props.addressSet && (
          <AddDeliveryAddress
            {...this.state}
            onChange={val => this.onChange(val)}
          />
        )}

        {!this.props.deliveryModeSet && (
          <DummyTab number="2" title="Delivery Mode" />
        )}
        {this.props.deliveryModeSet && (
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
