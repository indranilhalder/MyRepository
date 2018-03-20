import React from "react";
import styles from "./GetAllOrderDetails.css";
import OrderPlacedAndId from "./OrderPlacedAndId.js";
import OrderCard from "./OrderCard.js";
import PriceAndLink from "./PriceAndLink.js";
import OrderDelivered from "./OrderDelivered.js";
import ViewDetail from "./viewDetails.js";
import PropTypes from "prop-types";
import moment from "moment";
const dateFormat = "DD MMM YYYY";

export default class GetAllOrderDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      viewDetails: false,
      onClickViewDetails: false
    };
  }
  onViewDetails(orderID) {
    this.setState({ onClickViewDetails: true });
    if (this.props.fetchOrderDetails) {
      this.props.fetchOrderDetails(orderID);
    }
  }
  requestInvioice() {
    if (this.props.requestInvioice) {
      this.props.requestInvioice();
    }
  }

  componentDidMount() {
    this.props.getAllOrdersDetails();
  }
  render() {
    let orderDetails = this.props.profile.orderDetails;
    let fetchOrderDetails = this.props.profile.fetchOrderDetails;
    return (
      <div className={styles.base}>
        {!this.state.onClickViewDetails &&
          orderDetails &&
          orderDetails.orderData.map((orderDetails, i) => {
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
                  onViewDetails={() => this.onViewDetails(orderDetails.orderId)}
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
          })}
        {this.state.onClickViewDetails &&
          fetchOrderDetails && (
            <ViewDetail
              data={fetchOrderDetails}
              requestInvioice={() => this.requestInvioice()}
            />
          )}
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
  requestInvioice: PropTypes.func
};
