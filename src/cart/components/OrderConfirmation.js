import React from "react";
import RateYourExperienceCard from "./RateYourExperienceCard.js";
import OrderBanner from "./OrderBanner.js";
export default class OrderConfirmation extends React.Component {
  render() {
    return (
      <div>
        <OrderBanner
          headingText={this.props.orderDetails.orderStatusMessage}
          label={this.props.orderDetails.orderRefNo}
        />
        <RateYourExperienceCard />
      </div>
    );
  }
}
