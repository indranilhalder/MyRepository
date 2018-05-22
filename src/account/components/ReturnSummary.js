import React from "react";
import ReturnsFrame from "./ReturnsFrame";
import OrderCard from "./OrderCard";
import PropTypes from "prop-types";
import ReturnsToBank from "./ReturnsToBank";
import OrderReturnAddressDetails from "./OrderReturnAddressDetails";
import OrderReturnDateAndTimeDetails from "./OrderReturnDateAndTimeDetails";
import styles from "./ReturnSummary.css";
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
  MASTER
} from "../../lib/constants";
export default class ReturnSummary extends React.Component {
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

  onContinue() {
    if (this.props.onContinue) {
      this.props.onContinue();
    }
  }
  onChangeAddress() {
    if (this.props.onChangeAddress) {
      this.props.onChangeAddress();
    }
  }
  handleCancel() {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  }
  render() {
    return (
      <ReturnsFrame
        headerText="Return summary"
        onContinue={() => this.onContinue()}
        onCancel={() => this.handleCancel()}
      >
        <div className={styles.card}>
          <OrderReturnAddressDetails
            addressType={this.props.selectedAddress.addressType}
            address={`${
              this.props.selectedAddress.line1
                ? this.props.selectedAddress.line1
                : ""
            }, ${
              this.props.selectedAddress.landmark
                ? this.props.selectedAddress.landmark
                : ""
            }`}
            subAddress={`${
              this.props.selectedAddress.state
                ? this.props.selectedAddress.state
                : ""
            } ${
              this.props.selectedAddress.city
                ? this.props.selectedAddress.city
                : ""
            } ${
              this.props.selectedAddress.postalCode
                ? this.props.selectedAddress.postalCode
                : ""
            }`}
          />
          <OrderReturnDateAndTimeDetails
            date={this.props.dateSelected}
            time={this.props.timeSelected}
            underlineButtonLabel="change"
            onCancel={() => this.onChangeAddress()}
          />
        </div>

        <div className={styles.card}>
          <OrderCard
            imageUrl={
              this.props.returnProducts &&
              this.props.returnProducts.orderProductWsDTO &&
              this.props.returnProducts.orderProductWsDTO[0] &&
              this.props.returnProducts.orderProductWsDTO[0].imageURL
            }
            productName={`${
              this.props.returnProducts.orderProductWsDTO[0].productBrand
            } ${this.props.returnProducts.orderProductWsDTO[0].productName}`}
            price={this.props.returnProducts.orderProductWsDTO[0].price}
          >
            {this.props.returnProducts.orderProductWsDTO[0].quantity && (
              <div>
                Qty {this.props.returnProducts.orderProductWsDTO[0].quantity}
              </div>
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
ReturnSummary.propTypes = {
  onChangeAddress: PropTypes.func,
  onContinue: PropTypes.func,
  data: PropTypes.shape({
    addressType: PropTypes.string,
    address1: PropTypes.string,
    address2: PropTypes.string,
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
