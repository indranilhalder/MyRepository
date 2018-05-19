import React from "react";
import { Redirect } from "react-router-dom";
import OrderCard from "./OrderCard";
import SelectReturnDate from "./SelectReturnDate";
import ReturnsFrame from "./ReturnsFrame";
import PropTypes from "prop-types";
import styles from "./ReturnModes.css";
import {
  QUICK_DROP,
  SCHEDULED_PICKUP,
  SELF_COURIER,
  RETURNS_PREFIX,
  RETURN_LANDING,
  RETURNS_REASON
} from "../../lib/constants";

export default class ReturnModes extends React.Component {
  handleSelect(val) {
    if (this.props.selectMode) {
      this.props.selectMode(val);
    }
  }
  handleCancel() {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  }
  navigateToReturnLanding() {
    return (
      <Redirect
        to={`${RETURNS_PREFIX}/${
          this.orderCode
        }${RETURN_LANDING}${RETURNS_REASON}`}
      />
    );
  }

  isReturnModesEnabled = () => {
    const data = this.props.returnProductDetails;
    if (
      (data && data.returnModes && data.returnModes.quickDrop) ||
      (data && data.returnModes && data.returnModes.schedulePickup) ||
      (data && data.returnModes && data.returnModes.selfCourier)
    ) {
      return true;
    }
    return false;
  };
  render() {
    // Preventing user to open this page direct by hitting URL
    if (
      !this.props.location.state ||
      !this.props.location.state.authorizedRequest
    ) {
      return this.navigateToReturnLanding();
    }
    const { productInfo } = this.props;
    const data = this.props.returnProductDetails;
    return (
      <ReturnsFrame
        headerText="Select mode of return"
        onCancel={() => this.handleCancel()}
      >
        <div className={styles.content}>
          <div className={styles.card}>
            <OrderCard
              imageUrl={
                data &&
                data.orderProductWsDTO &&
                data.orderProductWsDTO[0] &&
                data.orderProductWsDTO[0].imageURL
              }
              productName={
                productInfo && productInfo.product && productInfo.product.name
              }
              price={
                productInfo &&
                productInfo.totalPrice &&
                productInfo.totalPrice.value
              }
            >
              {productInfo &&
                productInfo.quantity && (
                  <div className={styles.quantity}>
                    Qty {productInfo.quantity}
                  </div>
                )}
            </OrderCard>
          </div>
          {this.isReturnModesEnabled() && (
            <div>
              {data.returnModes.quickDrop && (
                <SelectReturnDate
                  label="Return to store"
                  selected={this.props.selectedMode === QUICK_DROP}
                  selectItem={() => {
                    this.handleSelect(QUICK_DROP);
                  }}
                />
              )}
              {data.returnModes.schedulePickup && (
                <SelectReturnDate
                  label="Tata CliQ Pick Up"
                  selectItem={() => {
                    this.handleSelect(SCHEDULED_PICKUP);
                  }}
                  selected={this.props.selectedMode === SCHEDULED_PICKUP}
                />
              )}
              {data.returnModes.selfCourier && (
                <SelectReturnDate
                  selectItem={() => {
                    this.handleSelect(SELF_COURIER);
                  }}
                  label="Self Courier"
                  selected={this.props.selectedMode === SELF_COURIER}
                />
              )}
            </div>
          )}
          {!this.isReturnModesEnabled() && (
            <div className={styles.text}>
              sorry we are not able to process your request, contact customer
              care 90291 08282
            </div>
          )}
        </div>
      </ReturnsFrame>
    );
  }
}
ReturnModes.propTypes = {
  selectedMode: PropTypes.oneOf([QUICK_DROP, SCHEDULED_PICKUP, SELF_COURIER]),
  selectMode: PropTypes.func
};
