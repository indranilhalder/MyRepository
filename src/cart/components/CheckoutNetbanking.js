import React from "react";
import netBankingIcon from "./img/netBanking.svg";
import PropTypes from "prop-types";
import NetBanking from "./NetBanking.js";
import ManueDetails from "../../general/components/MenuDetails.js";
import filter from "lodash/filter";
const PAYMENT_MODE = "Netbanking";
export default class CheckoutNetBanking extends React.Component {
  binValidationForNetBank = bankName => {
    if (this.props.binValidationForNetBank) {
      this.props.binValidationForNetBank(PAYMENT_MODE, bankName);
    }
  };

  softReservationForPaymentForNetBanking = cardDetails => {
    if (this.props.softReservationForPaymentForNetBanking) {
      this.props.softReservationForPaymentForNetBanking(cardDetails);
    }
  };
  render() {
    let validNetBankingDetails = filter(
      this.props.cart.netBankDetails.bankList,
      bank => {
        return bank.isAvailable === "true";
      }
    );

    return (
      <ManueDetails text="Net banking" icon={netBankingIcon}>
        <NetBanking
          onSelect={val => console.log(val)}
          selected={["1"]}
          bankList={validNetBankingDetails}
          binValidationForNetBank={bankName =>
            this.binValidationForNetBank(bankName)
          }
          softReservationForPaymentForNetBanking={cardDetails =>
            this.softReservationForPaymentForNetBanking(cardDetails)
          }
        />
      </ManueDetails>
    );
  }
}
