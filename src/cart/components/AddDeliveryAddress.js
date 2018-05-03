import React from "react";
import styles from "./AddDeliveryAddress.css";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import Icon from "../../xelpmoc-core/Icon";
import SelectBoxMobile2 from "../../general/components/SelectBoxMobile2";
import CircleButton from "../../xelpmoc-core/CircleButton";
import informationIcon from "../../general/components/img/GPS.svg";
import GridSelect from "../../general/components/GridSelect";
import CheckboxAndText from "./CheckboxAndText";
import TextArea from "../../general/components/TextArea.js";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import Button from "../../general/components/Button";
import { SUCCESS, ERROR } from "../../lib/constants.js";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
import {
  EMAIL_REGULAR_EXPRESSION,
  MOBILE_PATTERN
} from "../../auth/components/Login";
const SAVE_TEXT = "Save Address";
const PINCODE_TEXT = "Please enter pincode";
const NAME_TEXT = "Please enter first name";
const LAST_NAME_TEXT = "plese enter last name";
const ADDRESS_TEXT = "Please enter address";
const EMAIL_TEXT = "Please enter email id";
const LANDMARK_TEXT = "Please select landmark";
const LANDMARK_ENTER_TEXT = "Please enter landmark";
const MOBILE_TEXT = "Please enter mobile number";
const PINCODE_VALID_TEXT = "Please enter valid pincode";
const EMAIL_VALID_TEXT = "Please enter valid emailId";
const PHONE_VALID_TEXT = "Please fill valid mobile number";
const PHONE_TEXT = "Please enter mobile number";
const CITY_TEXT = "please enter city";
const STATE_TEXT = "please enter state";
const ISO_CODE = "IN";
const OTHER_LANDMARK = "other";
export default class AddDeliveryAddress extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      countryIso: ISO_CODE,
      addressType: "Home",
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
      defaultFlag: true,
      isOtherLandMarkSelected: false,
      selectedLandmarkLabel: "Landmark",
      landmarkList: []
    };
  }

  getPinCodeDetails = val => {
    let landmarkList = [];
    if (val.length <= 6) {
      this.setState({ postalCode: val, state: "", town: "", landmarkList });
    }
    if (val.length === 6 && this.props.getPinCode) {
      this.props.getPinCode(val);
    }
  };
  handlePhoneInput(val) {
    if (val.length <= 10) {
      this.setState({ phone: val });
    }
  }
  onChange(val) {
    this.setState(val);
    if (this.props.getAddressDetails) {
      this.props.getAddressDetails(this.state);
    }
  }
  onChangeDefaultFlag() {
    this.setState(prevState => ({
      defaultFlag: !prevState.defaultFlag
    }));
  }
  componentWillUnmount() {
    if (this.props.resetAutoPopulateDataForPinCode) {
      this.props.resetAutoPopulateDataForPinCode();
    }
  }
  componentWillReceiveProps(nextProps) {
    let landmarkList = [];
    if (nextProps.addUserAddressStatus === SUCCESS) {
      this.props.history.goBack();
    }
    if (nextProps.getPincodeStatus === ERROR) {
      landmarkList = [{ landmark: OTHER_LANDMARK }];
      this.setState({
        state: "",
        town: "",
        landmarkList
      });
    }
    if (nextProps.getPincodeStatus === SUCCESS && nextProps.getPinCodeDetails) {
      if (nextProps.getPinCodeDetails.landMarks) {
        landmarkList = [
          ...nextProps.getPinCodeDetails.landMarks,
          { landmark: OTHER_LANDMARK }
        ];
        this.setState({
          state:
            nextProps.getPinCodeDetails &&
            nextProps.getPinCodeDetails.state &&
            nextProps.getPinCodeDetails.state.name,
          town:
            nextProps.getPinCodeDetails && nextProps.getPinCodeDetails.cityName,
          landmarkList
        });
      } else {
        landmarkList = [{ landmark: OTHER_LANDMARK }];
        let stateName =
          nextProps.getPinCodeDetails &&
          nextProps.getPinCodeDetails.state &&
          nextProps.getPinCodeDetails.state.name
            ? nextProps.getPinCodeDetails.state.name
            : "";
        let townName =
          nextProps.getPinCodeDetails && nextProps.getPinCodeDetails.cityName
            ? nextProps.getPinCodeDetails.cityName
            : "";
        this.setState({
          state: stateName,
          town: townName,
          landmarkList
        });
      }
    }
  }
  onSelectLandmark = landmark => {
    if (landmark.value === OTHER_LANDMARK) {
      this.setState({
        isOtherLandMarkSelected: true,
        selectedLandmarkLabel: landmark.value
      });
    } else {
      this.setState({
        isOtherLandMarkSelected: false,
        landmark: landmark.value,
        selectedLandmarkLabel: landmark.value
      });
    }
  };
  addNewAddress = () => {
    if (!this.state.postalCode) {
      this.props.displayToast(PINCODE_TEXT);
      return false;
    }
    if (this.state.postalCode && this.state.postalCode.length < 6) {
      this.props.displayToast(PINCODE_VALID_TEXT);
      return false;
    }
    if (!this.state.firstName) {
      this.props.displayToast(NAME_TEXT);
      return false;
    }
    if (!this.state.lastName) {
      this.props.displayToast(LAST_NAME_TEXT);
      return false;
    }
    if (!this.state.line1) {
      this.props.displayToast(ADDRESS_TEXT);
      return false;
    }
    if (
      !this.state.landmark &&
      this.state.selectedLandmarkLabel === "Landmark"
    ) {
      this.props.displayToast(LANDMARK_TEXT);
      return false;
    }
    if (
      this.state.selectedLandmarkLabel === OTHER_LANDMARK &&
      !this.state.line2
    ) {
      this.props.displayToast(LANDMARK_ENTER_TEXT);
      return false;
    }
    if (!this.state.emailId) {
      this.props.displayToast(EMAIL_TEXT);
      return false;
    }
    if (
      this.state.emailId &&
      !EMAIL_REGULAR_EXPRESSION.test(this.state.emailId)
    ) {
      this.props.displayToast(EMAIL_VALID_TEXT);
      return false;
    }
    if (!this.state.town) {
      this.props.displayToast(CITY_TEXT);
      return false;
    }
    if (!this.state.state) {
      this.props.displayToast(STATE_TEXT);
      return false;
    }
    if (!this.state.phone) {
      this.props.displayToast(PHONE_TEXT);
      return false;
    }
    if (this.state.phone && !MOBILE_PATTERN.test(this.state.phone)) {
      this.props.displayToast(PHONE_VALID_TEXT);
      return false;
    } else {
      this.props.addUserAddress(this.state);
    }
  };

  clearAllValue = () => {
    this.setState({
      postalCode: "",
      firstName: "",
      lastName: "",
      line2: "",
      town: "",
      state: "",
      phone: "",
      line1: " ",
      titleValue: "",
      addressType: "",
      salutaion: "",
      defaultFlag: false,
      landmarkList: []
    });
  };
  onChangeSalutation(val) {
    this.setState({ salutaion: val.value });
  }
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
            onChange={postalCode => this.getPinCodeDetails(postalCode)}
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
            <SelectBoxMobile2
              height={33}
              value={
                this.state.salutaion ? this.state.salutaion : salutaion[0].label
              }
              options={salutaion.map((val, i) => {
                return {
                  value: val.label,
                  label: val.label
                };
              })}
              onChange={salutaion => this.onChangeSalutation(salutaion)}
            />
          </div>
          <div className={styles.name}>
            <Input2
              option={this.state.options}
              placeholder="First Name*"
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
          <Input2
            boxy={true}
            placeholder="Last Name*"
            value={
              this.props.lastName ? this.props.lastName : this.state.lastName
            }
            onChange={lastName => this.onChange({ lastName })}
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
          <SelectBoxMobile2
            height={33}
            placeholder={"Landmark"}
            options={
              this.state.landmarkList.length > 0 &&
              this.state.landmarkList.map((val, i) => {
                return {
                  value: val && val.landmark,
                  label: val && val.landmark
                };
              })
            }
            onChange={landmark => this.onSelectLandmark(landmark)}
          />
        </div>
        {this.state.isOtherLandMarkSelected && (
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
        )}
        <div className={styles.content}>
          <Input2
            boxy={true}
            placeholder="Email*"
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
            value={this.props.town ? this.props.town : this.state.town}
            onChange={town => this.onChange({ town })}
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
            onChange={phone => this.handlePhoneInput(phone)}
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
            selected={[this.state.addressType]}
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
            {!this.props.isFirstAddress && (
              <Button
                type="primary"
                label={SAVE_TEXT}
                width={176}
                height={38}
                onClick={() => this.addNewAddress()}
                textStyle={{ color: "#FFF", fontSize: 14 }}
              />
            )}
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
