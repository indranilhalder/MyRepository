import React from "react";
import styles from "./DeliveryAddressCart.css";
import CheckBox from "../../general/components/CheckBox.js";
import PropTypes from "prop-types";
export default class DeliveryAddressCopy extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.titleAddress}>{this.props.addressTitle}</div>
        <div className={styles.titleDescription}>
          {this.props.addressDescription}
          <div className={styles.checkCircle}>
            <CheckBox selected={this.props.selected} />
          </div>
        </div>
      </div>
    );
  }
}

DeliveryAddressCopy.propTypes = {
  addressTitle: PropTypes.string,
  addressDescription: PropTypes.string,
  selected: PropTypes.bool
};
