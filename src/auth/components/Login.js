import React, { Component } from "react";
import PropTypes from "prop-types";
import { Button } from "xelpmoc-core";
import MediaQuery from "react-responsive";
import Input from "../../general/components/Input";
import PasswordInput from "./PasswordInput";
import styles from "./Login.css";
import LoginButton from "./LogInButton";
import { SUCCESS } from "../../lib/constants";
import AuthFrame from "./AuthFrame.js";
import {
  LOGIN_PATH,
  SIGN_UP_PATH,
  HOME_ROUTER,
  MAIN_ROUTER
} from "../../lib/constants";
// Forgot password --> shows a modal
// Don't have an account --> sign up --> a route change.

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      emailValue: props.emailValue ? props.emailValue : "",
      passwordValue: props.passwordValue ? props.passwordValue : ""
    };
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.user.isLoggedIn === true) {
      this.props.history.push(HOME_ROUTER);
    }
  }

  navigateToSignUp() {
    this.props.history.push(SIGN_UP_PATH);
  }
  onSubmit = () => {
    if (this.props.onSubmit) {
      let userDetails = {};
      userDetails.username = this.state.emailValue;
      userDetails.password = this.state.passwordValue;
      this.props.customerAccessToken(userDetails);
    }
  };

  onForgotPassword() {
    if (this.props.onForgotPassword) {
      this.props.onForgotPassword();
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
      showSocialButtons = false;
    }
    return (
      <AuthFrame
        {...this.props}
        showSocialButtons={showSocialButtons}
        footerText={footerText}
        footerClick={footerClick}
      >
        <React.Fragment>
          <div>
            <div className={styles.input}>
              <Input
                placeholder={"Email or phone number"}
                emailValue={
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

            <div className={styles.forgotButton}>
              <MediaQuery query="(min-device-width: 1025px)">
                <Button
                  backgroundColor={"transparent"}
                  label={"Forgot Password?"}
                  onClick={() => this.onForgotPassword()}
                  loading={this.props.loading}
                  textStyle={{
                    color: "#FF1744",
                    fontSize: 14,
                    fontFamily: "regular"
                  }}
                />
              </MediaQuery>

              <MediaQuery query="(max-device-width:1024px)">
                <div className={styles.forgotButtonPosition}>
                  <Button
                    height={25}
                    backgroundColor={"transparent"}
                    label={"Forgot Password?"}
                    onClick={() => this.onForgotPassword()}
                    loading={this.props.loading}
                    textStyle={{
                      color: "#fffff",
                      fontSize: 14,
                      fontFamily: "regular"
                    }}
                  />
                </div>
              </MediaQuery>
            </div>
          </div>
          <div className={styles.buttonLogin}>
            <div className={styles.buttonHolder}>
              <LoginButton
                onClick={this.onSubmit}
                loading={this.props.loading}
              />
            </div>
          </div>
        </React.Fragment>
      </AuthFrame>
    );
  }
}

Login.propTypes = {
  onSubmit: PropTypes.func,
  onForgotPassword: PropTypes.func,
  onChangeEmail: PropTypes.func,
  onChangePassword: PropTypes.func,
  emailValue: PropTypes.string,
  passwordValue: PropTypes.string,
  loading: PropTypes.bool
};

Login.defaultProps = {
  loading: false
};

export default Login;
