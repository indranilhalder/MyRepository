import React from "react";
import styles from "./MyCoupons.css";
import { Icon } from "xelpmoc-core";
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
        <div className={styles.lebelText}>{this.props.label}</div>
        <div className={styles.coupounFooter}>
          <div className={styles.coupounFooterHolder}>
            <div className={styles.coupounInformation}>
              {this.props.maxRedemption}
            </div>
            <div className={styles.coupounInformation}>
              {this.props.maxRedemptionValue}
            </div>
            <div className={styles.coupounInformation}>
              {this.props.creationDate}
            </div>
            <div className={styles.coupounInformation}>
              {this.props.creationDateValue}
            </div>
            <div className={styles.coupounInformation}>
              {this.props.expiryDate}
            </div>
            <div className={styles.coupounInformation}>
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
