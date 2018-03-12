import React from "react";
import styles from "./CreditCardForm.css";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import { Icon, CircleButton } from "xelpmoc-core";
import SelectBoxMobile from "../../general/components/SelectBoxMobile.js";
import informationIcon from "../../general/components/img/Info-grey.svg";
import Button from "../../general/components/Button";
import CheckBox from "../../general/components/CheckBox.js";
export default class CreditCardForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selected: false,
      cardNumberValue: props.cardNumberValue ? props.cardNumberValue : "",
      cardNameValue: props.cardNameValue ? props.cardNameValue : "",
      cardCvvValue: props.cardCvvValue ? props.cardCvvValue : "",
      ExpiryMonth: props.ExpiryMonth ? props.ExpiryMonth : "",
      ExpiryYear: props.ExpiryYear ? props.ExpiryYear : "",
      value: props.value ? props.value : ""
    };
  }
  onSaveData() {
    this.setState(prevState => ({
      selected: !this.state.selected
    }));
  }
  getExpiryMonth(val) {
    this.setState({ cardNumberValue: val });
  }
  onChangeCardNumber(val) {
    this.setState({ cardNumberValue: val });
    if (val.length === 6) {
      this.props.binValidation(val);
    }
  }
  getCardDetails(val) {
    this.setState({ cardNumberValue: val });
  }
  getCardCvvValue(val) {
    this.setState({ cardCvvValue: val });
  }
  onChangeCardName(val) {
    this.setState({ cardNameValue: val });
  }
  monthChange(val) {
    this.setState({ monthValue: val });
  }
  onYearChange(val) {
    this.setState({ yearValue: val });
  }
  onSaveCardDetails(val) {
    if (this.props.onSaveCardDetails) {
      let cardDetails = {};
      cardDetails.cardNumber = this.state.cardNumberValue;
      cardDetails.cardName = "SHDHD";
      cardDetails.cvvNumber = this.state.cardCvvValue;
      cardDetails.monthValue = this.state.monthValue;
      cardDetails.yearValue = this.state.yearValue;
      cardDetails.selected = this.state.selected;
      cardDetails.merchant_id = "tul_uat2";
      this.props.onSaveCardDetails(cardDetails);
    }
  }

  payBill = cardDetails => {
    let cardValues = {};
    cardValues.cardNumber = this.state.cardNumberValue;
    cardValues.cardName = "SHDHD";
    cardValues.cvvNumber = this.state.cardCvvValue;
    cardValues.monthValue = this.state.monthValue;
    cardValues.yearValue = this.state.yearValue;
    cardValues.selected = this.state.selected;
    cardValues.merchant_id = "tul_uat2";
    this.props.softReservationForPayment(cardValues);
  };

  render() {
    return (
      <div className={styles.base}>
        <div className={styles.cardDetails}>
          <div className={styles.content}>
            <Input2
              placeholder={this.props.placeholder}
              cardNumberValue={
                this.props.cardNumberValue
                  ? this.props.cardNumberValue
                  : this.state.cardNumberValue
              }
              boxy={true}
              onChange={val => this.onChangeCardNumber(val)}
              textStyle={{ fontSize: 14 }}
              height={33}
            />
          </div>

          <div className={styles.content}>
            <Input2
              placeholder={this.props.placeHolderCardName}
              boxy={true}
              cardNameValue={
                this.props.cardNameValue
                  ? this.props.cardNameValue
                  : this.state.cardNameValue
              }
              onChange={val => this.onChangeCardName(val)}
              textStyle={{ fontSize: 14 }}
              height={33}
            />
          </div>
          <div className={styles.dropDownHolder}>
            <div className={styles.dropDownBox}>
              <Input2
                placeholder="Expiry Month"
                boxy={true}
                onChange={changedValue => this.monthChange(changedValue)}
                textStyle={{ fontSize: 14 }}
                height={33}
              />
            </div>
            <div className={styles.dropDownBox}>
              <Input2
                placeholder="Expiry year"
                boxy={true}
                onChange={changedValue => this.onYearChange(changedValue)}
                textStyle={{ fontSize: 14 }}
                height={33}
              />
            </div>
          </div>
          <div className={styles.payCardHolder}>
            <div className={styles.cardFooterText}>
              <div className={styles.cardCvvTextHolder}>
                <div className={styles.cardFooterInput}>
                  <Input2
                    boxy={true}
                    placeholder="Cvv"
                    type="number"
                    onChange={val => this.getCardCvvValue(val)}
                    textStyle={{ fontSize: 14 }}
                    height={33}
                    cardCvvValue={
                      this.props.cardCvvValue
                        ? this.props.cardCvvValue
                        : this.state.cardCvvValue
                    }
                    rightChildSize={33}
                    rightChild={
                      <CircleButton
                        size={33}
                        color={"transparent"}
                        icon={<Icon image={informationIcon} size={16} />}
                      />
                    }
                  />
                </div>
              </div>
            </div>
            <div className={styles.cardFooterText}>
              <div className={styles.buttonHolder}>
                <Button
                  type="primary"
                  color="#fff"
                  label="Pay now"
                  width={120}
                  onClick={() => this.payBill()}
                />
              </div>
            </div>
          </div>
          <div
            className={styles.saveCardText}
            onClick={() => this.onSaveData()}
          >
            <div className={styles.checkCircle}>
              <CheckBox selected={this.state.selected} />
            </div>
            <div className={styles.saveText}>
              Save this card for future payments
            </div>
          </div>
        </div>
      </div>
    );
  }
}
CreditCardForm.propTypes = {
  placeholder: PropTypes.string,
  placeHolderCardName: PropTypes.string,
  selected: PropTypes.bool,
  onClick: PropTypes.func,
  onSaveData: PropTypes.func,
  optionsYear: PropTypes.string,
  options: PropTypes.string
};
