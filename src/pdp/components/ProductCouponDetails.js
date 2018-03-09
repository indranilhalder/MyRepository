import React, { Component } from "react";
import CuponDetails from "./CuponDetails.js";
import SlideModal from "../../general/components/SlideModal";
import styles from "./ProductCouponDetails.css";
import PropTypes from "prop-types";
import GridSelect from "../../general/components/GridSelect";
const COUPON_HEADER = "Apply Coupon";
const COUPON_SUB_HEADER =
  "You can avail the below offer/coupon during checkout";

class ProductCouponDetails extends Component {
  applyUserCoupon = couponCode => {
    if (this.props.applyCoupon) {
      this.props.applyCoupon(couponCode);
    }
  };
  releaseUserCoupon = couponCode => {
    if (this.props.releaseCoupon) {
      this.props.releaseCoupon(couponCode);
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <SlideModal {...this.props}>
          <div className={styles.couponHeader}>{COUPON_HEADER}</div>
          <div className={styles.couponSubHeader}>{COUPON_SUB_HEADER}</div>
          <GridSelect
            elementWidthMobile={100}
            offset={0}
            limit={1}
            onSelect={val => this.applyUserCoupon(val)}
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
        </SlideModal>
      </div>
    );
  }
}

ProductCouponDetails.propTypes = {
  productOfferPromotion: PropTypes.array
};

ProductCouponDetails.defaultProps = {
  productOfferPromotion: [
    {
      promoID: "1234",
      promotionTitle: "Limited time offer",
      promotionDetail:
        "<span>Enter coupon code <b>EXTRA 20<b> at checkout for an extra 20% off on sales price</span>",
      validTill: {
        formattedDate: "13th Feb 2018",
        date: "2018-02-13 00:00:00"
      },
      showTimer: true
    },
    {
      promoID: "1235",
      promotionTitle: "HDFC",
      promotionDetail:
        "<span>Get 20% off (upto Rs. 1000)on payment via HDFC Bank Credit/Debit card</span>",
      validTill: -{
        formattedDate: "25th Nov 2018",
        date: "2018-11-25 01:01:17"
      },
      showTimer: false
    },
    {
      promoID: "123443",
      promotionTitle: "Limited time offer",
      promotionDetail:
        "<span>Enter coupon code <b>Festive 20<b> at checkout for an extra 20% off on sales price</span>",
      validTill: -{
        formattedDate: "25th Nov 2019",
        date: "2018-11-25 01:01:17"
      },
      showTimer: false
    },
    {
      promoID: "123423",
      promotionTitle: "Limited time offer",
      promotionDetail:
        "<span>Enter coupon code <b>EXTRA 20<b> at checkout for an extra 20% off on sales price</span>",
      validTill: -{
        formattedDate: "25th Nov 2018",
        date: "2018-11-25 01:01:17"
      },
      showTimer: false
    }
  ]
};

export default ProductCouponDetails;
