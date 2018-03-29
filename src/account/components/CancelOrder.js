import React from "react";
import { Redirect } from "react-router-dom";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  LOGIN_PATH,
  HOME_ROUTER,
  MY_ACCOUNT_ORDERS_PAGE
} from "../../lib/constants";
import CancelReasonForm from "./CancelReasonForm";
import PropTypes from "prop-types";
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
      this.props.cancelProduct(CancelProductDetails);
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
    this.props.cancelOrder(CancelProductDetails);
  }
  onCancel() {
    this.props.history.push(HOME_ROUTER);
  }
  componentWillReceiveProps(nextProps) {
    if (this.props.cancelOrderStatus) {
      this.props.history.push({
        MY_ACCOUNT_ORDERS_PAGE
      });
    }
  }
  navigateToLogin() {
    return <Redirect to={LOGIN_PATH} />;
  }
  render() {
    let cancelProductDetails = this.props.cancelProductDetails;
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (!cancelProductDetails) {
      return <div />;
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
