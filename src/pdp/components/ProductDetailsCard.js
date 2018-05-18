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

  handleLinkClick = e => {
    e.preventDefault();
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.productImageHolder}>
          {this.props.outOfStock && (
            <div className={styles.flag}>Out Of Stock</div>
          )}

          <ProductImage
            image={this.props.productImage}
            onClickImage={() => this.onClickImage()}
          />
        </div>
        <div className={styles.productDescriptionHolder}>
          <div
            itemProp="brand"
            itemScope=""
            itemType="http://schema.org/Organization"
          >
            {this.props.brandName && (
              <h2 className={styles.brandName}>
                <span itemProp="name">{this.props.brandName}</span>
              </h2>
            )}
          </div>
          <a
            itemProp="url"
            href={window.location.href}
            onClick={this.handleLinkClick}
          >
            <div itemProp="name">
              <h1 className={styles.productName}>{this.props.productName}</h1>
            </div>
          </a>
          <div
            className={styles.productPrice}
            itemProp="offers"
            itemScope=""
            itemType="http://schema.org/Offer"
          >
            {this.props.price && (
              <span itemType="price" className={styles.onPrice}>
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
  brandName: PropTypes.string,
  productName: PropTypes.string,
  price: PropTypes.string,
  discountPrice: PropTypes.string,
  averageRating: PropTypes.number,
  totalNoOfReviews: PropTypes.number,
  outOfStock: PropTypes.bool
};
