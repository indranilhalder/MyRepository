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
              Buy this product on EMI
              <span className={styles.link} onClick={this.showEmiModal}>
                View plans
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
