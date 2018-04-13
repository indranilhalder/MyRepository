import React from "react";
import { Redirect } from "react-router-dom";
import ReturnsFrame from "./ReturnsFrame";
import OrderCard from "./OrderCard";
import ReturnsToBank from "./ReturnsToBank";
import OrderReturnAddressDetails from "./OrderReturnAddressDetails";
import PropTypes from "prop-types";
import styles from "./ReturnStoreConfirmation.css";
import {
  RETURNS_PREFIX,
  RETURN_LANDING,
  RETURNS_REASON
} from "../../lib/constants";
export default class ReturnsStoreConfirmation extends React.Component {
  navigateToReturnLanding() {
    return (
      <Redirect
        to={`${RETURNS_PREFIX}/${
          this.orderCode
        }${RETURN_LANDING}${RETURNS_REASON}`}
      />
    );
  }
  render() {
    // Preventing user to open this page direct by hitting URL
    if (
      !this.props.location.state ||
      !this.props.location.state.authorizedRequest
    ) {
      return this.navigateToReturnLanding();
    }
    const data = this.props.returnProductDetails;

    return (
      <ReturnsFrame
        headerText="Return to store"
        onContinue={this.props.onContinue}
      >
        <OrderReturnAddressDetails />
        <div className={styles.card}>
          <OrderCard
            imageUrl={
              data &&
              data.orderProductWsDTO[0] &&
              data.orderProductWsDTO[0].imageURL
            }
            productName={`${data.orderProductWsDTO[0].productBrand} ${
              data.orderProductWsDTO[0].productName
            }`}
            price={data.orderProductWsDTO[0].price}
          >
            {data.orderProductWsDTO[0].quantity && (
              <div>Qty {data.orderProductWsDTO[0].quantity}</div>
            )}
          </OrderCard>
          <ReturnsToBank />
        </div>
      </ReturnsFrame>
    );
  }
}
ReturnsStoreConfirmation.propTypes = {
  onContinue: PropTypes.func,
  data: PropTypes.shape({
    orderProductWsDTO: PropTypes.arrayOf([
      PropTypes.shape({
        imageURL: PropTypes.string,
        productName: PropTypes.string,
        productBrand: PropTypes.string,
        price: PropTypes.string,
        quantity: PropTypes.string
      })
    ])
  })
};
