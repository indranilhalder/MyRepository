import React from "react";
import ReturnsFrame from "./ReturnsFrame";
import OrderCard from "./OrderCard";
import PropTypes from "prop-types";
import ReturnsToBank from "./ReturnsToBank";
import OrderReturnAddressDetails from "./OrderReturnAddressDetails";
import OrderReturnDateAndTimeDetails from "./OrderReturnDateAndTimeDetails";
import styles from "./ReturnSummary.css";

export default class ReturnSummary extends React.Component {
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
            address={this.props.selectedAddress.formattedAddress}
            subAddress={`${this.props.selectedAddress.state} ${
              this.props.selectedAddress.city
            } ${this.props.selectedAddress.postalCode}`}
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
            productImage={
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
          <ReturnsToBank
            cartNumber={this.props.orderDetails.paymentCardDigit}
          />
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
