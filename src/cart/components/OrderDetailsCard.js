import React from "react";
import PropTypes from "prop-types";
import OrderPlacedAndId from "../../account/components/OrderPlacedAndId";
import OrderCard from "../../account/components/OrderCard";
import PriceAndLink from "../../account/components/PriceAndLink";
import styles from "./OrderDetailsCard.css";
export default class OrderDetailsCard extends React.Component {
  onViewDetails() {
    if (this.props.onViewDetails) {
      this.props.onViewDetails();
    }
  }
  onClick(val) {
    if (this.props.onClick) {
      this.props.onClick(val);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.orderIdHolder}>
          <OrderPlacedAndId orderId={this.props.orderId} />
        </div>
        <OrderCard
          imageUrl={this.props.imageUrl}
          productName={this.props.productName}
          price={this.props.price}
          discountPrice={this.props.discountPrice}
          onClick={() => this.onClick(this.props.orderId)}
        >
          <div className={styles.quantityHolder}>
            <div className={styles.quantityLabel}>Qty :</div>
            <div className={styles.quantityAmount}>{this.props.quantity}</div>
          </div>
        </OrderCard>
        <PriceAndLink
          onViewDetails={() => this.onViewDetails()}
          price={this.props.totalPrice}
        />
        <div className={styles.informationDataHolder}>
          {this.props.deliveredAddress && (
            <div className={styles.addressHolder}>
              <div className={styles.deliveredTo}>Delivery Address: </div>
              <div className={styles.address}>
                {this.props.deliveredAddress}
              </div>
            </div>
          )}
          {this.props.standardDelivery && (
            <div className={styles.addressHolder}>
              <div className={styles.deliveredTo}>Standard Delivery: </div>
              <div className={styles.address}>
                {this.props.standardDelivery}
              </div>
            </div>
          )}
          {this.props.standardDelivery && (
            <div className={styles.orderSoldBy}>
              <div className={styles.labelText}>Sold by:</div>
              <div className={styles.infoText}>{this.props.soldBy}</div>
            </div>
          )}
        </div>
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
