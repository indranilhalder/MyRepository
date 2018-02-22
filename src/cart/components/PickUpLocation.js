import React from "react";
import styles from "./PickUpLocation.css";
import PropTypes from "prop-types";
import Button from "../../general/components/Button";
export default class PickUpLocation extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.holder}>
          {this.props.headingText && (
            <div className={styles.headingText}>{this.props.headingText}</div>
          )}
          {this.props.iconText && (
            <div className={styles.textIcon}>{this.props.iconText}</div>
          )}
          {this.props.address && (
            <div className={styles.addressText}>{this.props.address}</div>
          )}
          {this.props.PickUpKey && (
            <div className={styles.pickUpBox}>
              <div className={styles.pickUpText}>{this.props.PickUpKey}</div>
              <div className={styles.pickUpDay}>{this.props.PickUpValue}</div>
            </div>
          )}
        </div>
        <div className={styles.buttonHolder}>
          <div
            className={styles.buttonContainer}
            onClick={() => this.handleClick()}
          >
            <Button
              type="primary"
              color="#fff"
              label={this.props.buttonText}
              width={121}
            />
          </div>
        </div>
      </div>
    );
  }
}

PickUpLocation.propTypes = {
  headingText: PropTypes.string,
  address: PropTypes.string,
  pickUpKey: PropTypes.string,
  pickUpValue: PropTypes.string,
  iconText: PropTypes.string,
  onClick: PropTypes.func,
  buttonText: PropTypes.string
};
