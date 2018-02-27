import React from "react";
import styles from "./StoresLocationCard.css";
import PropTypes from "prop-types";
import { Image } from "xelpmoc-core";

export default class StoresLocationCard extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.productInformation} />
        <div className={styles.image}>
          <Image image={this.props.image} />
        </div>
        <div className={styles.ProductStockInnerBox}>
          <div className={styles.ProductStockHeading}>
            {this.props.headingText}
          </div>
          <div className={styles.ProductStockLabel}>{this.props.label}</div>
        </div>
      </div>
    );
  }
}
StoresLocationCard.propTypes = {
  headingText: PropTypes.string,
  label: PropTypes.string
};
