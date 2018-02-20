import React from "react";
import styles from "./CheckOutHeader.css";
export default class CheckOutHeader extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.confirm}>
          {this.props.confirmTitle}
          <div className={styles.circle}>1</div>
        </div>
        {/* <DeliveryAddressCopy
          addressTitle={this.props.addressTitle}
          addressDescription={this.props.addressDescription}
          selected={this.props.selected}
        /> */}
      </div>
    );
  }
}
