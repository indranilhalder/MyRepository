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
  trackOrder() {
    this.props.trackOrder();
  }
  render() {
    return (
      <div>
        <OrderBanner
          headingText={this.props.orderStatusMessage}
          label={this.props.orderId}
          onClick={() => this.trackOrder()}
        />
        <RateYourExperienceCard
          captureOrderExperience={rating => this.captureOrderExperience(rating)}
          continueShopping={() => this.continueShopping()}
        />
      </div>
    );
  }

  componentWillUnmount() {
    if (this.props.orderConfirmationUpdate) {
      this.props.orderConfirmationUpdate();
    }
  }
}
