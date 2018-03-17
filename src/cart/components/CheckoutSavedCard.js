import React from "react";
import savedCardIcon from "./img/saved-card.svg";
import PropTypes from "prop-types";
import SavedCard from "./SavedCard.js";
import MenuDetails from "../../general/components/MenuDetails.js";
import filter from "lodash/filter";
import merge from "lodash/merge";

export default class CheckoutSavedCard extends React.Component {
  onChangeCvv(cvv, cardNo) {
    if (cvv.length === 3) {
      let paymentModesToDisplay = filter(this.props.saveCardDetails, modes => {
        return modes.value.cardEndingDigits === cardNo;
      });

      var cardDetails = merge(paymentModesToDisplay[0].value, {
        cvvNumber: cvv
      });

      this.props.binValidationForSavedCard(cardDetails);
    }
  }

  render() {
    return (
      <MenuDetails text="Saved Cards" icon={savedCardIcon}>
        {this.props.saveCardDetails &&
          this.props.saveCardDetails.map((data, i) => {
            return (
              <SavedCard
                key={i}
                cardNumber={data.value.cardEndingDigits}
                cardImage={data.cardImage}
                onChangeCvv={(cvv, cardNo) => this.onChangeCvv(cvv, cardNo)}
              />
            );
          })}
      </MenuDetails>
    );
  }
}

CheckoutSavedCard.propTypes = {
  saveCardDetails: PropTypes.arrayOf(
    PropTypes.shape({
      cardNumber: PropTypes.string,
      cardImage: PropTypes.string
    })
  ),
  onChangeCvv: PropTypes.func
};
