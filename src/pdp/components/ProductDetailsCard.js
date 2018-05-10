import React from "react";
import styles from "./ProductDetailsCard.css";
import ProductImage from "../../general/components/ProductImage.js";
import StarRating from "../../general/components/StarRating.js";
import PropTypes from "prop-types";
import { RUPEE_SYMBOL } from "../../lib/constants";
export default class ProductDetailsCard extends React.Component {
  onClickImage() {
    if (this.props.onClickImage) {
      this.props.onClickImage();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.productImageHolder}>
          <ProductImage
            image={this.props.productImage}
            onClickImage={() => this.onClickImage()}
          />
        </div>
        <div className={styles.productDescriptionHolder}>
          {this.props.productName && (
            <div className={styles.productName}>{this.props.productName}</div>
          )}
          <div className={styles.producMaterial}>
            {this.props.productMaterial}
          </div>

          <div className={styles.productPrice}>
            {this.props.price && (
              <span className={styles.onPrice}>
                {this.props.price.toString().includes(RUPEE_SYMBOL)
                  ? this.props.price
                  : `${RUPEE_SYMBOL}${this.props.price}`}
              </span>
            )}
            {this.props.discountPrice &&
              this.props.discountPrice !== this.props.price && (
                <del>
                  <span className={styles.deletePrice}>
                    {this.props.discountPrice.toString().includes(RUPEE_SYMBOL)
                      ? this.props.discountPrice
                      : `${RUPEE_SYMBOL}${this.props.discountPrice}`}
                  </span>
                </del>
              )}
          </div>
          <div className={styles.displayRating}>
            {this.props.averageRating && (
              <StarRating averageRating={this.props.averageRating}>
                {this.props.totalNoOfReviews && (
                  <div className={styles.noOfReviews}>{`(${
                    this.props.totalNoOfReviews
                  })`}</div>
                )}
              </StarRating>
            )}
          </div>
          {this.props.averageRating && (
            <div className={styles.displayRatingText}>
              Rating <span>{this.props.averageRating}/5</span>
            </div>
          )}
        </div>
      </div>
    );
  }
}
ProductDetailsCard.propTypes = {
  productImage: PropTypes.string,
  productName: PropTypes.string,
  productMaterial: PropTypes.string,
  price: PropTypes.string,
  discountPrice: PropTypes.string,
  averageRating: PropTypes.number,
  totalNoOfReviews: PropTypes.number
};
