import React, { Component } from "react";
import SearchCupon from "../../pdp/components/SearchCupon.js";
import BankCoupons from "./BankCoupons.js";
import SlideModal from "../../general/components/SlideModal";
import styles from "./BankOffersDetails.css";
import GridSelect from "../../general/components/GridSelect";
const COUPON_HEADER = "Bank promotions";

class BankOffersDetails extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isAlreadySetCoupon: false,
      previousSelectedCouponCode: props.selectedBankOfferCode,
      selectedBankOfferCode: props.selectedBankOfferCode
    };
  }
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
  applyUserCoupon() {
    if (this.state.selectedBankOfferCode) {
      if (
        this.state.selectedBankOfferCode !==
        this.state.previousSelectedCouponCode
      ) {
        if (this.state.previousSelectedCouponCode) {
          this.setState({
            previousSelectedCouponCode: this.state.selectedBankOfferCode
          });
          this.props.selecteBankOffer(this.state.selectedBankOfferCode);
          this.props.releasePreviousAndApplyNewBankOffer(
            this.state.previousSelectedCouponCode,
            this.state.selectedBankOfferCode
          );
        } else {
          this.props.selecteBankOffer(this.state.selectedBankOfferCode);
          this.props.applyBankOffer(this.state.selectedBankOfferCode);
          this.setState({
            previousSelectedCouponCode: this.state.selectedBankOfferCode
          });
        }
      } else {
        this.props.selecteBankOffer("");
        this.setState({
          previousSelectedCouponCode: "",
          selectedBankOfferCode: ""
        });
        this.props.releaseBankOffer(this.state.selectedBankOfferCode);
      }
    }
  }

  onSelectCouponCode = val => {
    this.setState({ selectedBankOfferCode: val[0] });
  };
  render() {
    return (
      <div className={styles.base}>
        <SlideModal {...this.props}>
          <div className={styles.couponHeader}>{COUPON_HEADER}</div>
          <div className={styles.searchHolder}>
            <SearchCupon
              label={
                this.state.previousSelectedCouponCode &&
                this.state.previousSelectedCouponCode ===
                  this.state.selectedBankOfferCode
                  ? "Remove"
                  : "Apply"
              }
              couponCode={this.state.selectedBankOfferCode}
              getValue={selectedBankOfferCode =>
                this.setState({ selectedBankOfferCode })
              }
              applyUserCoupon={couponCode => this.applyUserCoupon(couponCode)}
            />
          </div>
          <GridSelect
            elementWidthMobile={100}
            offset={0}
            limit={1}
            onSelect={val => this.onSelectCouponCode(val)}
            selected={[this.state.selectedBankOfferCode]}
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
