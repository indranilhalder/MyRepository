import React from "react";
import styles from "./AddDeliveryAddress.css";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import { Icon, CircleButton } from "xelpmoc-core";
import informationIcon from "../../general/components/img/GPS.svg";
import GridSelect from "../../general/components/GridSelect";
import CheckboxAndText from "./CheckboxAndText";
import TextArea from "../../general/components/TextArea.js";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import Button from "../../general/components/Button";
import { SUCCESS } from "../../lib/constants.js";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
const SAVE_TEXT = "Save Address";

const ISO_CODE = "IN";
export default class AddDeliveryAddress extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      countryIso: "",
      addressType: "",
      phone: "",
      firstName: "",
      lastName: "",
      postalCode: "",
      line1: "",
      state: "",
      emailId: "",
      line2: "",
      line3: "",
      town: "",
      salutaion: "",
      defaultFlag: true
    };
  }

  onChange(val) {
    this.setState(val);
  }
  onChangeDefaultFlag() {
    this.setState(prevState => ({
      defaultFlag: !prevState.defaultFlag
    }));
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.addUserAddressStatus === SUCCESS) {
      this.props.history.goBack();
    }
  }

  addNewAddress = () => {
    //add new Address
    let addressDetails = {};
    addressDetails.countryIso = ISO_CODE;
    addressDetails.addressType = this.state.addressType;
    addressDetails.phone = this.state.phone;
    addressDetails.firstName = this.state.firstName;
    addressDetails.lastName = "";
    addressDetails.postalCode = this.state.postalCode;
    addressDetails.line1 = this.state.line1;
    addressDetails.state = this.state.state;
    addressDetails.emailId = this.state.emailId;
    addressDetails.line2 = this.state.line2;
    addressDetails.line3 = this.state.line3;
    addressDetails.town = this.state.town;
    addressDetails.salutaion = this.state.salutaion;
    addressDetails.defaultFlag = this.state.defaultFlag;
    this.props.addUserAddress(addressDetails);
  };

  clearAllValue = () => {
    this.setState({
      postalCode: "",
      firstName: "",
      line2: "",
      town: "",
      city: "",
      state: "",
      phone: "",
      line1: " ",
      titleValue: "",
      addressType: "",
      salutaion: "",
      defaultFlag: false
    });
  };

  render() {
    if (this.props.loading) {
      if (this.props.showSecondaryLoader) {
        this.props.showSecondaryLoader();
      }
    } else {
      if (this.props.hideSecondaryLoader) {
        this.props.hideSecondaryLoader();
      }
    }

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
    const salutaion = [
      {
        label: "Mr."
      },
      {
        label: "Mrs."
      },
      {
        label: "Miss."
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
            value={
              this.props.postalCode
                ? this.props.postalCode
                : this.state.postalCode
            }
            type={"number"}
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
          <div className={styles.salutation}>
            <SelectBoxMobile
              height={33}
              label={salutaion[0].label}
              options={salutaion.map((val, i) => {
                return {
                  value: val.label,
                  label: val.label
                };
              })}
              onChange={salutaion => this.onChange({ salutaion })}
            />
          </div>
          <div className={styles.name}>
            <Input2
              option={this.state.options}
              placeholder="Name*"
              value={
                this.props.firstName
                  ? this.props.firstName
                  : this.state.firstName
              }
              onChange={firstName => this.onChange({ firstName })}
              textStyle={{ fontSize: 14 }}
              height={33}
            />
          </div>
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
            value={this.props.line2 ? this.props.line2 : this.state.line2}
            onChange={line2 => this.onChange({ line2 })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.content}>
          <Input2
            boxy={true}
            placeholder="Email"
            value={this.props.emailId ? this.props.emailId : this.state.emailId}
            onChange={emailId => this.onChange({ emailId })}
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
            selected={this.state.defaultFlag}
            selectItem={() => this.onChangeDefaultFlag()}
          />
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.saveAndContinueButton}>
            <Button
              type="primary"
              label={SAVE_TEXT}
              width={176}
              height={38}
              onClick={() => this.addNewAddress()}
              textStyle={{ color: "#FFF", fontSize: 14 }}
            />
          </div>
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
  heading: "Add address",
  defaultAddress: false
};
