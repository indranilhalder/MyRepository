import React from "react";
import OrderCard from "./OrderCard";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
import TextArea from "../../general/components/TextArea";
import ReturnsFrame from "./ReturnsFrame";
import styles from "./CancelReasonForm.css";
import PropTypes from "prop-types";
export default class CancelReasonForm extends React.Component {
  constructor(props) {
    super(props);

    this.state = {
      displaySecondary: false,
      secondaryReasons: null,
      comment: null
    };
  }
  handleContinue() {
    if (this.props.onContinue) {
      this.props.onContinue(this.state);
    }
  }
  onChangePrimary(code) {
    this.setState({ cancelReasonCode: code });
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
          <div className={styles.select}>
            <SelectBoxMobile
              label="Select a reason"
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
