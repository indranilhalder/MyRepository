import React from "react";
import creditCardIcon from "./img/credit-card.svg";
import PropTypes from "prop-types";
import CreditCardForm from "./CreditCardForm.js";
import ManueDetails from "../../general/components/MenuDetails.js";
import { CREDIT_CARD } from "../../lib/constants";
const PAYMENT_MODE = "Credit Card";

export default class CheckoutCreditCard extends React.Component {
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

  jusPayTokenizeForGiftCard = cardDetails => {
    if (this.props.jusPayTokenizeForGiftCard) {
      this.props.jusPayTokenizeForGiftCard(cardDetails);
    }
  };
  onChangeCardDetail = card => {
    if (this.props.onChangeCardDetail) {
      this.props.onChangeCardDetail(card);
    }
  };
  render() {
    return (
      <ManueDetails
        text={CREDIT_CARD}
        isOpen={this.props.currentPaymentMode === CREDIT_CARD}
        onOpenMenu={currentPaymentMode =>
          this.props.onChange({ currentPaymentMode })
        }
        icon={creditCardIcon}
      >
        <CreditCardForm
          onChangeCvv={i => this.onChangeCvv(i)}
          binValidation={binNo => this.binValidation(binNo)}
          onChangeCardDetail={card => this.onChangeCardDetail(card)}
        />
      </ManueDetails>
    );
  }
}

CheckoutCreditCard.propTypes = {
  onChangeCvv: PropTypes.func
};
