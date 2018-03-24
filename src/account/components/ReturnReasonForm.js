import React from "react";
import OrderCard from "./OrderCard";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
import TextArea from "../../general/components/TextArea";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import Button from "../../general/components/Button";
import styles from "./ReturnReasonForm.css";

export default class ReturnReasonForm extends React.Component {
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
    const data = this.props.returnProductDetails;
    this.setState({
      secondaryReasons: data.returnReasonMap
        .filter(val => {
          return val.parentReasonCode === code;
        })
        .map(val => {
          return val.subReasons.map(value => {
            return { value: value.subReasonCode, label: value.subReturnReason };
          });
        })[0]
    });
  }
  handleChange(val) {
    this.setState({ comment: val });
  }
  onChangeSecondary(code) {
    if (this.props.onChangeSecondary) {
      this.props.onChangeSecondary(code);
    }
  }
  handleCancel() {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  }
  render() {
    const data = this.props.returnProductDetails;
    console.log(data);
    return (
      <div className={styles.base}>
        <div className={styles.header}>
          Select reason for your return
          <div className={styles.cancelHolder}>
            <UnderLinedButton
              label="Cancel"
              color="#ff1744"
              onClick={() => this.handleCancel()}
            />
          </div>
        </div>
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
              options={data.returnReasonMap.map((val, i) => {
                return {
                  value: val.parentReasonCode,
                  label: val.parentReturnReason
                };
              })}
              onChange={val => this.onChangePrimary(val)}
            />
          </div>
          {this.state.secondaryReasons && (
            <div className={styles.select}>
              <SelectBoxMobile
                label="Select a reason"
                options={this.state.secondaryReasons}
                onChange={val => this.onChangeSecondary()}
              />
            </div>
          )}
          <div className={styles.textArea}>
            <TextArea onChange={val => this.handleChange(val)} />
          </div>
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <Button
              width={175}
              type="primary"
              label="Continue"
              onClick={() => this.handleContinue()}
            />
          </div>
        </div>
      </div>
    );
  }
}
