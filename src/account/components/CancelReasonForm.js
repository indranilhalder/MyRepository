import React from "react";
import OrderCard from "./OrderCard";
import SelectBoxMobile2 from "../../general/components/SelectBoxMobile2";
import TextArea from "../../general/components/TextArea";
import ReturnsFrame from "./ReturnsFrame";
import styles from "./CancelReasonForm.css";
import PropTypes from "prop-types";
import { Redirect } from "react-router-dom";
export default class CancelReasonForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      displaySecondary: false,
      secondaryReasons: null,
      comment: null,
      reason: "Select a reason"
    };
  }
  onClickImage(productCode) {
    if (this.props.onClickImage) {
      this.props.onClickImage(productCode);
    }
  }
  handleContinue() {
    if (this.props.onContinue) {
      this.props.onContinue(this.state);
    }
  }
  onChangePrimary(val) {
    const code = val.value;
    const label = val.label;

    this.setState({ cancelReasonCode: code, reason: label });
  }
  handleChange(val) {
    this.setState({ comment: val });
  }

  handleCancel() {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  }
  render() {
    const data = this.props.cancelProductDetails;
    return (
      <ReturnsFrame
        headerText="Select reason for your cancel"
        onContinue={() => this.handleContinue()}
        onCancel={() => this.handleCancel()}
      >
        <div className={styles.content}>
          <OrderCard
            imageUrl={
              data &&
              data.orderProductWsDTO &&
              data.orderProductWsDTO[0] &&
              data.orderProductWsDTO[0].imageURL
            }
            productName={`${data.orderProductWsDTO[0].productBrand} ${
              data.orderProductWsDTO[0].productName
            }`}
            onClick={() =>
              this.onClickImage(
                data.orderProductWsDTO &&
                  data.orderProductWsDTO[0] &&
                  data.orderProductWsDTO[0].productcode
              )
            }
            price={data.orderProductWsDTO[0].price}
            quantity={true}
            isSelect={true}
          >
            {data.orderProductWsDTO[0].quantity && (
              <div className={styles.quantity}>
                Qty {data.orderProductWsDTO[0].quantity}
              </div>
            )}
          </OrderCard>
          <div className={styles.select}>
            <SelectBoxMobile2
              placeholder={"Select a reason"}
              options={data.returnReasonDetailsWsDTO.map((val, i) => {
                return {
                  value: val.code,
                  label: val.reasonDescription
                };
              })}
              onChange={val => this.onChangePrimary(val)}
            />
          </div>

          <div className={styles.textArea}>
            <TextArea onChange={val => this.handleChange(val)} />
          </div>
        </div>
      </ReturnsFrame>
    );
  }
}
CancelReasonForm.propTypes = {
  onContinue: PropTypes.func,
  onCancel: PropTypes.func,
  cancelProductDetails: PropTypes.object
};
