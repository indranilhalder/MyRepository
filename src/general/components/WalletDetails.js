import React from "react";
import styles from "./WalletDetails.css";

export default class WalletDetail extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.walletDetail}>{this.props.walletDetail}</div>
        <div className={styles.walletDescription}>
          {this.props.walletDescription}
        </div>
      </div>
    );
  }
}

WalletDetail.defaultProps = {
  walletDetail: "You have insufficient balance",
  walletDescription: "please use split payment or try other payment methods"
};
