import React from "react";
import filter from "lodash/filter";
import CliqCashToggle from "./CliqCashToggle";
import styles from "./PaymentCardWrapper.css";
import CheckoutEmi from "./CheckoutEmi.js";
import CheckoutCreditCard from "./CheckoutCreditCard.js";
import CheckoutDebitCard from "./CheckoutDebitCard.js";
import CheckoutNetbanking from "./CheckoutNetbanking.js";
import CheckoutSavedCard from "./CheckoutSavedCard.js";

// prettier-ignore
const typeComponentMapping = {
  "EMI": props => <CheckoutEmi {...props} />,
  "Credit Card": props => <CheckoutCreditCard {...props} />,
  "Netbanking": props => <CheckoutNetbanking {...props} />,
    "Debit Card": props => <CheckoutDebitCard {...props} />,
};

export default class PaymentCardWrapper extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
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
    if (toggleState) {
      this.props.applyCliqCash();
    } else {
      this.props.removeCliqCash();
    }
  };

  render() {
    return (
      <div className={styles.base}>
        {this.renderSavedCards()}
        <div>
          {" "}
          <CliqCashToggle
            cashText="Use My CLiQ Cash Balance"
            price="400"
            onToggle={i => this.handleClick(i)}
          />
        </div>
        {this.props.cart.paymentModes && this.renderPaymentCardsComponents()}
      </div>
    );
  }
}
