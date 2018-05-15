import React from "react";
import PropTypes from "prop-types";
import Button from "../../general/components/Button.js";
import styles from "./InvalidCouponPopUp.css";
import Logo from "../../general/components/Logo";
import {
  INVALID_USER_COUPON_TYPE,
  INVALID_BANK_OFFER_TYPE,
  FAILURE_LOWERCASE,
  WRONG_FAILURE,
  SUCCESS,
  INVALID_NO_COST_EMI_TYPE,
  NO_COST_EMI_COUPON
} from "../../lib/constants";

export default class InvalidCouponPopUp extends React.Component {
  getBanksList(bankDetail) {
    return (
      bankDetail &&
      bankDetail
        .map(bank => {
          return bank.bankName;
        })
        .join(",")
    );
  }
  getPaymentModesList(paymentModes) {
    return (
      paymentModes &&
      paymentModes
        .map(mode => {
          return mode.mode;
        })
        .join(",")
    );
  }
  getInvalidUserCouponTemplate(couponResponse) {
    return `Coupon code ${
      couponResponse.couponCode ? couponResponse.couponCode : ""
    } is valid for ${this.getBanksList(
      couponResponse.bankDetails
    )} ${this.getPaymentModesList(couponResponse.paymentModes)} transactions`;
  }
  getInvalidBankOfferTemplate(couponResponse) {
    return `The ${
      couponResponse.couponCode ? couponResponse.couponCode : ""
    } is valid for ${this.getBanksList(
      couponResponse.bankDetails
    )} ${this.getPaymentModesList(couponResponse.paymentModes)} transactions`;
  }
  getInvalidNCEOfferTemplate(couponResponse) {
    return `The No Cost Emi offer is valid only for ${
      couponResponse.couponCode
    }`;
  }
  changePaymentMethod() {
    if (this.props.changePaymentMethod) {
      this.props.changePaymentMethod();
    }
  }
  async continueWithoutCoupon() {
    console.log(this.props);
    console.log("comes in console");
    let releaseStatus = {};
    if (this.props.result && this.props.result.userCoupon) {
      if (
        this.props.result.noCostEmiCoupon &&
        this.props.result.noCostEmiCoupon.couponType ===
          INVALID_NO_COST_EMI_TYPE
      ) {
        releaseStatus = await this.props.releaseNoCostEmiCoupon(
          localStorage.getItem(NO_COST_EMI_COUPON)
        );
      }
      if (!releaseStatus.status || releaseStatus.status === SUCCESS) {
        if (
          this.props.result.bankOffer &&
          this.props.result.bankOffer.couponCode
        ) {
          releaseStatus = await this.props.releaseBankOffer(
            this.props.result.bankOffer.couponCode
          );
        }
      }
      if (!releaseStatus.status || releaseStatus.status === SUCCESS) {
        if (
          this.props.result.userCoupon &&
          this.props.result.userCoupon.couponCode
        ) {
          releaseStatus = await this.props.releaseUserCoupon(
            this.props.result.userCoupon.couponCode
          );
        }
      }
    } else if (this.props.result && this.props.result.bankOffer) {
      if (
        this.props.result.noCostEmiCoupon &&
        this.props.result.noCostEmiCoupon.couponType ===
          INVALID_NO_COST_EMI_TYPE
      ) {
        releaseStatus = await this.props.releaseNoCostEmiCoupon(
          localStorage.getItem(NO_COST_EMI_COUPON)
        );
      }
      if (!releaseStatus.status || releaseStatus.status === SUCCESS) {
        if (
          this.props.result.bankOffer &&
          this.props.result.bankOffer.couponCode
        ) {
          releaseStatus = await this.props.releaseBankOffer(
            this.props.result.bankOffer.couponCode
          );
        }
      }
    } else if (this.props.result && this.props.result.noCostEmiCoupon) {
      if (
        this.props.result.noCostEmiCoupon.couponType ===
        INVALID_NO_COST_EMI_TYPE
      ) {
        releaseStatus = await this.props.releaseNoCostEmiCoupon(
          localStorage.getItem(NO_COST_EMI_COUPON)
        );
      }
    }
    if (releaseStatus.status === SUCCESS) {
      debugger;
      this.props.redoCreateJusPayApi();
      this.props.closeModal();
    }
  }
  render() {
    const data = this.props.result;
    console.log(this.props);
    return (
      <div className={styles.base}>
        <div className={styles.paymentMethodDescription}>
          <div className={styles.headingText}>Different Payment Method</div>
          <div className={styles.descriptionText}>
            <div className={styles.invalidCouponHeading}>
              The payment mode can not be used because :
            </div>

            {data &&
              data.userCoupon &&
              data.userCoupon.status &&
              data.userCoupon.status.toLowerCase() === FAILURE_LOWERCASE && (
                <div className={styles.invalidCouponHeading}>
                  {this.getInvalidUserCouponTemplate(data.userCoupon)}
                </div>
              )}
            {data &&
              data.bankOffer &&
              data.bankOffer.status &&
              data.bankOffer.status.toLowerCase() === FAILURE_LOWERCASE && (
                <div className={styles.invalidCouponHeading}>
                  {this.getInvalidBankOfferTemplate(data.bankOffer)}
                </div>
              )}
            {data &&
              data.noCostEmiCoupon &&
              data.noCostEmiCoupon.status &&
              data.noCostEmiCoupon.status.toLowerCase() ===
                FAILURE_LOWERCASE && (
                <div className={styles.invalidCouponHeading}>
                  {this.getInvalidNCEOfferTemplate(data.noCostEmiCoupon)}
                </div>
              )}
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
InvalidCouponPopUp.propTypes = {
  cardLogo: PropTypes.string,
  changePaymentMethod: PropTypes.func,
  continueWithoutCoupon: PropTypes.func,
  couponCode: PropTypes.string
};
