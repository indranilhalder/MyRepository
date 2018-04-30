import React from "react";
import styles from "./EditAccountDetails.css";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import SelectBoxMobile2 from "../../general/components/SelectBoxMobile2";
import MobileDatePicker from "../../general/components/MobileDatePicker";
import ShopByBrandLists from "../../blp/components/ShopByBrandLists.js";
import CheckboxAndText from "../../cart/components/CheckboxAndText.js";
import AccountFooter from "./AccountFooter.js";
import moment from "moment";
import { LOG_OUT_ACCOUNT_USING_MOBILE_NUMBER } from "../actions/account.actions.js";
import ChangePassword from "./ChangePassword.js";
import * as Cookie from "../../lib/Cookie";
import ProfilePicture from "../../blp/components/ProfilePicture.js";
import {
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN,
  LOGIN_PATH
} from "../../lib/constants";
const ACCOUNT_SETTING_HEADER = "Account Settings";
export default class EditAccountDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      firstName: this.props.firstName ? this.props.firstName : "",
      lastName: this.props.lastName ? this.props.lastName : "",
      dateOfBirth: this.props.dateOfBirth ? this.props.dateOfBirth : "",
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

      if (nextProps.userDetails.dateOfBirth) {
        let dateOfBirth = new Date(
          nextProps.userDetails.dateOfBirth.split("IST").join()
        );

        formattedDate = moment(dateOfBirth).format("DD/MM/YYYY");
      }

      this.setState({
        firstName: nextProps.userDetails.firstName,
        lastName: nextProps.userDetails.lastName,
        dateOfBirth: formattedDate,
        gender: nextProps.userDetails.gender,
        mobileNumber: nextProps.userDetails.mobileNumber,
        emailId: nextProps.userDetails.emailID
      });
    }
    if (nextProps.type === LOG_OUT_ACCOUNT_USING_MOBILE_NUMBER) {
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
    let formattedDate = moment(val).format("DD/MM/YYYY");

    this.setState({ dateOfBirth: formattedDate });
  };
  updateProfile = () => {
    if (this.state.firstName === "undefined" || !this.state.firstName) {
      this.props.displayToast("Please enter first name");
      return false;
    }
    if (this.state.lastName === "undefined" || !this.state.lastName) {
      this.props.displayToast("Please enter last name");
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

  changePassword = passwordDetails => {
    this.setState({ changePassword: false });
    this.props.changePassword(passwordDetails);
  };
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
                placeholder="Mobile NUmber"
                value={this.state.mobileNumber}
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={mobileNumber => this.onChange({ mobileNumber })}
                disabled={this.state.mobileNumber ? true : false}
              />
            </div>

            <div className={styles.container}>
              <SelectBoxMobile2
                placeholder={"Gender"}
                label={this.state.gender}
                value={this.state.gender}
                options={[
                  { label: "Female", value: "Female" },
                  { label: "Male", value: "Male" }
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
