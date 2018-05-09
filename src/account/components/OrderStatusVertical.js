import React from "react";
import styles from "./OrderStatusVertical.css";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import PropTypes from "prop-types";
const APPROVED = "APPROVED";
const PROCESSING = "PROCESSING";
const CANCEL = "CANCEL";
const SHIPPING = "SHIPPING";
const DELIVERED = "DELIVERED";
const REFUND_INITIATED = "REFUND_INITIATED";
export default class OrderStatusVertical extends React.Component {
  handleMoreDetails(val) {
    if (this.props.showShippingDetails && val) {
      this.props.showShippingDetails(val);
    }
  }
  render() {
    console.log(this.props);
    const completedSteps = this.props.statusMessageList.map(val => {
      return val.key;
    });
    const approvedData = this.props.statusMessageList.find(val => {
      return val.key === APPROVED;
    });
    const processingData = this.props.statusMessageList.find(val => {
      return val.key === PROCESSING;
    });
    const shippingData = this.props.statusMessageList.find(val => {
      return val.key === SHIPPING;
    });

    const cancelledData = this.props.statusMessageList.find(val => {
      return val.key === CANCEL;
    });
    let approvedDate = "";
    let approvedTime = "";
    if (
      approvedData &&
      approvedData.value.statusList &&
      approvedData.value.statusList[0] &&
      approvedData.value.statusList[0].statusMessageList &&
      approvedData.value.statusList[0].statusMessageList[0]
    ) {
      approvedDate = approvedData.value.statusList[0].statusMessageList[0].date;
      approvedTime = approvedData.value.statusList[0].statusMessageList[0].time;
    }
    let processingDate = "";
    let processingTime = "";
    if (
      processingData &&
      processingData.value.statusList &&
      processingData.value.statusList[0] &&
      processingData.value.statusList[0].statusMessageList &&
      processingData.value.statusList[0].statusMessageList[0]
    ) {
      processingDate =
        processingData.value.statusList[0].statusMessageList[0].date;
      processingTime =
        processingData.value.statusList[0].statusMessageList[0].time;
    }
    let shippingDate = "";
    let shippingTime = "";
    let shippingList = null;
    let shippingResponseCode = "";
    let deliveredData = null;
    let deliveredDate = "";
    let deliveredTime = "";
    let isDelivered = false;
    if (
      shippingData &&
      shippingData.value.statusList &&
      shippingData.value.statusList[0] &&
      shippingData.value.statusList[0].statusMessageList &&
      shippingData.value.statusList[0].statusMessageList[0]
    ) {
      shippingDate = shippingData.value.statusList[0].statusMessageList[0].date;
      shippingTime = shippingData.value.statusList[0].statusMessageList[0].time;
      shippingList = shippingData.value.statusList[0].statusMessageList;
      shippingResponseCode = shippingData.value.statusList[0].responseCode;

      isDelivered = shippingData.value.statusList
        .map(val => {
          return val.responseCode;
        })
        .includes(DELIVERED);
      if (isDelivered) {
        deliveredData = shippingData.value.statusList.filter(val => {
          return val.responseCode === DELIVERED;
        });
        if (
          deliveredData[0].statusMessageList &&
          deliveredData[0].statusMessageList[0]
        ) {
          deliveredDate = deliveredData[0].statusMessageList[0].date;
          deliveredTime = deliveredData[0].statusMessageList[0].time;
          console.log(deliveredDate);
        }
      }
    }

    let cancelledDate = "";
    let cancelledTime = "";
    if (
      cancelledData &&
      cancelledData.value.statusList &&
      cancelledData.value.statusList[0] &&
      cancelledData.value.statusList[0].statusMessageList &&
      cancelledData.value.statusList[0].statusMessageList[0]
    ) {
      cancelledDate =
        cancelledData.value.statusList[0].statusMessageList[0].date;
      cancelledTime =
        cancelledData.value.statusList[0].statusMessageList[0].time;
    }
    const orderCode = this.props.orderCode;
    return (
      <div className={styles.base}>
        <div
          className={
            completedSteps.includes(APPROVED)
              ? styles.step
              : styles.stepInactive
          }
        >
          <div
            className={
              completedSteps.includes(APPROVED)
                ? styles.checkActive
                : styles.check
            }
          />
          <div className={styles.processNameHolder}>Approved</div>
          <div className={styles.dateAndTimeHolder}>
            <div className={styles.dateHolder}>{approvedDate}</div>
            <div className={styles.timeHolder}>{approvedTime}</div>
          </div>
        </div>
        <div
          className={
            completedSteps.includes(PROCESSING) ||
            completedSteps.includes(CANCEL)
              ? styles.step
              : styles.stepInactive
          }
        >
          <div
            className={
              completedSteps.includes(PROCESSING) ||
              completedSteps.includes(CANCEL)
                ? styles.checkActive
                : styles.check
            }
          />
          <div className={styles.processNameHolder}>Processing</div>
          <div className={styles.dateAndTimeHolder}>
            <div className={styles.dateHolder}>{processingDate}</div>
            <div className={styles.timeHolder}>{processingTime}</div>
          </div>
        </div>
        {shippingResponseCode !== REFUND_INITIATED && (
          <div
            className={
              completedSteps.includes(SHIPPING)
                ? styles.step
                : styles.stepInactive
            }
          >
            <div
              className={
                completedSteps.includes(SHIPPING)
                  ? styles.checkActive
                  : styles.check
              }
            />
            <div className={styles.processNameHolder}>Shipping</div>
            <div className={styles.dateAndTimeHolder}>
              <div className={styles.dateHolder}>{shippingDate}</div>
              <div className={styles.timeHolder}>{shippingTime}</div>
            </div>
            {completedSteps.includes(SHIPPING) && (
              <div>
                {this.props.logisticName && (
                  <div className={styles.courierInfoHolder}>
                    <div className={styles.moreInfoQuestionHolder}>
                      Courier: {this.props.logisticName}
                    </div>
                  </div>
                )}
                {this.props.trackingAWB && (
                  <div className={styles.courierInfoHolder}>
                    <div className={styles.moreInfoQuestionHolder}>
                      AWB No: {this.props.trackingAWB}
                    </div>
                  </div>
                )}
                <div className={styles.courierInfoHolder}>
                  <UnderLinedButton
                    label="More details"
                    onClick={() =>
                      this.handleMoreDetails({ shippingList, orderCode })
                    }
                  />
                </div>

                <div className={styles.moreAnswerHolder} />
              </div>
            )}
          </div>
        )}
        {shippingResponseCode !== REFUND_INITIATED && (
          <div className={isDelivered ? styles.step : styles.stepInactive}>
            <div className={isDelivered ? styles.checkActive : styles.check} />
            <div className={styles.processNameHolder}>Delivered</div>
            <div className={styles.dateAndTimeHolder}>
              <div className={styles.dateHolder}>{deliveredDate}</div>
              <div className={styles.timeHolder}>{deliveredTime}</div>
            </div>
          </div>
        )}
        {completedSteps.includes(CANCEL) && (
          <div className={styles.step}>
            <div className={styles.checkActive} />
            <div className={styles.processNameHolder}>Cancelled</div>
            <div className={styles.dateAndTimeHolder}>
              <div className={styles.dateHolder}>{cancelledDate}</div>
              <div className={styles.timeHolder}>{cancelledTime}</div>
            </div>
          </div>
        )}
      </div>
    );
  }
}
OrderStatusVertical.propTypes = {
  moreDetails: PropTypes.func,
  statusMessageList: PropTypes.arrayOf(
    PropTypes.shape({
      date: PropTypes.string,
      statusDescription: PropTypes.string,
      time: PropTypes.string
    })
  )
};
