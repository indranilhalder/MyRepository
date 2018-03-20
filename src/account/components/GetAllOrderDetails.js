import React from "react";
import styles from "./GetAllOrderDetails.css";
import OrderPlacedAndId from "./OrderPlacedAndId.js";
import OrderCard from "./OrderCard.js";
import PriceAndLink from "./PriceAndLink.js";
import OrderDelivered from "./OrderDelivered.js";
import OrderReturn from "./OrderReturn.js";
import ViewDetail from "./viewDetails.js";
import WriteReview from "../../pdp/components/WriteReview";
import PropTypes from "prop-types";
import moment from "moment";
const dateFormat = "DD MMM YYYY";

export default class GetAllOrderDetails extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      viewDetails: false,
      viewData: false,
      visible: false
    };
  }
  onViewDetails(viewDetails) {
    this.setState({ viewDetails: true });
    this.setState({ viewData: viewDetails });
  }
  writeReview() {
    this.setState({ visible: true });
  }
  onSubmit = productReview => {
    this.props.addProductReview(
      this.props.productDetails.productListingId,
      productReview
    );
  };
  componentDidMount() {
    this.props.getAllOrdersDetails();
  }
  render() {
    let orderDetails = this.props.profile.orderDetails;
    return (
      <div className={styles.base}>
        {!this.state.viewDetails &&
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
                  onViewDetails={() => this.onViewDetails(orderDetails)}
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
                    buttonLabel={""}
                    underlineButtonLabel={this.props.underlineButtonLabel}
                    underlineButtonColour={this.props.underlineButtonColour}
                    isEditable={true}
                    replaceItem={() => this.replaceItem()}
                    writeReview={() => this.writeReview()}
                  />
                </div>
                {this.state.visible && <WriteReview onSubmit={this.onSubmit} />}
              </div>
            );
          })}
        {this.state.viewDetails && <ViewDetail data={this.state.viewData} />}
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
