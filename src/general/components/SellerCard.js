import React from "react";
import styles from "./SellerCard.css";
import CheckBox from "./CheckBox.js";
import PropTypes from "prop-types";

export default class SellerCard extends React.Component {
  render() {
    let priceClass = styles.priceHolder;

    if (
      this.props.discountPrice &&
      this.props.Price !== this.props.discountPrice
    ) {
      priceClass = styles.priceCancelled;
    }
    return (
      <div className={styles.base}>
        <div className={styles.textBox}>
          <div className={styles.heading}>
            {this.props.heading}
            <span className={styles.checkCircle}>
              <CheckBox selected={this.props.selected} />
            </span>
          </div>
          <div className={styles.priceTitle}>{this.props.priceTitle}</div>
          {this.props.discountPrice &&
            this.props.discountPrice !== this.props.price && (
              <div className={styles.discount}>
                {`Rs. ${this.props.discountPrice}`}
              </div>
            )}
          {this.props.price && (
            <span className={priceClass}>{`Rs. ${this.props.price}`}</span>
          )}
          <div className={styles.offerText}>{this.props.offerText}</div>
        </div>
        <div className={styles.textBox2}>
          <div className={styles.heading}>{this.props.deliveryText}</div>
          <div className={styles.shippingText}>{this.props.shippingText}</div>
          <div className={styles.offerText}>{this.props.cashText}</div>
          <div className={styles.offerText}>{this.props.policyText}</div>
        </div>
      </div>
    );
  }
}

SellerCard.propTypes = {
  heading: PropTypes.string,
  priceTitle: PropTypes.string,
  discountPrice: PropTypes.string,
  deliveryText: PropTypes.string,
  price: PropTypes.string,
  shippingText: PropTypes.string,
  cashText: PropTypes.string,
  policyText: PropTypes.string,
  offerText: PropTypes.string,
  selected: PropTypes.bool
};
