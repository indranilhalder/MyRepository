import React from "react";
import styles from "./OrderPlacedAndId.css";
import PropTypes from "prop-types";
export default class OrderPlacedAndId extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        {this.props.placedTime && (
          <div className={styles.orderPlacedHolder}>
            <div className={styles.labelHeader}>{this.props.label} :</div>
            <div className={styles.dataInformationText}>
              {this.props.placedTime}
            </div>
          </div>
        )}

        <div className={styles.orderIdHolder}>
          <div className={styles.labelHeader}>Order ID :</div>
          <div className={styles.dataInformationText}>{this.props.orderId}</div>
        </div>
      </div>
    );
  }
}
OrderPlacedAndId.propTypes = {
  label: PropTypes.string,
  placedTime: PropTypes.string,
  orderId: PropTypes.string
};
OrderPlacedAndId.defaultProps = {
  label: "Order placed"
};
