import React from "react";
import styles from "./Coupon.css";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
import couponIcon from "./img/coupon-1.svg";
import UnderLinedButton from "../../general/components/UnderLinedButton";

export default class Coupon extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.couponInnerBox}>
          <div className={styles.couponIcon}>
            <Icon image={couponIcon} size={35} />
          </div>
          <div className={styles.headingText}>{this.props.heading}</div>
          <div className={styles.button}>
            <UnderLinedButton
              label="Apply"
              onClick={() => {
                this.handleClick();
              }}
            />
          </div>
        </div>
      </div>
    );
  }
}
Coupon.propTypes = {
  couponImage: PropTypes.string,
  heading: PropTypes.string,
  buttonText: PropTypes.string
};
Coupon.defaultProps = {
  couponImage: couponIcon,
  heading: " Have a Coupon ?",
  label: "Apply"
};
