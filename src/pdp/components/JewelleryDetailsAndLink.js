import React from "react";
import styles from "./JewelleryDetailsAndLink.css";
import { HashLink as Link } from "react-router-hash-link";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
export default class JewelleryDetailsAndLink extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
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
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.linkHolder}>
          <div className={styles.detailsContainer}>
            {this.props.productName && (
              <div className={styles.productName}>{this.props.productName}</div>
            )}
            {this.props.productDescription && (
              <div className={styles.productDescription}>
                {this.props.productDescription}
              </div>
            )}
            {this.props.productMaterial && (
              <div className={styles.productMaterial}>
                {this.props.productMaterial}
              </div>
            )}
          </div>
          <div className={styles.priceContainer}>
            {this.props.price && (
              <div className={styles.price}>{`Rs. ${this.props.price}`}</div>
            )}
            {this.props.discountPrice &&
              this.props.discountPrice !== this.props.price && (
                <div className={styles.deletePriceAndDiscount}>
                  <div className={styles.discountPrice}>
                    {`Rs. ${this.props.discountPrice}`}
                  </div>
                  <div className={styles.discount}>
                    {this.props.discount && `(${this.props.discount}%)`}
                  </div>
                </div>
              )}
            {this.props.hasPriceBreakUp && (
              <div className={styles.button} onClick={() => this.handleClick()}>
                <Link to="#priceBreakup">
                  <UnderLinedButton
                    label={this.props.label}
                    color="#ff1744"
                    onClick={() => {
                      this.handlePriceBreakup();
                    }}
                  />
                </Link>
              </div>
            )}
          </div>
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
  productMaterial: PropTypes.string,
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
