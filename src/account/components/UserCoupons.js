import React from "react";
import PropTypes from "prop-types";
import MyCoupons from "../../blp/components/MyCoupons";
import * as styles from "./UserCoupons.css";
import * as Cookie from "../../lib/Cookie";
import {
  LOGGED_IN_USER_DETAILS,
  CUSTOMER_ACCESS_TOKEN
} from "../../lib/constants";
export default class UserCoupons extends React.Component {
  componentDidMount() {
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (userDetails && customerCookie && this.props.getUserCoupons) {
      this.props.getUserCoupons();
    }
  }
  render() {
    const { userCoupons } = this.props;
    return (
      <div className={styles.base}>
        {userCoupons && userCoupons.unusedCouponsList ? (
          userCoupons.unusedCouponsList.map(coupon => (
            <div className={styles.cardHolder}>
              <MyCoupons
                displayToast={message => this.props.displayToast(message)}
                heading={coupon.description}
                image="../../general/components/img/coupon-1.svg"
                couponNumber={coupon.couponCode}
                maxRedemption="Max Redemption:"
                maxRedemptionValue={coupon.redemptionQtyLimitPerUser}
                creationDate="Creation Date"
                creationDateValue={coupon.couponCreationDate}
                expiryDate="Expiry Date"
                expiryDateValue={coupon.expiryDate}
              />
            </div>
          ))
        ) : (
          <div className={styles.noCoupon}>{"No Coupons"}</div>
        )}
      </div>
    );
  }
}
UserCoupons.propTypes = {
  userCoupons: PropTypes.shape({ unusedCouponsList: PropTypes.array })
};
