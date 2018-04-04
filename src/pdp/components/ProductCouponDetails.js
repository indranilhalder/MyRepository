import React, { Component } from "react";
import CuponDetails from "./CuponDetails.js";
import SlideModal from "../../general/components/SlideModal";
import SearchCupon from "./SearchCupon.js";
import PropTypes from "prop-types";
import GridSelect from "../../general/components/GridSelect";
import StaticDarkHeader from "../../general/components/StaticDarkHeader";
import styles from "./ProductCouponDetails.css";
import * as Cookie from "../../lib/Cookie.js";
import { COUPON_COOKIE } from "../../lib/constants.js";

class ProductCouponDetails extends Component {
  constructor(props) {
    super(props);
    this.state = {
      couponVal: ""
    };
  }

  applyUserCoupon = couponCode => {
    let couponCookie = Cookie.getCookie(COUPON_COOKIE);
    if (couponCode) {
      if (couponCookie) {
        this.props.releaseUserCoupon(couponCookie, couponCode);
      } else {
        if (this.props.applyUserCoupon) {
          this.props.applyUserCoupon(couponCode);
        }
      }
    }
  };

  setUserCoupons = couponCode => {
    if (couponCode.length > 0) {
      this.setState({ couponVal: couponCode[0] });
    } else {
      this.setState({ couponVal: "" });
    }
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
            {this.props.opencouponsList &&
              this.props.opencouponsList.map((value, i) => {
                return (
                  <CuponDetails
                    promotionTitle={value.couponName}
                    promotionDetail={value.description}
                    dateTime={value.couponExpiryDate}
                    amount={value.maxDiscount}
                    key={i}
                    couponType={value.couponType}
                    value={value.couponCode}
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
