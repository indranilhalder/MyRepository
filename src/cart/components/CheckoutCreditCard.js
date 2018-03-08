import React from "react";
import creditCardIcon from "./img/credit-card.svg";
import PropTypes from "prop-types";
import CreditCardForm from "./CreditCardForm.js";
import ManueDetails from "../../general/components/MenuDetails.js";

export default class CheckoutCreditCard extends React.Component {
  onChangeCvv(i) {
    if (this.props.onChangeCvv) {
      this.props.onChangeCvv(i);
    }
  }

  render() {
    return (
      <ManueDetails text="Credit Card" icon={creditCardIcon}>
        <CreditCardForm onChangeCvv={i => this.onChangeCvv(i)} />
      </ManueDetails>
    );
  }
}

CheckoutCreditCard.propTypes = {
  onChangeCvv: PropTypes.func
};
