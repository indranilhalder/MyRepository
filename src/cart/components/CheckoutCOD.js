import React from "react";
import creditCardIcon from "./img/credit-card.svg";
import PropTypes from "prop-types";
import CodForm from "./CodForm.js";
import ManueDetails from "../../general/components/MenuDetails.js";
const PAYMENT_MODE = "Cash On Delivery";

export default class CheckoutCOD extends React.Component {
  binValidationForCOD(paymentMode) {
    if (this.props.binValidationForCOD) {
      this.props.binValidationForCOD(paymentMode);
    }
  }

  softReservationForCODPayment() {
    if (this.props.softReservationForCODPayment) {
      this.props.softReservationForCODPayment();
    }
  }

  render() {
    return (
      <div>
        {this.props.cart.codEligibilityDetails.status ? (
          <ManueDetails text="Cash On Delivery" icon={creditCardIcon}>
            <CodForm
              binValidationForCOD={paymentMode =>
                this.binValidationForCOD(paymentMode)
              }
              softReservationForCODPayment={() =>
                this.softReservationForCODPayment()
              }
            />
          </ManueDetails>
        ) : (
          <ManueDetails text="Cash On Delivery" icon={creditCardIcon}>
            <CodForm />
          </ManueDetails>
        )}
      </div>
    );
  }
}

CheckoutCOD.propTypes = {
  onChangeCvv: PropTypes.func
};
