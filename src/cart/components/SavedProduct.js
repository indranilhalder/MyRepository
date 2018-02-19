import React from "react";
import PropTypes from "prop-types";
import styles from "./SavedProduct.css";
import Button from "../../general/components/Button.js";
import GiftCard from "../../general/components/GiftCard.js";
import Coupon from "../../general/components/Coupon.js";
export default class SavedProduct extends React.Component {
  handleClick() {
    if (this.props.saveProduct) {
      this.props.saveProduct();
    }
  }
  onApplyCoupon() {
    if (this.props.onApplyCoupon) {
      this.props.onApplyCoupon();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <Button
              type="hollow"
              height={40}
              label="View saved products"
              width={200}
              textStyle={{ color: "#212121", fontSize: 14 }}
              onClick={() => this.handleClick()}
            />
          </div>
        </div>
        <div className={styles.applyCoupon}>
          <Coupon
            heading={this.props.cuponHeading}
            onClick={() => this.onApplyCoupon()}
          />
        </div>
        <div className={styles.giftCard}>
          <GiftCard
            heading={this.props.giftCardHeading}
            lable={this.props.giftCardLabel}
          />
        </div>
      </div>
    );
  }
}
SavedProduct.propTypes = {
  saveProduct: PropTypes.func,
  onApplyCoupon: PropTypes.func,
  cuponHeading: PropTypes.string,
  giftCardHeading: PropTypes.string,
  giftCardLabel: PropTypes.string
};
