import React from "react";
import filter from "lodash/filter";
import CliqCashToggle from "./CliqCashToggle";
import styles from "./PaymentCardWrapper.css";
import CheckoutEmi from "./CheckoutEmi.js";
import CheckoutCreditCard from "./CheckoutCreditCard.js";
import CheckoutDebitCard from "./CheckoutDebitCard.js";
import CheckoutNetbanking from "./CheckoutNetbanking.js";
import CheckoutSavedCard from "./CheckoutSavedCard.js";
import CheckoutCOD from "./CheckoutCOD.js";
let cliqCashToggleState = false;

// prettier-ignore
const typeComponentMapping = {
  "Credit Card": props => <CheckoutCreditCard {...props} />,
    "Debit Card": props => <CheckoutDebitCard {...props} />,
    "Netbanking": props => <CheckoutNetbanking {...props} />,

    "EMI": props => <CheckoutEmi {...props} />,
};

export default class PaymentCardWrapper extends React.Component {
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

  render() {
    if (this.props.cart.paymentModes) {
      return (
        <div className={styles.base}>
          {this.props.isRemainingBalance && this.renderSavedCards()}
          <div>
            {" "}
            <CliqCashToggle
              cashText="Use My CLiQ Cash Balance"
              price={this.props.cliqCashAmount}
              value={this.props.cliqCashAmount}
              active={cliqCashToggleState}
              onToggle={val => this.handleClick(val)}
            />
          </div>
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
