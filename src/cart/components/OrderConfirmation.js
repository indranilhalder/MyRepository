import React from "react";
import RateYourExperienceCard from "./RateYourExperienceCard.js";
import OrderBanner from "./OrderBanner.js";
import styles from "./OrderConfirmation.css";
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
      <div className={styles.base}>
        <div className={styles.orderBannerHolder}>
          <OrderBanner
            headingText={this.props.orderStatusMessage}
            label={this.props.orderId}
            onClick={() => this.trackOrder()}
          />
        </div>
        <div className={styles.rateHolder}>
          <RateYourExperienceCard
            captureOrderExperience={rating =>
              this.captureOrderExperience(rating)
            }
            continueShopping={() => this.continueShopping()}
          />
        </div>
      </div>
    );
  }

  componentWillUnmount() {
    if (this.props.orderConfirmationUpdate) {
      this.props.orderConfirmationUpdate();
    }
  }
}
