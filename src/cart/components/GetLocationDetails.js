import React from "react";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import Image from "../../xelpmoc-core/Image";
import checkIcon from "./img/check.svg";
import styles from "./GetLocationDetails.css";
const integerDayMapping = ["Sat", "Sun", "Mon", "Tue", "Wed", "Thur", "Fri"];
export default class GetLocationDetails extends React.Component {
  handleClick() {
    if (this.props.changeLocation) {
      this.props.changeLocation();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.address}>
          <div className={styles.headerTextHolder}>
            {this.props.headingText}
          </div>
          <div className={styles.subText}>{this.props.address}</div>
          <div className={styles.buttonHolder}>
            <UnderLinedButton
              size="14px"
              fontFamily="regular"
              color="#000"
              label="Change"
              onClick={() => this.handleClick()}
            />
          </div>
          <div className={styles.checkIconHolder}>
            <Image image={checkIcon} fit="cover" />
          </div>
        </div>
        <div className={styles.pickUpBox}>
          <div className={styles.pickUpText}>{this.props.pickUpKey}</div>
          <div className={styles.pickUpDay}>
            {" "}
            {this.props.workingDays === "7"
              ? "all days"
              : this.props.workingDays.split("").map(val => {
                  if (val !== ",") {
                    return integerDayMapping[parseInt(val)];
                  } else {
                    return ", ";
                  }
                })}
          </div>
        </div>
      </div>
    );
  }
}
GetLocationDetails.propTypes = {
  headingText: PropTypes.string,
  address: PropTypes.string,
  pickUpKey: PropTypes.string,
  pickUpValue: PropTypes.string,
  changeLocation: PropTypes.func
};
