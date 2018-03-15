import React from "react";
import creditCardIcon from "./img/credit-card.svg";
import PropTypes from "prop-types";
import CodForm from "./CodForm.js";
import MenuDetails from "../../general/components/MenuDetails.js";
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
        {this.props.cart.codEligibilityDetails.status ? (
          <MenuDetails text="Cash On Delivery" icon={creditCardIcon}>
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
          <MenuDetails text="Cash On Delivery Disable UI" icon={creditCardIcon}>
            <CodForm />
          </MenuDetails>
        )}
      </div>
    );
  }
}

CheckoutCOD.propTypes = {};
