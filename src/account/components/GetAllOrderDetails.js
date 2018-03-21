import React from "react";
import styles from "./GetAllOrderDetails.css";
import OrderPlacedAndId from "./OrderPlacedAndId.js";
import OrderCard from "./OrderCard.js";
import PriceAndLink from "./PriceAndLink.js";
import OrderDelivered from "./OrderDelivered.js";
import PropTypes from "prop-types";
import Button from "../../general/components/Button";
import moment from "moment";
import { MY_ACCOUNT_PAGE, ORDER_PREFIX } from "../../lib/constants";
import { HOME_ROUTER } from "../../lib/constants";
const dateFormat = "DD MMM YYYY";
export default class AllOrderDetails extends React.Component {
  onViewDetails(orderId) {
    this.props.history.push(`${MY_ACCOUNT_PAGE}${ORDER_PREFIX}:${orderId}`);
  }
  componentDidMount() {
    this.props.getAllOrdersDetails();
  }
  renderToContinueShopping() {
    this.props.history.push(HOME_ROUTER);
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
            onClick={() => this.renderToContinueShopping()}
          />
        </div>
      </div>
    );
  }
  render() {
    const orderDetails = this.props.profile.orderDetails;
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
                    onViewDetails={() =>
                      this.onViewDetails(orderDetails.orderId)
                    }
                    price={orderDetails.totalOrderAmount}
                  />
                  <OrderDelivered
                    deliveredAddress={`${
                      orderDetails.billingAddress.addressLine1
                    } ${orderDetails.billingAddress.town} ${
                      orderDetails.billingAddress.state
                    } ${orderDetails.billingAddress.postalcode}`}
                  />
                </div>
              );
            })
          : this.renderNoOrder()}
      </div>
    );
  }
}
AllOrderDetails.propTypes = {
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
