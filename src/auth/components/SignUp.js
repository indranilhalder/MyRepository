import React, { Component } from "react";
import PropTypes from "prop-types";
import { Button } from "xelpmoc-core";
import MediaQuery from "react-responsive";
import Input from "../../general/components/Input";
import PasswordInput from "./PasswordInput";
import styles from "./SignUp.css";

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
    if (this.props.user.isLoggedIn === true) {
      this.props.history.push("/home");
    }
  }
  onSubmit() {
    if (this.props.onSubmit) {
      this.props.onSubmit({
        loginId: this.state.emailValue,
        password: this.state.passwordValue
      });
    }
  }

  onChangeName(val) {
    if (this.props.onChangeName) {
      this.props.onChangeName(val);
    }
    this.setState({ nameValue: val });
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
      <div>
        <div>
          <div className={styles.input}>
            <Input
              value={this.props.value ? this.props.value : this.state.value}
              placeholder={"Name"}
              onChange={val => this.onChangeName(val)}
            />
          </div>
          <div className={styles.input}>
            <Input
              placeholder={"Email or phone number"}
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
