import React from "react";
import styles from "./OrderDetails.css";
import OrderPlacedAndId from "./OrderPlacedAndId.js";
import OrderCard from "./OrderCard.js";
import OrderDelivered from "./OrderDelivered.js";
import OrderViewPaymentDetails from "./OrderViewPaymentDetails";
import OrderPaymentMethod from "./OrderPaymentMethod";
import OrderStatusVertical from "./OrderStatusVertical";
import OrderStatusHorizontal from "./OrderStatusHorizontal";
import OrderReturn from "./OrderReturn.js";
import PropTypes from "prop-types";
import format from "date-fns/format";
import each from "lodash.foreach";
import queryString from "query-string";
import { Redirect } from "react-router-dom";
import * as Cookie from "../../lib/Cookie";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import { SUCCESS } from "../../lib/constants";
import {
  CASH_ON_DELIVERY,
  ORDER_PREFIX,
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  LOGIN_PATH,
  RETURNS_PREFIX,
  RETURN_LANDING,
  RETURNS_REASON,
  SHORT_URL_ORDER_DETAIL,
  SEARCH_RESULTS_PAGE,
  PRODUCT_REVIEWS_PATH_SUFFIX,
  CANCEL
} from "../../lib/constants";
import {
  setDataLayer,
  ADOBE_MY_ACCOUNT_ORDER_DETAILS
} from "../../lib/adobeUtils";
const dateFormat = "DD MMM YYYY";
const PRODUCT_RETURN = "Return";
const RETURN = "RETURN";
const PRODUCT_CANCEL = "Cancel";
const AWB_POPUP_TRUE = "Y";
const AWB_POPUP_FALSE = "N";
const CLICK_COLLECT = "click-and-collect";
export default class OrderDetails extends React.Component {
  onClickImage(productCode) {
    if (productCode) {
      this.props.history.push(`/p-${productCode.toLowerCase()}`);
    }
  }
  requestInvoice(lineID, orderNumber) {
    if (this.props.sendInvoice) {
      this.props.sendInvoice(lineID, orderNumber);
    }
  }
  handleshowShippingDetails(val) {
    if (this.props.showShippingDetails && val) {
      this.props.showShippingDetails(val);
    }
  }

