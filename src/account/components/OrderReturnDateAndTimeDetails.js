import React from "react";
import styles from "./OrderReturnDateAndTimeDetails.css";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
export default class OrderReturnDateAndTimeDetails extends React.Component {
  onCancel() {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        {this.props.date ||
          (this.props.time && (
            <div className={styles.dateAndTimeHolder}>
              {this.props.date && (
                <div className={styles.dateHolder}>
                  <div className={styles.date}>Date</div>
                  <div className={styles.dateInfo}>{this.props.date}</div>
                </div>
              )}
              {this.props.time && (
                <div className={styles.timeHolder}>
                  <div className={styles.time}>Time</div>
                  <div className={styles.timeInfo}>{this.props.time}</div>
                </div>
              )}
            </div>
          ))}
        {this.props.underlineButtonLabel && (
          <div className={styles.cancelHolder}>
            <div className={styles.cancel} onClick={() => this.onCancel()}>
              <UnderLinedButton
                label={this.props.underlineButtonLabel}
                color={this.props.underlineButtonColour}
              />
            </div>
          </div>
        )}
      </div>
    );
  }
}
OrderReturnDateAndTimeDetails.propTypes = {
  underlineButtonLabel: PropTypes.string,
  underlineButtonColour: PropTypes.string,
  time: PropTypes.string,
  date: PropTypes.string,
  onCancel: PropTypes.func
};
OrderReturnDateAndTimeDetails.defaultProps = {
  underlineButtonColour: "#9b9b9b"
};
