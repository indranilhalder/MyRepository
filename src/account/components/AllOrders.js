import React from "react";
import styles from "./AllOrders.css";
import OrderPlacedAndId from "./OrderPlacedAndId.js";
import OrderCard from "./OrderCard.js";
import PriceAndLink from "./PriceAndLink.js";
import OrderDelivered from "./OrderDelivered.js";
import OrderReturn from "./OrderReturn.js";
import PropTypes from "prop-types";
export default class AllOrders extends React.Component {
  onViewDetails() {
    if (this.props.onViewDetails) {
      this.props.onViewDetails();
    }
  }
  replaceItem() {
    if (this.props.replaceItem) {
      this.props.replaceItem();
    }
  }
  writeReview() {
    if (this.props.writeReview) {
      this.props.writeReview();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.orderIdHolder}>
          <OrderPlacedAndId
            placedTime={this.props.placedTime}
            orderId={this.props.orderId}
          />
        </div>
        <OrderCard
          imageUrl={this.props.imageUrl}
          price={this.props.Productprice}
          discountPrice={this.props.discountPrice}
          productName={this.props.productName}
        />
        <PriceAndLink
          onViewDetails={() => this.onViewDetails()}
          price={this.props.TotalPrice}
        />
        <OrderDelivered deliveredAddress={this.props.deliveryAddress} />
        <div className={styles.buttonHolder}>
          <OrderReturn
            buttonLabel="Return"
            underlineButtonLabel="Write a review"
            underlineButtonColour=" #ff1744"
            isEditable={true}
            replaceItem={() => this.replaceItem()}
            writeReview={() => this.writeReview()}
          />
        </div>
      </div>
    );
  }
}
AllOrders.propTypes = {
  placedTime: PropTypes.string,
  orderId: PropTypes.string,
  imageUrl: PropTypes.string,
  Productprice: PropTypes.string,
  discountPrice: PropTypes.string,
  productName: PropTypes.string,
  TotalPrice: PropTypes.string,
  deliveryAddress: PropTypes.string,
  onViewDetails: PropTypes.func,
  replaceItem: PropTypes.func,
  writeReview: PropTypes.func
};
