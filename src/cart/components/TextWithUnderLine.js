import React from "react";
import styles from "./TextWithUnderLine.css";

import SearchAndUpdate from "../../pdp/components/SearchAndUpdate";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import { DEFAULT_PIN_CODE_LOCAL_STORAGE } from "../../lib/constants";
export default class TextWithUnderLine extends React.Component {
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
