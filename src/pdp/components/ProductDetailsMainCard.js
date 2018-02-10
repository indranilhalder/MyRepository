import React from "react";
import styles from "./ProductDetailsMainCard.css";
import StarRating from "../../general/components/StarRating.js";
import { Icon } from "xelpmoc-core";
import arrowIcon from "../../general/components/img/arrow.svg";
import PropTypes from "prop-types";
export default class ProductDetailsMainCard extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    let classDiscountPrice = styles.discountPrice;
    if (
      this.props.discountPrice &&
      this.props.price !== this.props.discountPrice
    ) {
      classDiscountPrice = styles.priceCancelled;
    }
    return (
      <div className={styles.base}>
        <div className={styles.productInfo}>
          <div className={styles.productDescriptionSection}>
            <div className={styles.productName}>{this.props.productName}</div>
            <div className={styles.productDescription}>
              {this.props.productDescription}
            </div>
          </div>
          <div className={styles.productPriceSection}>
            <div className={styles.price}>{`Rs. ${this.props.price}`}</div>
            {this.props.discountPrice &&
              this.props.discountPrice !== this.props.price && (
                <div className={classDiscountPrice}>
                  {`Rs. ${this.props.discountPrice}`}
                </div>
              )}
          </div>
        </div>
        <div className={styles.ratingHolder}>
          {this.props.averageRating && (
            <StarRating averageRating={this.props.averageRating}>
              {this.props.averageRating && (
                <div
                  className={styles.ratingText}
                  onClick={() => this.handleClick()}
                >
                  Rating {this.props.averageRating}/5
                </div>
              )}
              <div className={styles.arrowHolder}>
                <Icon image={arrowIcon} size={15} />
              </div>
            </StarRating>
          )}
        </div>
      </div>
    );
  }
}
ProductDetailsMainCard.propTypes = {
  productName: PropTypes.string,
  productDescription: PropTypes.string,
  price: PropTypes.string,
  discountPrice: PropTypes.string,
  averageRating: PropTypes.number,
  onClick: PropTypes.func
};
