import React from "react";
import styles from "./OrderStatusHorizontal.css";
const RETURN_INITIATED = "RETURN_INITIATED";
const RETURN_CLOSED = "RETURN_CLOSED";
const REFUND_INITIATED = "REFUND_INITIATED";
const RETURN_COMPLETED = "RETURN_COMPLETED";
export default class OrderStatusHorizontal extends React.Component {
  render() {
    let returnInitiated = false;
    let returnClosed = false;
    let refundInitiated = false;
    let refundCompleted = false;
    let date = "";
    let time = "";
    let message = "";
    if (
      this.props.statusMessageList &&
      this.props.statusMessageList[0] &&
      this.props.statusMessageList[0].value &&
      this.props.statusMessageList[0].value.statusList
    ) {
      const codeArray = this.props.statusMessageList[0].value.statusList.map(
        val => {
          return val.responseCode;
        }
      );
      const filteredData = this.props.statusMessageList[0].value.statusList.filter(
        val => {
          return val.currentFlag;
        }
      );
      if (filteredData && filteredData[0]) {
        const data = filteredData[0].statusMessageList;
        if (data && data[0]) {
          date = data[0].date;
          time = data[0].time;
          message = data[0].statusDescription;
        }
        returnInitiated = codeArray.includes(RETURN_INITIATED);
        returnClosed = codeArray.includes(RETURN_CLOSED);
        refundInitiated = codeArray.includes(REFUND_INITIATED);
        refundCompleted = codeArray.includes(RETURN_COMPLETED);
      }
    }
    return (
      <div className={styles.base}>
        <div className={styles.status}>
          {message} on {date} {time}
        </div>
        {this.props.courier &&
          this.props.courier !== null &&
          this.props.courier !== "null" && (
            <div className={styles.row}>Courier: {this.props.courier}</div>
          )}
        {this.props.trackingAWB &&
          this.props.trackingAWB !== null &&
          this.props.trackingAWB !== "null" && (
            <div className={styles.row}>AWB No: {this.props.trackingAWB}</div>
          )}

        <div className={returnInitiated ? styles.step : styles.stepInactive}>
          <div
            className={returnInitiated ? styles.checkActive : styles.check}
          />
          {returnInitiated && <span>Return initiated</span>}
        </div>
        <div className={returnClosed ? styles.step : styles.stepInactive}>
          <div className={returnClosed ? styles.checkActive : styles.check} />
          {returnClosed && <span>Return closed</span>}
        </div>
        <div className={refundInitiated ? styles.step : styles.stepInactive}>
          <div
            className={refundInitiated ? styles.checkActive : styles.check}
          />
          {refundInitiated && <span>Refund initiated</span>}
        </div>
        <div className={refundCompleted ? styles.step : styles.stepInactive}>
          <div
            className={refundCompleted ? styles.checkActive : styles.check}
          />
          {refundCompleted && <span>Refund completed</span>}
        </div>
      </div>
    );
  }
}
