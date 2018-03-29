import React from "react";
import { Redirect } from "react-router-dom";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  LOGIN_PATH,
  MY_ACCOUNT,
  MY_ACCOUNT_ORDERS_PAGE
} from "../../lib/constants";
import CancelReasonForm from "./CancelReasonForm";
import PropTypes from "prop-types";
import MDSpinner from "react-md-spinner";
export default class CancelOrder extends React.Component {
  componentDidMount() {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    const CancelProductDetails = {};
    CancelProductDetails.transactionId = this.props.history.location.state.transactionId;
    CancelProductDetails.orderCode = this.props.match.params[0];
    CancelProductDetails.USSID = this.props.history.location.state.ussid;
    CancelProductDetails.returnCancelFlag = "C";
    if (userDetails && customerCookie) {
      this.props.cancelProductDetails(CancelProductDetails);
    }
  }
  finalSubmit(reason) {
    const CancelProductDetails = {};
    CancelProductDetails.transactionId = this.props.history.location.state.transactionId;
    CancelProductDetails.orderCode = this.props.match.params[0];
    CancelProductDetails.USSID = this.props.history.location.state.ussid;
    CancelProductDetails.ticketTypeCode = "C";
    CancelProductDetails.reasonCode = reason.cancelReasonCode;
    CancelProductDetails.refundType = "";
    this.props.cancelProduct(CancelProductDetails);
  }
  onCancel() {
    this.props.history.goBack();
  }

  navigateToLogin() {
    return <Redirect to={LOGIN_PATH} />;
  }
  renderLoader() {
    return <MDSpinner />;
  }
  render() {
    console.log(this.props);
    let cancelProductDetails = this.props.cancelProductDetailsObj;
    console.log(cancelProductDetails);
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (!cancelProductDetails) {
      return this.renderLoader();
    }
    if (!userDetails || !customerCookie) {
      return this.navigateToLogin();
    }

    return (
      <div>
        {cancelProductDetails && (
          <CancelReasonForm
            cancelProductDetails={cancelProductDetails}
            onContinue={reason => this.finalSubmit(reason)}
            onCancel={() => this.onCancel()}
          />
        )}
      </div>
    );
  }
}
CancelOrder.propTypes = {
  cancelProduct: PropTypes.func,
  cancelOrder: PropTypes.func,
  cancelProductDetails: PropTypes.object
};
