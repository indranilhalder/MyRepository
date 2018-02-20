import React from "react";
import styles from "./WalletDetails.css";

export default class Wallet extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.walletDetail}>{this.props.walletText}</div>
        <div className={styles.walletDescription}>
          {this.props.walletDescription}
        </div>
      </div>
    );
  }
}

Wallet.defaultProps = {
  walletText: "You have insufficient balance",
  walletDescription: "please use split payment or try other payment methods"
};
