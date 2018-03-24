import React from "react";
import ReturnsFrame from "./ReturnsFrame";
import OrderCard from "./OrderCard";
import ReturnsToBank from "./ReturnsToBank";
import OrderReturnAddressDetails from "./OrderReturnAddressDetails";
import PropTypes from "prop-types";
import styles from "./ReturnStoreConfirmation.css";

export default class ReturnsStoreConfirmation extends React.Component {
  render() {
    console.log(this.props);
    const data = this.props.returnProductDetails;
    console.log(data);
    return (
      <ReturnsFrame
        headerText="Return to store"
        onContinue={this.props.onContinue}
      >
        <OrderReturnAddressDetails />
        <div className={styles.card}>
          <OrderCard
            productImage={data.orderProductWsDTO[0].imageURL}
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
