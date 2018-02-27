import React from "react";
import PropTypes from "prop-types";
import styles from "./ColorCheckbox.css";
export default class ColorCheckbox extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    let classNameBase = styles.base;
    let classCheckbox = styles.checkCicle;
    let classActiveCheck = styles.checkCircleInside;
    if (this.props.selected) {
      classNameBase = styles.activeText;
      classCheckbox = styles.activeBorder;
      classActiveCheck = styles.checkCircleActive;
    }
    return (
      <div className={classNameBase} onClick={() => this.handleClick()}>
        <div className={classCheckbox}>
          <div className={classActiveCheck} />
        </div>
        {this.props.label}
      </div>
    );
  }
}
ColorCheckbox.propTypes = {
  selected: PropTypes.bool,
  selectItem: PropTypes.func
};
