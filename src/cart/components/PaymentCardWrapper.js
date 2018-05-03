import React from "react";
import filter from "lodash.filter";
import CliqCashToggle from "./CliqCashToggle";
import styles from "./PaymentCardWrapper.css";
import EmiPanel from "./EmiPanel.js";
import CheckoutCreditCard from "./CheckoutCreditCard.js";
import CheckoutDebitCard from "./CheckoutDebitCard.js";
import CheckoutNetbanking from "./CheckoutNetbanking.js";
import CheckoutSavedCard from "./CheckoutSavedCard.js";
import CheckoutCOD from "./CheckoutCOD.js";
import { PAYTM } from "../../lib/constants";
import PaytmOption from "./PaytmOption.js";
import CheckOutHeader from "./CheckOutHeader";
let cliqCashToggleState = false;

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
    let paymentModesToDisplay = filter(
      this.props.cart.paymentModes.paymentModes,
      modes => {
        return modes.value === true;
      }
    );
    return paymentModesToDisplay.map((feedDatum, i) => {
      return this.renderPaymentCard(feedDatum.key, i);
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
        removeNoCostEmi={couponCode => this.props.removeNoCostEmi(couponCode)}
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

  render() {
    if (this.props.cart.paymentModes) {
      return (
        <div className={styles.base}>
          <div className={styles.card}>
            <CheckOutHeader
              confirmTitle="Choose payment Method"
              indexNumber="3"
            />
          </div>
          {this.props.isRemainingBalance && this.renderSavedCards()}
          {!this.props.isFromGiftCard && (
            <div>
              {" "}
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
          {this.props.isRemainingBalance &&
            this.props.cart.paymentModes &&
            this.renderPaymentCardsComponents()}
        </div>
      );
    } else {
      return null;
    }
  }
}
