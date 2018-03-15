import React from "react";
import styles from "./CodForm.css";
import PropTypes from "prop-types";
import Captcha from "../../general/components/Captcha";
const PAYMENT_MODE = "COD";

export default class CodForm extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      selected: false
    };
  }

  componentDidMount() {
    if (this.props.binValidationForCOD) {
      this.props.binValidationForCOD(PAYMENT_MODE);
    }
  }

  softReservationForCODPayment() {
    if (this.props.softReservationForCODPayment) {
      this.props.softReservationForCODPayment();
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
            softReservationForCODPayment={() =>
              this.softReservationForCODPayment()
            }
          />
        </div>
      </div>
    );
  }
}
