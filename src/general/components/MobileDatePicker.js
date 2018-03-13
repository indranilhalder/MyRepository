import React from "react";
import PropTypes from "prop-types";
import Input2 from "./Input2.js";
import styles from "./MobileDatePicker.css";
import dateIcon from "./img/date.png";
import { Icon, CircleButton } from "xelpmoc-core";
export default class MobileDatePicker extends React.Component {
  getValue(val) {
    if (this.props.getValue) {
      this.props.getValue(val);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <Input2
          boxy={true}
          type="date"
          onChange={val => this.getValue(val)}
          textStyle={{ fontSize: 14 }}
          height={35}
          rightChildSize={35}
          borderBottom="1px solid #d2d2d2"
          rightChild={
            <CircleButton
              size={33}
              color={"transparent"}
              icon={<Icon image={dateIcon} size={20} />}
            />
          }
        />
      </div>
    );
  }
}
MobileDatePicker.propTypes = {
  getValue: PropTypes.func
};
