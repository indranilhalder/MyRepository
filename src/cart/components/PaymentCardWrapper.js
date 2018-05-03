import React from "react";
import filter from "lodash.filter";
import find from "lodash.find";
import CliqCashToggle from "./CliqCashToggle";
import styles from "./PaymentCardWrapper.css";
import EmiPanel from "./EmiPanel.js";
import CheckoutCreditCard from "./CheckoutCreditCard.js";
import CheckoutDebitCard from "./CheckoutDebitCard.js";
import CheckoutNetbanking from "./CheckoutNetbanking.js";
import CheckoutSavedCard from "./CheckoutSavedCard.js";
import CheckoutCOD from "./CheckoutCOD.js";
import { PAYTM, OLD_CART_GU_ID } from "../../lib/constants";
import PaytmOption from "./PaytmOption.js";
import BankOffer from "./BankOffer.js";
import GridSelect from "../../general/components/GridSelect";

import CheckOutHeader from "./CheckOutHeader";
import { getCookie } from "../../lib/Cookie";
let cliqCashToggleState = false;
const SEE_ALL_BANK_OFFERS = "See All Bank Offers";
const keyForCreditCard = "Credit Card";
const keyForDebitCard = "Debit Card";
const keyForNetbanking = "Netbanking";
const keyForEMI = "EMI";
const keyForCOD = "COD";
const keyForPaytm = "PAYTM";

const sequanceOfPaymentMode = [
  keyForCreditCard,
  keyForDebitCard,
  keyForEMI,
  keyForNetbanking,
  keyForPaytm,
  keyForCOD
];
// prettier-ignore
const typeComponentMapping = {
  "Credit Card": props => <CheckoutCreditCard {...props} />,
    "Debit Card": props => <CheckoutDebitCard {...props} />,
    "Netbanking": props => <CheckoutNetbanking {...props} />,
     "COD": props => <CheckoutCOD {...props}/>,
    "EMI": props => <EmiPanel {...props} />,
    "PAYTM": props => <PaytmOption {...props} />,
};

export default class PaymentCardWrapper extends React.Component {
  componentDidMount = () => {
    if (this.props.getPaymentModes && !this.props.cart.paymentModes) {
      this.props.getPaymentModes();
    }
    if (
      this.props.getCODEligibility &&
      !this.props.cart.codEligibilityDetails
    ) {
      this.props.getCODEligibility();
    }
  };
  binValidationForPaytm(val) {
    if (this.props.binValidationForPaytm) {
      this.props.binValidationForPaytm(PAYTM, "", val);
    }
  }
  renderPaymentCard = datumType => {
    return (
      <React.Fragment>
        {typeComponentMapping[datumType] &&
          typeComponentMapping[datumType]({ ...this.props })}
      </React.Fragment>
    );
  };

  renderPaymentCardsComponents() {
    let paymentModesToDisplay = sequanceOfPaymentMode.filter(mode => {
      return find(
        this.props.cart.paymentModes.paymentModes,
        availablePaymentMode => {
          return (
            availablePaymentMode.key === mode && availablePaymentMode.value
          );
        }
      );
    });
    return paymentModesToDisplay.map((feedDatum, i) => {
      return this.renderPaymentCard(feedDatum, i);
    });
  }

  binValidationForSavedCard = cardDetails => {
    if (this.props.binValidationForSavedCard) {
      this.props.binValidationForSavedCard(cardDetails);
    }
  };
  renderSavedCards = () => {
    return (
      <CheckoutSavedCard
        currentPaymentMode={this.props.currentPaymentMode}
        onSelectPaymentsMode={currentPaymentMode =>
          this.props.onChange({ currentPaymentMode })
        }
        binValidationForSavedCard={cardDetails =>
          this.binValidationForSavedCard(cardDetails)
        }
        saveCardDetails={
          this.props.cart.paymentModes.savedCardResponse.savedCardDetailsMap
        }
      />
    );
  };

  handleClick = toggleState => {
    cliqCashToggleState = toggleState;
    if (toggleState) {
      this.props.applyCliqCash();
    } else {
      this.props.removeCliqCash();
    }
  };

  addGiftCard = () => {
    if (this.props.addGiftCard) {
      this.props.addGiftCard();
    }
  };

  renderBankOffers = () => {
    let offerMinCartValue, offerTitle, offerCode;
    if (
      this.props.cart.paymentModes &&
      this.props.cart.paymentModes.paymentOffers &&
      this.props.cart.paymentModes.paymentOffers.coupons
    ) {
      const selectedCoupon = this.props.cart.paymentModes.paymentOffers.coupons.find(
        coupon => {
          return coupon.offerCode === this.props.selectedBankOfferCode;
        }
      );
      if (selectedCoupon) {
        offerMinCartValue = selectedCoupon.offerMinCartValue;
        offerTitle = selectedCoupon.offerTitle;
        offerCode = selectedCoupon.offerCode;
      } else {
        offerMinCartValue = this.props.cart.paymentModes.paymentOffers
          .coupons[0].offerMinCartValue;
        offerTitle = this.props.cart.paymentModes.paymentOffers.coupons[0]
          .offerTitle;
        offerCode = this.props.cart.paymentModes.paymentOffers.coupons[0]
          .offerCode;
      }
    }

    return (
      <GridSelect
        elementWidthMobile={100}
        offset={0}
        limit={1}
        onSelect={val => this.applyBankCoupons(val)}
        selected={[this.props.selectedBankOfferCode]}
      >
        <BankOffer
          bankName={offerTitle}
          offerText={offerMinCartValue}
          label={SEE_ALL_BANK_OFFERS}
          applyBankOffers={() => this.props.openBankOffers()}
          value={offerCode}
        />
      </GridSelect>
    );
  };

  render() {
    if (this.props.cart.paymentModes) {
      return (
        <div className={styles.base}>
          {!this.props.isFromGiftCard && (
            <div>
              <CliqCashToggle
                cashText="Use My CLiQ Cash Balance"
                price={
                  isNaN(this.props.cliqCashAmount)
                    ? 0
                    : this.props.cliqCashAmount
                }
                value={this.props.cliqCashAmount}
                active={cliqCashToggleState}
                onToggle={val => this.handleClick(val)}
                isFromGiftCard={this.props.isFromGiftCard}
                addGiftCard={() => this.addGiftCard()}
              />
            </div>
          )}
          {!this.props.isPaymentFailed && this.renderBankOffers()}
          {this.props.isRemainingBalance && (
            <div className={styles.paymentModes}>
              <div className={styles.card}>
                <CheckOutHeader
                  confirmTitle="Choose payment Method"
                  indexNumber="3"
                />
              </div>
              {this.renderSavedCards()}
              {this.props.cart.paymentModes &&
                this.renderPaymentCardsComponents()}
            </div>
          )}
        </div>
      );
    } else {
      return null;
    }
  }
}
