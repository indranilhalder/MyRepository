import React from "react";
import styles from "./SellerCard.css";
import CheckBox from "../../general/components/CheckBox";
import PropTypes from "prop-types";
export default class SellerCard extends React.Component {
  handleClick(val) {
    if (this.props.selectItem && !this.props.disabled) {
      this.props.selectItem(val);
    }
  }
  render() {
    let priceClass = styles.priceHolder;
    if (
      this.props.discountPrice &&
      this.props.Price !== this.props.discountPrice
    ) {
      priceClass = styles.priceCancelled;
    }

    return (
      <div className={styles.base} onClick={() => this.handleClick(this.props)}>
        <div className={styles.textBox}>
          <div className={this.props.disabled ? styles.faded : styles.buffer}>
            <div className={styles.heading}>
              {this.props.heading}
              {!this.props.disabled && (
                <span className={styles.checkCircle}>
                  <CheckBox selected={this.props.selected} />
                </span>
              )}
            </div>
            <div className={styles.priceTitle}>{this.props.priceTitle}:</div>
            {this.props.discountPrice &&
              this.props.discountPrice !== this.props.price && (
                <div className={styles.discount}>
                  {this.props.discountPrice}
                </div>
              )}
            {this.props.price && (
              <span className={priceClass}>{this.props.price}</span>
            )}
            {this.props.hasEmi && (
              <div className={styles.offerText}>EMI Available</div>
            )}
          </div>
        </div>
        <div className={styles.textBox2}>
          <div className={this.props.disabled ? styles.faded : styles.buffer}>
            <div className={styles.priceTitle}>{this.props.deliveryText}</div>
            {this.props.eligibleDeliveryModes &&
              this.props.eligibleDeliveryModes.map((val, i) => {
                return (
                  <div className={styles.shippingText}>
                    {val.name}
                    {val.description && <span>-</span>}
                    {val.description}
                  </div>
                );
              })}
            {this.props.hasCod && (
              <div className={styles.offerText}>{this.props.cashText}</div>
            )}
          </div>
        </div>
      </div>
    );
  }
}

SellerCard.propTypes = {
  heading: PropTypes.string,
  priceTitle: PropTypes.string,
  disabled: PropTypes.bool,
  discountPrice: PropTypes.string,
  deliveryText: PropTypes.string,
  price: PropTypes.string,
  hasEmi: PropTypes.bool,
  cashText: PropTypes.string,
  selected: PropTypes.bool,
  onClick: PropTypes.func,
  hasCod: PropTypes.bool
};
