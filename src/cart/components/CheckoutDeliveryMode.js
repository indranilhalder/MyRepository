import React from "react";
import CheckoutFrame from "./CheckoutFrame";
import DeliveryModeSet from "./DeliveryModeSet";
export default class extends React.Component {
  render() {
    return (
      <CheckoutFrame>
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
      </CheckoutFrame>
    );
  }
}
