import React from "react";
import styles from "./DeliveryAddressCopy.css";
import CheckBox from "../../general/components/CheckBox.js";
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
