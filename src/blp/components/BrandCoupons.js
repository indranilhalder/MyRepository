import React from "react";
import PropTypes from "prop-types";
import styles from "./BrandCoupons.css";
import { Image } from "xelpmoc-core";
export default class BrandCoupons extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.couponInerBox}>
          <div
            className={
              this.props.time ? styles.headingTextWithTime : styles.headingText
            }
          >
            {this.props.heading}
          </div>
          {this.props.time && (
            <div className={styles.timeText}>{this.props.time}</div>
          )}
        </div>
        <div className={styles.label}>{this.props.label}</div>
        <div className={styles.imageHolder}>
          <div className={styles.image}>
            <Image image={this.props.image} />
          </div>
        </div>
      </div>
    );
  }
}
BrandCoupons.propTypes = {
  label: PropTypes.string,
  time: PropTypes.string,
  image: PropTypes.string
};
