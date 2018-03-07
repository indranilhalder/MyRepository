import React from "react";
import styles from "./PickUpLocation.css";
import PropTypes from "prop-types";
import Button from "../../general/components/Button";
const integerDayMapping = [
  "Saturday",
  "Sunday",
  "Monday",
  "Tuesday",
  "Wednesday",
  "Thursday",
  "Friday"
];
export default class PickUpLocation extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }

  render() {
    console.log(Math.floor((this.props.workingDays.length - 1) / 2));
    console.log(
      this.props.workingDays.split(",")[
        Math.floor((this.props.workingDays.length - 1) / 2)
      ]
    );
    console.log(this.props.workingDays.split(",")[5]);
    console.log(this.props.workingDays.split(",")[0]);
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
          {this.props.address2 && (
            <div className={styles.addressText}>{this.props.address2}</div>
          )}
          {this.props.PickUpKey && (
            <div className={styles.pickUpBox}>
              <div className={styles.pickUpDay}>
                <span className={styles.pickUpText}>
                  {this.props.PickUpKey}
                </span>
                {this.props.openingTime} to {this.props.closingTime}
                Open{" "}
                {this.props.workingDays === "7"
                  ? "all days"
                  : `${
                      integerDayMapping[
                        parseInt(this.props.workingDays.split(",")[0])
                      ]
                    } to ${
                      integerDayMapping[
                        parseInt(
                          this.props.workingDays.split(",")[
                            (this.props.workingDays.length + 1) / 2
                          ]
                        )
                      ]
                    }`}
              </div>
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
  closingTime: PropTypes.string,
  openingTime: PropTypes.string,
  workingDays: PropTypes.string,
  iconText: PropTypes.string,
  onClick: PropTypes.func,
  buttonText: PropTypes.string
};

// mplClosingTime: "22:00",
// mplOpeningTime: "10:00",
// mplWorkingDays: "1,2,3,4,5,6",
