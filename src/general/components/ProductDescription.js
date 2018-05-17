import React, { Component } from "react";
import PropTypes from "prop-types";
import AddToWishListButtonContainer from "../../wishlist/containers/AddToWishListButtonContainer";
import styles from "./ProductDescription.css";
import { RUPEE_SYMBOL } from "../../lib/constants";
export default class ProductDescription extends Component {
  handleClick() {
    if (this.props.onDownload) {
      this.props.onDownload();
    }
  }

  renderTitle = headerText => {
    if (this.props.isPlp) {
      return <div className={headerText}>{this.props.title}</div>;
    } else {
      return <h2 className={headerText}>{this.props.title}</h2>;
    }
  };

  render() {
    let headerClass = styles.header;
    let priceClass = styles.priceHolder;
    let headerText = styles.headerText;
    let contentClass = styles.content;
    if (this.props.onDownload) {
      headerClass = styles.hasDownload;
    }

    if (
      this.props.discountPrice &&
      this.props.price !== this.props.discountPrice
    ) {
      priceClass = styles.priceCancelled;
    }
    if (this.props.isWhite) {
      headerText = styles.headerWhite;
      contentClass = styles.contentWhite;
    }

    return (
      <div className={styles.base}>
        <div className={headerClass}>
          {this.renderTitle(headerText)}

          {this.props.showWishListButton &&
            this.props.productListingId &&
            this.props.winningUssID &&
            this.props.isShowAddToWishlistIcon && (
              <div className={styles.button}>
                <AddToWishListButtonContainer
                  productListingId={this.props.productListingId}
                  winningUssID={this.props.winningUssID}
                  isWhite={this.props.isWhite}
                />
              </div>
            )}
        </div>
        <div className={contentClass}>
          {this.props.description && (
            <h2 className={styles.description}>{this.props.description}</h2>
          )}

          {!this.props.isRange &&
            this.props.discountPrice &&
            this.props.discountPrice !== this.props.price && (
              <div className={styles.discount}>
                {this.props.discountPrice.toString().includes(RUPEE_SYMBOL)
                  ? this.props.discountPrice
                  : `${RUPEE_SYMBOL}${this.props.discountPrice}`}
              </div>
            )}

          {!this.props.isRange &&
            this.props.price && (
              <div className={priceClass}>
                {this.props.price.toString().includes(RUPEE_SYMBOL)
                  ? this.props.price
                  : `${RUPEE_SYMBOL}${this.props.price}`}
              </div>
            )}
          {this.props.isRange &&
            this.props.minPrice &&
            this.props.maxPrice && (
              <div className={styles.description}>
                {this.props.minPrice.toString().includes(RUPEE_SYMBOL)
                  ? this.props.minPrice
                  : `${RUPEE_SYMBOL}${this.props.minPrice}`}{" "}
                {this.props.maxPrice !== this.props.minPrice && (
                  <React.Fragment>
                    -{" "}
                    {this.props.maxPrice.toString().includes(RUPEE_SYMBOL)
                      ? this.props.maxPrice
                      : `${RUPEE_SYMBOL}${this.props.maxPrice}`}
                  </React.Fragment>
                )}
              </div>
            )}
        </div>
      </div>
    );
  }
}

ProductDescription.propTypes = {
  title: PropTypes.string,
  description: PropTypes.string,
  price: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  discountPrice: PropTypes.oneOfType([PropTypes.string, PropTypes.number]),
  icon: PropTypes.string,
  onDownload: PropTypes.func,
  isWhite: PropTypes.bool
};

ProductDescription.defaultProps = {
  title: "",
  icon: "",
  description: "",
  price: "",
  isWhite: false,
  isRange: false,
  textColor: "#212121",
  showWishListButton: true,
  isShowAddToWishlistIcon: true,
  isPlp: PropTypes.bool
};
