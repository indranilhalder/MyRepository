import React from "react";
import styles from "./CreditCardForm.css";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import { Icon, CircleButton } from "xelpmoc-core";
import SelectBoxMobile from "../../general/components/SelectBoxMobile.js";
import informationIcon from "../../general/components/img/Info-grey.svg";
import Button from "../../general/components/Button";
import CheckBox from "../../general/components/CheckBox.js";
import { DEFAULT_PIN_CODE_LOCAL_STORAGE } from "../../lib/constants.js";
const MERCHANT_ID = "tul_uat2";

const MINIMUM_YEARS_TO_SHOW = 0;
const MAXIMUM_YEARS_TO_SHOW = 9;

export default class CreditCardForm extends React.Component {
  constructor(props) {
    super(props);

    this.expiryYearObject = [];
    const currentYear = new Date().getFullYear();
    for (let i = MINIMUM_YEARS_TO_SHOW; i <= MAXIMUM_YEARS_TO_SHOW; i++) {
      this.expiryYearObject.push({
        label: currentYear + i,
        value: currentYear + i
      });
    }

    this.monthOptions = [
      { label: "1", value: 1 },
      { label: "2", value: 2 },
      { label: "3", value: 3 },
      { label: "4", value: 4 },
      { label: "5", value: 5 },
      { label: "6", value: 6 },
      { label: "7", value: 7 },
      { label: "8", value: 8 },
      { label: "9", value: 9 },
      { label: "10", value: 10 },
      { label: "11", value: 11 },
      { label: "12", value: 12 }
    ];
    this.state = {
      selected: false,
      cardNumberValue: props.cardNumberValue ? props.cardNumberValue : "",
      cardNameValue: props.cardNameValue ? props.cardNameValue : "",
      cardCvvValue: props.cardCvvValue ? props.cardCvvValue : "",
      ExpiryMonth: props.ExpiryMonth ? props.ExpiryMonth : "1",
      ExpiryYear: props.ExpiryYear ? props.ExpiryYear : "2018",
      value: props.value ? props.value : "",
      monthValue: this.monthOptions[0].label,
      yearValue: "" + this.expiryYearObject[0].label
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
      cardDetails.cardName = this.state.cardNameValue;
      cardDetails.cvvNumber = this.state.cardCvvValue;
      cardDetails.monthValue = this.state.monthValue;
      cardDetails.yearValue = this.state.yearValue;
      cardDetails.selected = this.state.selected;
      cardDetails.merchant_id = MERCHANT_ID;
      this.props.onSaveCardDetails(cardDetails);
    }
  }

  payBill = cardDetails => {
    let cardValues = {};
    cardValues.cardNumber = this.state.cardNumberValue;
    cardValues.cardName = this.state.cardNameValue;
    cardValues.cvvNumber = this.state.cardCvvValue;
    cardValues.monthValue = this.state.monthValue;
    cardValues.yearValue = this.state.yearValue;
    cardValues.selected = this.state.selected;
    cardValues.merchant_id = MERCHANT_ID;
    cardValues.pincode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);
    if (this.props.isFromGiftCard) {
      this.props.jusPayTokenizeForGiftCard(cardValues);
    } else {
      this.props.softReservationForPayment(cardValues);
    }
  };

  render() {
    return (
      <div className={styles.base}>
        <div className={styles.cardDetails}>
          <div className={styles.content}>
            <Input2
              placeholder="Card Number"
              cardNumberValue={
                this.props.cardNumberValue
                  ? this.props.cardNumberValue
                  : this.state.cardNumberValue
              }
              boxy={true}
              onChange={val => this.onChangeCardNumber(val)}
              textStyle={{ fontSize: 14 }}
              height={33}
              maxLength="16"
            />
          </div>

          <div className={styles.content}>
            <Input2
              placeholder="John Doe"
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
              <SelectBoxMobile
                theme="hollowBox"
                value="Expiry Month"
                onChange={changedValue => this.monthChange(changedValue)}
                options={this.monthOptions}
                textStyle={{ fontSize: 14 }}
              />
            </div>
            <div className={styles.dropDownBox}>
              <SelectBoxMobile
                theme="hollowBox"
                options={this.expiryYearObject}
                value="Expiry year"
                onChange={expiryYear => this.onYearChange(expiryYear)}
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
                    type="password"
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
            </div>{" "}
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
