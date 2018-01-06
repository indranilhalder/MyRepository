import React from "react";
import AuthPopUp from "./AuthPopUp";
import PropTypes from "prop-types";
import { Input, Button } from "xelpmoc-core";
import { default as styles } from "./AuthPopUp.css";
import { default as ownStyles } from "./RestorePassword.css";
export default class RestorePassword extends React.Component {
  handleCancelClick() {
    if (this.props.handleCancel) {
      this.props.handleCancel();
    }
  }

  handleRestoreClick() {
    if (this.props.handleRestoreClick) {
      this.props.handleRestoreClick();
    }
  }
  render() {
    return (
      <AuthPopUp>
        <div className={styles.header}>Restore password</div>
        <div className={styles.content}>
          Please enter your Email or phone number to restore the password
        </div>
        <div className={styles.input}>
          <Input hollow={true} />
        </div>
        <div className={styles.button}>
          <div className={ownStyles.submit}>
            <Button
              label={"Restore"}
              width={150}
              height={40}
              borderRadius={20}
              backgroundColor={"#FF1744"}
              onClick={() => this.handleRestoreClick()}
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

RestorePassword.propTypes = {
  handleCancel: PropTypes.func,
  handleRestoreClick: PropTypes.func
};
