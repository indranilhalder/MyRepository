import React from "react";
import OrderCard from "./OrderCard";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import SelectReturnDate from "./SelectReturnDate";
import PropTypes from "prop-types";
import styles from "./ReturnModes.css";
import GridSelect from "../../general/components/GridSelect";
const QUICK_DROP = "quickDrop";
const SCHEDULED_PICKUP = "schedulePickup";
const SELF_COURIER = "selfCourier";

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

export default class ReturnModes extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      displaySecondary: false,
      secondaryReasons: null
    };
  }
  handleSelect(val) {
    if (this.props.selectMode) {
      this.props.selectMode(val);
    }
  }

  handleCancel() {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          Select mode of return
          <div className={styles.cancel}>
            <UnderLinedButton
              label="Cancel"
              color="#ff1744"
              onClick={() => this.handleCancel()}
            />
          </div>
        </div>
        <div className={styles.content}>
          <div className={styles.card}>
            <OrderCard
              productImage={data.orderProductWsDTO[0].imageURL}
              productName={`${data.orderProductWsDTO[0].productBrand} ${
                data.orderProductWsDTO[0].productName
              }`}
              price={data.orderProductWsDTO[0].price}
            >
              {data.orderProductWsDTO[0].quantity && (
                <div className={styles.quantity}>
                  Qty {data.orderProductWsDTO[0].quantity}
                </div>
              )}
            </OrderCard>
          </div>

          {data.returnModes.quickDrop && (
            <div className={styles.check}>
              <SelectReturnDate
                label="Return to store"
                selected={this.props.selectedMode === QUICK_DROP}
                selectItem={() => {
                  this.handleSelect(QUICK_DROP);
                }}
              />
            </div>
          )}
          {data.returnModes.schedulePickup && (
            <div className={styles.check}>
              <SelectReturnDate
                label="Tata CliQ Pick Up"
                selectItem={() => {
                  this.handleSelect(SCHEDULED_PICKUP);
                }}
                selected={this.props.selectedMode === SCHEDULED_PICKUP}
              />
            </div>
          )}
          {data.returnModes.selfCourier && (
            <div className={styles.check}>
              <SelectReturnDate
                selectItem={() => {
                  this.handleSelect(SELF_COURIER);
                }}
                label="Self Courier"
                selected={this.props.selectedMode === SELF_COURIER}
              />
            </div>
          )}
        </div>
      </div>
    );
  }
}

ReturnModes.propTypes = {
  selectedMode: PropTypes.oneOf([QUICK_DROP, SCHEDULED_PICKUP, SELF_COURIER]),
  selectMode: PropTypes.func
};
