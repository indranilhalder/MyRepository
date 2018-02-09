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
    return (
      <div className={styles.base}>
        <div className={styles.productDescriptionSection}>
          <div className={styles.productName}>{this.props.productName}</div>
          <div className={styles.productDescription}>
            {this.props.productDescription}
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
        <div className={styles.productPriceSection}>
          <div className={styles.price}>Rs. {this.props.price}</div>
          {this.props.discountPrice && (
            <div className={styles.discountPrice}>
              <del>Rs. {this.props.discountPrice}</del>
            </div>
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
