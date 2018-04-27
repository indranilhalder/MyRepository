import React from "react";
import styles from "./OrderViewPaymentDetails.css";
import PropTypes from "prop-types";
export default class OrderViewPaymentDetails extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.subTotalsHolder}>
          <div className={styles.labelText}>Sub total</div>
          {this.props.SubTotal && (
            <div className={styles.infoText}>
              {`Rs. ${this.props.SubTotal}`}
            </div>
          )}
        </div>
        <div className={styles.deliverDataHolder}>
          <div className={styles.labelText}>Delivery & Shipping Charges</div>
          {this.props.DeliveryCharges && (
            <div className={styles.infoText}>
              {`Rs. ${this.props.DeliveryCharges}`}
            </div>
          )}
        </div>
        <div className={styles.discountDataHolder}>
          <div className={styles.labelText}>Discount</div>
          {this.props.Discount && (
            <div className={styles.infoText}>
              {`-Rs. ${this.props.Discount}`}
            </div>
          )}
        </div>
        <div className={styles.discountDataHolder}>
          <div className={styles.labelText}>Coupon</div>
          {this.props.coupon && (
            <div className={styles.infoText}>{`-Rs. ${this.props.coupon}`}</div>
          )}
        </div>
        <div className={styles.chargeHolder}>
          <div className={styles.labelText}>Convenience Charges</div>
          {this.props.ConvenienceCharges && (
            <div className={styles.infoText}>
              {`Rs. ${this.props.ConvenienceCharges}`}
            </div>
          )}
        </div>
        <div className={styles.totalHolder}>
          <div className={styles.labelTextTotal}>Total Amount</div>
          {this.props.Total && (
            <div className={styles.infoTextTotal}>{`Rs. ${
              this.props.Total
            }`}</div>
          )}
        </div>
      </div>
    );
  }
}
OrderViewPaymentDetails.propTypes = {
  DeliveryCharges: PropTypes.string,
  Discount: PropTypes.string,
  Total: PropTypes.string,
  ConvenienceCharges: PropTypes.string,
  SubTotal: PropTypes.string
};