  replaceItem(sellerorderno, paymentMethod, transactionId) {
    if (sellerorderno) {
      let isCOD = false;
      if (paymentMethod === CASH_ON_DELIVERY) {
        isCOD = true;
      }
      this.props.history.push({
        pathname: `${RETURNS_PREFIX}/${sellerorderno}${RETURN_LANDING}${RETURNS_REASON}`,
        state: {
          isCOD,
          authorizedRequest: true,
          transactionId: transactionId
        }
      });
    }
  }
  cancelItem(transactionId, ussid, orderCode) {
    this.props.history.push({
      pathname: `${CANCEL}/${orderCode}`,
      state: {
        transactionId: transactionId,
        ussid: ussid
      }
    });
  }
  writeReview(productCode) {
    this.props.history.push(
      `${SEARCH_RESULTS_PAGE}p-${productCode.toLowerCase()}/${PRODUCT_REVIEWS_PATH_SUFFIX}`
    );
  }
  componentDidMount() {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (
      userDetails &&
      customerCookie &&
      this.props.match.path === `${ORDER_PREFIX}`
    ) {
      const orderCode = queryString.parse(this.props.location.search).orderCode;
      this.props.fetchOrderDetails(orderCode);
      this.props.setHeaderText(`#${orderCode}`);
    } else if (
      userDetails &&
      customerCookie &&
      this.props.match.path === `${SHORT_URL_ORDER_DETAIL}`
    ) {
      const orderCode = this.props.match.params.orderCode;
      this.props.fetchOrderDetails(orderCode);
      this.props.setHeaderText(`#${orderCode}`);
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

  componentDidUpdate(prevProps) {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (
      userDetails &&
      customerCookie &&
      this.props.match.path === `${ORDER_PREFIX}`
    ) {
      const orderCode = queryString.parse(this.props.location.search).orderCode;
      this.props.setHeaderText(`#${orderCode}`);
    } else if (
      userDetails &&
      customerCookie &&
      this.props.match.path === `${SHORT_URL_ORDER_DETAIL}`
    ) {
      const orderCode = this.props.match.params.orderCode;
      this.props.setHeaderText(`#${orderCode}`);
    }
  }

  navigateToLogin() {
    const url = this.props.location.pathname;
    this.props.setUrlToRedirectToAfterAuth(url);

    return <Redirect to={LOGIN_PATH} />;
  }
  componentWillReceiveProps(nextProps) {
    if (nextProps.sendInvoiceSatus === SUCCESS) {
      this.props.displayToast("Invoice has been sent");
    }
  }
  render() {
    if (this.props.loadingForFetchOrderDetails) {
      this.props.showSecondaryLoader();
    } else {
      this.props.hideSecondaryLoader();
    }
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
            let isOrderReturnable = false;
            let isReturned = false;

            if (
              products.statusDisplayMsg
                .map(val => {
                  return val.key;
                })
                .includes(RETURN)
            )
              isReturned = products.statusDisplayMsg
                .map(val => {
                  return val.key;
                })
                .includes(RETURN);

            each(products && products.statusDisplayMsg, orderStatus => {
              each(
                orderStatus &&
                  orderStatus.value &&
                  orderStatus.value.statusList,
                status => {
                  if (status.responseCode === "DELIVERED") {
                    isOrderReturnable = true;
                  }
                }
              );
            });
            return (
              <div className={styles.order} key={i}>
                <div className={styles.orderIdHolder}>
                  <OrderPlacedAndId
                    placedTime={format(orderDetails.orderDate, dateFormat)}
                    orderId={orderDetails.orderId}
                  />
                </div>
                <OrderCard
                  imageUrl={products.imageURL}
                  price={products.price}
                  discountPrice={""}
                  productName={products.productName}
                  isGiveAway={products.isGiveAway}
                  onClick={() => this.onClickImage(products.productcode)}
                />
                <div className={styles.payment}>
                  <OrderViewPaymentDetails
                    SubTotal={
                      orderDetails.orderAmount &&
                      orderDetails.orderAmount.bagTotal &&
                      orderDetails.orderAmount.bagTotal.value
                        ? Math.round(
                            orderDetails.orderAmount.bagTotal.value * 100
                          ) / 100
                        : "0.00"
                    }
                    DeliveryCharges={orderDetails.deliveryCharge}
                    Discount={
                      orderDetails.orderAmount &&
                      orderDetails.orderAmount.totalDiscountAmount &&
                      orderDetails.orderAmount.totalDiscountAmount.value
                        ? Math.round(
                            orderDetails.orderAmount.totalDiscountAmount.value *
                              100
                          ) / 100
                        : "0.00"
                    }
                    coupon={
                      orderDetails.orderAmount &&
                      orderDetails.orderAmount.couponDiscountAmount &&
                      orderDetails.orderAmount.couponDiscountAmount.value
                        ? Math.round(
                            orderDetails.orderAmount.couponDiscountAmount
                              .value * 100
                          ) / 100
                        : "0.00"
                    }
                    ConvenienceCharges={orderDetails.convenienceCharge}
                    Total={
                      orderDetails.orderAmount &&
                      orderDetails.orderAmount.paybleAmount &&
                      orderDetails.orderAmount.paybleAmount.value
                        ? Math.round(
                            orderDetails.orderAmount.paybleAmount.value * 100
                          ) / 100
                        : "0.00"
                    }
                    cliqCashAmountDeducted={
                      orderDetails && orderDetails.cliqCashAmountDeducted
                    }
                  />
                </div>
                <OrderPaymentMethod
                  phoneNumber={
                    orderDetails.deliveryAddress &&
                    orderDetails.deliveryAddress.phone
                  }
                  paymentMethod={orderDetails.paymentMethod}
                  isInvoiceAvailable={products.isInvoiceAvailable}
                  statusDisplay={products.statusDisplayMsg}
                  request={() =>
                    this.requestInvoice(
                      products.transactionId,
                      products.sellerorderno
                    )
                  }
                />
                {orderDetails.billingAddress &&
                  Object.keys(orderDetails.billingAddress).length !== 0 && (
                    <OrderDelivered
                      deliveredAddress={`${
                        orderDetails.billingAddress.addressLine1
                          ? orderDetails.billingAddress.addressLine1
                          : ""
                      } ${
                        orderDetails.billingAddress.town
                          ? orderDetails.billingAddress.town
                          : ""
                      } ${
                        orderDetails.billingAddress.state
                          ? orderDetails.billingAddress.state
                          : ""
                      } ${
                        orderDetails.billingAddress.postalcode
                          ? orderDetails.billingAddress.postalcode
                          : ""
                      }`}
                    />
                  )}
                {products.statusDisplayMsg &&
                  products.selectedDeliveryMode.code !== CLICK_COLLECT && (
                    <div className={styles.orderStatusVertical}>
                      {/* This block of code needs to be duplicated below for CNC as well */}
                      {!products.statusDisplayMsg
                        .map(val => {
                          return val.key;
                        })
                        .includes(RETURN) && (
                        <OrderStatusVertical
                          isCNC={false}
                          statusMessageList={products.statusDisplayMsg}
                          logisticName={products.logisticName}
                          trackingAWB={products.trackingAWB}
                          showShippingDetails={this.props.showShippingDetails}
                          orderCode={orderDetails.orderId}
                        />
                      )}
                      {products.statusDisplayMsg
                        .map(val => {
                          return val.key;
                        })
                        .includes(RETURN) && (
                        <OrderStatusHorizontal
                          trackingAWB={products.trackingAWB}
                          courier={products.reverseLogisticName}
                          statusMessageList={products.statusDisplayMsg.filter(
                            val => {
                              return val.key === RETURN;
                            }
                          )}
                        />
                      )}
                      {/* Block of code ends here */}
                    </div>
                  )}
                {products.selectedDeliveryMode.code === CLICK_COLLECT &&
                  products.storeDetails && (
                    <div className={styles.orderStatusVertical}>
                      <div className={styles.header}>Store details:</div>
                      <div className={styles.row}>
                        {products.storeDetails.displayName &&
                          products.storeDetails.displayName !== undefined &&
                          products.storeDetails.displayName !== "undefined" && (
                            <span>{products.storeDetails.displayName} ,</span>
                          )}{" "}
                        {products.storeDetails.returnAddress1 &&
                          products.storeDetails.returnAddress1 !== undefined &&
                          products.storeDetails.returnAddress1 !==
                            "undefined" && (
                            <span>
                              {products.storeDetails.returnAddress1} ,
                            </span>
                          )}{" "}
                        {products.storeDetails.returnAddress2 &&
                          products.storeDetails.returnAddress2 !== undefined &&
                          products.storeDetails.returnAddress2 !==
                            "undefined" && (
                            <span>{products.storeDetails.returnAddress2}</span>
                          )}{" "}
                      </div>
                      <div className={styles.row}>
                        {products.storeDetails.returnCity}{" "}
                        {products.storeDetails.returnPin}
                      </div>
                    </div>
                  )}
                {products.selectedDeliveryMode.code === CLICK_COLLECT &&
                  (orderDetails.pickupPersonName ||
                    orderDetails.pickupPersonMobile) && (
                    <div className={styles.orderStatusVertical}>
                      <div className={styles.header}>Pickup details:</div>
                      <div className={styles.row}>
                        {orderDetails.pickupPersonName}
                      </div>
                      <div className={styles.row}>
                        {orderDetails.pickupPersonMobile}
                      </div>
                      {/* This block of code needs to be duplicated above for non CNC as well */}
                      {!products.statusDisplayMsg
                        .map(val => {
                          return val.key;
                        })
                        .includes(RETURN) && (
                        <OrderStatusVertical
                          isCNC={true}
                          statusMessageList={products.statusDisplayMsg}
                          logisticName={products.logisticName}
                          trackingAWB={products.trackingAWB}
                          showShippingDetails={this.props.showShippingDetails}
                          orderCode={orderDetails.orderId}
                        />
                      )}
                      {products.statusDisplayMsg
                        .map(val => {
                          return val.key;
                        })
                        .includes(RETURN) && (
                        <OrderStatusHorizontal
                          trackingAWB={products.trackingAWB}
                          courier={products.reverseLogisticName}
                          statusMessageList={products.statusDisplayMsg.filter(
                            val => {
                              return val.key === RETURN;
                            }
                          )}
                        />
                      )}
                      {/* Block of code ends here */}
                    </div>
                  )}

                {products.awbPopupLink === AWB_POPUP_FALSE && (
                  <div className={styles.buttonHolder}>
                    <div className={styles.buttonHolderForUpdate}>
                      <div className={styles.replaceHolder}>
                        {products.isReturned &&
                          isOrderReturnable && (
                            <div
                              className={styles.review}
                              onClick={() =>
                                this.replaceItem(
                                  products.sellerorderno,
                                  orderDetails.paymentMethod,
                                  products.transactionId
                                )
                              }
                            >
                              <UnderLinedButton
                                label={PRODUCT_RETURN}
                                color="#000"
                              />
                            </div>
                          )}
                        {products.cancel && (
                          <div
                            className={styles.review}
                            onClick={() =>
                              this.cancelItem(
                                products.transactionId,
                                products.USSID,
                                products.sellerorderno
                              )
                            }
                          >
                            <UnderLinedButton
                              label={PRODUCT_CANCEL}
                              color="#000"
                            />
                          </div>
                        )}
                      </div>
                      {!isReturned && (
                        <div className={styles.reviewHolder}>
                          <div
                            className={styles.replace}
                            onClick={val =>
                              this.writeReview(products.productcode)
                            }
                          >
                            <UnderLinedButton
                              label="Write a review"
                              color="#ff1744"
                            />
                          </div>
                        </div>
                      )}
                    </div>
                  </div>
                )}
                {products.awbPopupLink === AWB_POPUP_TRUE && (
                  <div className={styles.buttonHolder}>
                    <div className={styles.buttonHolderForUpdate}>
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
                            label="Update Return Details"
                            color="#000"
                          />
                        </div>
                      </div>
                      <div className={styles.reviewHolder}>
                        {products.isReturned && (
                          <div
                            className={styles.review}
                            onClick={() =>
                              this.replaceItem(
                                products.sellerorderno,
                                orderDetails.paymentMethod,
                                products.transactionId
                              )
                            }
                          >
                            <UnderLinedButton
                              label={PRODUCT_RETURN}
                              color="#ff1744"
                            />
                          </div>
                        )}
                        {products.cancel && (
                          <div
                            className={styles.review}
                            onClick={() =>
                              this.cancelItem(
                                products.transactionId,
                                products.USSID,
                                products.sellerorderno
                              )
                            }
                          >
                            <UnderLinedButton
                              label={PRODUCT_CANCEL}
                              color="#ff1744"
                            />
                          </div>
                        )}
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
