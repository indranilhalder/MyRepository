import React from "react";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import gpsIcon from "../../general/components/img/GPS.svg";
import Icon from "../../xelpmoc-core/Icon";
import CircleButton from "../../xelpmoc-core/CircleButton";
import styles from "./SearchLocationByPincode.css";
import { DEFAULT_PIN_CODE_LOCAL_STORAGE } from "../../lib/constants";
export default class SearchLocationByPincode extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      pinCode: localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
        ? localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
        : null,
      errorMessage: null
    };
  }
  getValue(pinCode) {
    if (pinCode.length <= 6) {
      this.setState({ pinCode });
      if (pinCode.length === 6) {
        this.onUpdate(pinCode);
      }
    }
  }

  onUpdate(pinCode) {
    if (pinCode && pinCode.match(/^\d{6}$/)) {
      if (this.props.changePincode) {
        this.props.changePincode(pinCode);
      }
      this.setState({ errorMessage: null });
    } else {
      this.setState({ errorMessage: "Please enter a  valid pincode" });
    }
  }
  render() {
    return (
      <div className={styles.base}>
        {this.props.header && (
          <div className={styles.header}>{this.props.header}</div>
        )}
        {this.state.errorMessage && (
          <div className={styles.errorMessage}>{this.state.errorMessage}</div>
        )}
        <div
          className={
            this.props.disabled ? styles.disabledInput : styles.inputHolder
          }
        >
          <Input2
            placeholder={
              this.state.pincode
                ? `Your pincode :${this.state.pincode}`
                : "Enter your pincode"
            }
            type="number"
            value={this.state.pinCode ? this.state.pinCode : ""}
            boxy={true}
            onChange={val => this.getValue(val)}
            textStyle={{ fontSize: 14 }}
            height={35}
            disabled={this.props.disabled}
            rightChildSize={35}
          />
        </div>
      </div>
    );
  }
}
SearchLocationByPincode.propTypes = {
  header: PropTypes.string,
  pincode: PropTypes.string,
  getLocation: PropTypes.func,
  changePincode: PropTypes.func,
  disabled: PropTypes.bool
};
SearchLocationByPincode.defaultProps = {
  disabled: false
};
