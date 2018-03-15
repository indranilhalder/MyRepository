import React from "react";
import PropTypes from "prop-types";
import styles from "./BrandsItem.css";
export default class BrandsItem extends React.Component {
  onSelect() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    let className = styles.base;
    if (this.props.selected) {
      className = styles.active;
    }
    return (
      <div className={className} onClick={() => this.onSelect()}>
        {this.props.label}
      </div>
    );
  }
}
