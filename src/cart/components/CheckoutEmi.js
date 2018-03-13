import React from "react";
import emiIcon from "./img/emi.svg";
import PropTypes from "prop-types";
import EmiAccordian from "./EmiAccordian";
import ManueDetails from "../../general/components/MenuDetails.js";
import { SUCCESS } from "../../lib/constants";
import styles from "./CheckoutEmi.css";

export default class CheckoutEmi extends React.Component {
  binValidation = (paymentMode, binNo) => {
    if (this.props.binValidation) {
      this.props.binValidation(paymentMode, binNo);
    }
  };

  softReservationForPayment = cardDetails => {
    if (this.props.softReservationForPayment) {
      this.props.softReservationForPayment(cardDetails);
    }
  };

  render() {
    if (this.props.cart.emiBankStatus === SUCCESS) {
      return (
        <ManueDetails text="Easy monthly installments" icon={emiIcon}>
          <EmiAccordian
            emiList={this.props.cart.emiBankDetails.bankList}
            onChangeCvv={i => this.onChangeCvv(i)}
            binValidation={binNo => this.binValidation(binNo)}
            softReservationForPayment={cardDetails =>
              this.softReservationForPayment(cardDetails)
            }
          />
        </ManueDetails>
      );
    } else {
      return (
        <div className={styles.errorText}>{this.props.cart.emiBankError}</div>
      );
    }
  }
}
