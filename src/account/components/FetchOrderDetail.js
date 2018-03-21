import React from "react";
import styles from "./FetchOrderDetail.css";
import OrderPlacedAndId from "./OrderPlacedAndId.js";
import OrderCard from "./OrderCard.js";
import OrderDelivered from "./OrderDelivered.js";
import OrderViewPaymentDetails from "./OrderViewPaymentDetails";
import OrderPaymentMethod from "./OrderPaymentMethod";
import OrderStatusVertical from "./OrderStatusVertical";
import OrderReturn from "./OrderReturn.js";
import PropTypes from "prop-types";
import moment from "moment";
import { ORDER_DESCRIPTION } from "../../lib/constants";
const dateFormat = "DD MMM YYYY";
const PRODUCT_Returned = "Return";
const PRODUCT_Cancel = "Cancel";
export default class FetchOrderDetail extends React.Component {
  requestInvoice() {
    if (this.props.requestInvioice) {
      this.props.requestInvioice();
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
    if (this.props.match.path === ORDER_DESCRIPTION) {
      let orderId = this.props.match.params[1];
      this.props.fetchOrderDetails(orderId);
    }
  }
  render() {
    const orderDetails = this.props.profile.fetchOrderDetails;
    return (
      <div className={styles.base}>
        {orderDetails &&
          orderDetails.products.map((products, i) => {
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

                <OrderCard
                  imageUrl={products.imageURL}
                  price={products.price}
                  discountPrice={""}
                  productName={products.productName}
                />

                <div className={styles.payment}>
                  <OrderViewPaymentDetails
                    SubTotal={products.price}
                    DeliveryCharges={orderDetails.deliveryCharge}
                    Discount={orderDetails.totalDiscount}
                    ConvenienceCharges={orderDetails.convenienceCharge}
                    Total={products.price}
                  />
                </div>
                <OrderPaymentMethod
                  phoneNumber={orderDetails.billingAddress.phone}
                  paymentMethod={orderDetails.paymentMethod}
                  request={() => this.requestInvioice()}
                />
                <OrderDelivered
                  deliveredAddress={`${
                    orderDetails.billingAddress.addressLine1
                  } ${orderDetails.billingAddress.town} ${
                    orderDetails.billingAddress.state
                  } ${orderDetails.billingAddress.postalcode}`}
                />
                <div className={styles.orderStatusVertical}>
                  <OrderStatusVertical
                    statusMessageList={
                      products.statusDisplayMsg[0].value.statusList[0]
                        .statusMessageList
                    }
                  />
                </div>
                <div className={styles.buttonHolder}>
                  <OrderReturn
                    buttonLabel={
                      products.isReturned === false
                        ? PRODUCT_Cancel
                        : PRODUCT_Returned
                    }
                    isEditable={true}
                    replaceItem={() => this.replaceItem()}
                    writeReview={() => this.writeReview()}
                  />
                </div>
              </div>
            );
          })}
      </div>
    );
  }
}
FetchOrderDetail.propTypes = {
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
  requestInvoice: PropTypes.func
};
