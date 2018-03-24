import React from "react";
import cloneDeep from "lodash/cloneDeep";
import { Route } from "react-router-dom";
import ReturnReasonForm from "./ReturnReasonForm.js";
import ReturnCliqAndPiqContainer from "../containers/ReturnCliqAndPiqContainer.js";

import ReturnModes from "./ReturnModes.js";
import ReturnToStoreContainer from "../containers/ReturnToStoreContainer";
import ReturnBankForm from "./ReturnBankForm";
import ReturnReasonAndModesContainer from "../containers/ReturnReasonAndModesContainer";
import {
  RETURNS,
  RETURNS_REASON,
  RETURNS_MODES,
  RETURNS_STORE_MAP,
  RETURNS_STORE_BANK_FORM,
  RETURNS_STORE_FINAL,
  RETURN_TO_STORE,
  RETURN_LANDING,
  RETURNS_PREFIX,
  RETURN_CLIQ_PIQ
} from "../../lib/constants";

export default class ReturnFlow extends React.Component {
  constructor(props) {
    super(props);
    this.orderCode = props.location.pathname.split("/")[2];
    this.state = {
      bankDetail: {}
    };
  }
  componentDidMount() {
    this.props.returnProductDetails();
    this.props.getReturnRequest();
  }
  onChange(val) {
    let bankDetail = cloneDeep(this.state.bankDetail);
    Object.assign(bankDetail, val);
    this.setState({ bankDetail });
  }
  navigateToShowInitiateReturn() {
    this.props.history.push({
      pathname: `${RETURNS_PREFIX}/${
        this.orderCode
      }${RETURN_LANDING}${RETURNS_MODES}`,
      state: {
        isRequestFromFlow: true
      }
    });
  }
  render() {
    return (
      <React.Fragment>
        <Route
          path={`${RETURNS}${RETURN_LANDING}`}
          component={ReturnReasonAndModesContainer}
        />
        <Route
          path={`${RETURNS}${RETURNS_STORE_BANK_FORM}`}
          render={() => (
            <ReturnBankForm
              onChange={val => this.onChange(val)}
              onContinue={() => this.navigateToShowInitiateReturn()}
            />
          )}
        />

        <Route
          path={`${RETURNS}${RETURN_TO_STORE}`}
          render={() => <ReturnToStoreContainer {...this.state} />}
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
