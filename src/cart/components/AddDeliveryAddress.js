import React from "react";
import styles from "./AddDeliveryAddress.css";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import { Icon, CircleButton } from "xelpmoc-core";
import informationIcon from "../../general/components/img/GPS.svg";
import SelectBoxWithInput from "../../general/components/SelectBoxWithInput.js";
import GridSelect from "../../general/components/GridSelect";
import CheckboxAndText from "./CheckboxAndText";
import TextArea from "../../general/components/TextArea.js";
import UnderLinedButton from "../../general/components/UnderLinedButton";
export default class AddDeliveryAddress extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selected: false,
      address: props.address ? props.address : "",
      pinCodeValue: props.pinCodeValue ? props.pinCodeValue : "",
      fullNameValue: props.fullNameValue ? props.fullNameValue : "",
      phoneNumberValue: props.phoneNumberValue ? props.phoneNumberValue : "",
      stateName: props.stateName ? props.stateName : "",
      cityNameValue: props.cityNameValue ? props.cityNameValue : "",
      localityValue: props.localityValue ? props.localityValue : "",
      landmark: props.landmark ? props.landmark : "",
      titleValue: props.titleValue ? props.titleValue : "",
      options: props.options ? props.titleValue : ""
    };
  }
  getPinCodeValue(val) {
    this.setState({ pinCodeValue: val });
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
  getFullNameValue(val) {
    this.setState({ fullNameValue: val });
  }
  onChangeLandmark(val) {
    this.setState({ landmark: val });
  }
  onChangeAddress(val) {
    this.setState({ value: val });
  }
  onSelect() {
    this.setState({ selected: !this.state.selected });
    if (this.props.onSelect) {
      this.props.onSelect();
    }
  }
  clearAllValue = () => {
    this.setState({
      pinCodeValue: "",
      fullNameValue: "",
      phoneNumberValue: "",
      stateName: "",
      cityNameValue: "",
      localityValue: "",
      landmark: "",
      titleValue: "",
      options: ""
    });
  };
  onSaveAddressDetails(val) {
    if (this.props.onSaveAddressDetails) {
      let addressDetails = {};
      addressDetails.address = this.state.value;
      addressDetails.pinCodeValue = this.state.pinCodeValue;
      addressDetails.fullNameValue = this.state.fullNameValue;
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
    const dataLabel = [
      {
        label: "Home"
      },
      {
        label: "Office"
      },
      {
        label: "Others"
      }
    ];
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
        <div className={styles.content}>
          <SelectBoxWithInput
            option={this.state.options}
            onChange={titleValue => this.setState({ titleValue })}
            titleValue={this.state.titleValue}
            fullNameValue={this.state.fullNameValue}
            getFullNameValue={val => this.getFullNameValue(val)}
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
          <GridSelect limit={1} offset={0} elementWidthMobile={50}>
            {dataLabel.map((val, i) => {
              return <CheckboxAndText label={val.label} value={i} />;
            })}
          </GridSelect>
        </div>
        <div className={styles.defaultText}>
          <CheckboxAndText label="Make this default address" />
        </div>
      </div>
    );
  }
}
AddDeliveryAddress.propTypes = {
  onClick: PropTypes.func,
  saveDefaultTextItem: PropTypes.string,
  selected: PropTypes.bool,
  onSaveData: PropTypes.func,
  heading: PropTypes.string,
  home: PropTypes.string,
  office: PropTypes.string,
  other: PropTypes.string,
  default: PropTypes.string,
  clearAllValue: PropTypes.func,
  buttonText: PropTypes.string,
  options: PropTypes.string,
  titleValue: PropTypes.string
};
AddDeliveryAddress.defaultProps = {
  heading: "Add address"
};
