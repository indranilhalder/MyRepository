import React from "react";
import styles from "./TransactionFailed.css";
export default class extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.labelHeader}>Transaction Failed</div>
        <div className={styles.transactionText}>
          We were unable to process the transaction Please retry.{" "}
        </div>
      </div>
    );
  }
}
