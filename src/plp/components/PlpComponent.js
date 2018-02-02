import React from "react";
import styles from "./PlpComponent.css";
export default class PlpComponent extends React.Component {
  render() {
    let className = styles.base;
    if (this.props.type === "plpAd" || this.props.type === "iconicFilter") {
      className = styles.banner;
    }
    return <div className={className}>{this.props.children}</div>;
  }
}
