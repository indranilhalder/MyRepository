import React from "react";
import styles from "./SelectBoxWithInput.css";
import PropTypes from "prop-types";
import Input2 from "./Input2.js";
import SelectBoxMobile from "./SelectBoxMobile.js";

export default class SelectBoxWithInput extends React.Component {
  getTitleValue(val) {
    if (this.props.onChange) {
      this.props.onChange();
    }
  }
  getFullNameValue(val) {
    if (this.props.getFullNameValue) {
      this.props.getFullNameValue();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.dropDownHolder}>
          <div className={styles.dropDownBox}>
            <SelectBoxMobile
              theme="hollowBox"
              options={this.props.option}
              selected={this.props.titleValue}
              onChange={val => this.getTitleValue(val)}
            />
          </div>
          <div className={styles.fullNameBox}>
            <Input2
              boxy={true}
              placeholder="Full name*"
              onChange={val => this.getFullNameValue(val)}
              textStyle={{ fontSize: 14 }}
              height={33}
              value={this.props.fullNameValue && this.props.fullNameValue}
            />
          </div>
        </div>
      </div>
    );
  }
}
SelectBoxWithInput.propTypes = {
  value: PropTypes.string,
  heading: PropTypes.string,
  theme: PropTypes.string,
  onClick: PropTypes.func,
  height: PropTypes.string,
  placeholder: PropTypes.string
};
