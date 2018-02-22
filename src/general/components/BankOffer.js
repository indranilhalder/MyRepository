import React from "react";
import styles from "./BankOffer.css";
import CheckBox from "./CheckBox.js";
import UnderLinedButton from "./UnderLinedButton";

export default class BankOffer extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.container}>
          <span className={styles.bankName}>{this.props.bankName}</span>
          <span className={styles.offerText}>{this.props.offerText}</span>
          <div className={styles.checkBoxHolder}>
            <CheckBox selected={this.props.selected} />
          </div>
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <UnderLinedButton
              label={this.props.label}
              onClick={() => {
                this.handleClick();
              }}
            />
          </div>
        </div>
      </div>
    );
  }
}
