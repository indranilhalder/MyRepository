import React from "react";
import styles from "./StoresLocationCard.css";
import PropTypes from "prop-types";
import Image from "../../xelpmoc-core/Image";

export default class StoresLocationCard extends React.Component {
  render() {
    return (
      <div
        className={styles.base}
        style={{
          backgroundImage: `url(${this.props.image})`,
          backgroundSize: "cover"
        }}
      >
        <div className={styles.ProductStockInnerBox}>
          <div className={styles.ProductStockHeading}>
            {this.props.headingText}
          </div>

          {this.props.label && (
            <div className={styles.ProductStockLabel}>{this.props.label}</div>
          )}
          {this.props.descriptionText && (
            <div className={styles.productDescriptionText}>
              {this.props.descriptionText}
            </div>
          )}
        </div>
      </div>
    );
  }
}
StoresLocationCard.propTypes = {
  headingText: PropTypes.string,
  label: PropTypes.string,
  image: PropTypes.string
};
