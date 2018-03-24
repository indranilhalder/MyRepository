import React from "react";
import { Route } from "react-router-dom";
import ReturnReasonForm from "./ReturnReasonForm.js";
import ReturnCliqAndPiqContainer from "../containers/ReturnCliqAndPiqContainer.js";

import ReturnModes from "./ReturnModes.js";
import {
  RETURNS,
  RETURNS_REASON,
  RETURNS_MODES,
  RETURNS_STORE_MAP,
  RETURNS_STORE_BANK_FORM,
  RETURNS_STORE_FINAL,
  RETURN_TO_STORE,
  RETURN_CLIQ_PIQ
} from "../../lib/constants";
export default class ReturnFlow extends React.Component {
  componentDidMount() {
    this.props.returnProductDetails();
    this.props.getReturnRequest();
  }

  renderReturnReason = () => {
    return (
      <div>
        <ReturnReasonForm onContinue={() => this.addReasonForReturn()} />
      </div>
    );
  };

  goToReturnMode = val => {};
  renderReturnModes = () => {
    return (
      <div>
        <ReturnModes selectMode={val => this.goToReturnMode(val)} />
      </div>
    );
  };

  render() {
    return (
      <React.Fragment>
        <Route
          exact
          path={`${RETURNS}${RETURNS_REASON}`}
          component={ReturnReasonForm}
        />
        <Route
          exact
          path={`${RETURNS}${RETURNS_MODES}`}
          component={ReturnModes}
        />
        {/* need to call return bia store pick up  routes change component according to route */}
        <Route
          exact
          path={`${RETURNS}${RETURN_TO_STORE}${RETURNS_STORE_MAP}`}
          component={ReturnReasonForm}
        />
        <Route
          exact
          path={`${RETURNS}${RETURN_TO_STORE}${RETURNS_STORE_BANK_FORM}`}
          component={ReturnReasonForm}
        />
        <Route
          exact
          path={`${RETURNS}${RETURN_TO_STORE}${RETURNS_STORE_FINAL}`}
          component={ReturnReasonForm}
        />
        <Route
          exact
          path={`${RETURNS}${RETURN_TO_STORE}${RETURNS_STORE_FINAL}`}
          component={ReturnReasonForm}
        />

        <Route
          path={`${RETURNS}${RETURN_CLIQ_PIQ}`}
          component={ReturnCliqAndPiqContainer}
        />
        {/* end of need to call return bia store pick up  routes */}
      </React.Fragment>
    );
  }
}
