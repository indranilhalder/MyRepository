import React from "react";
import PropTypes from "prop-types";
import styles from "./SavedProduct.css";
import Button from "../../general/components/Button.js";
import Coupon from "../../general/components/Coupon.js";
import * as Cookie from "../../lib/Cookie.js";
import { COUPON_COOKIE } from "../../lib/constants.js";
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
    let couponText = this.props.couponHeading;
    let couponButtonText;

    if (this.props.appliedCouponCode) {
      couponText = `Coupon: ${this.props.appliedCouponCode}`;
      couponButtonText = "Change";
    }
    return (
      <div className={styles.base}>
        <div className={styles.applyCoupon}>
          <Coupon
            heading={couponText}
            onClick={() => this.onApplyCoupon()}
            couponButtonText={couponButtonText}
          />
        </div>
        <div className={styles.buttonHolder}>
          <div className={styles.button}>
            <Button
              type="hollow"
              height={40}
              label="View My Wish List"
              width={200}
              textStyle={{ color: "#212121", fontSize: 14 }}
              onClick={() => this.handleClick()}
            />
          </div>
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
SavedProduct.defaultProps = {
  couponHeading: "Have a Coupon ?",
  giftCardHeading: "Surprise for a special one ?",
  giftCardLabel: "Gift wrap for free"
};
