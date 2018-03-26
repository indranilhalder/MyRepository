import React from "react";
import AuthPopUp from "./AuthPopUp";
import PropTypes from "prop-types";
import { Button } from "xelpmoc-core";
import Input from "../../general/components/Input";
import { default as styles } from "./AuthPopUp.css";
import { default as ownStyles } from "./RestorePassword.css";
export default class NewPassword extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      userId: ""
    };
  }

  handleCancelClick() {
    if (this.props.handleCancel) {
      this.props.handleCancel();
    }
  }

  handleContinue() {
    if (this.props.onContinue) {
      this.props.onContinue(this.state.newPassword, this.state.oldPassword);
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
          <Input
            hollow={true}
            placeholder="New password"
            onChange={val => this.setState({ newPassword: val })}
          />
        </div>
        <div className={styles.input}>
          <Input
            hollow={true}
            placeholder="Old password"
            onChange={val => this.setState({ oldPassword: val })}
          />
        </div>
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
