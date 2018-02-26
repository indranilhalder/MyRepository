import React from "react";
import PropTypes from "prop-types";
import ProductImage from "../../general/components/ProductImage.js";
import styles from "./OrderCard.css";
export default class OrderCard extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.productImageHolder}>
          <ProductImage image={this.props.productImage} />
        </div>
        <div className={styles.productDetails}>
          <div className={styles.productName}>{this.props.productName}</div>
          <div className={styles.priceHolder}>
            <div className={styles.price}>{`Rs ${this.props.price}`}</div>
            {this.props.discountPrice &&
              this.props.discountPrice !== this.props.price && (
                <div className={styles.discountPrice}>
                  {`Rs ${this.props.discountPrice}`}
                </div>
              )}
          </div>
          {this.props.children && (
            <div className={styles.additionalContent}>
              {this.props.children}
            </div>
          )}
        </div>
      </div>
    );
  }
}
OrderCard.propTypes = {
  productImage: PropTypes.string,
  productName: PropTypes.string,
  price: PropTypes.string,
  discountPrice: PropTypes.string
};
