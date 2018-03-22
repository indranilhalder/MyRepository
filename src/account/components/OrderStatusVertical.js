import React from "react";
import styles from "./OrderStatusVertical.css";
import UnderLinedButton from "../../general/components/UnderLinedButton.js";
import PropTypes from "prop-types";
export default class OrderStatusVertical extends React.Component {
  handleClick() {
    if (this.props.moreDetails) {
      this.props.moreDetails();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        {this.props.statusMessageList &&
          this.props.statusMessageList.map((datum, i) => {
            return (
              <div className={styles.deliveredStepHolder}>
                <div
                  className={
                    datum.statusDescription === "Order Failed"
                      ? styles.processFail
                      : styles.processDone
                  }
                />
                <div className={styles.deliveredProcesesAndTime}>
                  <div className={styles.processNameHolder}>
                    {datum.statusDescription}
                  </div>
                  <div className={styles.dateAndTimeHolder}>
                    <div className={styles.dateHolder}>{datum.date}</div>
                    <div className={styles.timeHolder}>{datum.time} </div>
                  </div>
                </div>
                {datum.statusDescription === "Shipping" && (
                  <div className={styles.dummyHolder}>
                    {datum.courierProcess && (
                      <div className={styles.courierInfoHolder}>
                        <div className={styles.moreInfoQuestionHolder}>
                          {" "}
                          Courier:
                        </div>
                        <div className={styles.moreAnswerHolder}>
                          {datum.courierProcess}
                        </div>
                      </div>
                    )}
                    {datum.awbNo && (
                      <div className={styles.courierInfoHolder}>
                        <div className={styles.moreInfoQuestionHolder}>
                          AWB No.
                        </div>
                        <div className={styles.moreAnswerHolder}>
                          {datum.awbNo}
                        </div>
                      </div>
                    )}
                    <div className={styles.buttonHolder}>
                      <div className={styles.button}>
                        <UnderLinedButton
                          size="14px"
                          fontFamily="regular"
                          color="#000000"
                          label="More details"
                          onClick={() => this.handleClick()}
                        />
                      </div>
                    </div>
                  </div>
                )}
              </div>
            );
          })}
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
