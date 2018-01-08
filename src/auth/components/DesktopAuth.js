import React from "react";
import Login from "./Login";
import Signup from "./SignUp";
import SocialButtons from "./SocialButtons";
import styles from "./DesktopAuthStyles.css";
export default class DesktopAuth extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.left}>
          <div className={styles.authHolder}>
            <h2 className={styles.header}>Welcome back</h2>
            <Login />
          </div>
        </div>
        <div className={styles.right}>
          <div className={styles.authHolder}>
            <h2 className={styles.header}>New to Cliq</h2>
            <Signup />
          </div>
        </div>
        <div className={styles.socialButtonHolder}>
          <SocialButtons />
        </div>
      </div>
    );
  }
}
