import React from "react";
import styles from "./EditAccountDetails.css";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import SelectBoxMobile2 from "../../general/components/SelectBoxMobile2";
import MobileDatePicker from "../../general/components/MobileDatePicker";
import ShopByBrandLists from "../../blp/components/ShopByBrandLists.js";
import CheckboxAndText from "../../cart/components/CheckboxAndText.js";
import AccountFooter from "./AccountFooter.js";
import format from "date-fns/format";

import { LOG_OUT_ACCOUNT_USING_MOBILE_NUMBER } from "../actions/account.actions.js";
import ChangePassword from "./ChangePassword.js";
import * as Cookie from "../../lib/Cookie";
import ProfilePicture from "../../blp/components/ProfilePicture.js";
import {
  EMAIL_REGULAR_EXPRESSION,
  MOBILE_PATTERN
} from "../../auth/components/Login";
import {
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN,
  LOGIN_PATH
} from "../../lib/constants";
const ACCOUNT_SETTING_HEADER = "Account Settings";
const MINIMUM_PASSWORD_LENGTH = 8;
const OLD_PASSWORD_TEXT = "Please enter old password";
const NEW_PASSWORD_TEXT = "Please enter new password";
const PASSWORD_LENGTH_TEXT = "Password length should be minimum 8 character";
const CONFIRM_PASSWORD_TEXT = "Please confirm your passowrd";
const PASSWORD_MATCH_TEXT = "Password did not match";
const DATE_FORMAT_TO_UPDATE_PROFILE = "DD/MM/YYYY";
export default class EditAccountDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      firstName:
        this.props.firstName && this.props.firstName !== "undefined"
          ? this.props.firstName
          : "",
      lastName:
        this.props.lastName && this.props.lastName !== "undefined"
          ? this.props.lastName
          : "",
      dateOfBirth: "",
      gender: "",
      mobileNumber: "",
      emailId: "",
      changePassword: false
    };
  }
  componentDidMount() {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);

    if (userDetails && customerCookie) {
      this.props.getUserDetails();
    } else {
      this.props.history.push(LOGIN_PATH);
    }
    this.props.setHeaderText(ACCOUNT_SETTING_HEADER);
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.userDetails) {
      let formattedDate = "";

      if (
        nextProps.userDetails.dateOfBirth &&
        nextProps.userDetails.dateOfBirth.indexOf("IST") > -1
      ) {
        let dateOfBirth = new Date(
          nextProps.userDetails.dateOfBirth.split("IST").join()
        );
        formattedDate = format(dateOfBirth, "YYYY-MM-DD");
      } else if (nextProps.userDetails.dateOfBirth) {
        formattedDate = nextProps.userDetails.dateOfBirth
          .split("/")
          .reverse()
          .join("-");
      }
      let email = nextProps.userDetails.emailId
        ? nextProps.userDetails.emailId
        : nextProps.userDetails.emailID;

      this.setState({
        firstName: nextProps.userDetails.firstName,
        lastName: nextProps.userDetails.lastName,
        dateOfBirth: formattedDate,
        gender: nextProps.userDetails.gender,
        mobileNumber: nextProps.userDetails.mobileNumber,
        emailId: email
      });
    }
    if (nextProps.type === LOG_OUT_ACCOUNT_USING_MOBILE_NUMBER) {
      if (this.props.clearAccountUpdateType) {
        this.props.clearAccountUpdateType();
      }
      this.props.history.push(LOGIN_PATH);
    }
  }
  onChangeGender(val) {
    this.setState({ gender: val.value });
  }
  onChange(val) {
    this.setState(val);
  }
  onChangeDateOfBirth = val => {
    this.setState({ dateOfBirth: val });
  };
  onChangeMobileNumber(val) {
    if (val.length <= 10) {
      this.setState({ mobileNumber: val });
    }
  }
  updateProfile = () => {
    if (
      this.state.emailId &&
      !EMAIL_REGULAR_EXPRESSION.test(this.state.emailId)
    ) {
      this.props.displayToast("Please fill valid emailId");
      return false;
    }
    if (
      this.state.mobileNumber &&
      !MOBILE_PATTERN.test(this.state.mobileNumber)
    ) {
      this.props.displayToast("Please fill valid mobile number");
      return false;
    } else {
      if (this.props.updateProfile) {
        this.props.updateProfile(this.state);
      }
    }
  };

  cancel = () => {
    this.props.history.goBack();
  };
  changePassword(passwordDetails) {
    const oldPassword = passwordDetails.oldPassword;
    const newPassword = passwordDetails.newPassword;
    const confirmedPassword = passwordDetails.confirmPassword;
    if (!oldPassword) {
      this.props.displayToast(OLD_PASSWORD_TEXT);
      return false;
    }
    if (!newPassword) {
      this.props.displayToast(NEW_PASSWORD_TEXT);
      return false;
    }
    if (newPassword.length < MINIMUM_PASSWORD_LENGTH) {
      this.props.displayToast(PASSWORD_LENGTH_TEXT);
      return false;
    }
    if (!confirmedPassword) {
      this.props.displayToast(CONFIRM_PASSWORD_TEXT);
      return false;
    }
    if (newPassword !== confirmedPassword) {
      this.props.displayToast(PASSWORD_MATCH_TEXT);
    } else {
      this.setState({ changePassword: false });
      this.props.changePassword(passwordDetails);
    }
  }
  renderChangePassword = () => {
    this.setState({ changePassword: true });
  };
  render() {
    let userDetails = this.props.userDetails;
    if (userDetails && !this.state.changePassword) {
      return (
        <div className={styles.base}>
          <div className={styles.profileImage}>
            <ProfilePicture
              firstName={
                this.state.firstName !== "undefined" ? this.state.firstName : ""
              }
              lastName={
                this.state.lastName !== "undefined" ? this.state.lastName : ""
              }
            />
          </div>
          <div className={styles.holder}>
            <div className={styles.container}>
              <Input2
                placeholder="First Name"
                value={
                  this.state.firstName !== "undefined"
                    ? this.state.firstName
                    : ""
                }
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={firstName => this.onChange({ firstName })}
                onlyAlphabet={true}
              />
            </div>
            <div className={styles.container}>
              <Input2
                placeholder="Last Name"
                value={
                  this.state.lastName !== "undefined" ? this.state.lastName : ""
                }
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={lastName => this.onChange({ lastName })}
                onlyAlphabet={true}
              />
            </div>
            <div className={styles.container}>
              <Input2
                placeholder="Email"
                value={this.state.emailId}
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={emailId => this.onChange({ emailId })}
              />
            </div>
            <div className={styles.container}>
              <Input2
                placeholder="Mobile Number"
                value={this.state.mobileNumber}
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={mobileNumber =>
                  this.onChangeMobileNumber(mobileNumber)
                }
                disabled={false}
              />
            </div>

            <div className={styles.container}>
              <SelectBoxMobile2
                placeholder={"Gender"}
                label={this.state.gender}
                value={this.state.gender}
                options={[
                  { label: "Female", value: "FEMALE" },
                  { label: "Male", value: "MALE" }
                ]}
                arrowColour="grey"
                height={33}
                onChange={gender => this.onChangeGender(gender)}
              />
            </div>
            <div className={styles.container}>
              <MobileDatePicker
                value={this.state.dateOfBirth}
                onChange={dateOfBirth => this.onChangeDateOfBirth(dateOfBirth)}
              />
            </div>
          </div>
          <div className={styles.changePassword}>
            <ShopByBrandLists
              brandList={"Change Password"}
              onClick={() => this.renderChangePassword()}
            />
          </div>
          <AccountFooter
            cancel={() => this.cancel()}
            update={() => this.updateProfile()}
          />
        </div>
      );
    } else if (this.state.changePassword) {
      return (
        <div className={styles.changePasswordPageHolder}>
          <ChangePassword
            updatePassword={passwordDetails =>
              this.changePassword(passwordDetails)
            }
          />
        </div>
      );
    } else {
      return null;
    }
  }
}
EditAccountDetails.propTypes = {
  emailId: PropTypes.string,
  mobileNumber: PropTypes.string,
  firstName: PropTypes.string,
  gender: PropTypes.string,
  onChange: PropTypes.func
};
