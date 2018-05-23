import React from "react";
import styles from "./CodForm.css";
import PropTypes from "prop-types";
import Captcha from "../../general/components/Captcha";
import Button from "../../general/components/Button";

const CASH_ON_DELIVERY = "COD";

export default class CodForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selected: false,
      captcha: null
    };
  }

  binValidationForCOD(paymentMode) {
    if (this.props.binValidationForCOD) {
      this.props.binValidationForCOD(paymentMode);
    }
  }

  verifyCallback(response) {
    if (response) {
      this.setState({ captcha: response });
      if (this.props.verifyCaptcha) {
        this.props.verifyCaptcha(response);
      }
    }
  }

  render() {
    return (
      <div className={styles.base}>
        <div className={styles.codText}>
          Pay with Cash when your order is delivered
        </div>
        <div className={styles.captcha}>
          <Captcha
            binValidationForCOD={paymentMode =>
              this.binValidationForCOD(paymentMode)
            }
            verifyCallback={response => {
              this.verifyCallback(response);
            }}
          />
        </div>
      </div>
    );
  }
}

CodForm.propTypes = {
  binValidationForCOD: PropTypes.func,
  softReservationForCODPayment: PropTypes.func
};
