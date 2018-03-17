import React, { Component } from "react";
import CuponDetails from "./CuponDetails.js";
import SlideModal from "../../general/components/SlideModal";
import SearchCupon from "./SearchCupon.js";
import PropTypes from "prop-types";
import GridSelect from "../../general/components/GridSelect";
import StaticDarkHeader from "../../general/components/StaticDarkHeader";
import styles from "./ProductCouponDetails.css";
const COUPON_HEADER = "Apply Coupon";
const COUPON_SUB_HEADER =
  "You can avail the below offer/coupon during checkout";

class ProductCouponDetails extends Component {
  constructor(props) {
    super(props);
    this.state = {
      couponVal: ""
    };
  }
  applyUserCoupon = couponCode => {
    if (this.props.applyUserCoupon) {
      this.props.applyUserCoupon(couponCode);
    }
  };

  setUserCoupons = couponCode => {
    this.setState({ couponVal: couponCode });
  };
  releaseUserCoupon = couponCode => {
    if (this.props.releaseUserCoupon) {
      this.props.releaseUserCoupon(couponCode);
    }
  };

  render() {
    return (
      <SlideModal {...this.props}>
        <div className={styles.base}>
          <div className={styles.header}>
            <StaticDarkHeader text="Apply Coupon" />
          </div>
          <div className={styles.searchHolder}>
            <SearchCupon
              couponCode={this.state.couponVal}
              getValue={coupon => this.setUserCoupons(coupon)}
              applyUserCoupon={couponCode => this.applyUserCoupon(couponCode)}
            />
          </div>
          <GridSelect
            elementWidthMobile={100}
            offset={0}
            limit={1}
            onSelect={val => this.setUserCoupons(val)}
          >
            {this.props.productOfferPromotion &&
              this.props.productOfferPromotion.map((value, i) => {
                return (
                  <CuponDetails
                    promotionTitle={value.promotionTitle}
                    promotionDetail={value.promotionDetail}
                    dateTime={value.validTill.formattedDate}
                    amount={value.validTill.amount}
                    key={i}
                    value={value.promoID}
                  />
                );
              })}
          </GridSelect>
        </div>
      </SlideModal>
    );
  }
}

ProductCouponDetails.propTypes = {
  productOfferPromotion: PropTypes.array
};

export default ProductCouponDetails;
