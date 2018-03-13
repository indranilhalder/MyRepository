import React from "react";
import styles from "./OrderReturnAddressDetails.css";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
export default class OrderReturnAddressDetails extends React.Component {
  onCancel() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.home}>{this.props.addressType}</div>
        <div className={styles.addressDetails}>{this.props.address}</div>
        <div className={styles.address}>{this.props.subAddress}</div>
        <div className={styles.dateAndTimeHolder}>
          <div className={styles.dateHolder}>
            <div className={styles.date}>Date</div>
            <div className={styles.dateInfo}>{this.props.date}</div>
          </div>
          <div className={styles.timeHolder}>
            <div className={styles.time}>Time</div>
            <div className={styles.timeInfo}>{this.props.time}</div>
          </div>
        </div>
        <div className={styles.cancelHolder}>
          <div className={styles.cancel} onClick={() => this.onCancel()}>
            <UnderLinedButton
              label={this.props.underlineButtonLabel}
              color={this.props.underlineButtonColour}
            />
          </div>
        </div>
      </div>
    );
  }
}
OrderReturnAddressDetails.propTypes = {
  address: PropTypes.string,
  underlineButtonLabel: PropTypes.string,
  underlineButtonColour: PropTypes.string,
  time: PropTypes.string,
  date: PropTypes.string,
  subAddress: PropTypes.string,
  addressType: PropTypes.string,
  onCancel: PropTypes.func
};
OrderReturnAddressDetails.defaultProps = {
  underlineButtonLabel: "Change",
  underlineButtonColour: "#9b9b9b"
};
