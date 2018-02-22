import React from "react";
import PropTypes from "prop-types";
import DeliveryCard from "./DeliveryCard.js";
import styles from "./DeliveryAddress.css";
export default class DeliveryAddress extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <DeliveryCard
        onClick={() => this.handleClick()}
        confirmTitle="Delivery Address"
        indexNumber="1"
      >
        <div className={styles.base}>
          <div className={styles.deliveryAddressText}>
            <span class={styles.home}>Home</span>
            <span>{this.props.address}</span>
          </div>
        </div>
      </DeliveryCard>
    );
  }
}
DeliveryAddress.propTypes = {
  onClick: PropTypes.func,
  address: PropTypes.string
};
