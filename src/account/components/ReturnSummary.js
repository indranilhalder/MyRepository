import React from "react";
import ReturnsFrame from "./ReturnsFrame";
import OrderCard from "./OrderCard";
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
  render() {
    return (
      <ReturnsFrame
        headerText="Return summary"
        onContinue={() => this.onContinue()}
      >
        <div className={styles.card}>
          <OrderReturnAddressDetails
            addressType={data.addressType}
            address={data.address1}
            subAddress={data.address2}
          />
          <OrderReturnDateAndTimeDetails
            date={data.date}
            time={data.time}
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
