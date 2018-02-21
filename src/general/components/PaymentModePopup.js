import React from "react";
import styles from "./PaymentModePopup.css";
import WalletDetail from "./WalletDetails";
import Button from "./Button";
import image from "./img/citigroup.jpg";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
export default class PaymentModePopup extends React.Component {
  changePaymentMethod() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  continueWithoutCoupon() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.container}>
          <WalletDetail>
            <div className={styles.logo}>
              <Icon image={image} size={60} />
            </div>
            <div className={styles.logoText}>{this.props.label}</div>
          </WalletDetail>
        </div>
        <div className={styles.buttonContainer}>
          <div className={styles.buttonHolder}>
            <Button
              type="primary"
              label={this.props.btnLabel}
              width={211}
              color="#b2b2b2"
              onClick={() => this.changePaymentMethod()}
            />
          </div>
          <div className={styles.buttonHolder}>
            <Button
              type="hollow"
              label={this.props.btnLabel}
              width={211}
              color=" #4a4a4a"
              onClick={() => this.continueWithoutCoupon()}
            />
          </div>
        </div>
      </div>
    );
  }
}
PaymentModePopup.propTypes = {
  onClick: PropTypes.func
};
PaymentModePopup.defaultProps = {
  label: "FESTIVE20",
  btnLabel: "Change Payment Mode"
};
