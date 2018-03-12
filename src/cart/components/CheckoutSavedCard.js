import React from "react";
import savedCardIcon from "./img/saved-card.svg";
import PropTypes from "prop-types";
import SavedCard from "./SavedCard.js";
import ManueDetails from "../../general/components/MenuDetails.js";

export default class CheckoutSavedCard extends React.Component {
  onChangeCvv(i) {
    if (this.props.onChangeCvv) {
      this.props.onChangeCvv(i);
    }
  }

  render() {
    return (
      <ManueDetails text="Saved Cards" icon={savedCardIcon}>
        {this.props.saveCardDetails &&
          this.props.saveCardDetails.map((data, i) => {
            return (
              <SavedCard
                key={i}
                cardNumber={data.cardNumber}
                cardImage={data.cardImage}
                onChangeCvv={i => this.onChangeCvv(i)}
              />
            );
          })}
      </ManueDetails>
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
