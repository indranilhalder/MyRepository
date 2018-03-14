import React from "react";
import styles from "./RefundDetails.css";
import PropTypes from "prop-types";
import OrderReturnAddressDetails from "./OrderReturnAddressDetails";
import OrderReturnDateAndTimeDetails from "./OrderReturnDateAndTimeDetails";
export default class RefundAddress extends React.Component {
  onClick() {
    if (this.props.onCancel) {
      this.props.onCancel();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <OrderReturnAddressDetails
          addressType={this.props.addressType}
          address={this.props.address}
          subAddress={this.props.subAddress}
        />

        <OrderReturnDateAndTimeDetails
          date={this.props.date}
          underlineButtonLabel={this.props.underlineButtonLabel}
          underlineButtonColour={this.props.underlineButtonColour}
          time={this.props.time}
          onCancel={() => this.onClick()}
        />
      </div>
    );
  }
}
RefundAddress.propTypes = {
  address: PropTypes.string,
  subAddress: PropTypes.string,
  addressType: PropTypes.string,
  underlineButtonLabel: PropTypes.string,
  underlineButtonColour: PropTypes.string,
  time: PropTypes.string,
  date: PropTypes.string,
  onCancel: PropTypes.func
};
