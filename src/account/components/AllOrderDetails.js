import React from "react";
import styles from "./AllOrderDetails.css";
import OrderPlacedAndId from "./OrderPlacedAndId.js";
import OrderCard from "./OrderCard.js";
import PriceAndLink from "./PriceAndLink.js";
import OrderDelivered from "./OrderDelivered.js";
import PropTypes from "prop-types";
import Button from "../../general/components/Button";
import format from "date-fns/format";

import { Redirect } from "react-router-dom";
import * as Cookie from "../../lib/Cookie";
import {
  MY_ACCOUNT,
  ORDER,
  ORDER_CODE,
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  LOGIN_PATH,
  ORDER_HISTORY
} from "../../lib/constants";

import { HOME_ROUTER } from "../../lib/constants";
import throttle from "lodash.throttle";
const dateFormat = "DD MMM YYYY";
const SUFFIX = `&isTextSearch=false&isFilter=false`;
const SCROLL_CHECK_INTERVAL = 500;
const OFFSET_BOTTOM = 800;
export default class AllOrderDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      showOrder: null
    };
  }
  onClickImage(productCode) {
    if (productCode) {
      this.props.history.push(`/p-${productCode.toLowerCase()}`);
    }
  }
  onViewDetails(orderId) {
    this.props.history.push(`${MY_ACCOUNT}${ORDER}/?${ORDER_CODE}=${orderId}`);
  }
  componentDidMount() {
    if (this.props.shouldCallHeaderContainer) {
      this.props.setHeaderText(ORDER_HISTORY);
    }
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (userDetails && customerCookie) {
      this.throttledScroll = this.handleScroll();
      window.addEventListener("scroll", this.throttledScroll);
      this.props.getAllOrdersDetails();
    }
  }
  componentWillUnmount() {
    this.props.clearOrderDetails();
    window.removeEventListener("scroll", this.throttledScroll);
  }

  componentDidUpdate() {
    if (this.props.shouldCallHeaderContainer) {
      this.props.setHeaderText(ORDER_HISTORY);
    }
  }

  renderToContinueShopping() {
    this.props.history.push(HOME_ROUTER);
  }
  handleScroll = () => {
    return throttle(() => {
      if (
        this.props.profile.orderDetails &&
        (this.props.profile.orderDetails.currentPage + 1) * 3 <
          this.props.profile.orderDetails.totalNoOfOrders
      ) {
        const windowHeight =
          "innerHeight" in window
            ? window.innerHeight
            : document.documentElement.offsetHeight;
        const body = document.body;
        const html = document.documentElement;
        const docHeight = Math.max(
          body.scrollHeight,
          body.offsetHeight,
          html.clientHeight,
          html.scrollHeight,
          html.offsetHeight
        );
        const windowBottom = windowHeight + window.pageYOffset;
        if (
          windowBottom >= docHeight - OFFSET_BOTTOM &&
          !this.props.profile.loading
        ) {
          this.props.paginate(
            this.props.profile.orderDetails.pageSize + 1,
            SUFFIX
          );
        }
      }
    }, SCROLL_CHECK_INTERVAL);
  };
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
            onClick={() => this.renderToContinueShopping()}
          />
        </div>
      </div>
    );
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
    const orderDetails = this.props.profile.orderDetails;
    let orderListWithoutEgv =
      orderDetails &&
      orderDetails.orderData.filter(order => {
        return order.isEgvOrder === false;
      });
    return (
      <div className={styles.base}>
        {orderListWithoutEgv
          ? orderListWithoutEgv.map((orderDetails, i) => {
              let formattedDate = "";
              if (orderDetails && orderDetails.orderDate) {
                formattedDate = format(orderDetails.orderDate, dateFormat);
              }
              return (
                <div className={styles.order} key={i}>
                  <div className={styles.orderIdHolder}>
                    <OrderPlacedAndId
                      placedTime={formattedDate}
                      orderId={orderDetails && orderDetails.orderId}
                    />
                  </div>
                  <OrderCard
                    imageUrl={
                      orderDetails &&
                      orderDetails.products &&
                      orderDetails.products[0].imageURL
                    }
                    price={orderDetails && orderDetails.totalOrderAmount}
                    discountPrice={""}
                    productName={
                      orderDetails &&
                      orderDetails.products &&
                      orderDetails.products[0].productName
                    }
                    onClick={() =>
                      this.onClickImage(
                        orderDetails &&
                          orderDetails.products &&
                          orderDetails.products[0] &&
                          orderDetails.products.length &&
                          orderDetails.products[0].productcode
                      )
                    }
                  />
                  {!orderDetails.isEgvOrder && (
                    <PriceAndLink
                      onViewDetails={() =>
                        this.onViewDetails(orderDetails && orderDetails.orderId)
                      }
                      price={orderDetails && orderDetails.totalOrderAmount}
                    />
                  )}
                  {orderDetails &&
                    orderDetails.billingAddress && (
                      <OrderDelivered
                        deliveredAddress={`${
                          orderDetails &&
                          orderDetails.billingAddress.addressLine1
                            ? orderDetails.billingAddress.addressLine1
                            : ""
                        } ${
                          orderDetails && orderDetails.billingAddress.town
                            ? orderDetails.billingAddress.town
                            : ""
                        } ${
                          orderDetails && orderDetails.billingAddress.state
                            ? orderDetails.billingAddress.state
                            : ""
                        } ${
                          orderDetails && orderDetails.billingAddress.postalcode
                            ? orderDetails.billingAddress.postalcode
                            : ""
                        }`}
                      />
                    )}
                </div>
              );
            })
          : this.renderNoOrder()}
      </div>
    );
  }
}
AllOrderDetails.propTypes = {
  shouldCallHeaderContainer: PropTypes.bool,
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
  )
};
AllOrderDetails.defaultProps = {
  shouldCallHeaderContainer: true
};
