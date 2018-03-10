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
const SAVE_TEXT = "Save & Continue";
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
      defaultFlag: false
    };
  }

  onChangeAddressType(val) {
    this.setState({ addressType: val.addressType });
  }

  onChangePhone(val) {
    this.setState({ phone: val.phone });
  }

  onChangeFirstName(val) {
    this.setState({ firstName: val.firstName });
  }

  onChangeLastName(val) {
    this.setState({ lastName: val.lastName });
  }
  onChangePostalCode(val) {
    this.setState({ postalCode: val.postalCode });
  }
  onChangeLine1(val) {
    this.setState({ line1: val.line1 });
  }
  onChangeState(val) {
    this.setState({ state: val.state });
  }

  onChangeEmailId(val) {
    this.setState({ emailId: val.emailId });
  }

  onChangeLine2(val) {
    this.setState({ line2: val.landmark });
  }

  onChangeLine3(val) {
    this.setState({ line3: val.town });
  }

  onChangeTown(val) {
    this.setState({ town: val.city });
  }
  onChangeDefaultFlag(val) {
    this.setState({ defaultFlag: val.defaultFlag });
  }

  addNewAddress = () => {
    //add new Address
    let addressDetails = {};
    addressDetails.countryIso = ISO_CODE;
    addressDetails.addressType = this.state.addressType;
    addressDetails.phone = this.state.phone;
    addressDetails.firstName = this.state.firstName;
    addressDetails.lastName = this.state.firstName;
    addressDetails.postalCode = this.state.postalCode;
    addressDetails.line1 = this.state.line1;
    addressDetails.state = this.state.state;
    addressDetails.emailId = this.state.phone;
    addressDetails.line2 = this.state.line2;
    addressDetails.line3 = this.state.line3;
    addressDetails.town = this.state.town;
    addressDetails.defaultFlag = this.state.defaultFlag;
    this.props.addUserAddress(addressDetails);
  };

  clearAllValue = () => {
    this.onChange({
      pinCodeValue: "",
      fullNameValue: "",
      phoneNumberValue: "",
      stateName: "",
      cityNameValue: "",
      localityValue: "",
      landmark: "",
      titleValue: ""
    });
  };

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
            onChange={postalCode => this.onChangePostalCode({ postalCode })}
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
            onChange={firstName => this.onChangeFirstName({ firstName })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>

        <div className={styles.content}>
          <TextArea
            placeholder="Address*"
            onChange={line1 => this.onChangeLine1({ line1 })}
          />
        </div>
        <div className={styles.content}>
          <Input2
            boxy={true}
            placeholder="Landmark*"
            value={
              this.props.landmark ? this.props.landmark : this.state.landmark
            }
            onChange={landmark => this.onChangeLine2({ landmark })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.content}>
          <Input2
            boxy={true}
            placeholder="Locality/town*"
            value={this.props.town ? this.props.town : this.state.town}
            onChange={town => this.onChangeLine3({ town })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.content}>
          <Input2
            boxy={true}
            placeholder="City/district*"
            value={this.props.city ? this.props.city : this.state.city}
            onChange={city => this.onChangeTown({ city })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>
        <div className={styles.content}>
          <Input2
            placeholder="State*"
            value={this.props.state ? this.props.state : this.state.state}
            boxy={true}
            onChange={state => this.onChangeState({ state })}
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
            onChange={phone => this.onChangePhone({ phone })}
            textStyle={{ fontSize: 14 }}
            height={33}
          />
        </div>

        <div className={styles.content}>
          <GridSelect
            limit={1}
            offset={0}
            elementWidthMobile={50}
            onSelect={val => this.onChangeAddressType({ addressType: val[0] })}
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
              this.onChangeDefaultFlag({
                defaultAddress: !this.props.defaultAddress
              })
            }
          />
        </div>
        <Button
          backgroundColor={"#FF1744"}
          label={SAVE_TEXT}
          width={150}
          height={45}
          borderRadius={22.5}
          onClick={() => this.addNewAddress()}
          textStyle={{ color: "#FFF", fontSize: 14 }}
        />
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
