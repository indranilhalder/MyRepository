import React from "react";
import OrderCard from "./OrderForm";
import MobileSelect from "../../general/components/MobileSelect";
import TextArea from "../../general/components/TextArea";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import styles from "./ReasonForm.css";
export default class ReturnReasonForm extends React.Component {
  render() {
    const data = {
      type: "returnRequestDTO",
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
          transactionId: "273570000120025"
        }
      ],
      returnModes: {
        quickDrop: true,
        schedulePickup: true,
        selfCourier: false
      },
      returnReasonMap: [
        {
          parentReasonCode: "01",
          parentReturnReason: "The product I received was damaged"
        },
        {
          parentReasonCode: "02",
          parentReturnReason: "The product delivered is faulty"
        },
        {
          parentReasonCode: "03",
          parentReturnReason: "I'm not happy with the product quality"
        },
        {
          parentReasonCode: "04",
          parentReturnReason: "I was sent the wrong product"
        },
        {
          parentReasonCode: "05",
          parentReturnReason: "The product is missing a part"
        },
        {
          parentReasonCode: "06",
          parentReturnReason: "My order took too long to reach me"
        },
        {
          parentReasonCode: "07",
          parentReturnReason:
            "The product doesn't look like what I saw on the website"
        },
        {
          parentReasonCode: "08",
          parentReturnReason: "What I ordered doesn't fit me well"
        },
        {
          parentReasonCode: "09",
          parentReturnReason: "Forward Seal Mismatch"
        }
      ],
      showReverseSealFrJwlry: "no"
    };
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          Select reason for your return
          <div classNae={styles.buttonHolder}>
            <UnderLinedButton label="Cancel" />
          </div>
        </div>
        <div className={styles.content}>
          <OrderCard
            productImage={data.orderProductWsDTO[0].imageURL}
            productName={`${data.orderProductWsDTO[0].productBrand} ${
              data.orderProductWsDTO[0].productName
            }`}
            price={data.orderProductWsDTO[0].productName}
          >
            {data.orderProductWsDTO[0].quantity && (
              <div className={styles.quantity}>
                Qty {data.orderProductWsDTO[0].quantity}
              </div>
            )}
          </OrderCard>
          <div className={styles.select}>
            <MobileSelect />
          </div>
          <div className={styles.select}>
            <MobileSelect />
          </div>
          <TextArea />
        </div>
      </div>
    );
  }
}
