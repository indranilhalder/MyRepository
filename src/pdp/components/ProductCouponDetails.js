import React, { Component } from "react";
import CuponDetails from "./CuponDetails.js";
import SlideModal from "../../general/components/SlideModal";
import styles from "./ProductCouponDetails.css";
const COUPON_HEADER = "Apply Coupon";
const COUPON_SUB_HEADER =
  "You can avail the below offer/coupon during checkout";

class ProductCouponDetails extends Component {
  render() {
    return (
      <div className={styles.base}>
        <SlideModal {...this.props}>
          <div className={styles.couponHeader}>{COUPON_HEADER}</div>
          <div className={styles.couponSubHeader}>{COUPON_SUB_HEADER}</div>
          <CuponDetails
            productOfferPromotion={this.props.productOfferPromotion}
          />
        </SlideModal>
      </div>
    );
  }
}

export default ProductCouponDetails;
