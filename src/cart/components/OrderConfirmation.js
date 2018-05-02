import React from "react";
import RateYourExperienceCard from "./RateYourExperienceCard.js";
import OrderBanner from "./OrderBanner.js";
import styles from "./OrderConfirmation.css";
import OrderDetailsCard from "./OrderDetailsCard.js";
import OrderConfirmationFooter from "./OrderConfirmationFooter.js";
export default class OrderConfirmation extends React.Component {
  captureOrderExperience = rating => {
    this.props.captureOrderExperience(rating);
  };
  componentWillUnmount() {
    this.props.clearCartDetails();
  }
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
        {this.props.orderDetails &&
          this.props.orderDetails.products &&
          this.props.orderDetails.products.map(order => {
            return (
              <OrderDetailsCard
                productDetails={order}
                orderDetails={this.props.orderDetails}
                orderId={this.props.orderId}
                trackOrder={() => this.trackOrder()}
              />
            );
          })}

        <OrderConfirmationFooter
          continueShopping={() => this.continueShopping()}
          trackOrder={() => this.trackOrder()}
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
