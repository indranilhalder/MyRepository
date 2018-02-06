import React from "react";
import { Input } from "xelpmoc-core";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import styles from "./PinCodeUpdate.css";
export default class PinCodeUpdate extends React.Component {
  handleChange() {
    if (this.props.onChange) {
      this.props.onChange();
    }
  }
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.inputHolder}>
          <Input
            onChange={() => {
              this.handleChange();
            }}
          />
        </div>
        <div className={styles.buttonHolder}>
          <UnderLinedButton
            label="Updates"
            onClick={() => {
              this.handleClick();
            }}
          />
        </div>
      </div>
    );
  }
}

PinCodeUpdate.propTypes = {
  onChange: PropTypes.func,
  onClick: PropTypes.func
};
