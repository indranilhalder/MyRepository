import React from "react";
import PropTypes from "prop-types";
import DeliveryCard from "./DeliveryCard.js";
import styles from "./DeliveryModeSet.css";
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
        {this.props.productDelivery.map((data, i) => {
          return (
            <div className={styles.base} key={i}>
              <div className={styles.productName}>{data.productName}</div>
              <div className={styles.deliveryWay}>{`${
                this.props.selectedDeliveryDetails.name
              }:${this.props.selectedDeliveryDetails.desc}`}</div>
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
