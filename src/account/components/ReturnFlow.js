import React from "react";
import cloneDeep from "lodash.clonedeep";
import { Route } from "react-router-dom";
import ReturnCliqAndPiqContainer from "../containers/ReturnCliqAndPiqContainer.js";

import ReturnModes from "./ReturnModes.js";
import ReturnToStoreContainer from "../containers/ReturnToStoreContainer";
import ReturnBankForm from "./ReturnBankForm";
import ReturnReasonAndModes from "./ReturnReasonAndModes";
import Loader from "../../general/components/Loader";
import SelfCourierContainer from "../containers/SelfCourierContainer";
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
  RETURN_CLIQ_PIQ,
  RETURNS_SELF_COURIER,
  MY_ACCOUNT,
  MY_ACCOUNT_ORDERS_PAGE
} from "../../lib/constants";
const RETURN_FLAG = "R";
const IFSC_PATTERN = /^[A-Za-z]{4}0[A-Z0-9a-z]{6}$/;
const ACCOUNT_NUMBER = "Please enter account number";
const RE_ENTER_ACCOUNT_NUMBER = "Please re-enter account number";
const ACCOUNT_NUMBER_MATCH_TEXT = "Account number did not match";
const ACCOUNT_HOLDER_NAME = "Please enter account holder name";
const BANK_NAME = "Please enter bank name";
const IFSC_CODE_TEXT = "Please enter ifsc code";
const IFSC_CODE_VALID_TEXT = "Please enter valid ifsc code";
const REFUND_MODE_TEXT = "please select refund mode";
export default class ReturnFlow extends React.Component {
  constructor(props) {
    super(props);
    this.orderCode = props.location.pathname.split("/")[2];
    this.transactionId = props.location.state
      ? props.location.state.transactionId
      : null;
    this.isCOD = props.location.state && props.location.state.isCOD;
    this.state = {
      bankDetail: {},
      isCOD: this.isCOD
    };
  }
  componentDidMount() {
    let orderCode = this.orderCode;
    let transactionId = this.transactionId;

    let productDetails = {};
    productDetails.transactionId = transactionId;
    productDetails.returnCancelFlag = RETURN_FLAG;
    productDetails.orderCode = orderCode;

    this.props.returnProductDetailsFunc(productDetails);
    this.props.getReturnRequest(orderCode, transactionId);
  }
  onChangeBankingDetail(val) {
    let bankDetail = cloneDeep(this.state.bankDetail);
    Object.assign(bankDetail, val);
    this.setState({ bankDetail });
  }

  onChangeReasonAndMode(val) {
    this.setState(val);
  }
  navigateToShowInitiateReturn() {
    if (!this.state.bankDetail.accountNumber) {
      this.props.displayToast(ACCOUNT_NUMBER);
      return false;
    }
    if (!this.state.bankDetail.reEnterAccountNumber) {
      this.props.displayToast(RE_ENTER_ACCOUNT_NUMBER);
      return false;
    }
    if (
      this.state.bankDetail.accountNumber !==
      this.state.bankDetail.reEnterAccountNumber
    ) {
      this.props.displayToast(ACCOUNT_NUMBER_MATCH_TEXT);
      return false;
    }
    if (!this.state.bankDetail.userName) {
      this.props.displayToast(ACCOUNT_HOLDER_NAME);
      return false;
    }
    if (!this.state.bankDetail.mode) {
      this.props.displayToast(REFUND_MODE_TEXT);
      return false;
    }
    if (!this.state.bankDetail.bankName) {
      this.props.displayToast(BANK_NAME);
      return false;
    }
    if (!this.state.bankDetail.code) {
      this.props.displayToast(IFSC_CODE_TEXT);
      return false;
    }
    if (
      this.state.bankDetail.code &&
      !IFSC_PATTERN.test(this.state.bankDetail.code)
    ) {
      this.props.displayToast(IFSC_CODE_VALID_TEXT);
      return false;
    } else {
      this.props.history.push({
        pathname: `${RETURNS_PREFIX}/${
          this.orderCode
        }${RETURN_LANDING}${RETURNS_MODES}`,
        state: {
          authorizedRequest: true
        }
      });
    }
  }
  onCancel() {
    this.props.history.goBack();
  }
  navigateToOrderDetail() {
    this.props.history.push(`${MY_ACCOUNT}${MY_ACCOUNT_ORDERS_PAGE}`);
  }
  renderLoader() {
    return <Loader />;
  }
  render() {
    // if user hit return page by url then i am navigating him on orderDetial page
    if (this.props.error) {
      this.props.displayToast(this.props.error);
      this.props.history.goBack();
    }
    if (!this.transactionId) {
      this.navigateToOrderDetail();
    }
    if (!this.props.returnRequest && !this.props.returnProductDetails) {
      return this.renderLoader();
    }

    return (
      <React.Fragment>
        <Route
          path={`${RETURNS}${RETURN_LANDING}`}
          render={() => (
            <ReturnReasonAndModes
              {...this.state}
              {...this.props}
              onChange={val => this.onChangeReasonAndMode(val)}
            />
          )}
        />
        <Route
          exact
          path={`${RETURNS}${RETURNS_STORE_BANK_FORM}`}
          render={() => (
            <ReturnBankForm
              onChange={val => this.onChangeBankingDetail(val)}
              onContinue={() => this.navigateToShowInitiateReturn()}
              onCancel={() => this.onCancel()}
            />
          )}
        />
        <Route
          path={`${RETURNS}${RETURN_TO_STORE}`}
          render={() => (
            <ReturnToStoreContainer {...this.state} {...this.props} />
          )}
        />
        <Route
          path={`${RETURNS}${RETURN_CLIQ_PIQ}`}
          render={() => (
            <ReturnCliqAndPiqContainer {...this.state} {...this.props} />
          )}
        />
        <Route
          exact
          path={`${RETURNS}${RETURNS_SELF_COURIER}`}
          render={() => <SelfCourierContainer {...this.state} />}
        />
        {/* end of need to call return bia store pick up  routes */}
      </React.Fragment>
    );
  }
}
