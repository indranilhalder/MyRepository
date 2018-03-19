import React from "react";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import Logo from "../../general/components/Logo";
import styles from "./SavedPaymentCard.css";
import OrderReturn from "./OrderReturn.js";
export default class SavedPaymentCard extends React.Component {
  replaceItem() {
    if (this.props.replaceItem) {
      this.props.replaceItem();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.paymentDetailsHolder}>
          <div className={styles.bankAndCardHolder}>
            <div className={styles.bankLogoAndNameHolder}>
              <div className={styles.bankLogo}>
                <Logo image={this.props.bankLogo} />
              </div>
              <div
                className={
                  this.props.bankLogo ? styles.bankNameGap : styles.bankName
                }
              >
                {this.props.bankName}
              </div>
            </div>
            <div className={styles.cardAndTextHolder}>
              <div className={styles.cardLogoHolder}>
                <div className={styles.cardLogo}>
                  <Logo image={this.props.cardLogo} />
                </div>
              </div>
              <div className={styles.cardName}>{this.props.cardName}</div>
            </div>
          </div>
          <div className={styles.cardNumberHolder}>
            <div className={styles.cardDataHolder}>
              <div className={styles.dataHeader}>Card Number</div>
              <div className={styles.dataHolder}>{this.props.cardNumber}</div>
            </div>
          </div>
          <div className={styles.cardNameAndValidityHolder}>
            <div className={styles.nameOfCardHolder}>
              <div className={styles.dataHeader}>Name on the card</div>
              <div className={styles.dataHolder}>
                {this.props.cardHolderName}
              </div>
            </div>
            <div className={styles.validityHolder}>
              <div className={styles.dataHeader}>Validity</div>
              <div className={styles.dataHolder}>{this.props.validityDate}</div>
            </div>
          </div>
        </div>
        <div className={styles.actionHolder}>
          <OrderReturn
            replaceItem={() => this.replaceItem()}
            buttonLabel="Remove"
            underlineButtonLabel="Edit"
            isEditable={true}
          />
        </div>
      </div>
    );
  }
}
SavedPaymentCard.propTypes = {
  bankLogo: PropTypes.string,
  bankName: PropTypes.string,
  cardLogo: PropTypes.string,
  replaceItem: PropTypes.func,
  cardName: PropTypes.string,
  cardNumber: PropTypes.string,
  cardHolderName: PropTypes.string,
  validityDate: PropTypes.string
};
