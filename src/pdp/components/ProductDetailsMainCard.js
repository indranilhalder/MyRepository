import React from "react";
import styles from "./ProductDetailsMainCard.css";
import StarRating from "../../general/components/StarRating.js";
import Icon from "../../xelpmoc-core/Icon";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import arrowIcon from "../../general/components/img/arrow.svg";
import PropTypes from "prop-types";
import MetaTags from "react-meta-tags";
export default class ProductDetailsMainCard extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }

  handleBrandClick() {
    if (this.props.brandUrl) {
      const urlSuffix = this.props.brandUrl.replace(TATA_CLIQ_ROOT, "$1");
      this.props.history.push(urlSuffix);
    }
  }

  renderSchemaTags = () => {
    return (
      <MetaTags>
        <meta itemprop="price" content={this.props.price} />
        <meta itemprop="priceCurrency" content="INR" />
      </MetaTags>
    );
  };
  render() {
    const displayPrice = this.props.discountPrice
      ? this.props.discountPrice
      : this.props.price;
    let averageRating = "";
    if (this.props.averageRating) {
      averageRating = Math.floor(this.props.averageRating);
    }
    return (
      <div className={styles.base}>
        <div className={styles.productInfo}>
          <div
            itemprop="description"
            className={styles.productDescriptionSection}
          >
            <div
              itemprop="brand"
              itemscope
              itemtype="http://schema.org/Organization"
            >
              <h1
                className={styles.productName}
                onClick={() => this.handleBrandClick()}
              >
                {this.props.productName}
              </h1>
            </div>
            <h2 className={styles.productDescription}>
              {this.props.productDescription}
            </h2>
          </div>
          <div className={styles.productPriceSection}>
            <div className={styles.price}>{displayPrice}</div>
            {this.props.discountPrice &&
              this.props.discountPrice !== this.props.price && (
                <div className={styles.priceCancelled}>
                  <span className={styles.cancelPrice}>{this.props.price}</span>
                  <span className={styles.discount}>
                    {this.props.discount && `(${this.props.discount}%)`}
                  </span>
                </div>
              )}
          </div>
        </div>
        {this.props.averageRating && (
          <div
            itemprop="aggregateRating"
            itemscope
            itemtype="http://schema.org/AggregateRating"
            className={styles.ratingHolder}
          >
            <StarRating averageRating={this.props.averageRating}>
              {this.props.averageRating && (
                <div
                  className={styles.ratingText}
                  onClick={() => this.handleClick()}
                >
                  <span itemprop="ratingValue">
                    Rating {`${averageRating}`} /5
                  </span>
                </div>
              )}
              <div className={styles.arrowHolder}>
                <Icon image={arrowIcon} size={15} />
              </div>
            </StarRating>
          </div>
        )}
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
  onClick: PropTypes.func,
  discount: PropTypes.string
};
