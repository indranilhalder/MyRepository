import React from "react";
import styles from "./SizeSelect.css";
import PropTypes from "prop-types";

export default class SizeSelect extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    let classSelected = styles.textHolder;
    let classActive = styles.base;
    if (this.props.selected) {
      classSelected = styles.selected;
      classActive = styles.baseActive;
    }
    return (
      <div className={classActive} onClick={() => this.handleClick()}>
        <div
          className={classSelected}
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
  fontSize: PropTypes.oneOfType([PropTypes.string, PropTypes.string])
};
SizeSelect.defaultProps = {
  fontSize: 14
};
