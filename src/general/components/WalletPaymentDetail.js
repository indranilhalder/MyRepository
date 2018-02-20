import React from "react";
import styles from "./WalletPaymentDetail.css";
import WalletDetail from "./WalletDetails";

export default class WalletPaymentDetail extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.paymentDetail}>{this.props.paymentDetail}</div>
        <div className={styles.walletPrice}>{this.props.walletPrice}</div>
        <WalletDetail />
      </div>
    );
  }
}
WalletPaymentDetail.defaultProps = {
  paymentDetail: "Current wallet balance"
};
