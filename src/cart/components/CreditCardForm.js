import React from "react";
import styles from "./CreditCardForm.css";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import Icon from "../../xelpmoc-core/Icon";
import CircleButton from "../../xelpmoc-core/CircleButton";
import SelectBoxMobile from "../../general/components/SelectBoxMobile.js";
import informationIcon from "../../general/components/img/Info-grey.svg";
import Button from "../../general/components/Button";
import CheckBox from "../../general/components/CheckBox.js";
import { DEFAULT_PIN_CODE_LOCAL_STORAGE } from "../../lib/constants.js";

const INSUFFICIENT_DATA_ERROR_MESSAGE = "PLease enter valid card details";

const MERCHANT_ID = "tul_uat2";

const MINIMUM_YEARS_TO_SHOW = 0;
const MAXIMUM_YEARS_TO_SHOW = 9;

export default class CreditCardForm extends React.Component {
  constructor(props) {
    super(props);

    this.expiryYearObject = [{ label: "YY", value: "YY" }];
    const currentYear = new Date().getFullYear();
    for (let i = MINIMUM_YEARS_TO_SHOW; i <= MAXIMUM_YEARS_TO_SHOW; i++) {
      this.expiryYearObject.push({
        label: currentYear + i,
        value: currentYear + i
      });
    }

    this.monthOptions = [
      { label: "MM", value: "MM" },
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
      cardNumber: props.cardNumber ? props.cardNumber : "",
      cardName: props.cardName ? props.cardName : "",
      cvvNumber: props.cvvNumber ? props.cvvNumber : "",
      ExpiryMonth: props.ExpiryMonth ? props.ExpiryMonth : null,
      ExpiryYear: props.ExpiryYear ? props.ExpiryYear : null,
      value: props.value ? props.value : "",
      monthValue: "",
      yearValue: ""
    };
  }

  onChangeCardNumber(val) {
    this.setState({ cardNumber: val });
    this.onChange({ cardNumber: val });
    if (val.length === 6) {
      this.props.binValidation(val);
    }
  }

  onChange(val) {
    this.setState(val);
    if (this.props.onChangeCardDetail) {
      this.props.onChangeCardDetail(val);
    }
  }

  componentWillReceiveProps(nextProps) {
    if (
      nextProps.cardDetails &&
      (!nextProps.cardDetails.cardNumber ||
        nextProps.cardDetails.cardNumber === "")
    ) {
      this.setState({
        selected: false,
        cardNumber: "",
        cardName: "",
        cvvNumber: "",
        ExpiryMonth: null,
        ExpiryYear: null,
        value: "",
        monthValue: "",
        yearValue: ""
      });
    }
  }
  // payBill = cardDetails => {
  //   let cardValues = {};
  //   cardValues.cardNumber = this.state.cardNumber;
  //   cardValues.cardName = this.state.cardName;
  //   cardValues.cvvNumber = this.state.cvvNumber;
  //   cardValues.monthValue = this.state.monthValue;
  //   cardValues.yearValue = this.state.yearValue;
  //   cardValues.selected = this.state.selected;
  //   cardValues.merchant_id = MERCHANT_ID;
  //   cardValues.pincode = localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE);
  //   if (
  //     cardValues.cardNumber &&
  //     cardValues.cardName &&
  //     cardValues.cvvNumber &&
  //     cardValues.monthValue &&
  //     cardValues.yearValue
  //   ) {
  //     if (this.props.isFromGiftCard) {
  //       this.props.jusPayTokenizeForGiftCard(cardValues);
  //     } else {
  //       this.props.softReservationForPayment(cardValues);
  //     }
  //   } else {
  //     this.props.displayToast(INSUFFICIENT_DATA_ERROR_MESSAGE);
  //   }
  // };

  render() {
    return (
      <div className={styles.base}>
        <div className={styles.cardDetails}>
          <div className={styles.content}>
            <Input2
              placeholder="Card Number"
              value={
                this.props.cardNumber
                  ? this.props.cardNumber
                  : this.state.cardNumber
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
              placeholder="Name on card*"
              boxy={true}
              cardName={
                this.props.cardName ? this.props.cardName : this.state.cardName
              }
              onChange={cardName => this.onChange({ cardName })}
              textStyle={{ fontSize: 14 }}
              height={33}
            />
          </div>
          <div className={styles.dropDownHolder}>
            <div className={styles.dropDownBox}>
              <SelectBoxMobile
                theme="hollowBox"
                label={
                  this.state.monthValue ? this.state.monthValue : "Expiry Month"
                }
                onChange={monthValue => this.onChange({ monthValue })}
                options={this.monthOptions}
                textStyle={{ fontSize: 14 }}
                value={this.state.monthValue}
              />
            </div>
            <div className={styles.dropDownBox}>
              <SelectBoxMobile
                theme="hollowBox"
                options={this.expiryYearObject}
                label={
                  this.state.yearValue ? this.state.yearValue : "Expiry year"
                }
                onChange={yearValue => this.onChange({ yearValue })}
                value={this.state.yearValue}
              />
            </div>
          </div>
          <div className={styles.payCardHolder}>
            <div className={styles.cardFooterText}>
              <div className={styles.cvvNumberTextHolder}>
                <div className={styles.cardFooterInput}>
                  <Input2
                    boxy={true}
                    placeholder="CVV"
                    type="password"
                    onChange={cvvNumber => this.onChange({ cvvNumber })}
                    textStyle={{ fontSize: 14 }}
                    height={33}
                    maxLength={"3"}
                    value={
                      this.props.cvvNumber
                        ? this.props.cvvNumber
                        : this.state.cvvNumber
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
          </div>
          <div className={styles.saveCardText}>
            <div className={styles.saveText}>
              We will save your card for a faster checkout. To remove your
              details, visit My Cliq.
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
