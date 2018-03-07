import React from "react";
import styles from "./OrderPaymentMethod.css";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
export default class OrderPaymentMethod extends React.Component {
  request() {
    if (this.props.request) {
      this.props.request();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.paymentHolder}>
          <div className={styles.paymentMethod}>Payment Method</div>
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
        </div>
        <div className={styles.cashAndMobileHolder}>
          <div className={styles.cashText}>Cash on delivery</div>
          {this.props.phoneNumber && (
            <div className={styles.mobileNumber}>
              {`Ph: +91 ${this.props.phoneNumber}`}
            </div>
          )}
        </div>
      </div>
    );
  }
}
OrderPaymentMethod.propTypes = {
  DeliveryCharges: PropTypes.string,
  Discount: PropTypes.string,
  Total: PropTypes.string,
  ConvenienceCharges: PropTypes.string,
  SubTotal: PropTypes.string
};
OrderPaymentMethod.defaultProps = {
  underlineButtonLabel: "Request Invoice",
  underlineButtonColour: "#979797"
};
