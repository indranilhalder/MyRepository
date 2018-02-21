import React from "react";
import PaymentMethodCard from "./PaymentMethodCard";
import styles from "./CheckoutPage.css";
export default class CheckoutPage extends React.Component {
  render() {
    return (
      <div className={styles.base}>
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
