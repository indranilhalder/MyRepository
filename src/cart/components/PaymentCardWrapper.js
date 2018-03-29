import React from "react";
import filter from "lodash/filter";
import CliqCashToggle from "./CliqCashToggle";
import styles from "./PaymentCardWrapper.css";
import CheckoutEmi from "./CheckoutEmi.js";
import CheckoutCreditCard from "./CheckoutCreditCard.js";
import CheckoutDebitCard from "./CheckoutDebitCard.js";
import CheckoutNetbanking from "./CheckoutNetbanking.js";
import PaytmOption from "./PaytmOption.js";

import CheckoutSavedCard from "./CheckoutSavedCard.js";

import CheckoutCOD from "./CheckoutCOD.js";
import { PAYTM } from "../../lib/constants";
// prettier-ignore
const typeComponentMapping = {
  "Credit Card": props => <CheckoutCreditCard {...props} />,
    "Debit Card": props => <CheckoutDebitCard {...props} />,
    "Netbanking": props => <CheckoutNetbanking {...props} />,
    "COD": props => <CheckoutCOD {...props}/>,
    "EMI": props => <CheckoutEmi {...props} />,
    "PAYTM": props => <PaytmOption {...props} />,
};

export default class PaymentCardWrapper extends React.Component {
  constructor(props) {
    super(props);
    this.state = {};
  }
  binValidationForPaytm(val) {
    console.log("here");
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
    console.log(this.props);
    if (this.props.cart.paymentModes) {
      let cliqCashBalance = 0;
      if (this.props.cart.paymentModes.cliqCash) {
        cliqCashBalance = this.props.cart.paymentModes.cliqCash
          .totalCliqCashBalance.value;
      }
      return (
        <div className={styles.base}>
          {this.renderSavedCards()}
          <div>
            {" "}
            <CliqCashToggle
              cashText="Use My CLiQ Cash Balance"
              price={cliqCashBalance}
              value={cliqCashBalance}
              onToggle={i => this.handleClick(i)}
            />
          </div>
          {this.props.cart.paymentModes && this.renderPaymentCardsComponents()}
        </div>
      );
    } else {
      return null;
    }
  }
}
