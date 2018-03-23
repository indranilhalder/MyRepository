import React from "react";
import { Route } from "react-router-dom";

import ReturnReasonAndModesContainer from "../containers/ReturnReasonAndModesContainer";
import {
  RETURNS,
  RETURNS_REASON,
  RETURNS_MODES,
  RETURNS_STORE_MAP,
  RETURNS_STORE_BANK_FORM,
  RETURNS_STORE_FINAL,
  RETURN_TO_STORE,
  RETURN_LANDING
} from "../../lib/constants";
export default class ReturnFlow extends React.Component {
  componentDidMount() {
    this.props.returnProductDetails();
    this.props.getReturnRequest();
  }

  render() {
    return (
      <React.Fragment>
        <Route
          path={`${RETURNS}${RETURN_LANDING}`}
          component={ReturnReasonAndModesContainer}
        />
      </React.Fragment>
    );
  }
}
