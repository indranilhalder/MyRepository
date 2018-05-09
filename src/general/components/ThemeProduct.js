import React from "react";
import styles from "./ThemeProduct.css";
import CircleProductImage from "./CircleProductImage";
import PropTypes from "prop-types";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import { RUPEE_SYMBOL } from "../../lib/constants";
export default class ThemeProduct extends React.Component {
  onClick = () => {
    let urlSuffix;
    if (this.props.webURL) {
      urlSuffix = this.props.webURL.replace(TATA_CLIQ_ROOT, "$1");
    } else if (this.props.productId) {
      urlSuffix = `p-${this.props.productId.toLowerCase()}`;
    } else if (this.props.productListingId) {
      urlSuffix = `p-${this.props.productListingId.toLowerCase()}`;
    }

    if (this.props.onClick) {
      this.props.onClick(urlSuffix);
    }
  };
  render() {
    let className = styles.base;
    let priceClass = styles.priceHolder;

    if (this.props.isWhite) {
      className = styles.colorWhite;
    }

    if (
      this.props.discountPrice &&
      this.props.price !== this.props.discountPrice
    ) {
      priceClass = styles.priceCancelled;
    }
    return (
      <div className={className}>
        <CircleProductImage image={this.props.image} onClick={this.onClick} />
        {this.props.label && (
          <div className={styles.label}>{this.props.label}</div>
        )}
        {this.props.discountPrice &&
          this.props.discountPrice !== this.props.price && (
            <div className={styles.discount}>
              {this.props.discountPrice.toString().includes(RUPEE_SYMBOL)
                ? this.props.discountPrice
                : `${RUPEE_SYMBOL}${this.props.discountPrice}`}
            </div>
          )}

        {this.props.price && (
          <div className={priceClass}>
            {this.props.price.toString().includes(RUPEE_SYMBOL)
              ? this.props.price
              : `${RUPEE_SYMBOL}${this.props.price}`}
          </div>
        )}
      </div>
    );
  }
}
ThemeProduct.propTypes = {
  label: PropTypes.string,
  price: PropTypes.string,
  image: PropTypes.string
};

/*




*/
