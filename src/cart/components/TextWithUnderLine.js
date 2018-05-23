import React from "react";
import styles from "./TextWithUnderLine.css";
import SearchAndUpdate from "../../pdp/components/SearchAndUpdate";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";

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
    return (
      <div
        className={this.props.defaultPinCode ? styles.base : styles.noOffset}
      >
        {this.props.defaultPinCode && (
          <div className={styles.headingText}>{this.props.defaultPinCode}</div>
        )}
        {!this.props.defaultPinCode && (
          <SearchAndUpdate
            id="searchAndUpdateInput"
            focused={true}
            checkPinCodeAvailability={pincode =>
              this.checkPinCodeAvailability(pincode)
            }
            hasAutofocus={true}
            // labelText={this.props.labelText}
            labelText="Update"
          />
        )}

        {this.props.defaultPinCode && (
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
  defaultPinCode: PropTypes.string,
  onClick: PropTypes.func,
  buttonLabel: PropTypes.string,
  heading: PropTypes.string
};
