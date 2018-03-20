import React from "react";
import styles from "./GetAllOrderDetails.css";
import OrderPlacedAndId from "./OrderPlacedAndId.js";
import OrderCard from "./OrderCard.js";
import PriceAndLink from "./PriceAndLink.js";
import OrderDelivered from "./OrderDelivered.js";
import OrderReturn from "./OrderReturn.js";
import PropTypes from "prop-types";
import Button from "../../general/components/Button";
import moment from "moment";
const dateFormat = "DD MMM YYYY";
export default class GetAllOrderDetails extends React.Component {
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
  componentDidMount() {
    this.props.getAllOrdersDetails();
  }
  renderNoOrder() {
    return (
      <div className={styles.noOrder}>
        <div className={styles.noOderText}>
          You have not made any purchase yet
        </div>
        <div className={styles.continueShoppingButton}>
          <Button
            label="Continue Shopping"
            type="primary"
            width={170}
            height={40}
          />
        </div>
      </div>
    );
  }
  render() {
    let orderDetails = this.props.profile.orderDetails;
    return (
      <div className={styles.base}>
        {orderDetails && orderDetails.orderData
          ? orderDetails.orderData.map((orderDetails, i) => {
              return (
                <div className={styles.order} key={i}>
                  <div className={styles.orderIdHolder}>
                    <OrderPlacedAndId
                      placedTime={moment(orderDetails.orderDate).format(
                        dateFormat
                      )}
                      orderId={orderDetails.orderId}
                    />
                  </div>
                  {orderDetails.products &&
                    orderDetails.products.map((products, j) => {
                      return (
                        <OrderCard
                          key={j}
                          imageUrl={products.imageURL}
                          price={products.price}
                          discountPrice={""}
                          productName={products.productName}
                        />
                      );
                    })}
                  <PriceAndLink
                    onViewDetails={() => this.onViewDetails()}
                    price={orderDetails.totalOrderAmount}
                  />

                  <OrderDelivered
                    deliveredAddress={`${
                      orderDetails.billingAddress.addressLine1
                    } ${orderDetails.billingAddress.town} ${
                      orderDetails.billingAddress.state
                    } ${orderDetails.billingAddress.postalcode}`}
                  />
                  <div className={styles.buttonHolder}>
                    <OrderReturn
                      buttonLabel={this.props.buttonLabel}
                      underlineButtonLabel={this.props.underlineButtonLabel}
                      underlineButtonColour={this.props.underlineButtonColour}
                      isEditable={true}
                      replaceItem={() => this.replaceItem()}
                      writeReview={() => this.writeReview()}
                    />
                  </div>
                </div>
              );
            })
          : this.renderNoOrder()}
      </div>
    );
  }
}
GetAllOrderDetails.propTypes = {
  orderDetails: PropTypes.arrayOf(
    PropTypes.shape({
      orderDate: PropTypes.string,
      orderId: PropTypes.string,
      totalOrderAmount: PropTypes.string,
      billingAddress: PropTypes.arrayOf(
        PropTypes.shape({
          addressLine1: PropTypes.string,
          town: PropTypes.string,
          state: PropTypes.string,
          postalcode: PropTypes.string
        })
      )
    })
  ),
  onViewDetails: PropTypes.func,
  replaceItem: PropTypes.func,
  writeReview: PropTypes.func
};

GetAllOrderDetails.defaultprops = {
  buttonLabel: "Return or Replace",
  underlineButtonLabel: "Write a review",
  underlineButtonColour: " #ff1744"
};
