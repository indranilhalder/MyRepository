import React from "react";
import styles from "./SizeSelect.css";
import PropTypes from "prop-types";

export default class SizeSelect extends React.Component {
  handleClick() {
    if (!this.props.disabled && this.props.onSelect) {
      this.props.onSelect();
    }
  }
  render() {
    let className = styles.base;
    if (this.props.disabled) {
      className = styles.disabled;
    } else if (this.props.selected) {
      className = styles.baseActive;
    }
    return (
      <div className={className} onClick={() => this.handleClick()}>
        <div
          className={this.props.selected ? styles.selected : styles.textHolder}
          style={{ fontSize: this.props.fontSize }}
        >
          {this.props.size}
        </div>
      </div>
    );
  }
}
SizeSelect.propTypes = {
  size: PropTypes.string,
  selected: PropTypes.bool,
  disabled: PropTypes.bool,
  fontSize: PropTypes.oneOfType([PropTypes.string, PropTypes.number])
};
SizeSelect.defaultProps = {
  fontSize: 14,
  disabled: false
};
