import React from "react";
import styles from "./JewelleryDetailsAndLink.css";
import StarRating from "../../general/components/StarRating.js";
import Icon from "../../xelpmoc-core/Icon";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import MetaTags from "react-meta-tags";
import arrowIcon from "../../general/components/img/arrow.svg";

export default class JewelleryDetailsAndLink extends React.Component {
  readMore() {
    if (this.props.readMore) {
      this.props.readMore();
    }
  }
  handlePriceBreakup() {
    if (this.props.showPriceBreakUp) {
      this.props.showPriceBreakUp();
    }
  }
  handleRatingLink() {
    if (this.props.goToReviewPage) {
      this.props.goToReviewPage();
    }
  }
  handleLinkClick = e => {
    e.preventDefault();
  };
  handleBrandClick() {
    if (this.props.brandUrl) {
      const urlSuffix = this.props.brandUrl.replace(TATA_CLIQ_ROOT, "$1");
      this.props.history.push(urlSuffix);
    }
  }

  renderSchemaTags = () => {
    return (
      <MetaTags>
        <meta itemProp="priceCurrency" content="INR" />
        <meta
          itemProp="itemCondition"
          content="http://schema.org/NewCondition"
        />
      </MetaTags>
    );
  };
  render() {
    let averageRating = "";
    if (this.props.averageRating) {
      averageRating = Math.floor(this.props.averageRating);
    }
    return (
      <div className={styles.base}>
        {this.renderSchemaTags()}
        <div className={styles.linkHolder}>
          <div className={styles.detailsContainer}>
            {this.props.brandName && (
              <div
                itemProp="brand"
                itemScope=""
                itemType="http://schema.org/Organization"
                className={styles.brandName}
                onClick={() => {
                  this.handleBrandClick();
                }}
              >
                <span itemProp="name">
                  <h2>{this.props.brandName}</h2>
                </span>
              </div>
            )}
            {this.props.productName && (
              <a
                className={styles.productNameLink}
                itemProp="url"
                href={window.location.href}
                onClick={this.handleLinkClick}
              >
                <div itemProp="name">
                  <h1 className={styles.productName}>
                    {this.props.productName}
                  </h1>
                </div>
              </a>
            )}
          </div>
          <div
            itemProp="offers"
            itemScope=""
            itemType="http://schema.org/Offer"
            className={styles.priceContainer}
          >
            {this.props.price && (
              <div className={styles.price}>
                <span itemType="price">{`${this.props.price}`}</span>
              </div>
            )}
            {this.props.discountPrice &&
              this.props.discountPrice !== this.props.price && (
                <div className={styles.deletePriceAndDiscount}>
                  <div className={styles.discountPrice}>
                    {`${this.props.discountPrice}`}
                  </div>
                  <div className={styles.discount}>
                    {this.props.discount && `(${this.props.discount}%)`}
                  </div>
                </div>
              )}
            {this.props.hasPriceBreakUp && (
              <div className={styles.button}>
                <UnderLinedButton
                  label={this.props.label}
                  color="#ff1744"
                  onClick={() => {
                    this.handlePriceBreakup();
                  }}
                />
              </div>
            )}
          </div>
          {this.props.averageRating && (
            <div
              className={styles.ratingHolder}
              onClick={() => this.handleRatingLink()}
            >
              <StarRating averageRating={this.props.averageRating}>
                {this.props.averageRating && (
                  <div
                    itemprop="aggregateRating"
                    itemscope
                    itemtype="http://schema.org/AggregateRating"
                    className={styles.ratingText}
                  >
                    Rating {`${averageRating}`} /5
                  </div>
                )}
                <div className={styles.arrowHolder}>
                  <Icon image={arrowIcon} size={15} />
                </div>
              </StarRating>
            </div>
          )}
        </div>
        {this.props.informationText && (
          <div className={styles.textHolder}>
            <div className={styles.informationText}>
              <span>{this.props.informationText}</span>
              <span
                className={styles.buttonSpan}
                onClick={() => this.readMore()}
              >
                Read More
              </span>
            </div>
          </div>
        )}
      </div>
    );
  }
}

JewelleryDetailsAndLink.propTypes = {
  productName: PropTypes.string,
  productDescription: PropTypes.string,
  brandName: PropTypes.string,
  discountPrice: PropTypes.string,
  discount: PropTypes.string,
  onClick: PropTypes.func,
  readMore: PropTypes.func,
  viewPlans: PropTypes.func,
  informationText: PropTypes.string
};
JewelleryDetailsAndLink.defaultProps = {
  label: "Price Breakup"
};
