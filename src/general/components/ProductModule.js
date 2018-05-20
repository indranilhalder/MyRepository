import React from "react";
import ProductImage from "./ProductImage";
import ProductDescription from "./ProductDescription";
import PropTypes from "prop-types";
import ConnectButton from "./ConnectButton.js";
import styles from "./ProductModule.css";
import downloadIcon from "./img/download.svg";
import downloadIconWhite from "./img/downloadWhite.svg";
import ProductInfo from "./ProductInfo.js";
import ProductFlags from "./ProductFlags.js";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

import {
  ON_EXCLUSIVE,
  DISCOUNT_PERCENT,
  IS_NEW,
  IS_OFFER_EXISTING
} from "../../lib/constants";

export default class ProductModule extends React.Component {
  onDownload = () => {
    if (this.props.onDownload) {
      this.props.onDownload();
    }
  };

  onClick = () => {
    let urlSuffix;
    if (this.props.webURL) {
      urlSuffix = this.props.webURL.replace(TATA_CLIQ_ROOT, "$1");
    } else if (this.props.productId) {
      urlSuffix = `/p-${this.props.productId.toLowerCase()}`;
    } else if (this.props.productListingId) {
      urlSuffix = `/p-${this.props.productListingId.toLowerCase()}`;
    }

    if (this.props.onClick) {
      this.props.onClick(
        urlSuffix,
        null,
        `ProductModule-${this.props.productId}`
      );
    }
  };
  handleConnect = () => {
    if (this.props.onConnect) {
      this.props.onConnect();
    }
  };
  render() {
    let downloadImage = downloadIcon;
    if (this.props.isWhite) {
      downloadImage = downloadIconWhite;
    }

    return (
      <div
        className={styles.base}
        onClick={this.onClick}
        id={`ProductModule-${this.props.productId}`}
      >
        <div
          className={
            this.props.view === "grid"
              ? styles.imageHolder
              : styles.ListimageHolder
          }
        >
          <ProductImage image={this.props.productImage} />
          {this.props.onConnect && (
            <ConnectButton onClick={this.handleConnect} />
          )}

          <div className={styles.flagHolder}>
            <ProductFlags
              discountPercent={this.props.discountPercent}
              isOfferExisting={this.props.isOfferExisting}
              onlineExclusive={this.props.onlineExclusive}
              outOfStock={this.props.outOfStock}
              newProduct={this.props.newProduct}
            />
          </div>
        </div>
        <div
          className={
            this.props.view === "grid" ? styles.content : styles.Listcontent
          }
        >
          <ProductDescription {...this.props} />
          {this.props.view === "list" && (
            <ProductInfo
              averageRating={this.props.averageRating}
              totalNoOfReviews={this.props.totalNoOfReviews}
              offerText={this.props.offerText}
              bestDeliveryInfo={this.props.bestDeliveryInfo}
            />
          )}
        </div>
      </div>
    );
  }
}
ProductModule.propTypes = {
  productImage: PropTypes.string,
  onClick: PropTypes.func,
  onDownload: PropTypes.func,
  isWhite: PropTypes.bool,
  onConnect: PropTypes.func,
  averageRating: PropTypes.number,
  totalNoOfReviews: PropTypes.number,
  offerText: PropTypes.string,
  bestDeliveryInfo: PropTypes.string,
  onOffer: PropTypes.bool,
  isPlp: PropTypes.bool
};
ProductModule.defaultProps = {
  view: "grid",
  showWishListButton: true,
  isPlp: false
};
