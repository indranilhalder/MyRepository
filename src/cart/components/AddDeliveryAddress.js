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
      options: [{ label: "Mr.", value: "MR" }, { label: "Mrs.", value: "MRS" }]
    };
  }

  onChange(val) {
    if (this.props.onChange) {
      this.props.onChange(val);
    }
  }

  clearAllValue = () => {
    this.onChange({
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
            onChange={postalCode => this.onChange({ postalCode })}
            textStyle={{ fontSize: 14 }}
            height={33}
            value={
              this.props.postalCode
                ? this.props.postalCode
                : this.state.postalCode
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
          <Input2
            option={this.state.options}
            placeholder="Name*"
            onChange={firstName => this.onChange({ firstName })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>

        <div className={styles.content}>
          <TextArea
            placeholder="Address*"
            value={this.props.line1 ? this.props.line1 : this.state.line1}
            onChange={line1 => this.onChange({ line1 })}
          />
        </div>
        <div className={styles.content}>
          <Input2
            boxy={true}
            placeholder="Landmark*"
            value={
              this.props.landmark ? this.props.landmark : this.state.landmark
            }
            onChange={landmark => this.onChange({ landmark })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.content}>
          <Input2
            boxy={true}
            placeholder="Locality/town*"
            value={this.props.town ? this.props.town : this.state.town}
            onChange={town => this.onChange({ town })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.content}>
          <Input2
            boxy={true}
            placeholder="City/district*"
            value={this.props.city ? this.props.city : this.state.city}
            onChange={city => this.onChange({ city })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.content}>
          <Input2
            placeholder="State*"
            value={this.props.state ? this.props.state : this.state.state}
            boxy={true}
            onChange={state => this.onChange({ state })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.content}>
          <Input2
            type="number"
            placeholder="Phone number*"
            value={this.props.phone ? this.props.phone : this.state.phone}
            boxy={true}
            onChange={phone => this.onChange({ phone })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>

        <div className={styles.content}>
          <GridSelect
            limit={1}
            offset={0}
            elementWidthMobile={50}
            onSelect={val => this.onChange({ addressType: val[0] })}
          >
            {dataLabel.map((val, i) => {
              return (
                <CheckboxAndText key={i} label={val.label} value={val.label} />
              );
            })}
          </GridSelect>
        </div>
        <div className={styles.defaultText}>
          <CheckboxAndText
            label="Make this default address"
            selectItem={() =>
              this.onChange({ defaultAddress: !this.props.defaultAddress })
            }
          />
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
