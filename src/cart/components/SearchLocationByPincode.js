import React from "react";
import PropTypes from "prop-types";
import Input2 from "../../general/components/Input2.js";
import gpsIcon from "../../general/components/img/GPS.svg";
import { Icon, CircleButton } from "xelpmoc-core";
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
    }
  }

  onUpdate() {
    if (this.state.pinCode && this.state.pinCode.match(/^\d{6}$/)) {
      if (this.props.checkPinCodeAvailability) {
        this.props.changePincode(this.state.pinCode);
      }
      this.setState({ errorMessage: null });
    } else {
      this.setState({ errorMessage: "Please enter a  valid pincode" });
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.header}>{this.props.header}</div>
        {this.state.errorMessage && (
          <div className={styles.errorMessage}>{this.state.errorMessage}</div>
        )}
        <div className={styles.inputHolder}>
          <Input2
            placeholder={`Your pincode :${this.state.pincode}`}
            type="number"
            value={this.state.pinCode && this.state.pinCode}
            boxy={true}
            onChange={val => this.getValue(val)}
            textStyle={{ fontSize: 14 }}
            height={35}
            rightChildSize={35}
            rightChild={
              <CircleButton
                size={35}
                color={"transparent"}
                icon={<Icon image={gpsIcon} size={20} />}
                onClick={() => this.onUpdate()}
              />
            }
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
  changePincode: PropTypes.func
};
