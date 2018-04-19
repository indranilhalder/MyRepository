import React, { Component } from "react";
import BankCoupons from "./BankCoupons.js";
import SlideModal from "../../general/components/SlideModal";
import styles from "./BankOffersDetails.css";
import GridSelect from "../../general/components/GridSelect";
const COUPON_HEADER = "Bank promotions";

class BankOffersDetails extends Component {
  applyBankCoupons = val => {
    if (this.props.applyBankOffer) {
      this.props.applyBankOffer(val);
    }
  };

  releaseBankOffer = val => {
    if (this.props.releaseBankOffer) {
      this.props.releaseBankOffer(val);
    }
  };
  onSelectCouponCode = val => {
    if (val[0]) {
      if (this.props.applyBankOffer) {
        this.props.applyBankOffer(val);
        this.props.selecteBankOffer(val[0]);
      }
    } else {
      if (this.props.releaseBankOffer) {
        this.props.selecteBankOffer(null);
        this.props.releaseBankOffer(val);
      }
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <SlideModal {...this.props}>
          <div className={styles.couponHeader}>{COUPON_HEADER}</div>
          <GridSelect
            elementWidthMobile={100}
            offset={0}
            limit={1}
            onSelect={val => this.onSelectCouponCode(val)}
            selected={[this.props.selectedBankOfferCode]}
          >
            {this.props.coupons &&
              this.props.coupons.coupons.map((value, i) => {
                return (
                  <BankCoupons
                    offerDescription={value.offerDescription}
                    offerCode={value.offerCode}
                    offerMinCartValue={value.offerMinCartValue}
                    offerTitle={value.offerTitle}
                    key={i}
                    value={value.offerCode}
                  />
                );
              })}
          </GridSelect>
        </SlideModal>
      </div>
    );
  }
}

export default BankOffersDetails;
