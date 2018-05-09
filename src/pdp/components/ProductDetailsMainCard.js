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
        <meta itemprop="priceCurrency" content="INR" />
        <meta
          itemProp="itemCondition"
          content="http://schema.org/NewCondition"
        />
      </MetaTags>
    );
  };

  handleLinkClick = e => {
    e.preventDefault();
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
        {this.renderSchemaTags()}
        <div className={styles.productInfo}>
          <div className={styles.productDescriptionSection}>
            <div
              itemProp="brand"
              itemScope=""
              itemType="http://schema.org/Organization"
            >
              <span itemProp="name">
                <h2
                  className={styles.brandName}
                  onClick={() => this.handleBrandClick()}
                >
                  {this.props.brandName}
                </h2>
              </span>
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
            <div className={styles.productDescription} itemProp="description">
              {this.props.productDescription}
            </div>
          </div>
          <div
            itemProp="offers"
            itemScope=""
            itemType="http://schema.org/Offer"
            className={styles.productPriceSection}
          >
            <div className={styles.price}>
              <span itemType="price">{displayPrice}</span>
            </div>
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
          <StarRating averageRating={this.props.averageRating}>
            {this.props.averageRating && (
              <div
                className={styles.ratingText}
                onClick={() => this.handleClick()}
              >
                Rating {`${averageRating}`} /5
              </div>
            )}
            <div className={styles.arrowHolder}>
              <Icon image={arrowIcon} size={15} />
            </div>
          </StarRating>
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
