import React from "react";
import { Redirect } from "react-router-dom";
import ReturnsFrame from "./ReturnsFrame";
import PropTypes from "prop-types";
import CourierProduct from "./CourierProduct.js";
import styles from "./SelfCourier.css";
import {
  SELF_COURIER,
  RETURNS_PREFIX,
  RETURN_LANDING,
  RETURNS_REASON
} from "../../lib/constants";
const NEFT = "NEFT";
const SELF_SHIPMENT = "selfShipment";
export default class SelfCourier extends React.Component {
  cancel = () => {};

  onCancel() {
    this.props.history.goBack();
  }
  onContinue() {
    if (this.props.newReturnInitial) {
      const orderDetails = this.props.returnProductDetails.orderProductWsDTO[0];
      const returnRequest = this.props.returnRequest.codSelfShipData;
      const initiateReturn = {};
      initiateReturn.transactionId = orderDetails.transactionId;
      initiateReturn.transactionType = "1";
      initiateReturn.ussid = orderDetails.USSID;
      initiateReturn.refundMode = NEFT;
      initiateReturn.refundType = "S";
      initiateReturn.orderCode = orderDetails.sellerorderno;
      initiateReturn.returnMethod = SELF_SHIPMENT;
      initiateReturn.paymentMethod = returnRequest && returnRequest.paymentMode;
      initiateReturn.isCODorder = "N";
      if (this.props.bankDetail.accountNumber) {
        initiateReturn.accountNumber =
          returnRequest && returnRequest.bankAccount;
        initiateReturn.reEnterAccountNumber =
          returnRequest && returnRequest.bankAccount;
        initiateReturn.bankName = returnRequest && returnRequest.bankName;
        initiateReturn.IFSCCode = returnRequest && returnRequest.bankKey;
        initiateReturn.title = returnRequest && returnRequest.title;
        initiateReturn.accountHolderName = returnRequest && returnRequest.name;
      }
      if (this.props.data) {
        initiateReturn.returnReasonCode = this.props.data.returnReasonCode;
        initiateReturn.subReasonCode = this.props.data.subReasonCode;
        initiateReturn.comment = this.props.data.comment;
      }
      this.props.newReturnInitial(initiateReturn);
    }
  }

  navigateToReturnLanding() {
    return (
      <Redirect
        to={`${RETURNS_PREFIX}/${
          this.orderCode
        }${RETURN_LANDING}${RETURNS_REASON}`}
      />
    );
  }
  render() {
    if (this.props.loading) {
      this.props.showSecondaryLoader();
    } else {
      this.props.hideSecondaryLoader();
    }
    return (
      <ReturnsFrame
        headerText="Steps for self courier"
        onContinue={() => this.onContinue()}
        buttonText="Initiate Return"
        onCancel={() => this.onCancel()}
      >
        <div className={styles.card}>
          <CourierProduct
            indexNumber="1"
            header="Courier the product"
            text="Use any courier services to ship back the products to our address. "
            subText="Please use the form sent along with the invoice or
            Re-download the form again from below"
            underlineButtonLabel="Download form"
            underlineButtonColour="#ff1744"
            selfCourierDocumentLink={
              this.props.returnRequest &&
              this.props.returnRequest.selfCourierDocumentLink
            }
          />
        </div>
        <div className={styles.card}>
          <CourierProduct indexNumber="2" header="Update the AWB number">
            <div className={styles.awbText}>
              Please update the AWB number provided by the courier service in
              the <span>Order history</span> section of My Account against the
              order
            </div>
          </CourierProduct>
        </div>
      </ReturnsFrame>
    );
  }
}
SelfCourier.propTypes = {
  onCancel: PropTypes.func,
  onContinue: PropTypes.func,
  downloadForm: PropTypes.func
};
