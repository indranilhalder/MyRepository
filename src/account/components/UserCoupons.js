import React from "react";
import PropTypes from "prop-types";
import BrandCoupons from "../../blp/components/BrandCoupons";
import ShippingCommenced from "../../blp/components/ShippingCommenced";
import MyCoupons from "../../blp/components/MyCoupons";
import * as styles from "./UserCoupons.css";
export default class UserCoupons extends React.Component {
  render() {
    const { userCoupons } = this.props;
    return (
      <div className={styles.base}>
        {userCoupons &&
          userCoupons.unusedCouponsList &&
          userCoupons.unusedCouponsList.map(coupon => (
            <div className={styles.cardHolder}>
              <MyCoupons
                heading="2 Rs. Voucher"
                image="../../general/components/img/coupon-1.svg"
                couponNumber={coupon.couponCode}
                label={coupon.description}
                maxRedemption="Max Redemption:"
                maxRedemptionValue={coupon.redemptionQtyLimitPerUser}
                creationDate="Creation Date"
                creationDateValue={coupon.couponCreationDate}
                expiryDate="Expiry Date"
                expiryDateValue={coupon.expiryDate}
              />
            </div>
          ))}
      </div>
    );
  }
}
UserCoupons.propTypes = {
  userCoupons: PropTypes.shape({ unusedCouponsList: PropTypes.array })
};
