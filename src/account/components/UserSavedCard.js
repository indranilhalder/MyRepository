import React from "react";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  SAVED_PAYMENTS
} from "../../lib/constants";

import SavedPaymentCard from "./SavedPaymentCard.js";
import styles from "./UserSavedCard.css";
import {
  setDataLayer,
  ADOBE_MY_ACCOUNT_SAVED_PAYMENTS
} from "../../lib/adobeUtils";
const CARD_FORMAT = /\B(?=(\d{4})+(?!\d))/g;
const NO_SAVED_CARDS = "No Saved Cards";
export default class UserSavedCard extends React.Component {
  componentDidMount() {
    this.props.setHeaderText(SAVED_PAYMENTS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    if (customerCookie && userDetails) {
      if (this.props.getSavedCardDetails) {
        this.props.getSavedCardDetails(
          JSON.parse(userDetails).userName,
          JSON.parse(customerCookie).access_token
        );
      }
    }
  }
  componentDidUpdate() {
    this.props.setHeaderText(SAVED_PAYMENTS);
  }
  removeSavedCardDetails = () => {
    if (this.props.removeSavedCardDetails) {
      this.props.removeSavedCardDetails();
    }
  };

  render() {
    if (this.props.loading) {
      this.props.showSecondaryLoader();
    } else {
      this.props.hideSecondaryLoader();
    }
    if (
      this.props.profile.savedCards &&
      this.props.profile.savedCards.savedCardDetailsMap
    ) {
      return (
        <div className={styles.base}>
          {this.props.profile.savedCards.savedCardDetailsMap &&
            this.props.profile.savedCards.savedCardDetailsMap.map((data, i) => {
              let cardNumber = `${data.value.cardISIN}xx xxxx ${
                data.value.cardEndingDigits
              }`.replace(CARD_FORMAT, " ");
              let cardHolderName = `${data.value.firstName}  ${
                data.value.firstName
              }`;
              return (
                <div className={styles.cardHolder}>
                  <SavedPaymentCard
                    key={i}
                    bankLogo={""}
                    bankName={data.value.cardIssuer}
                    cardLogo={""}
                    cardName={data.value.cardType}
                    cardHolderName={cardHolderName}
                    validityDate={`${data.value.expiryMonth}/${
                      data.value.expiryYear
                    }`}
                    cardNumber={cardNumber}
                    cardImage={data.cardImage}
                    onChangeCvv={(cvv, cardNo) => this.onChangeCvv(cvv, cardNo)}
                    removeSavedCardDetails={() => this.removeSavedCardDetails()}
                  />
                </div>
              );
            })}
        </div>
      );
    } else {
      return <div className={styles.noSavedCardBlock}>{NO_SAVED_CARDS}</div>;
    }
  }
}
