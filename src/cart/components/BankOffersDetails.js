import React, { Component } from "react";
import BankCoupons from "./BankCoupons.js";
import SlideModal from "../../general/components/SlideModal";
import styles from "./BankOffersDetails.css";
import GridSelect from "../../general/components/GridSelect";
const COUPON_HEADER = "Bank promotions";

class BankOffersDetails extends Component {
  applyBankCoupons = val => {};

  render() {
    return (
      <div className={styles.base}>
        <SlideModal {...this.props}>
          <div className={styles.couponHeader}>{COUPON_HEADER}</div>

          <BankCoupons
            coupons={this.props.coupons}
            value={this.props.coupons}
          />
        </SlideModal>
      </div>
    );
  }
}

export default BankOffersDetails;
