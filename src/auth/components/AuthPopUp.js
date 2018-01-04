import React from "react";
import { default as styles } from "./AuthPopUp.css";
export default class LoginPopUp extends React.Component {
  render() {
    return <div className={styles.base}>{this.props.children}</div>;
  }
}
