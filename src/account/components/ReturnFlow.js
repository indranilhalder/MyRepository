import React from "react";
import { Route } from "react-router-dom";
import ReturnReasonForm from "./ReturnReasonForm.js";
import ReturnModes from "./ReturnModes.js";
import ReturnToStoreContainer from "../containers/ReturnToStoreContainer";
import {
  RETURNS,
  RETURNS_REASON,
  RETURNS_MODES,
  RETURN_TO_STORE
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
    console.log(this.props);
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
        <Route
          path={`${RETURNS}${RETURN_TO_STORE}`}
          component={ReturnToStoreContainer}
        />
      </React.Fragment>
    );
  }
}
