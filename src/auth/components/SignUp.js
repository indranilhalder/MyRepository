import React, { Component } from "react";
import PropTypes from "prop-types";
import Button from "../../xelpmoc-core/Button";
import MediaQuery from "react-responsive";
import Input from "../../general/components/Input";
import PasswordInput from "./PasswordInput";
import styles from "./SignUp.css";
import AuthFrame from "./AuthFrame.js";
import SecondaryLoader from "../../general/components/SecondaryLoader";
import {
  LOGIN_PATH,
  SIGN_UP_PATH,
  HOME_ROUTER,
  MAIN_ROUTER
} from "../../lib/constants";
import { EMAIL_REGULAR_EXPRESSION, MOBILE_PATTERN } from "./Login";

class SignUp extends Component {
  constructor(props) {
    super(props);
    this.state = {
      nameValue: props.nameValue ? props.nameValue : "",
      emailValue: props.emailValue ? props.emailValue : "",
      passwordValue: props.passwordValue ? props.passwordValue : ""
    };
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.authCallsIsSucceed) {
      if (this.props.redirectToAfterAuthUrl) {
        this.props.history.replace(this.props.redirectToAfterAuthUrl);
        this.props.clearUrlToRedirectToAfterAuth();
      } else {
        this.props.history.replace(HOME_ROUTER);
      }
    }
  }
  onSubmit() {
    if (!this.state.phoneNumberValue) {
      this.props.displayToast("Please fill mobile number ");
      return false;
    }
    if (!MOBILE_PATTERN.test(this.state.phoneNumberValue)) {
      this.props.displayToast("Please fill valid mobile number");
      return false;
    }
    if (this.state.emailValue) {
      if (!EMAIL_REGULAR_EXPRESSION.test(this.state.emailValue)) {
        this.props.displayToast("Please fill valid emailId");
        return false;
      }
    }

    if (!this.state.passwordValue) {
      this.props.displayToast("Please fill password");
      return false;
    }
    if (this.state.passwordValue.length < "8") {
      this.props.displayToast("Password length should be minimum 8 character");
      return false;
    } else {
      this.props.onSubmit({
        emailId: this.state.emailValue,
        username: this.state.phoneNumberValue,
        password: this.state.passwordValue
      });
    }
  }
  navigateToLogin() {
    this.props.history.push(LOGIN_PATH);
  }
  onChangeName(val) {
    if (this.props.onChangeName) {
      this.props.onChangeName(val);
    }
    this.setState({ nameValue: val });
  }

  onPhoneNumberChange(val) {
    if (this.props.onPhoneNumberChange) {
      this.props.onPhoneNumberChange(val);
    }
    if (val.length <= 10) {
      this.setState({ phoneNumberValue: val });
    }
  }

  onChangeEmail(val) {
    if (this.props.onChangeEmail) {
      this.props.onChangeEmail(val);
    }
    this.setState({ emailValue: val });
  }

  onChangePassword(val) {
    if (this.props.onChangePassword) {
      this.props.onChangePassword(val);
    }
    this.setState({ passwordValue: val });
  }

  render() {
    const pathName = this.props.location.pathname;
    let footerText = "";
    let footerClick;
    let showSocialButtons;
    if (pathName === LOGIN_PATH || MAIN_ROUTER) {
      footerText = "Don't have an account? Sign up";
      footerClick = () => this.navigateToSignUp();
      showSocialButtons = true;
    }

    if (pathName === SIGN_UP_PATH) {
      footerText = "Already have an account? Login";
      footerClick = () => this.navigateToLogin();
      showSocialButtons = true;
    }
    if (this.props.authCallsInProcess) {
      return (
        <div className={styles.loadingIndicator}>
          <SecondaryLoader />
        </div>
      );
    }
    return (
      <AuthFrame
        {...this.props}
        showSocialButtons={showSocialButtons}
        footerText={footerText}
        footerClick={footerClick}
        isSignUp={true}
      >
        <div>
          <div>
            <div className={styles.input}>
              <Input
                value={
                  this.props.phoneNumberValue
                    ? this.props.phoneNumberValue
                    : this.state.phoneNumberValue
                }
                placeholder={"Phone number"}
                type={"number"}
                onChange={val => this.onPhoneNumberChange(val)}
                maxLength={"10"}
              />
            </div>
            <div className={styles.input}>
              <Input
                placeholder={"Email (optional)"}
                value={
                  this.props.emailValue
                    ? this.props.emailValue
                    : this.state.emailValue
                }
                onChange={val => this.onChangeEmail(val)}
              />
            </div>
            <PasswordInput
              placeholder={"Password"}
              password={
                this.props.passwordValue
                  ? this.props.passwordValue
                  : this.state.passwordValue
              }
              onChange={val => this.onChangePassword(val)}
            />
          </div>
          <div className={styles.buttonSignup}>
            <div className={styles.buttonHolder}>
              <MediaQuery query="(min-device-width: 1025px)">
                <Button
                  label={"Sign Up"}
                  width={200}
                  height={40}
                  borderColor={"#000000"}
                  borderRadius={20}
                  backgroundColor={"#ffffff"}
                  onClick={() => this.onSubmit()}
                  loading={this.props.loading}
                  textStyle={{
                    color: "#000000",
                    fontSize: 14,
                    fontFamily: "regular"
                  }}
                />
              </MediaQuery>
              <MediaQuery query="(max-device-width:1024px)">
                <Button
                  backgroundColor={"#FF1744"}
                  label={"Sign Up"}
                  width={150}
                  height={45}
                  borderRadius={22.5}
                  onClick={() => this.onSubmit()}
                  loading={this.props.loading}
                  textStyle={{
                    color: "#FFFFFF",
                    fontSize: 14,
                    fontFamily: "regular"
                  }}
                />
              </MediaQuery>
            </div>
          </div>
        </div>
      </AuthFrame>
    );
  }
}

SignUp.propTypes = {
  onSubmit: PropTypes.func,
  onChangeName: PropTypes.func,
  onChangeEmail: PropTypes.func,
  onChangePassword: PropTypes.func,
  nameValue: PropTypes.string,
  emailValue: PropTypes.string,
  passwordValue: PropTypes.string,
  loading: PropTypes.bool
};

SignUp.defaultProps = {
  loading: false
};

export default SignUp;
