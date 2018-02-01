import React from "react";
import styles from "./ProductInfo.css";
import PropTypes from "prop-types";
import StarRating from "./StarRating.js";
export default class ProductInfo extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        {this.props.bestDeliveryInfo && (
          <div className={styles.bestDeliveryInfo}>
            Get it by&nbsp;<span className={styles.date}>
              {this.props.bestDeliveryInfo}
            </span>
          </div>
        )}
        {this.props.offerText && (
          <div className={styles.offerText}>{this.props.offerText}</div>
        )}
        {this.props.averageRating && (
          <StarRating averageRating={this.props.averageRating}>
            {this.props.totalNoOfReviews && (
              <div className={styles.totalNoOfReviews}>{`(${
                this.props.totalNoOfReviews
              })`}</div>
            )}
          </StarRating>
        )}
      </div>
    );
  }
}
ProductInfo.propTypes = {
  bestDeliveryInfo: PropTypes.string,
  offerText: PropTypes.string,
  averageRating: PropTypes.number,
  totalNoOfReviews: PropTypes.string
};
