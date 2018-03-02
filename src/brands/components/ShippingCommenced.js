import React from "react";
import styles from "./ShippingCommenced.css";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
import couponIcon from "../../general/components/img/coupon-1.svg";

export default class ShippingCommenced extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.couponInnerBox}>
          <div className={styles.couponIcon}>
            <Icon image={couponIcon} size={25} />
          </div>
          <div className={styles.headingText}>{this.props.heading}</div>
        </div>
        <div className={styles.lebelText}>{this.props.label}</div>
      </div>
    );
  }
}
ShippingCommenced.propTypes = {
  heading: PropTypes.string,
  label: PropTypes.string,
  image: PropTypes.string
};
