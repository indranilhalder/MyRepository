import React from "react";
import PropTypes from "prop-types";
import styles from "./CheckBoxPoint.css";
export default class CheckBoxPoint extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    let classNameBase = styles.base;
    return (
      <div
        className={this.props.selected ? styles.selected : classNameBase}
        onClick={() => this.handleClick()}
      >
        <div className={styles.checkCicle}>
          <div className={styles.checkCircleInside} />
        </div>
        {this.props.label}
      </div>
    );
  }
}
CheckBoxPoint.propTypes = {
  selected: PropTypes.bool,
  selectItem: PropTypes.func,
  label: PropTypes.string
};
