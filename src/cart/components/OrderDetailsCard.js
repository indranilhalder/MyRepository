import React from "react";
import PropTypes from "prop-types";
import OrderPlacedAndId from "../../account/components/OrderPlacedAndId";
import OrderCard from "../../account/components/OrderCard";
import PriceAndLink from "../../account/components/PriceAndLink";
import styles from "./OrderDetailsCard.css";
import { COLLECT } from "../../lib/constants";
const NO_SIZE = "NO SIZE";
export default class OrderDetailsCard extends React.Component {
  onViewDetails() {
    if (this.props.trackOrder) {
      this.props.trackOrder();
    }
  }
  onClick(val) {
    if (!this.props.orderDetails.isEgvOrder) {
      if (this.props.onClick) {
        this.props.onClick(val);
      }
    }
  }
  render() {
    const deliveryOption = this.props.productDetails.selectedDeliveryMode;
    return (
      <div className={styles.base}>
        <div className={styles.orderIdHolder}>
          <OrderPlacedAndId orderId={this.props.orderId} />
        </div>
        <OrderCard
          imageUrl={
            this.props.productDetails.imageURL
              ? this.props.productDetails.imageURL
              : this.props.productDetails.imageUrl
          }
          productName={this.props.productDetails.productName}
          price={this.props.productDetails.pricevalue}
          isGiveAway={this.props.productDetails.isGiveAway}
          discountPrice=""
          onClick={() => this.onClick(this.props.orderId)}
        >
          {this.props.productDetails.quantity && (
            <div className={styles.quantityHolder}>
              <div className={styles.quantityLabel}>Qty :</div>
              <div className={styles.quantityAmount}>
                {this.props.productDetails.quantity}
              </div>
            </div>
          )}
          {this.props.productDetails.size &&
            this.props.productDetails.size.toUpperCase() !== NO_SIZE && (
              <div className={styles.quantityHolder}>
                <div className={styles.quantityLabel}>Size :</div>
                <div className={styles.quantityAmount}>
                  {this.props.productDetails.size}
                </div>
              </div>
            )}
          {this.props.productDetails.productColour && (
            <div className={styles.quantityHolder}>
              <div className={styles.quantityLabel}>Color :</div>
              <div className={styles.quantityAmount}>
                {this.props.productDetails.productColour}
              </div>
            </div>
          )}
        </OrderCard>
        <PriceAndLink
          isEgvOrder={this.props.orderDetails.isEgvOrder}
          onViewDetails={() => this.onViewDetails()}
          price={this.props.orderDetails.finalAmount}
          status={this.props.orderDetails.status}
        />
        {!this.props.orderDetails.isEgvOrder && (
          <div className={styles.informationDataHolder}>
            {this.props.orderDetails.shippingAddress && (
              <div className={styles.addressHolder}>
                <div className={styles.deliveredTo}>Delivery Address: </div>
                <div className={styles.address}>
                  {`${
                    this.props.orderDetails.shippingAddress.addressLine1
                      ? this.props.orderDetails.shippingAddress.addressLine1
                      : ""
                  } ${
                    this.props.orderDetails.shippingAddress.addressLine2
                      ? this.props.orderDetails.shippingAddress.addressLine2
                      : ""
                  } ${
                    this.props.orderDetails.shippingAddress.state
                      ? this.props.orderDetails.shippingAddress.state
                      : ""
                  } ${
                    this.props.orderDetails.shippingAddress.postalcode
                      ? this.props.orderDetails.shippingAddress.postalcode
                      : ""
                  }`}
                </div>
              </div>
            )}
            {this.props.productDetails.selectedDeliveryMode && (
              <div className={styles.addressHolder}>
                <div className={styles.deliveredTo}>
                  {`${
                    deliveryOption.name === "Home Delivery"
                      ? "Standard Shipping"
                      : deliveryOption.name === "Express Delivery"
                        ? "Express Shipping"
                        : deliveryOption.name
                  } `}
                </div>
                <div className={styles.address}>
                  {this.props.productDetails.selectedDeliveryMode.desc}
                </div>
              </div>
            )}

            {this.props.orderDetails.sellerName && (
              <div className={styles.orderSoldBy}>
                <div className={styles.labelText}>Sold by:</div>
                <div className={styles.infoText}>
                  {this.props.orderDetails.sellerName}
                </div>
              </div>
            )}
          </div>
        )}
      </div>
    );
  }
}
OrderDetailsCard.propTypes = {
  imageUrl: PropTypes.string,
  productName: PropTypes.string,
  price: PropTypes.string,
  discountPrice: PropTypes.string,
  quantity: PropTypes.string,
  totalPrice: PropTypes.string,
  deliveredAddress: PropTypes.string,
  standardDelivery: PropTypes.string,
  soldBy: PropTypes.string
};
