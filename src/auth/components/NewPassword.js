import React from "react";
import AuthPopUp from "./AuthPopUp";
import PropTypes from "prop-types";
import Button from "../../xelpmoc-core/Button";
import PasswordInput from "../../auth/components/PasswordInput";
import { default as styles } from "./AuthPopUp.css";
import { default as ownStyles } from "./RestorePassword.css";
import Error from "../../general/components/Error.js";
export default class NewPassword extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userId: "",
      error: false
    };
  }

  handleCancelClick() {
    if (this.props.handleCancel) {
      this.props.handleCancel();
    }
  }

  handleContinue() {
    const newPassword = this.state.newPassword;
    const confirmedPassword = this.state.confirmedPassword;
    if (newPassword === confirmedPassword) {
      this.setState({ error: false });
    } else {
      this.setState({ error: true });
    }
    if (this.props.onContinue) {
      if (newPassword === confirmedPassword) {
        this.props.onContinue({
          newPassword: this.state.newPassword,
          username: this.props.userName,
          otp: this.props.otpDetails
        });
      }
    }
  }
  render() {
    return (
      <AuthPopUp>
        <div className={styles.header}>Create new password</div>
        {/* <div className={styles.content}>
          Please enter your Email or phone number to restore the password
        </div> */}
        <div className={styles.input}>
          <PasswordInput
            hollow={true}
            placeholder="New password"
            onChange={val => this.setState({ newPassword: val })}
          />
        </div>
        <div className={styles.input}>
          <PasswordInput
            hollow={true}
            type="password"
            placeholder="Confirm Password"
            onChange={val => this.setState({ confirmedPassword: val })}
          />
        </div>
        <Error message="Passwords must match" show={this.state.error} />

        <div className={styles.button}>
          <div className={ownStyles.submit}>
            <Button
              label={"Continue"}
              width={150}
              height={40}
              borderRadius={20}
              backgroundColor={"#FF1744"}
              onClick={() => this.handleContinue()}
              loading={this.props.loading}
              textStyle={{ color: "#FFF", fontSize: 14 }}
            />
          </div>
        </div>
        <div className={styles.button}>
          <div className={ownStyles.cancel}>
            <Button
              label={"Cancel"}
              onClick={() => this.handleCancelClick()}
              backgroundColor="transparent"
              width={100}
              height={40}
              borderRadius={20}
              loading={this.props.loading}
              textStyle={{ color: "#FFF", fontSize: 14 }}
            />
          </div>
        </div>
      </AuthPopUp>
    );
  }
}

NewPassword.propTypes = {
  handleCancel: PropTypes.func,
  onContinue: PropTypes.func
};
