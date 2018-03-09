import React from "react";
import _ from "lodash";
import EmiAccordian from "./EmiAccordian.js";

// prettier-ignore
const typeComponentMapping = {
  "EMI": props => <EmiAccordian {...props} />,
};

export default class PaymentCardWrapper extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      isToggleOn: true
    };
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

  handleClick = () => {
    this.setState({ isToggleOn: !this.state.isToggleOn });
    if (this.state.isToggleOn) {
      this.props.applyCliqCash();
    } else {
      this.props.removeCliqCash();
    }
  };

  render() {
    return (
      <div>
        <div>
          {" "}
          <button onClick={this.handleClick}>
            {this.state.isToggleOn ? "On" : "OFF"}
          </button>
        </div>
        {this.renderPaymentCardsComponents()}
      </div>
    );
  }
}
