import React from "react";
import styles from "./OrderPaymentMethod.css";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import each from "lodash.foreach";
export default class OrderPaymentMethod extends React.Component {
  request() {
    if (this.props.request) {
      this.props.request();
    }
  }

  render() {
    let isDelivered = false;
    each(this.props.statusDisplay, (status, i) => {
      each(status.value.statusList, statusNew => {
        if (statusNew.responseCode === "DELIVERED") {
          isDelivered = true;
        }
      });
    });
    return (
      <div className={styles.base}>
        <div className={styles.paymentHolder}>
          <div className={styles.paymentMethod}>Payment Method</div>
          {this.props.isInvoiceAvailable &&
            isDelivered && (
              <div className={styles.requestHolder}>
                <div
                  className={styles.requestWithUnderline}
                  onClick={() => this.request()}
                >
                  <UnderLinedButton
                    label={this.props.underlineButtonLabel}
                    color={this.props.underlineButtonColour}
                  />
                </div>
              </div>
            )}
        </div>
        <div className={styles.cashAndMobileHolder}>
          <div className={styles.cashText}>{this.props.paymentMethod}</div>
          {this.props.phoneNumber && (
            <div className={styles.mobileNumber}>
              {`Ph: +${this.props.phoneNumber}`}
            </div>
          )}
        </div>
      </div>
    );
  }
}
OrderPaymentMethod.propTypes = {
  underlineButtonLabel: PropTypes.string,
  underlineButtonColour: PropTypes.string,
  phoneNumber: PropTypes.string,
  request: PropTypes.func
};
OrderPaymentMethod.defaultProps = {
  underlineButtonLabel: "Request Invoice",
  underlineButtonColour: "#181818",
  isInvoiceAvailable: false
};
