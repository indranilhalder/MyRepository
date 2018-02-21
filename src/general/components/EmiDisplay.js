import React from "react";
import styles from "./EmiDisplay.css";
import { Icon } from "xelpmoc-core";
import image from "./img/check.svg";
import PropTypes from "prop-types";
export default class EmiDisplay extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.emiText}>{this.props.emi}</div>
        <div className={styles.price}>
          {this.props.price}
          <span className={styles.month}>/ {this.props.emiRate}</span>
        </div>
        <div className={styles.checkBoxHolder}>
          <Icon image={image} />
        </div>
      </div>
    );
  }
}
EmiDisplay.propTypes = {
  emi: PropTypes.string,
  emiRate: PropTypes.string,
  price: PropTypes.string
};
