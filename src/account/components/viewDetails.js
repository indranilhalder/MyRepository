import React from "react";
import styles from "./viewDetails.css";
import OrderPlacedAndId from "./OrderPlacedAndId.js";
import OrderCard from "./OrderCard.js";
import OrderDelivered from "./OrderDelivered.js";
import OrderViewPaymentDetails from "./OrderViewPaymentDetails";
import OrderPaymentMethod from "./OrderPaymentMethod";
//import VerticalDeliveredDetails from "./VerticalDeliveredDetails";
import PropTypes from "prop-types";
import moment from "moment";
const dateFormat = "DD MMM YYYY";
export default class ViewDetails extends React.Component {
  requestInvioice() {
    if (this.props.requestInvioice) {
      this.props.requestInvioice();
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
              request={() => this.requestInvioice()}
            />
            <OrderDelivered
              deliveredAddress={`${orderDetails.billingAddress.addressLine1} ${
                orderDetails.billingAddress.town
              } ${orderDetails.billingAddress.state} ${
                orderDetails.billingAddress.postalcode
              }`}
            />
            {/* <VerticalDeliveredDetails /> */}
          </div>
        )}
      </div>
    );
  }
}
ViewDetails.propTypes = {
  data: PropTypes.arrayOf(
    PropTypes.shape({
      orderDate: PropTypes.string,
      orderId: PropTypes.string,
      totalOrderAmount: PropTypes.string,
      subTotal: PropTypes.string,
      totalDiscounts: PropTypes.string,
      convenienceCharge: PropTypes.string,
      paymentMethod: PropTypes.string,
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
  requestInvioice: PropTypes.func
};
