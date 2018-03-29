import React from "react";
import ReturnsFrame from "./ReturnsFrame";
import BankDetails from "./BankDetails";

export default class ReturnBankForm extends React.Component {
  render() {
    return (
      <ReturnsFrame
        headerText="Refund Details"
        onContinue={this.props.onContinue}
      >
        <BankDetails onChange={this.props.onChange} />
      </ReturnsFrame>
    );
  }
}
