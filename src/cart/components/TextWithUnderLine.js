import React from "react";
import styles from "./TextWithUnderLine.css";

import SearchAndUpdate from "../../pdp/components/SearchAndUpdate";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import { DEFAULT_PIN_CODE_LOCAL_STORAGE } from "../../lib/constants";
export default class TextWithUnderLine extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      borderColor: "#d2d2d2",
      borderBottom: "1px solid #d2d2d2"
    };
  }
  onClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  checkPinCodeAvailability(pincode) {
    if (this.props.checkPinCodeAvailability) {
      this.props.checkPinCodeAvailability(pincode);
    }
  }
  addBorder(x) {
    this.setState({ borderColor: "red", borderBottom: "1px solid red" });
  }
  removeBorder(x) {
    this.setState({
      borderColor: "#d2d2d2",
      borderBottom: "1px solid #d2d2d2"
    });
  }
  render() {
    const defaultPinCode =
      localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE) &&
      localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE) !== "undefined"
        ? localStorage.getItem(DEFAULT_PIN_CODE_LOCAL_STORAGE)
        : null;

    return (
      <div className={defaultPinCode ? styles.base : styles.noOffset}>
        {defaultPinCode && (
          <div className={styles.headingText}>{defaultPinCode}</div>
        )}
        {!defaultPinCode && (
          <SearchAndUpdate
            id="searchAndUpdateInput"
            focused={true}
            checkPinCodeAvailability={pincode =>
              this.checkPinCodeAvailability(pincode)
            }
            labelText="Update"
            onFocus={() => this.addBorder(this)}
            onBlur={() => this.removeBorder(this)}
            borderColor={this.state.borderColor}
            borderBottom={this.state.borderBottom}
          />
        )}

        {defaultPinCode && (
          <React.Fragment>
            <div className={styles.button}>
              <UnderLinedButton label={this.props.buttonLabel} />
            </div>
            <div
              className={styles.defaultClickArea}
              onClick={() => this.onClick()}
            />
          </React.Fragment>
        )}
      </div>
    );
  }
}
TextWithUnderLine.propTypes = {
  onClick: PropTypes.func,
  buttonLabel: PropTypes.string,
  heading: PropTypes.string
};
