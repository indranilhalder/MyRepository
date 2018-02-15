import React from "react";
import styles from "./CreditCard.css";
import PropTypes from "prop-types";
import Input2 from "./Input2.js";
import { Icon, CircleButton } from "xelpmoc-core";
import SelectBox from "./SelectBox.js";
import informationIcon from "./img/Info.svg";
import Button from "../../general/components/Button";
import CheckBox from "./CheckBox.js";
export default class CreditCard extends React.Component {
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
      this.props.onSaveCardDetails(cardDetails);
    }
  }

  render() {
    const options = [
      { value: "January", label: "January" },
      { value: "February", label: "February" },
      { value: "March", label: "March" },
      { value: "April", label: "April" },
      { value: "May", label: "May" },
      { value: "June", label: "June" },
      { value: "July", label: "July" },
      { value: "August", label: "August" },
      { value: "September", label: "September" },
      { value: "October", label: "October" },
      { value: "November", label: "November" },
      { value: "December", label: "December" }
    ];
    const optionsYear = [
      { value: "2018", label: "2018" },
      { value: "2019", label: "2019" },
      { value: "2020", label: "2020" },
      { value: "2021", label: "2021" },
      { value: "2022", label: "2022" },
      { value: "2023", label: "2023" },
      { value: "2024", label: "2024" },
      { value: "2025", label: "2025" },
      { value: "2026", label: "2026" },
      { value: "2027", label: "2027" },
      { value: "2028", label: "2028" },
      { value: "2029", label: "2029" }
    ];
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
              rightChildSize={33}
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
              rightChildSize={33}
            />
          </div>
          <div className={styles.dropDownHolder}>
            <div className={styles.dropDownBox}>
              <SelectBox
                borderNone={true}
                options={options}
                placeholder="Expiry month"
                onChange={changedValue => this.monthChange(changedValue)}
                selected={
                  this.state.monthValue && {
                    label: this.state.monthValue,
                    value: this.state.monthValue
                  }
                }
              />
            </div>
            <div className={styles.dropDownBox}>
              <SelectBox
                borderNone={true}
                options={optionsYear}
                placeholder="Expiry year"
                onChange={changedValue => this.onYearChange(changedValue)}
                selected={
                  this.state.yearValue && {
                    label: this.state.yearValue,
                    value: this.state.yearValue
                  }
                }
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
                        size={35}
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
                  label={this.props.buttonText}
                  width={120}
                  onClick={val => this.onSaveCardDetails(val)}
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
            <div className={styles.saveText}>{this.props.saveTextItem}</div>
          </div>
        </div>
      </div>
    );
  }
}
CreditCard.propTypes = {
  placeholder: PropTypes.string,
  placeHolderCardName: PropTypes.string,
  buttonText: PropTypes.string,
  selected: PropTypes.bool,
  onClick: PropTypes.func,
  onSaveData: PropTypes.func
};
