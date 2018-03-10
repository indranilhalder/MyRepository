import React from "react";
import styles from "./MobileMonthPicker.css";
import SelectBoxMobile from "./SelectBoxMobile.js";
import PropTypes from "prop-types";
export default class MobileMonthPicker extends React.Component {
  getTitleValue(val) {
    if (this.props.onChange) {
      this.props.onChange(val);
    }
  }
  render() {
    const monthOption = [
      { value: "01" },
      { value: "02" },
      { value: "03" },
      { value: "04" },
      { value: "05" },
      { value: "06" },
      { value: "07" },
      { value: "08" },
      { value: "09" },
      { value: "10" },
      { value: "11" },
      { value: "12" }
    ];
    return (
      <div className={styles.base}>
        <SelectBoxMobile
          value="Expiry month"
          onChange={val => this.getTitleValue(val)}
          options={monthOption}
        />
      </div>
    );
  }
}
MobileMonthPicker.propTypes = {
  onChange: PropTypes.func
};
