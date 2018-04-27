import React from "react";
import styles from "./MyCoupons.css";
import Icon from "../../xelpmoc-core/Icon";
import PropTypes from "prop-types";
import couponIcon from "../../general/components/img/coupon-1.svg";
export default class MyCoupons extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.couponInnerBox}>
          <div className={styles.couponIcon}>
            <Icon image={couponIcon} size={25} />
          </div>
          <div className={styles.headingText}>{this.props.heading}</div>
        </div>
        <div className={styles.couponNumber}>{this.props.couponNumber}</div>
        <div className={styles.lebelText}>
          (Long press here to copy the coupon code)
        </div>
        <div className={styles.couponFooter}>
          <div className={styles.couponFooterHolder}>
            <div className={styles.couponInformation}>
              {this.props.maxRedemption}
            </div>
            <div className={styles.couponData}>
              {this.props.maxRedemptionValue}
            </div>
          </div>
          <div className={styles.couponFooterHolder}>
            <div className={styles.couponInformation}>
              {this.props.creationDate}
            </div>
            <div className={styles.couponData}>
              {this.props.creationDateValue}
            </div>
          </div>
          <div className={styles.couponFooterHolder}>
            <div className={styles.couponInformation}>
              {this.props.expiryDate}
            </div>
            <div className={styles.couponData}>
              {this.props.expiryDateValue}
            </div>
          </div>
        </div>
      </div>
    );
  }
}
MyCoupons.propTypes = {
  bannerImage: PropTypes.string,
  bannerHeaderText: PropTypes.string,
  bannerText: PropTypes.string
};
