import React from "react";
import styles from "./TabHolder.css";
export default class TabHolder extends React.Component {
  render() {
    return <div className={styles.base}>{this.props.children}</div>;
  }
}
