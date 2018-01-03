import React from "react";
import AuthPopUp from "./AuthPopUp";
import { Input, Button } from "xelpmoc-core";
import { default as styles } from "./AuthPopUp.css";
import { default as ownStyles } from "./RestorePassword.css";
export default class RestorePassword extends React.Component {
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
            <Button label={"Submit"} />
          </div>
          <div className={ownStyles.cancel}>
            <Button label={"Cancel"} />
          </div>
        </div>
      </AuthPopUp>
    );
  }
}
