import React from "react";
import { Redirect } from "react-router-dom";
import ReturnsFrame from "./ReturnsFrame";
import OrderCard from "./OrderCard";
import ReturnsToBank from "./ReturnsToBank";
import OrderReturnAddressDetails from "./OrderReturnAddressDetails";
import PropTypes from "prop-types";
import styles from "./ReturnStoreConfirmation.css";
import visaLogo from "../../cart/components/img/Visa.svg";
import masterLogo from "../../cart/components/img/Master.svg";
import amexLogo from "../../cart/components/img/amex.svg";
import repayLogo from "../../cart/components/img/rupay.svg";
import dinersLogo from "../../cart/components/img/diners.svg";
import discoverLogo from "../../cart/components/img/discover.svg";
import jcbLogo from "../../cart/components/img/jcb.svg";
import {
  RUPAY_CARD,
  VISA_CARD,
  MASTER_CARD,
  AMEX_CARD,
  MESTRO_CARD,
  DINERS_CARD,
  DISCOVER_CARD,
  JCB_CARD,
  MASTER,
  RETURNS_PREFIX,
  RETURN_LANDING,
  RETURNS_REASON
} from "../../lib/constants";
export default class ReturnsStoreConfirmation extends React.Component {
  getCardLogo(cardType) {
    switch (cardType) {
      case VISA_CARD:
        return visaLogo;
      case MASTER_CARD:
        return masterLogo;
      case AMEX_CARD:
        return amexLogo;
      case RUPAY_CARD:
        return repayLogo;
      case MESTRO_CARD:
        return masterLogo;
      case DINERS_CARD:
        return dinersLogo;
      case DISCOVER_CARD:
        return discoverLogo;
      case JCB_CARD:
        return jcbLogo;
      case MASTER:
        return masterLogo;
      default:
        return false;
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
    // Preventing user to open this page direct by hitting URL
    if (
      !this.props.location.state ||
      !this.props.location.state.authorizedRequest
    ) {
      return this.navigateToReturnLanding();
    }
    const data = this.props.returnProductDetails;

    return (
      <ReturnsFrame
        headerText="Return to store"
        onContinue={this.props.onContinue}
        onCancel={this.props.cancel}
      >
        <OrderReturnAddressDetails />
        <div className={styles.card}>
          <OrderCard
            imageUrl={
              data &&
              data.orderProductWsDTO[0] &&
              data.orderProductWsDTO[0].imageURL
            }
            productName={`${data.orderProductWsDTO[0].productBrand} ${
              data.orderProductWsDTO[0].productName
            }`}
            price={data.orderProductWsDTO[0].price}
          >
            {data.orderProductWsDTO[0].quantity && (
              <div>Qty {data.orderProductWsDTO[0].quantity}</div>
            )}
          </OrderCard>
          {this.props.orderDetails &&
            this.props.orderDetails.paymentCardDigit && (
              <ReturnsToBank
                cartNumber={
                  this.props.orderDetails &&
                  this.props.orderDetails.paymentCardDigit
                }
                cardLogo={this.getCardLogo(
                  this.props.orderDetails && this.props.orderDetails.paymentCard
                )}
              />
            )}
        </div>
      </ReturnsFrame>
    );
  }
}
ReturnsStoreConfirmation.propTypes = {
  onContinue: PropTypes.func,
  data: PropTypes.shape({
    orderProductWsDTO: PropTypes.arrayOf([
      PropTypes.shape({
        imageURL: PropTypes.string,
        productName: PropTypes.string,
        productBrand: PropTypes.string,
        price: PropTypes.string,
        quantity: PropTypes.string
      })
    ])
  })
};
