import React from "react";
import RateYourExperienceCard from "./RateYourExperienceCard.js";
import OrderBanner from "./OrderBanner.js";
export default class OrderConfirmation extends React.Component {
  captureOrderExperience = rating => {
    this.props.captureOrderExperience(rating);
  };

  continueShopping = () => {
    this.props.continueShopping();
  };
  render() {
    return (
      <div>
        <OrderBanner
          headingText={this.props.orderStatusMessage}
          label={this.props.orderId}
        />
        <RateYourExperienceCard
          captureOrderExperience={rating => this.captureOrderExperience(rating)}
          continueShopping={() => this.continueShopping()}
        />
      </div>
    );
  }
}
