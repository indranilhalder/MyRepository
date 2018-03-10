import React from "react";
import styles from "./OrderDelivered.css";
import PropTypes from "prop-types";
export default class OrderDelivered extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.addressHolder}>
          <div className={styles.deliveredTo}>Delivered to: </div>
          <div className={styles.address}>{this.props.deliveredAddress}</div>
        </div>
        <div className={styles.deliverDateHolder}>
          <div className={styles.labelText}>Delivered on:</div>
          <div className={styles.infoText}>{this.props.deliveredDate}</div>
        </div>
        <div className={styles.orderSoldBy}>
          <div className={styles.labelText}>Sold by:</div>
          <div className={styles.infoText}>{this.props.soldBy}</div>
        </div>
      </div>
    );
  }
}
OrderDelivered.propTypes = {
  deliveredAddress: PropTypes.string,
  deliveredDate: PropTypes.string,
  soldBy: PropTypes.string
};
