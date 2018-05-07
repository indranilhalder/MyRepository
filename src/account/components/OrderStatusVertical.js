import React from "react";
import styles from "./OrderStatusVertical.css";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import PropTypes from "prop-types";
const APPROVED = "APPROVED";
const PROCESSING = "PROCESSING";
const SHIPPING = "SHIPPING";
const DELIVERED = "DELIVERED";
export default class OrderStatusVertical extends React.Component {
  handleClick() {
    if (this.props.moreDetails) {
      this.props.moreDetails();
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
    const deliveredData = this.props.statusMessageList.find(val => {
      return val.key === DELIVERED;
    });
    console.log(approvedData.value.statusList[0].statusMessageList[0].date);
    let approvedDate = "";
    let approvedTime = "";
    if (
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
    if (
      shippingData.value.statusList &&
      shippingData.value.statusList[0] &&
      shippingData.value.statusList[0].statusMessageList &&
      shippingData.value.statusList[0].statusMessageList[0]
    ) {
      shippingDate = approvedData.value.statusList[0].statusMessageList[0].date;
      shippingTime = approvedData.value.statusList[0].statusMessageList[0].time;
    }
    let deliveredDate = "";
    let deliveredTime = "";
    if (
      deliveredData.value.statusList &&
      deliveredData.value.statusList[0] &&
      deliveredData.value.statusList[0].statusMessageList &&
      deliveredData.value.statusList[0].statusMessageList[0]
    ) {
      deliveredDate =
        deliveredData.value.statusList[0].statusMessageList[0].date;
      deliveredTime =
        deliveredData.value.statusList[0].statusMessageList[0].time;
    }
    console.log(deliveredDate);
    // console.log(completedSteps);
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
            <div className={styles.dateHolder}>12/09/18</div>
            <div className={styles.timeHolder}>19:00 Hrs</div>
          </div>
        </div>
        <div
          className={
            completedSteps.includes(PROCESSING)
              ? styles.step
              : styles.stepInactive
          }
        >
          <div
            className={
              completedSteps.includes(PROCESSING)
                ? styles.checkActive
                : styles.check
            }
          />
          <div className={styles.processNameHolder}>Processing</div>
          <div className={styles.dateAndTimeHolder}>
            <div className={styles.dateHolder}>12/09/18</div>
            <div className={styles.timeHolder}>19:00 Hrs</div>
          </div>
        </div>
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
            <div className={styles.dateHolder}>12/09/18</div>
            <div className={styles.timeHolder}>19:00 Hrs</div>
          </div>
          <div className={styles.courierInfoHolder}>
            <div className={styles.moreInfoQuestionHolder}> Courier:</div>
            <div className={styles.moreAnswerHolder}>Courier process</div>
          </div>
        </div>
        <div
          className={
            completedSteps.includes(DELIVERED)
              ? styles.step
              : styles.stepInactive
          }
        >
          <div
            className={
              completedSteps.includes(DELIVERED)
                ? styles.checkActive
                : styles.check
            }
          />
          <div className={styles.processNameHolder}>Delivered</div>
          <div className={styles.dateAndTimeHolder}>
            <div className={styles.dateHolder}>12/09/18</div>
            <div className={styles.timeHolder}>19:00 Hrs</div>
          </div>
        </div>
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
// {this.props.statusMessageList &&
//   this.props.statusMessageList.map((datum, i) => {
//     console.log(datum);
//     return (
//       <div className={styles.deliveredStepHolder}>
//         <div
//           className={
//             datum.statusDescription === "Order Failed"
//               ? styles.processFail
//               : styles.processDone
//           }
//         />
//         <div className={styles.deliveredProcesesAndTime}>
//           <div className={styles.processNameHolder}>
//             {datum.statusDescription}
//           </div>
//           <div className={styles.dateAndTimeHolder}>
//             <div className={styles.dateHolder}>{datum.date}</div>
//             <div className={styles.timeHolder}>{datum.time} </div>
//           </div>
//         </div>
//         {datum.statusDescription === "Shipping" && (
//           <div className={styles.dummyHolder}>
//             {datum.courierProcess && (
//               <div className={styles.courierInfoHolder}>
//                 <div className={styles.moreInfoQuestionHolder}>
//                   {" "}
//                   Courier:
//                 </div>
//                 <div className={styles.moreAnswerHolder}>
//                   {datum.courierProcess}
//                 </div>
//               </div>
//             )}
//             {datum.awbNo && (
//               <div className={styles.courierInfoHolder}>
//                 <div className={styles.moreInfoQuestionHolder}>
//                   AWB No.
//                 </div>
//                 <div className={styles.moreAnswerHolder}>
//                   {datum.awbNo}
//                 </div>
//               </div>
//             )}
//             <div className={styles.buttonHolder}>
//               <div className={styles.button}>
//                 <UnderLinedButton
//                   size="14px"
//                   fontFamily="regular"
//                   color="#000000"
//                   label="More details"
//                   onClick={() => this.handleClick()}
//                 />
//               </div>
//             </div>
//           </div>
//         )}
//       </div>
//     );
//   })}
