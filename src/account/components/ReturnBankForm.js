import React from "react";
import ReturnsFrame from "./ReturnsFrame";
import BankDetails from "./BankDetails";

export default class ReturnBankForm extends React.Component {
  handleCancel() {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  }
  render() {
    return (
      <ReturnsFrame
        headerText="Refund Details"
        onContinue={this.props.onContinue}
        onCancel={() => this.handleCancel()}
      >
        <BankDetails onChange={this.props.onChange} />
      </ReturnsFrame>
    );
  }
}
