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
    this.orderCode = props.location.pathname.split("/")[2];
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
  renderToModes(data) {
    // may be we have to hit any api for setting reason here
    this.setState({
      comment: data.comment,
      reasonCode: data.secondaryReasons
    });
    this.props.history.push(
      `${RETURNS_PREFIX}/${this.orderCode}${RETURN_LANDING}${RETURNS_MODES}`
    );
  }
  onSelectMode(mode) {
    console.log(this.state);
    if (mode === QUICK_DROP) {
      this.props.history.push({
        pathname: `${RETURNS_PREFIX}/${ORDER_ID}${RETURN_TO_STORE}${RETURNS_STORE_MAP}`,
        state: {
          isRequestFromFlow: true
        }
      });
    }
  }
  render() {
    if (!this.props.returnRequest || !this.props.returnProductDetails) {
      return this.renderLoader();
    }
    console.log(this.props);
    const { pathname } = this.props.location;
    console.log(pathname);
    const renderReasonForm = (
      <ReturnReasonForm
        returnProductDetails={this.props.returnProductDetails}
        onChange={comment => this.onChange({ comment })}
        onChangePrimary={reason => this.onChange({ reason })}
        onContinue={data => this.renderToModes(data)}
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
