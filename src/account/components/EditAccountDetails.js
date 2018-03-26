import React from "react";
import styles from "./EditAccountDetails.css";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import SelectBoxMobile from "../../general/components/SelectBoxMobile";
import MobileDatePicker from "../../general/components/MobileDatePicker";
import ShopByBrandLists from "../../blp/components/ShopByBrandLists.js";
import CheckboxAndText from "../../cart/components/CheckboxAndText.js";
import AccountFooter from "./AccountFooter.js";
import moment from "moment";
import { LOG_OUT_ACCOUNT } from "../actions/account.actions.js";
import ChangePassword from "./ChangePassword.js";
import * as Cookie from "../../lib/Cookie";
import {
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN,
  LOGIN_PATH
} from "../../lib/constants";

export default class EditAccountDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      firstName: "",
      lastName: "",
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
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.userDetails !== this.props.userDetails) {
      if (nextProps.userDetails) {
        this.setState({
          firstName: nextProps.userDetails.firstName,
          lastName: nextProps.userDetails.lastName,
          dateOfBirth: nextProps.userDetails.dateOfBirth,
          gender: nextProps.userDetails.gender,
          mobileNumber: nextProps.userDetails.mobileNumber,
          emailId: nextProps.userDetails.emailID
        });
      }
    }
    if (nextProps.type === LOG_OUT_ACCOUNT) {
      this.props.history.push(LOGIN_PATH);
    }
  }

  onChange(val) {
    this.setState(val);
  }
  onChangeDateOfBirth = val => {
    let dateOfBirth = moment(val).format("DD/MM/YYYY");
    this.setState({ dateOfBirth: dateOfBirth });
  };
  updateProfile = () => {
    if (this.props.updateProfile) {
      this.props.updateProfile(this.state);
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
    console.log("test");
    let userDetails = this.props.userDetails;
    if (userDetails && !this.state.changePassword) {
      return (
        <div className={styles.base}>
          <div className={styles.holder}>
            <div className={styles.container}>
              <Input2
                value={this.state.firstName}
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={firstName => this.onChange({ firstName })}
              />
            </div>
            <div className={styles.container}>
              <Input2
                value={this.state.emailId}
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={emailId => this.onChange({ emailId })}
              />
            </div>
            <div className={styles.container}>
              <Input2
                value={this.state.mobileNumber}
                boxy={true}
                textStyle={{ fontSize: 14 }}
                height={33}
                onChange={mobileNumber => this.onChange({ mobileNumber })}
              />
            </div>

            <div className={styles.container}>
              <SelectBoxMobile
                value={this.state.gender}
                options={[
                  { label: "Female", value: "Female" },
                  { label: "Male", value: "Male" }
                ]}
                arrowColour="grey"
                height={33}
                onChange={gender => this.onChange({ gender })}
              />
            </div>
            <div className={styles.container}>
              <MobileDatePicker
                onChange={dateOfBirth =>
                  this.onChangeDateOfBirth({ dateOfBirth })
                }
              />
            </div>
          </div>
          <div className={styles.changePassword}>
            <ShopByBrandLists
              brandList={"Change Password"}
              onClick={() => this.renderChangePassword()}
            />
          </div>
          <div className={styles.sendNotification}>
            <CheckboxAndText label="Send Me Notifications" selected={false} />
          </div>
          <AccountFooter
            cancel={() => this.cancel()}
            update={() => this.updateProfile()}
          />
        </div>
      );
    } else if (this.state.changePassword) {
      return (
        <ChangePassword
          updatePassword={passwordDetails =>
            this.changePassword(passwordDetails)
          }
        />
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
