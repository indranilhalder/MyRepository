import React from "react";
import MDSpinner from "react-md-spinner";
import ReturnReasonForm from "./ReturnReasonForm.js";
import ReturnModes from "./ReturnModes.js";
import {
  RETURNS_PREFIX,
  RETURN_LANDING,
  RETURNS_MODES,
  RETURNS_REASON,
  QUICK_DROP,
  RETURN_TO_STORE,
  RETURNS_STORE_MAP
} from "../../lib/constants";
const REG_X_FOR_REASON = /reason/i;
const REG_X_FOR_MODES = /modes/i;

const ORDER_ID = "345-34534534";
export default class ReturnReasonAndModes extends React.Component {
  constructor(props) {
    super();
    this.state = {
      reason: null
    };
  }
  renderLoader() {
    return <MDSpinner />;
  }
  onChange(val) {
    this.setState(val);
  }
  renderToModes() {
    // may be we have to hit any api for setting reason here
    this.props.history.push(
      `${RETURNS_PREFIX}/${ORDER_ID}${RETURN_LANDING}${RETURNS_MODES}`
    );
  }
  onSelectMode(mode) {
    if (mode === QUICK_DROP) {
      this.props.history.push(
        `${RETURNS_PREFIX}/${ORDER_ID}${RETURN_TO_STORE}${RETURNS_STORE_MAP}`
      );
    }
  }
  render() {
    if (!this.props.returnRequest) {
      return this.renderLoader();
    }
    const { pathname } = this.props.location;
    const renderReasonForm = (
      <ReturnReasonForm
        productInfo={this.props.returnRequest.returnEntry.orderEntries[0]}
        optionsForReason={this.props.returnRequest.returnReasonDetailsList}
        onChange={comment => this.onChange({ comment })}
        onChangePrimary={reason => this.onChange({ reason })}
        onContinue={() => this.renderToModes()}
      />
    );
    const renderReturnMode = (
      <ReturnModes
        productInfo={this.props.returnRequest.returnEntry.orderEntries[0]}
        selectMode={mode => this.onSelectMode(mode)}
      />
    );
    return (
      <React.Fragment>
        {pathname.match(REG_X_FOR_REASON) && renderReasonForm}
        {pathname.match(REG_X_FOR_MODES) && renderReturnMode}
      </React.Fragment>
    );
  }
}
