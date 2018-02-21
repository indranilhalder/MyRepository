import React from "react";
import PropTypes from "prop-types";
import CheckOutHeader from "./CheckOutHeader.js";
import SavedCard from "./SavedCard.js";
import NetBanking from "./NetBanking.js";
import CreditCardForm from "./CreditCardForm.js";
import Toggle from "../../general/components/Toggle";
import ManueDetails from "../../general/components/MenuDetails.js";
import savedCardIcon from "./img/saved-card.svg";
import netBankingIcon from "./img/netBanking.svg";
import creditCardIcon from "./img/credit-card.svg";
import debitCardIcon from "./img/debit-card.svg";
import styles from "./PaymentMethodCard.css";

export default class PaymentMethodCard extends React.Component {
  onChangeCvv(i) {
    if (this.props.onChangeCvv) {
      this.props.onChangeCvv(i);
    }
  }
  onToggle(val) {
    if (this.props.onToggle) {
      this.props.onToggle(val);
    }
  }
  render() {
    let headerTextHolder = styles.paymentHeader;
    if (this.props.hasCashBalance) {
      headerTextHolder = styles.spaceBetween;
    }
    return (
      <div className={styles.base}>
        <div className={styles.paymentDataHolder}>
          <div className={headerTextHolder}>
            <CheckOutHeader
              confirmTitle="Choose payment Method"
              indexNumber="3"
            />
          </div>
          {this.props.hasCashBalance && (
            <div className={styles.cashBalanceHolder}>
              <div className={styles.cashBalanceTextHolder}>
                <div className={styles.casBalanceText}>
                  {this.props.cashText}
                </div>
                <div className={styles.cashRupyText}>{`Rs. ${
                  this.props.price
                } available`}</div>
              </div>
              <div className={styles.toggleButtonHolder}>
                <div className={styles.toggleButton}>
                  <Toggle
                    active={this.props.active}
                    onToggle={val => this.onToggle(val)}
                  />
                </div>
              </div>
            </div>
          )}
        </div>
        <div className={styles.dropdownHolder}>
          <ManueDetails text="Saved Cards" icon={savedCardIcon}>
            {this.props.saveCardDetails &&
              this.props.saveCardDetails.map((data, i) => {
                return (
                  <SavedCard
                    key={i}
                    cardNumber={data.cardNumber}
                    cardImage={data.cardImage}
                    onChangeCvv={i => this.onChangeCvv(i)}
                  />
                );
              })}
          </ManueDetails>
          <ManueDetails text="Credit Card" icon={creditCardIcon}>
            <CreditCardForm />
          </ManueDetails>
          <ManueDetails text="Debit Card" icon={debitCardIcon}>
            <CreditCardForm />
          </ManueDetails>
          <ManueDetails text="Net banking" icon={netBankingIcon}>
            <NetBanking
              onSelect={val => console.log(val)}
              selected={["1"]}
              bankList={[
                {
                  image:
                    "https://competitiondigest.com/wp-content/uploads/2014/12/bank-of-1.gif",
                  value: "1"
                },
                {
                  image:
                    "https://competitiondigest.com/wp-content/uploads/2014/12/CBicons_03.png",
                  value: "2"
                },
                {
                  image:
                    "https://competitiondigest.com/wp-content/uploads/2014/12/UNITED.png",
                  value: "3"
                },
                {
                  image:
                    "https://competitiondigest.com/wp-content/uploads/2014/12/UNION.png",
                  value: "4"
                },
                {
                  image:
                    "https://competitiondigest.com/wp-content/uploads/2014/12/UNION.png",
                  value: "6"
                },
                {
                  image:
                    "https://competitiondigest.com/wp-content/uploads/2014/12/UNITED.png",
                  value: "7"
                }
              ]}
            />
          </ManueDetails>
        </div>
      </div>
    );
  }
}
PaymentMethodCard.propTypes = {
  saveCardDetails: PropTypes.arrayOf(
    PropTypes.shape({
      cardNumber: PropTypes.string,
      cardImage: PropTypes.string
    })
  ),
  onChangeCvv: PropTypes.func,
  onToggle: PropTypes.func,
  cashText: PropTypes.string,
  price: PropTypes.string,
  active: PropTypes.bool,
  hasCashBalance: PropTypes.bool
};
