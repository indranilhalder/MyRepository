import React from "react";
import styles from "./OrderDetails.css";
import OrderPlacedAndId from "./OrderPlacedAndId.js";
import OrderCard from "./OrderCard.js";
import OrderDelivered from "./OrderDelivered.js";
import OrderViewPaymentDetails from "./OrderViewPaymentDetails";
import OrderPaymentMethod from "./OrderPaymentMethod";
import OrderStatusVertical from "./OrderStatusVertical";
import OrderReturn from "./OrderReturn.js";
import PropTypes from "prop-types";
import moment from "moment";
import queryString from "query-string";
import { Redirect } from "react-router-dom";
import * as Cookie from "../../lib/Cookie";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import ModalContainer from "../../general/containers/ModalContainer";
import {
  ORDER_PREFIX,
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  LOGIN_PATH
} from "../../lib/constants";
const dateFormat = "DD MMM YYYY";
const PRODUCT_Returned = "Return Product";
const PRODUCT_Cancel = "Cancel Product";
export default class OrderDetails extends React.Component {
  requestInvoice(ussid, sellerOrderNo) {
    if (this.props.sendInvoice) {
      this.props.sendInvoice(ussid, sellerOrderNo);
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
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (
      userDetails &&
      customerCookie &&
      this.props.match.path === `${ORDER_PREFIX}`
    ) {
      const orderId = queryString.parse(this.props.location.search).orderCode;
      this.props.fetchOrderDetails(orderId);
    }
  }
  updateRefundDetailsPopUp(orderId, transactionId) {
    const orderDetails = {};
    orderDetails.orderId = orderId;
    orderDetails.transactionId = transactionId;
    if (this.props.showModal) {
      this.props.showModal(orderDetails);
    }
  }
  navigateToLogin() {
    return <Redirect to={LOGIN_PATH} />;
  }
  render() {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (!userDetails || !customerCookie) {
      return this.navigateToLogin();
    }
    const orderDetails = this.props.orderDetails;
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
                    SubTotal={orderDetails.subTotal}
                    DeliveryCharges={orderDetails.deliveryCharge}
                    Discount={orderDetails.totalDiscount}
                    ConvenienceCharges={orderDetails.convenienceCharge}
                    Total={orderDetails.totalOrderAmount}
                  />
                </div>

                <OrderPaymentMethod
                  phoneNumber={
                    orderDetails.billingAddress &&
                    orderDetails.billingAddress.phone
                  }
                  paymentMethod={orderDetails.paymentMethod}
                  isInvoiceAvailable={products.isInvoiceAvailable}
                  request={() =>
                    this.requestInvoice(products.USSID, products.sellerorderno)
                  }
                />
                {orderDetails.billingAddress && (
                  <OrderDelivered
                    deliveredAddress={`${
                      orderDetails.billingAddress.addressLine1
                    } ${orderDetails.billingAddress.town} ${
                      orderDetails.billingAddress.state
                    } ${orderDetails.billingAddress.postalcode}`}
                  />
                )}
                {products.statusDisplayMsg && (
                  <div className={styles.orderStatusVertical}>
                    <OrderStatusVertical
                      statusMessageList={
                        products.statusDisplayMsg[0].value.statusList[0]
                          .statusMessageList
                      }
                    />
                  </div>
                )}
                {products.awbPopupLink === "N" && (
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
                )}
                {products.awbPopupLink === "Y" && (
                  <div className={styles.buttonHolder}>
                    <div className={styles.buttonHolderForUpadte}>
                      <div className={styles.replaceHolder}>
                        <div
                          className={styles.replace}
                          onClick={() =>
                            this.updateRefundDetailsPopUp(
                              orderDetails.orderId,
                              products.transactionId
                            )
                          }
                        >
                          <UnderLinedButton
                            label="Update  Return Details"
                            color="#000"
                          />
                        </div>
                      </div>
                      <div className={styles.reviewHolder}>
                        <div
                          className={styles.review}
                          replaceItem={() => this.replaceItem()}
                        >
                          <UnderLinedButton
                            label={
                              products.isReturned === false
                                ? PRODUCT_Cancel
                                : PRODUCT_Returned
                            }
                            color="#ff1744"
                          />
                        </div>
                      </div>
                    </div>
                  </div>
                )}
              </div>
            );
          })}
      </div>
    );
  }
}
OrderDetails.propTypes = {
  orderDetails: PropTypes.arrayOf(
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
