import React from "react";
import debitCardIcon from "./img/debit-card.svg";
import PropTypes from "prop-types";
import CreditCardForm from "./CreditCardForm.js";
import ManueDetails from "../../general/components/MenuDetails.js";
import { DEBIT_CARD } from "../../lib/constants";
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
  jusPayTokenizeForGiftCard = cardDetails => {
    if (this.props.jusPayTokenizeForGiftCard) {
      this.props.jusPayTokenizeForGiftCard(cardDetails);
    }
  };

  render() {
    return (
      <ManueDetails
        text={DEBIT_CARD}
        osOpen={this.props.currentPaymentMode === DEBIT_CARD}
        onOpenMenu={currentPaymentMode =>
          this.props.onChange({ currentPaymentMode })
        }
        icon={debitCardIcon}
      >
        <CreditCardForm
          binValidation={binNo => this.binValidation(binNo)}
          isFromGiftCard={this.props.isFromGiftCard}
          onChangeCardDetail={card => this.onChangeCardDetail(card)}
        />
      </ManueDetails>
    );
  }
}

CheckoutDebitCard.propTypes = {
  onChangeCvv: PropTypes.func
};
