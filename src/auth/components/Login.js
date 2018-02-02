import React, { Component } from "react";
import PropTypes from "prop-types";
import { Button } from "xelpmoc-core";
import MediaQuery from "react-responsive";
import Input from "../../general/components/Input";
import PasswordInput from "./PasswordInput";
import styles from "./Login.css";
import LoginButton from "./LogInButton";
import { SUCCESS } from "../../lib/constants";

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
    if (this.props.user.status === SUCCESS) {
      this.props.homeFeed();
      this.props.history.push("/home");
    }
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
    return (
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
            <MediaQuery query="(min-device-width: 1024px)">
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

            <MediaQuery query="(max-device-width:1023px)">
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
            <LoginButton onClick={this.onSubmit} loading={this.props.loading} />
          </div>
        </div>
      </React.Fragment>
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
