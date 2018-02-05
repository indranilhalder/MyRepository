import React from "react";
import styles from "./CheckBox.css";
export default class CheckBox extends React.Component {
  render() {
    let className = styles.base;
    if (this.props.selected) {
      className = styles.selected;
    }
    return <div className={className} />;
  }
}
