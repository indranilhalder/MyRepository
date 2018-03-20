import React from "react";
import styles from "./viewDetails.css";
import OrderPlacedAndId from "./OrderPlacedAndId.js";
import OrderCard from "./OrderCard.js";
import OrderDelivered from "./OrderDelivered.js";
import OrderReturn from "./OrderReturn.js";
import OrderViewPaymentDetails from "./OrderViewPaymentDetails";
import OrderPaymentMethod from "./OrderPaymentMethod";
//import VerticalDeliveredDetails from "./VerticalDeliveredDetails";
import PropTypes from "prop-types";
import moment from "moment";
const dateFormat = "DD MMM YYYY";
export default class ViewDetails extends React.Component {
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
    let orderDetails = this.props.data;
    return (
      <div className={styles.base}>
        {orderDetails && (
          <div className={styles.order}>
            <div className={styles.orderIdHolder}>
              <OrderPlacedAndId
                placedTime={moment(orderDetails.orderDate).format(dateFormat)}
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
            <div className={styles.payment}>
              <OrderViewPaymentDetails
                SubTotal={orderDetails.subTotal}
                DeliveryCharges={orderDetails.deliveryCharge}
                Discount={orderDetails.totalDiscounts}
                ConvenienceCharges={orderDetails.convenienceCharge}
                Total={orderDetails.totalOrderAmount}
              />
            </div>
            <OrderPaymentMethod
              phoneNumber={orderDetails.billingAddress.phone}
              paymentMethod={orderDetails.paymentMethod}
              request={() => this.test1()}
            />
            <OrderDelivered
              deliveredAddress={`${orderDetails.billingAddress.addressLine1} ${
                orderDetails.billingAddress.town
              } ${orderDetails.billingAddress.state} ${
                orderDetails.billingAddress.postalcode
              }`}
            />
            {/* <VerticalDeliveredDetails /> */}
            <div className={styles.buttonHolder}>
              <OrderReturn
                buttonLabel={""}
                underlineButtonLabel={this.props.underlineButtonLabel}
                underlineButtonColour={this.props.underlineButtonColour}
                isEditable={true}
                replaceItem={() => this.replaceItem()}
                writeReview={() => this.writeReview()}
              />
            </div>
          </div>
        )}
      </div>
    );
  }
}
ViewDetails.propTypes = {
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
ViewDetails.defaultprops = {
  buttonLabel: "Return or Replace",
  underlineButtonLabel: "Write a review",
  underlineButtonColour: " #ff1744"
};
