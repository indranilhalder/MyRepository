import React from "react";
import PropTypes from "prop-types";
import styles from "./TabData.css";
export default class TabData extends React.Component {
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
      <div
        className={className}
        onClick={() => this.onSelect()}
        style={{ width: `${this.props.width}` }}
      >
        {this.props.label}
      </div>
    );
  }
}
TabData.propTypes = {
  width: PropTypes.string,
  label: PropTypes.string,
  selectItem: PropTypes.func
};
TabData.defaultProps = {
  width: "auto"
};
