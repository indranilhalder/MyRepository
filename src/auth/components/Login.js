import React, { Component } from "react";
import PropTypes from "prop-types";
import { Button } from "xelpmoc-core";
import MediaQuery from "react-responsive";
import Input from "../../general/components/Input";
import PasswordInput from "./PasswordInput";
import styles from "./Login.css";
import LoginButton from "./LogInButton";

class Login extends Component {
  onButtonPress = () => {
    if (this.props.onButtonPress) {
      this.props.onButtonPress();
    }
  };

  onForgotPassword() {
    if (this.props.onForgotPassword) {
      this.props.onForgotPassword();
    }
  }

  render() {
    return (
      <div>
        <div>
          <Input placeholder={"Email or phone number"} />
          <PasswordInput placeholder={"Password"} />

          <div className={styles.forgotButton}>
            <MediaQuery query="(min-device-width: 1024px)">
              <Button
                backgroundColor={"transparent"}
                label={"FORGOT PASSWORD?"}
                onClick={() => this.onForgotPassword()}
                loading={this.props.loading}
                textStyle={{
                  color: "#d00",
                  fontSize: 12,
                  fontFamily: "regular"
                }}
              />
            </MediaQuery>

            <MediaQuery query="(max-device-width:1023px)">
              <div className={styles.forgotButtonPosition}>
                <Button
                  backgroundColor={"transparent"}
                  label={"FORGOT PASSWORD?"}
                  onClick={() => this.onForgotPassword()}
                  loading={this.props.loading}
                  textStyle={{
                    color: "#000",
                    fontSize: 12,
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
              onClick={this.onButtonPress}
              loading={this.props.loading}
            />
          </div>
        </div>
      </div>
    );
  }
}

Login.propTypes = {
  onButtonPress: PropTypes.func,
  onForgotPassword: PropTypes.func,
  loading: PropTypes.bool
};

Login.defaultProps = {
  loading: false
};

export default Login;
