import React from "react";
import styles from "./ProductDetailsCard.css";
import ProductImage from "../../general/components/ProductImage.js";
import StarRating from "../../general/components/StarRating.js";
import PropTypes from "prop-types";
export default class ProductDetailsCard extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.productImageHolder}>
          <ProductImage image={this.props.productImage} />
        </div>
        <div className={styles.productDescriptionHolder}>
          <div className={styles.productName}>{this.props.productName}</div>
          <div className={styles.productMatirial}>
            {this.props.productMaterial}
          </div>

          <div className={styles.productPrice}>
            {this.props.onPrice && (
              <span className={styles.onPrice}>Rs {this.props.onPrice}</span>
            )}
            {this.props.oldPrice && (
              <del>
                <span className={styles.deletePrice}>
                  Rs
                  {this.props.oldPrice}
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
              Rating <span>{this.props.averageRating}/ 5</span>
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
  onPrice: PropTypes.string,
  oldPrice: PropTypes.string,
  averageRating: PropTypes.number,
  totalNoOfReviews: PropTypes.number
};
