import React from "react";
import creditCardIcon from "./img/credit-card.svg";
import PropTypes from "prop-types";
import CodForm from "./CodForm.js";
import ManueDetails from "../../general/components/MenuDetails.js";
const CASH_ON_DELIVERY = "COD";

export default class CheckoutCOD extends React.Component {
  binValidationForCOD(paymentMode) {
    if (this.props.binValidationForCOD) {
      this.props.binValidationForCOD(paymentMode);
    }
  }

  render() {
    return (
      <div>
        {this.props.cart.codEligibilityDetails.status ? (
          <ManueDetails
            text="Cash On Delivery"
            icon={creditCardIcon}
            onOpenMenu={val => {
              if (val) {
                this.binValidationForCOD(CASH_ON_DELIVERY);
              }
            }}
          >
            <CodForm
              binValidationForCOD={paymentMode =>
                this.binValidationForCOD(paymentMode)
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
