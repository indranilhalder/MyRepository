import React from "react";
import styles from "./OrderReturnAddressDetails.css";
import PropTypes from "prop-types";
import CheckBox from "../../general/components/CheckBox.js";
export default class OrderReturnAddressDetails extends React.Component {
  render() {
    return (
      <div className={this.props.isSelect ? styles.withCheckbox : styles.base}>
        {this.props.isSelect && (
          <div className={styles.checkBoxHolder}>
            <CheckBox selected={true} />
          </div>
        )}
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
  addressType: PropTypes.string,
  isSelect: PropTypes.bool
};
