import React from "react";
import PropTypes from "prop-types";
import DeliveryCard from "./DeliveryCard.js";
import styles from "./DeliveryModeSet.css";
import { COLLECT } from "../../lib/constants";
export default class DeliveryModeSet extends React.Component {
  handleClick() {
    if (this.props.changeDeliveryModes) {
      this.props.changeDeliveryModes();
    }
  }
  render() {
    return (
      <DeliveryCard
        onClick={() => this.handleClick()}
        confirmTitle="Delivery Mode"
        indexNumber="2"
      >
        {this.props.productDelivery &&
          this.props.productDelivery.map((data, i) => {
            const selectedDeliveryModes = this.props.selectedDeliveryDetails[
              data.USSID
            ];
            const deliveryOption =
              data &&
              data.elligibleDeliveryMode &&
              data.elligibleDeliveryMode.find(mode => {
                return mode.code === selectedDeliveryModes;
              });
            let expectedDeliveryDate = deliveryOption.desc
              ? `:${deliveryOption.desc}`
              : "";
            return (
              <div className={styles.base} key={i}>
                <div className={styles.productName}>{data.productName}</div>
                <div className={styles.deliveryWay}>
                  {deliveryOption &&
                    `${
                      deliveryOption.name === "Home Delivery"
                        ? "Standard Shipping"
                        : deliveryOption.name === "Express Delivery"
                          ? "Express Shipping"
                          : deliveryOption.name
                    } ${
                      deliveryOption.code === COLLECT
                        ? data.storeDetails &&
                          `Pickup Store: ${data.storeDetails.displayName} ${
                            data.storeDetails.address.city
                          }`
                        : expectedDeliveryDate
                    }`}
                </div>
              </div>
            );
          })}
      </DeliveryCard>
    );
  }
}
DeliveryModeSet.propTypes = {
  productDelivery: PropTypes.arrayOf(
    PropTypes.shape({
      productName: PropTypes.string,
      deliveryWay: PropTypes.string
    })
  ),
  onClick: PropTypes.func
};
