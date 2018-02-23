import React from "react";
import CheckBox from "./CheckBox.js";
import styles from "./CheckboxAndText.css";
import PropTypes from "prop-types";
export default class CheckboxAndText extends React.Component {
  handleClick() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    return (
      <div className={styles.base} onClick={() => this.handleClick()}>
        <div className={styles.checkboxHolder}>
          <CheckBox selected={this.props.selected} />
        </div>
        {this.props.label}
      </div>
    );
  }
}
CheckboxAndText.propTypes = {
  selectItem: PropTypes.func,
  label: PropTypes.string
};
