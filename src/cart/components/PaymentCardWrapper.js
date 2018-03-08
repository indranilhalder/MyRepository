import React from "react";
import _ from "lodash";
import EmiAccordian from "./EmiAccordian.js";

// prettier-ignore
const typeComponentMapping = {
  "EMI": props => <EmiAccordian {...props} />,
};

export default class PaymentCardWrapper extends React.Component {
  renderPaymentCard = datumType => {
    console.log(datumType);
    console.log(this.props);
    return (
      <React.Fragment>
        {typeComponentMapping[datumType] &&
          typeComponentMapping[datumType]({ ...this.props })}
      </React.Fragment>
    );
  };

  renderPaymentCardsComponents() {
    // let paymentModesDisplay = this.props.paymentDetails.paymentModes;
    let paymentModesToDisplay = _.filter(
      this.props.paymentDetails.paymentModes,
      modes => {
        return modes.value === true;
      }
    );
    return paymentModesToDisplay.map((feedDatum, i) => {
      return this.renderPaymentCard(feedDatum.key, i);
    });
  }

  render() {
    return <div>{this.renderPaymentCardsComponents()}</div>;
  }
}
