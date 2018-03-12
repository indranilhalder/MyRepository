import React from "react";
import debitCardIcon from "./img/debit-card.svg";
import PropTypes from "prop-types";
import CreditCardForm from "./CreditCardForm.js";
import ManueDetails from "../../general/components/MenuDetails.js";

export default class CheckoutDebitCard extends React.Component {
  onChangeCvv(i) {
    if (this.props.onChangeCvv) {
      this.props.onChangeCvv(i);
    }
  }

  render() {
    return (
      <ManueDetails text="Debit Card" icon={debitCardIcon}>
        <CreditCardForm onChangeCvv={i => this.onChangeCvv(i)} />
      </ManueDetails>
    );
  }
}

CheckoutDebitCard.propTypes = {
  onChangeCvv: PropTypes.func
};
