import React from "react";
import netBankingIcon from "./img/netBanking.svg";
import PropTypes from "prop-types";
import NetBanking from "./NetBanking.js";
import ManueDetails from "../../general/components/MenuDetails.js";
import filter from "lodash.filter";
import { NET_BANKING } from "../../lib/constants";
const PAYMENT_MODE = "Netbanking";
export default class CheckoutNetBanking extends React.Component {
  binValidationForNetBank = bankName => {
    if (this.props.binValidationForNetBank) {
      this.props.binValidationForNetBank(PAYMENT_MODE, bankName);
    }
  };

  softReservationPaymentForNetBanking = cardDetails => {
    if (this.props.softReservationPaymentForNetBanking) {
      this.props.softReservationPaymentForNetBanking(cardDetails);
    }
  };
  createJusPayOrderForGiftCardNetBanking = cardDetails => {
    if (this.props.createJusPayOrderForGiftCardNetBanking) {
      this.props.createJusPayOrderForGiftCardNetBanking(cardDetails);
    }
  };

  getNetBankDetails = () => {
    if (this.props.getNetBankDetails) {
      this.props.getNetBankDetails();
    }
  };
  render() {
    let validNetBankingDetails;
    if (
      this.props.cart.netBankDetails &&
      this.props.cart.netBankDetails.bankList
    ) {
      validNetBankingDetails = filter(
        this.props.cart.netBankDetails.bankList,
        bank => {
          return bank.isAvailable === "true";
        }
      );
    }

    return (
      <ManueDetails
        text={NET_BANKING}
        isOpen={this.props.currentPaymentMode === NET_BANKING}
        onOpenMenu={currentPaymentMode =>
          this.props.onChange({ currentPaymentMode })
        }
        icon={netBankingIcon}
        getNetBankDetails={() => this.getNetBankDetails()}
        bankList={validNetBankingDetails}
      >
        <NetBanking
          selected={["1"]}
          bankList={validNetBankingDetails}
          binValidationForNetBank={bankName =>
            this.binValidationForNetBank(bankName)
          }
          softReservationPaymentForNetBanking={cardDetails =>
            this.softReservationPaymentForNetBanking(cardDetails)
          }
          isFromGiftCard={this.props.isFromGiftCard}
          createJusPayOrderForGiftCardNetBanking={cardDetails =>
            this.createJusPayOrderForGiftCardNetBanking(cardDetails)
          }
        />
      </ManueDetails>
    );
  }
}
