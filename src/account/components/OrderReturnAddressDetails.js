import React from "react";
import styles from "./OrderReturnAddressDetails.css";
import PropTypes from "prop-types";
export default class OrderReturnAddressDetails extends React.Component {
  onCancel() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        {this.props.addressType && (
          <div className={styles.home}>{this.props.addressType}</div>
        )}
        {this.props.address && (
          <div className={styles.addressDetails}>{this.props.address}</div>
        )}
        {this.props.subAddress && (
          <div className={styles.address}>{this.props.subAddress}</div>
        )}
      </div>
    );
  }
}
OrderReturnAddressDetails.propTypes = {
  address: PropTypes.string,
  subAddress: PropTypes.string,
  addressType: PropTypes.string
};
