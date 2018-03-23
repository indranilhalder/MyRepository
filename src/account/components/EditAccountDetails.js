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

import {
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN,
  LOGIN_PATH
} from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
export default class EditAccountDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      firstName: "",
      lastName: "",
      dateOfBirth: "",
      gender: "",
      mobileNumber: "",
      emailId: ""
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
        console.log(nextProps.userDetails);
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
  }

  onChange(val) {
    this.setState(val);
  }
  onChangeDateOfBirth = val => {
    let dateOfBirth = moment(val).format("DD/MM/YYYY");
    console.log(dateOfBirth);
    this.setState({ dateOfBirth: dateOfBirth });
  };
  updateProfile = () => {
    if (this.props.updateProfile) {
      console.log(this.props);
      this.props.updateProfile(this.state);
    }
  };

  render() {
    let userDetails = this.props.userDetails;
    if (userDetails) {
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
            <ShopByBrandLists brandList={"Change Password"} />
          </div>
          <div className={styles.sendNotification}>
            <CheckboxAndText label="Send Me Notifications" selected={false} />
          </div>
          <AccountFooter update={() => this.updateProfile()} />
        </div>
      );
    } else {
      return null;
    }
  }
}
EditAccountDetails.propTypes = {
  emailId: PropTypes.string,
  phoneNumber: PropTypes.string,
  userName: PropTypes.string,
  gender: PropTypes.string,
  onChange: PropTypes.func
};
EditAccountDetails.defaultProps = {
  phoneNumber: "7358082465",
  emailId: "ananya_patel@123.com",
  userName: "Anaya Patel",
  gender: "Male"
};
