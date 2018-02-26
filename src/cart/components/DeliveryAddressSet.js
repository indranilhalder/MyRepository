import React from "react";
import PropTypes from "prop-types";
import DeliveryCard from "./DeliveryCard.js";
import styles from "./DeliveryAddressSet.css";
export default class DeliveryAddressSet extends React.Component {
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
          <div className={styles.deliveryAddressSetText}>
            <span className={styles.home}>{this.props.addressType}</span>
            <span>{this.props.address}</span>
          </div>
        </div>
      </DeliveryCard>
    );
  }
}
DeliveryAddressSet.propTypes = {
  onClick: PropTypes.func,
  address: PropTypes.string,
  addressType: PropTypes.string
};
