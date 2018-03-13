import React from "react";
import _ from "lodash";
import CliqCashToggle from "./CliqCashToggle";
import styles from "./PaymentCardWrapper.css";
import CheckoutEmi from "./CheckoutEmi.js";
import CheckoutCreditCard from "./CheckoutCreditCard.js";
import CheckoutDebitCard from "./CheckoutDebitCard.js";
import CheckoutNetbanking from "./CheckoutNetbanking.js";

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
    let paymentModesToDisplay = _.filter(
      this.props.cart.paymentModes.paymentModes,
      modes => {
        return modes.value === true;
      }
    );
    return paymentModesToDisplay.map((feedDatum, i) => {
      return this.renderPaymentCard(feedDatum.key, i);
    });
  }

  handleClick = toggleState => {
    if (toggleState) {
      this.props.applyCliqCash();
    } else {
      this.props.removeCliqCash();
    }
  };

  binValidation = (paymentMode, binNo) => {
    if (this.props.binValidation) {
      this.props.binValidation(paymentMode, binNo);
    }
  };

  softReservationForPayment = cardDetails => {
    if (this.props.softReservationForPayment) {
      this.props.softReservationForPayment(cardDetails);
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div>
          {" "}
          <CliqCashToggle
            cashText="Use My CLiQ Cash Balance"
            price="400"
            onToggle={i => this.handleClick(i)}
          />
        </div>
        {this.renderPaymentCardsComponents()}
      </div>
    );
  }
}
