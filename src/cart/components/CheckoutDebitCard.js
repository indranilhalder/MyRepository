import React from "react";
import debitCardIcon from "./img/debit-card.svg";
import PropTypes from "prop-types";
import CreditCardForm from "./CreditCardForm.js";
import ManueDetails from "../../general/components/MenuDetails.js";
const PAYMENT_MODE = "Debit Card";
export default class CheckoutDebitCard extends React.Component {
  onChangeCvv(i) {
    if (this.props.onChangeCvv) {
      this.props.onChangeCvv(i);
    }
  }
  binValidation = binNo => {
    if (this.props.binValidation) {
      this.props.binValidation(PAYMENT_MODE, binNo);
    }
  };

  softReservationForPayment = cardDetails => {
    if (this.props.softReservationForPayment) {
      this.props.softReservationForPayment(cardDetails);
    }
  };

  render() {
    return (
      <ManueDetails text="Debit Card" icon={debitCardIcon}>
        <CreditCardForm
          onChangeCvv={i => this.onChangeCvv(i)}
          binValidation={binNo => this.binValidation(binNo)}
          softReservationForPayment={cardDetails =>
            this.softReservationForPayment(cardDetails)
          }
        />
      </ManueDetails>
    );
  }
}

CheckoutDebitCard.propTypes = {
  onChangeCvv: PropTypes.func
};
