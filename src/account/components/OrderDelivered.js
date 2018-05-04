import React from "react";
import styles from "./OrderDelivered.css";
import PropTypes from "prop-types";
export default class OrderDelivered extends React.Component {
  render() {
    let deliveredAddress, address;
    if (this.props.heading) {
      address = this.props.deliveredAddress;
      deliveredAddress = address.trim();
    }
    return (
      <div className={styles.base}>
        {deliveredAddress && (
          <div className={styles.addressHolder}>
            <div className={styles.deliveredTo}>Delivered to: </div>
            <div className={styles.address}>{this.props.deliveredAddress}</div>
          </div>
        )}
        {this.props.deliveredDate && (
          <div className={styles.deliverDateHolder}>
            <div className={styles.labelText}>Delivered on:</div>
            <div className={styles.infoText}>{this.props.deliveredDate}</div>
          </div>
        )}
        {this.props.soldBy && (
          <div className={styles.orderSoldBy}>
            <div className={styles.labelText}>Sold by:</div>
            <div className={styles.infoText}>{this.props.soldBy}</div>
          </div>
        )}
      </div>
    );
  }
}
OrderDelivered.propTypes = {
  deliveredAddress: PropTypes.string,
  deliveredDate: PropTypes.string,
  soldBy: PropTypes.string
};
