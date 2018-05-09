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
import Loader from "../../general/components/Loader";
const SELECT_REASON_MESSAGE = "Select the Reason";
export default class CancelOrder extends React.Component {
  componentDidMount() {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    const cancelProductDetails = {};
    cancelProductDetails.transactionId = this.props.history.location.state.transactionId;
    cancelProductDetails.orderCode = this.props.match.params[0];
    cancelProductDetails.USSID = this.props.history.location.state.ussid;
    cancelProductDetails.returnCancelFlag = "C";
    if (userDetails && customerCookie) {
      this.props.getDetailsOfCancelledProduct(cancelProductDetails);
    }
  }
  finalSubmit(reason) {
    if (reason.cancelReasonCode) {
      if (window.confirm("Are you sure you want to cancel your order ?")) {
        this.cancelOrder(reason);
      }
    } else {
      this.props.displayToast(SELECT_REASON_MESSAGE);
    }
  }
  cancelOrder = reason => {
    const cancelProductDetails = {};
    cancelProductDetails.transactionId = this.props.history.location.state.transactionId;
    cancelProductDetails.orderCode = this.props.match.params[0];
    cancelProductDetails.USSID = this.props.history.location.state.ussid;
    cancelProductDetails.ticketTypeCode = "C";
    cancelProductDetails.reasonCode = reason.cancelReasonCode;
    cancelProductDetails.refundType = "";
    this.props.cancelProduct(
      cancelProductDetails,
      this.props.cancelProductDetails.orderProductWsDTO[0]
    );
  };
  onCancel() {
    this.props.history.goBack();
  }

  onClickImage(productCode) {
    if (productCode) {
      this.props.history.push(`/p-${productCode.toLowerCase()}`);
    }
  }
  navigateToLogin() {
    return <Redirect to={LOGIN_PATH} />;
  }
  renderLoader() {
    return <Loader />;
  }
  render() {
    let cancelProductDetails = this.props.cancelProductDetails;
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (this.props.loadingForCancelProductDetails) {
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
            onClickImage={productCode => this.onClickImage(productCode)}
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
