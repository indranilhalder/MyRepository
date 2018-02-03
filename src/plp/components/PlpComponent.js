import React from "react";
import styles from "./PlpComponent.css";
import { PLPAD, ICONICFILTER } from "./ProductGrid";
export default class PlpComponent extends React.Component {
  render() {
    let className = styles.base;
    if (this.props.type === PLPAD || this.props.type === ICONICFILTER) {
      className = styles.banner;
    }
    return <div className={className}>{this.props.children}</div>;
  }
}
