import React from "react";
import PaymentMethodCard from "./PaymentMethodCard";
import ConfirmAddress from "./ConfirmAddress";

import styles from "./CheckoutPage.css";
export default class CheckoutPage extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <ConfirmAddress
          address={[
            {
              addressTitle: "Home",
              addressDescription:
                "Lal Bahadur Shastri Marg, Chandan Nagar, Vikhori West"
            },
            {
              addressTitle: "Office",
              addressDescription:
                "Homi Modi St, Kala Ghoda, Fort Mumbai, Maharashtra 400023"
            },
            {
              addressTitle: "Other1",
              addressDescription:
                "Tagore Nagar, Vikhroli East, Mumbai, Maharashtra 400012"
            },
            {
              addressTitle: "Other2",
              addressDescription:
                "Homi Modi St, Kala Ghoda, Fort Mumbai, Maharashtra 400023"
            },
            {
              addressTitle: "Other3",
              addressDescription:
                "Homi Modi St, Kala Ghoda, Fort Mumbai, Maharashtra 400023"
            }
          ]}
        />
        <PaymentMethodCard
          cashText="Use My CLiQ Cash Balance"
          price="400"
          onToggle={i => this.testClick(i)}
          active={true}
          hasCashBalance={true}
          saveCardDetails={[
            {
              cardNumber: "4211",
              cardImage:
                "https://upload.wikimedia.org/wikipedia/commons/4/41/Visa_Logo.png"
            },
            {
              cardNumber: "4311",
              cardImage:
                "http://www.oplata.com/static/v1/img/logos/mastercard-brand.png"
            }
          ]}
        />
      </div>
    );
  }
}
