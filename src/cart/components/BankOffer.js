import React from "react";
import styles from "./BankOffer.css";
import CheckBox from "../../general/components/CheckBox.js";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import PropTypes from "prop-types";

export default class BankOffer extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
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

BankOffer.propTypes = {
  bankName: PropTypes.string,
  offerText: PropTypes.string,
  label: PropTypes.string,
  onClick: PropTypes.func
};
