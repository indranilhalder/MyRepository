import React from "react";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS
} from "../../lib/constants";
import SavedPaymentCard from "./SavedPaymentCard.js";
const CARD_FORMAT = /\B(?=(\d{4})+(?!\d))/g;
export default class UserSavedCard extends React.Component {
  componentDidMount() {
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

  removeSavedCardDetails = () => {
    if (this.props.removeSavedCardDetails) {
      this.props.removeSavedCardDetails();
    }
  };

  render() {
    if (this.props.profile.savedCards) {
      return (
        <div>
          {this.props.profile.savedCards.savedCardDetailsMap.map((data, i) => {
            let cardNumber = `${data.value.cardISIN}xx xxxx ${
              data.value.cardEndingDigits
            }`.replace(CARD_FORMAT, " ");
            let cardHolderName = `${data.value.firstName}  ${
              data.value.firstName
            }`;
            return (
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
            );
          })}
        </div>
      );
    } else {
      return null;
    }
  }
}
