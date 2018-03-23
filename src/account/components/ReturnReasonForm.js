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
      secondaryReasons: null
    };
  }
  handleContinue() {
    if (this.props.onContinue) {
      this.props.onContinue();
    }
  }
  onChangePrimary(code) {
    if (this.props.onChangePrimary) {
      this.props.onChangePrimary(code);
    }
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
  handleChange(val) {
    if (this.props.onChange) {
      this.props.onChange(val);
    }
  }
  render() {
    const { productInfo } = this.props;
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
            productImage={productInfo.product.imageURL}
            productName={productInfo.product.name}
            price={productInfo.totalPrice.value}
          >
            {productInfo.quantity && (
              <div className={styles.quantity}>Qty {productInfo.quantity}</div>
            )}
          </OrderCard>
          <div className={styles.select}>
            <SelectBoxMobile
              label="Select a reason"
              options={this.props.optionsForReason.map((val, i) => {
                return {
                  value: val.code,
                  label: val.reasonDescription
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
                onChange={val => this.onChangeSecondary(val)}
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
