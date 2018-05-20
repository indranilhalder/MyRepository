import React, { Component } from "react";
import SearchCupon from "../../pdp/components/SearchCupon.js";
import BankCoupons from "./BankCoupons.js";
import SlideModal from "../../general/components/SlideModal";
import styles from "./BankOffersDetails.css";
import GridSelect from "../../general/components/GridSelect";
import { SUCCESS, ERROR, BANK_COUPON_COOKIE } from "../../lib/constants";
import {
  RELEASE_BANK_OFFER_FAILURE,
  APPLY_BANK_OFFER_FAILURE
} from "../actions/cart.actions";
const COUPON_HEADER = "Bank Offers";
const REMOVE = "Remove";
const APPLY = "Apply";
class BankOffersDetails extends Component {
  constructor(props) {
    super(props);
    this.state = {
      previousSelectedCouponCode: props.selectedBankOfferCode,
      selectedBankOfferCode: props.selectedBankOfferCode
    };
  }
  async applyUserCoupon() {
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
          const applyNewBankOfferStatus = await this.props.releaseBankOffer(
            this.state.previousSelectedCouponCode,
            this.state.selectedBankOfferCode
          );
          if (applyNewBankOfferStatus.status === SUCCESS) {
            this.props.selecteBankOffer(this.state.selectedBankOfferCode);
            this.props.closeModal();
          } else {
            if (
              applyNewBankOfferStatus.status === ERROR &&
              applyNewBankOfferStatus.type === RELEASE_BANK_OFFER_FAILURE
            ) {
              this.setState({
                selectedBankOfferCode: this.state.previousSelectedCouponCode
              });
            } else if (
              applyNewBankOfferStatus.status === ERROR &&
              applyNewBankOfferStatus.type === APPLY_BANK_OFFER_FAILURE
            ) {
              this.props.selecteBankOffer("");
              this.setState({
                previousSelectedCouponCode: "",
                selectedBankOfferCode: ""
              });
            }
          }
        } else {
          const applyNewCouponCode = await this.props.applyBankOffer(
            this.state.selectedBankOfferCode
          );
          if (applyNewCouponCode.status === SUCCESS) {
            this.props.selecteBankOffer(this.state.selectedBankOfferCode);
            this.props.closeModal();
          } else {
            this.setState({
              previousSelectedCouponCode: "",
              selectedBankOfferCode: ""
            });
          }
        }
      } else {
        const releaseBankOfferReq = await this.props.releaseBankOffer(
          this.state.selectedBankOfferCode
        );
        if (releaseBankOfferReq.status === SUCCESS) {
          this.props.selecteBankOffer("");
          this.setState({
            previousSelectedCouponCode: "",
            selectedBankOfferCode: ""
          });
        }
      }
    }
  }

  onSelectCouponCode = val => {
    if (val[0]) {
      this.setState({ selectedBankOfferCode: val[0] });
    } else {
      this.setState({ selectedBankOfferCode: "" });
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <SlideModal {...this.props}>
          <div className={styles.dataHolder}>
            <div className={styles.couponHeader}>{COUPON_HEADER}</div>
            <div className={styles.searchHolder}>
              <SearchCupon
                label={
                  this.state.previousSelectedCouponCode &&
                  this.state.previousSelectedCouponCode ===
                    this.state.selectedBankOfferCode
                    ? REMOVE
                    : APPLY
                }
                placeholder="Bank Offer Code"
                couponCode={this.state.selectedBankOfferCode}
                getValue={selectedBankOfferCode =>
                  this.setState({ selectedBankOfferCode })
                }
                applyUserCoupon={() => this.applyUserCoupon()}
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
                      offerMaxDiscount={value.offerMaxDiscount}
                      offerTitle={value.offerTitle}
                      key={i}
                      value={value.offerCode}
                    />
                  );
                })}
            </GridSelect>
          </div>
        </SlideModal>
      </div>
    );
  }
}

export default BankOffersDetails;
