import React from "react";
import styles from "./SizeSelect.css";
import PropTypes from "prop-types";

export default class SizeAdd extends React.Component {
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
        <div className={classSelected}>{this.props.size}</div>
      </div>
    );
  }
}
SizeAdd.propTypes = {
  size: PropTypes.string,
  selected: PropTypes.bool
};
