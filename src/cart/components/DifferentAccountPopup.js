import React from "react";
import PropTypes from "prop-types";
import Button from "../../general/components/Button.js";
import styles from "./DifferentAccountPopup.css";
import Logo from "../../general/components/Logo";
export default class DifferentAccountPopup extends React.Component {
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
        <div className={styles.paymentMethodDescription}>
          <div className={styles.headingText}>Different Payment Method</div>
          <div className={styles.descriptionText}>
            The payment method that you are trying to use is different than the
            one for which you availed the discount
          </div>
          <div className={styles.bankLogoAndCouponCode}>
            {this.props.cardLogo && (
              <div className={styles.cardLogo}>
                <Logo image={this.props.cardLogo} />
              </div>
            )}
            <div className={styles.couponCodeHolder}>
              {this.props.couponCode}
            </div>
          </div>
        </div>
        <div className={styles.buttonHolderForPaymentMode}>
          <div className={styles.button}>
            <Button
              type="primary"
              backgroundColor="#ff1744"
              height={36}
              label="Change Payment Mode"
              width={211}
              textStyle={{ color: "#FFF", fontSize: 14 }}
              onClick={() => this.changePaymentMethod()}
            />
          </div>
        </div>
        <div className={styles.buttonHolderForContinueCoupon}>
          <div className={styles.button}>
            <Button
              type="secondary"
              height={36}
              label="Continue without coupon"
              width={211}
              onClick={() => this.continueWithoutCoupon()}
            />
          </div>
        </div>
      </div>
    );
  }
}
DifferentAccountPopup.propTypes = {
  cardLogo: PropTypes.string,
  changePaymentMethod: PropTypes.func,
  continueWithoutCoupon: PropTypes.func,
  couponCode: PropTypes.string
};
