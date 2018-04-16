import React from "react";
import styles from "./PaymentModePopup.css";
import WalletDetail from "./WalletDetails";
import Button from "./Button";
import image from "./img/citigroup.jpg";
import Icon from "../../xelpmoc-core/Icon";
import PropTypes from "prop-types";
export default class PaymentModePopup extends React.Component {
  changePaymentMethod() {
    if (this.props.changePaymentMethod) {
      this.props.changePaymentMethod();
    }
  }
  continueWithoutCoupon() {
    if (this.props.continueWithoutCoupon) {
      this.props.continueWithoutCoupon();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.container}>
          <WalletDetail
            walletDetail={this.props.walletDetail}
            walletDescription={this.props.walletDescription}
          >
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
              label={this.props.couponLabel}
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
  changePaymentMethod: PropTypes.func,
  continueWithoutCoupon: PropTypes.func,
  walletDetail: PropTypes.string,
  walletDescription: PropTypes.string
};
