import React from "react";
import styles from "./AddDeliveryAddress.css";
import PropTypes from "prop-types";
import Input2 from "./Input2.js";
import { Icon, CircleButton } from "xelpmoc-core";
import informationIcon from "./img/GPS.svg";
import SelectBoxWithInput from "./SelectBoxWithInput.js";
import CheckBox from "./CheckBox.js";
import TextArea from "./TextArea.js";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import Button from "./Button";

export default class AddDeliveryAddress extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selected: false,
      address: props.address ? props.address : "",
      pinCodeValue: props.pinCodeValue ? props.pinCodeValue : "",
      fullNmaeValue: props.fullNmaeValue ? props.fullNmaeValue : "",
      phoneNumberValue: props.phoneNumberValue ? props.phoneNumberValue : "",
      stateName: props.stateName ? props.stateName : "",
      cityNameValue: props.cityNameValue ? props.cityNameValue : "",
      localityValue: props.localityValue ? props.localityValue : "",
      landmark: props.landmark ? props.landmark : "",
      titleValue: props.titleValue ? props.titleValue : ""
    };
  }
  getPinCodeValue(val) {
    this.setState({ pinCodeValue: val });
  }
  getFullNameValue(val) {
    this.setState({ fullNmaeValue: val });
  }
  onChangePhoneNumber(val) {
    this.setState({ phoneNumberValue: val });
  }
  onChangeStateName(val) {
    this.setState({ stateName: val });
  }
  onChangeCityName(val) {
    this.setState({ cityNameValue: val });
  }

  onChangeLocality(val) {
    this.setState({ localityValue: val });
  }
  onChangeLandmark(val) {
    this.setState({ landmark: val });
  }
  onChangeAddress(val) {
    this.setState({ value: val });
  }

  clearAllValue = () => {
    this.setState({
      pinCodeValue: "",
      fullNmaeValue: "",
      phoneNumberValue: "",
      stateName: "",
      cityNameValue: "",
      localityValue: "",
      landmark: "",
      titleValue: "",
      value: "",
      selected: ""
    });
  };
  onSaveAddressDetails(val) {
    if (this.props.onSaveAddressDetails) {
      let addressDetails = {};
      addressDetails.address = this.state.value;
      addressDetails.pinCodeValue = this.state.pinCodeValue;
      addressDetails.fullNmaeValue = this.state.fullNmaeValue;
      addressDetails.phoneNumberValue = this.state.phoneNumberValue;
      addressDetails.stateName = this.state.stateName;
      addressDetails.cityNameValue = this.state.cityNameValue;
      addressDetails.localityValue = this.state.localityValue;
      addressDetails.landmark = this.state.landmark;
      addressDetails.titleValue = this.state.titleValue;

      this.props.onSaveAddressDetails(addressDetails);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.addressInnerBox}>
          <div className={styles.headingText}>{this.props.heading}</div>
          <div className={styles.button} onClick={this.clearAllValue}>
            <UnderLinedButton label="Clear all" />
          </div>
        </div>
        <div className={styles.content}>
          <Input2
            placeholder="Enter a pincode/zipcode*"
            onChange={val => this.getPinCodeValue(val)}
            textStyle={{ fontSize: 14 }}
            height={33}
            value={
              this.props.pinCodeValue
                ? this.props.pinCodeValue
                : this.state.pinCodeValue
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
        <div className={styles.selectName}>
          <SelectBoxWithInput
            options={this.props.options}
            onChange={titleValue => this.setState({ titleValue })}
            titleValue={this.state.titleValue}
          />
        </div>

        <div className={styles.content}>
          <TextArea
            placeholder="Address*"
            value={this.props.value ? this.props.value : this.state.value}
            onChange={val => this.onChangeAddress(val)}
          />
        </div>
        <div className={styles.content}>
          <Input2
            boxy={true}
            placeholder="Landmark*"
            value={
              this.props.landmark ? this.props.landmark : this.state.landmark
            }
            onChange={val => this.onChangeLandmark(val)}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.content}>
          <Input2
            boxy={true}
            placeholder="Locality/town*"
            value={
              this.props.localityValue
                ? this.props.localityValue
                : this.state.localityValue
            }
            onChange={val => this.onChangeLocality(val)}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.content}>
          <Input2
            boxy={true}
            placeholder="City/district*"
            value={
              this.props.cityNameValue
                ? this.props.cityNameValue
                : this.state.cityNameValue
            }
            onChange={val => this.onChangeCityName(val)}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.content}>
          <Input2
            placeholder="State*"
            value={
              this.props.stateName ? this.props.stateName : this.state.stateName
            }
            boxy={true}
            onChange={val => this.onChangeStateName(val)}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.content}>
          <Input2
            type="number"
            placeholder="Phone number*"
            value={
              this.props.phoneNumberValue
                ? this.props.phoneNumberValue
                : this.state.phoneNumberValue
            }
            boxy={true}
            onChange={val => this.onChangePhoneNumber(val)}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.content}>
          <div className={styles.checkAddressContent}>
            <div className={styles.checkboxHolder}>
              <CheckBox selected={this.state.selected} />
            </div>
            <div className={styles.textHolder}>{this.props.home}</div>
          </div>

          <div className={styles.checkAddressContent}>
            <div className={styles.checkboxHolder}>
              <CheckBox selected={this.state.selected2} />
            </div>
            <div className={styles.textHolder}>{this.props.office}</div>
          </div>

          <div className={styles.checkAddressContent}>
            <div className={styles.checkboxHolder}>
              <CheckBox selected={this.state.selected3} />
            </div>
            <div className={styles.textHolder}>{this.props.other}</div>
          </div>
        </div>
        <div className={styles.defaultText}>
          <div className={styles.checkboxHolder}>
            <CheckBox selected={this.state.selected4} />
          </div>
          <div className={styles.textHolder}>
            {this.props.saveDefaultTextItem}
          </div>
        </div>
        <div className={styles.buttonBox}>
          <div className={styles.buttonHolder}>
            <Button
              type="primary"
              color="#fff"
              label={this.props.buttonText}
              width={176}
              onClick={val => this.onSaveAddressDetails(val)}
            />
          </div>
        </div>
      </div>
    );
  }
}
AddDeliveryAddress.propTypes = {
  onClick: PropTypes.func,
  height: PropTypes.string,
  placeholder: PropTypes.string,
  saveDefaultTextItem: PropTypes.string,
  selected: PropTypes.bool,
  onSaveData: PropTypes.func,
  heading: PropTypes.func,
  home: PropTypes.string,
  office: PropTypes.string,
  other: PropTypes.string,
  default: PropTypes.string,
  clearAllValue: PropTypes.func,
  buttonText: PropTypes.string,
  options: PropTypes.string,
  fullNmaeValue: PropTypes.string,
  titleValue: PropTypes.string
};
