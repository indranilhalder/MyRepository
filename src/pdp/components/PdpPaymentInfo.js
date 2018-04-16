import React from "react";
import styles from "./PdpPaymentInfo.css";
export default class PdpPaymentInfo extends React.Component {
  showEmiModal = () => {
    if (this.props.showEmiModal) {
      this.props.showEmiModal();
    }
  };
  render() {
    if (this.props.hasEmi === "Y" || this.props.hasCod === "Y") {
      return (
        <div className={styles.base}>
          {this.props.hasEmi === "Y" && (
            <div className={styles.content}>
              Emi available on this product.
              <span className={styles.link} onClick={this.showEmiModal}>
                View Plans
              </span>
            </div>
          )}
          {this.props.hasCod === "Y" && (
            <div className={styles.content}>Cash on Delivery available</div>
          )}
        </div>
      );
    } else {
      return null;
    }
  }
}
