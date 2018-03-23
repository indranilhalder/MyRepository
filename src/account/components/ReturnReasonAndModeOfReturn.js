import React from "react";
import PropTypes from "prop-types";
import ReturnReasonForm from "./ReturnReasonForm.js";
import ReturnModes from "./ReturnModes.js";

export default class ReturnReasonAndModeOfReturn extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      reason: false,
      modeSelected: false
    };
  }

  componentDidMount() {
    this.props.returnProductDetails();
    this.props.getReturnRequest();
  }

  addReasonForReturn = () => {
    this.setState({ reason: true });
  };

  renderReturnReason = () => {
    return (
      <div>
        <ReturnReasonForm onContinue={() => this.addReasonForReturn()} />
      </div>
    );
  };

  renderReturnModes = () => {
    return (
      <div>
        <ReturnModes selectMode={val => console.log(val)} />
      </div>
    );
  };

  render() {
    console.log(this.props);
    return (
      <div>
        {!this.state.reason && this.renderReturnReason()}
        {!this.state.modeSelected && this.renderReturnModes()}
      </div>
    );
  }
}
