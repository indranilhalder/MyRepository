import React from "react";
import creditCardIcon from "./img/credit-card.svg";
import PropTypes from "prop-types";
import CodForm from "./CodForm.js";
import CodUnavailable from "./CodUnavailable";
import MenuDetails from "../../general/components/MenuDetails.js";
import { CASH_ON_DELIVERY_PAYMENT_MODE } from "../../lib/constants";
const CASH_ON_DELIVERY = "COD";

export default class CheckoutCOD extends React.Component {
  binValidationForCOD = paymentMode => {
    this.props.binValidationForCOD(paymentMode);
  };

  softReservationForCODPayment = () => {
    this.props.softReservationForCODPayment();
  };

  render() {
    return (
      <div>
        {this.props.cart &&
        this.props.cart.codEligibilityDetails &&
        this.props.cart.codEligibilityDetails.status ? (
          <MenuDetails
            text={CASH_ON_DELIVERY_PAYMENT_MODE}
            isOpen={
              this.props.currentPaymentMode === CASH_ON_DELIVERY_PAYMENT_MODE
            }
            onOpenMenu={currentPaymentMode =>
              this.props.onChange({ currentPaymentMode })
            }
            icon={creditCardIcon}
          >
            <CodForm
              cart={this.props.cart}
              binValidationForCOD={paymentMode =>
                this.binValidationForCOD(paymentMode)
              }
              softReservationForCODPayment={() =>
                this.softReservationForCODPayment()
              }
            />
          </MenuDetails>
        ) : (
          <CodUnavailable message="Cash on delivery not available " />
        )}
      </div>
    );
  }
}

CheckoutCOD.propTypes = {
  binValidationForCOD: PropTypes.func,
  softReservationForCODPayment: PropTypes.func,
  cart: PropTypes.object
};
