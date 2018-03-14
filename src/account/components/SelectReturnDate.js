import React from "react";
import styles from "./SelectReturnDate.css";
import CheckBox from "../../general/components/CheckBox.js";
import PropTypes from "prop-types";
export default class SelectReturnDate extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    return (
      <div className={styles.base} onClick={() => this.handleClick()}>
        <div className={styles.checkBoxHolder}>
          <CheckBox selected={this.props.selected} />
        </div>
        {this.props.label}
      </div>
    );
  }
}
SelectReturnDate.PropTypes = {
  selectItem: PropTypes.func,
  label: PropTypes.string,
  selected: PropTypes.bool
};
