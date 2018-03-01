import React from "react";
import PropTypes from "prop-types";
import styles from "./BrandCoupons.css";
import { Image } from "xelpmoc-core";

export default class BrandCoupons extends React.Component {
  onSelect() {
    if (this.props.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    console.log(this.props);
    return (
      <div className={styles.base} onClick={() => this.onSelect()}>
        <div className={styles.headingText}>{this.props.heading}</div>
        <div className={styles.label}>{this.props.label}</div>
        {/* <div className={styles.couponInerBox}> */}
        <div className={styles.imageHolder}>
          <Image image={this.props.image} />
          {/* </div> */}
        </div>
      </div>
    );
  }
}
BrandCoupons.propTypes = {
  label: PropTypes.string,
  image: PropTypes.string
};
