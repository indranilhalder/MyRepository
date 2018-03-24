import React from "react";
import ReturnsFrame from "./ReturnsFrame";
import OrderCard from "./OrderCard";
import PropTypes from "prop-types";
import ReturnsToBank from "./ReturnsToBank";
import OrderReturnAddressDetails from "./OrderReturnAddressDetails";
import OrderReturnDateAndTimeDetails from "./OrderReturnDateAndTimeDetails";
import styles from "./ReturnSummary.css";
const data = {
  type: "returnRequestDTO",
  addressType: "Home",
  address1: "Lal Bahadur Shastri Marg, Chandan Nagar",
  address2: "Chandanagar, Hooghly,WestBengal",
  orderProductWsDTO: [
    {
      USSID: "273570HMAIBSSZ06",
      imageURL:
        "//pcmuat2.tataunistore.com/images/97Wx144H/MP000000000113801_97Wx144H_20171102155229.jpeg",
      price: "778.0",
      productBrand: "Red Rose",
      productColour: "Red",
      productName: "Infants Boys Clothing Shirts",
      productSize: "M",
      productcode: "MP000000000113801",
      sellerID: "273570",
      sellerName: "Saravanan",
      sellerorderno: "180314-000-111548",
      transactionId: "273570000120027"
    }
  ],
  returnModes: {
    quickDrop: true,
    schedulePickup: true,
    selfCourier: false
  },
  returnReasonMap: [
    {
      parentReasonCode: "JEW100",
      parentReturnReason: "Dummy reason 1",
      subReasons: [
        {
          subReasonCode: "JEW1S1",
          subReturnReason: "Sub reason 11"
        },
        {
          subReasonCode: "JEW1S2",
          subReturnReason: "Sub reason 12"
        }
      ]
    },
    {
      parentReasonCode: "JEW200",
      parentReturnReason: "Dummy reason 2",
      subReasons: [
        {
          subReasonCode: "JEW2S2",
          subReturnReason: "Sub reason 21"
        }
      ]
    }
  ],
  showReverseSealFrJwlry: "no"
};

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
            productImage={data.orderProductWsDTO[0].imageURL}
            productName={`${data.orderProductWsDTO[0].productBrand} ${
              data.orderProductWsDTO[0].productName
            }`}
            price={data.orderProductWsDTO[0].price}
          >
            {data.orderProductWsDTO[0].quantity && (
              <div>Qty {data.orderProductWsDTO[0].quantity}</div>
            )}
          </OrderCard>
          <ReturnsToBank />
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
