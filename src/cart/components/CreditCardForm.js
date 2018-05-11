import React from "react";
import styles from "./CreditCardForm.css";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import Icon from "../../xelpmoc-core/Icon";
import CircleButton from "../../xelpmoc-core/CircleButton";
import SelectBoxMobile2 from "../../general/components/SelectBoxMobile2.js";
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
        monthValue: "Expiry Month",
        yearValue: "Expiry year"
      });
    }
  }

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
              onFocus={() => {
                this.props.onFocusInput();
              }}
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
              value={
                this.props.cardName ? this.props.cardName : this.state.cardName
              }
              onChange={cardName => this.onChange({ cardName })}
              textStyle={{ fontSize: 14 }}
              height={33}
              onFocus={() => {
                this.props.onFocusInput();
              }}
            />
          </div>
          <div className={styles.dropDownHolder}>
            <div className={styles.dropDownBox}>
              <SelectBoxMobile2
                theme="hollowBox"
                placeholder="Expiry Month"
                onChange={monthValue =>
                  this.onChange({ monthValue: monthValue.value })
                }
                options={this.monthOptions}
                textStyle={{ fontSize: 14 }}
                value={this.state.monthValue}
                label={this.state.monthValue}
              />
            </div>
            <div className={styles.dropDownBox}>
              <SelectBoxMobile2
                theme="hollowBox"
                placeholder="Expiry year"
                options={this.expiryYearObject}
                onChange={yearValue =>
                  this.onChange({ yearValue: yearValue.value })
                }
                value={this.state.yearValue}
                label={this.state.yearValue}
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
                    onFocus={() => {
                      this.props.onFocusInput();
                    }}
                  />
                </div>
              </div>
            </div>
          </div>
          <div className={styles.saveCardText}>
            <div className={styles.saveText}>
              We will save your card details securely for a faster checkout; we
              don't store the CVV number. To remove your card details, visit My
              Account.
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
