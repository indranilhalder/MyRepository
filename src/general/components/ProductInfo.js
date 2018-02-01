import React from "react";
import styles from "./ProductInfo.css";
import PropTypes from "prop-types";
import StarRating from "./StarRating.js";
export default class ProductInfo extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        {this.props.date && (
          <div className={styles.deliveryDate}>
            Get it by&nbsp;<span className={styles.date}>
              {this.props.date}
            </span>
          </div>
        )}
        {this.props.offer && (
          <div className={styles.offer}>
            <span>{`${this.props.offer}%`}</span>&nbsp;offer from&nbsp;
            <span>{`Rs. ${this.props.price}`}</span>
          </div>
        )}
        {this.props.dynamicRating && (
          <StarRating dynamicRating={this.props.dynamicRating}>
            <div className={styles.customerReview}>{`(${
              this.props.review
            })`}</div>
          </StarRating>
        )}
      </div>
    );
  }
}
ProductInfo.propTypes = {
  date: PropTypes.string,
  offer: PropTypes.string,
  price: PropTypes.string,
  dynamicRating: PropTypes.number,
  review: PropTypes.string
};
