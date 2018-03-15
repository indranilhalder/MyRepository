import React from "react";
import styles from "./CodForm.css";
import PropTypes from "prop-types";
import Captcha from "../../general/components/Captcha";
const CASH_ON_DELIVERY = "COD";

export default class CodForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selected: false
    };
  }

  binValidationForCOD(paymentMode) {
    if (this.props.binValidationForCOD) {
      this.props.binValidationForCOD(paymentMode);
    }
  }

  render() {
    return (
      <div className={styles.base}>
        <div className={styles.codText}>
          Pay with Cash or Card when your order is delivered
        </div>
        <div className={styles.captcha}>
          <Captcha
            binValidationForCOD={paymentMode =>
              this.binValidationForCOD(paymentMode)
            }
          />
        </div>
      </div>
    );
  }
}
