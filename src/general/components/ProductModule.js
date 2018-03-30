import React from "react";
import ProductImage from "./ProductImage";
import ProductDescription from "./ProductDescription";
import PropTypes from "prop-types";
import ConnectButton from "./ConnectButton.js";
import styles from "./ProductModule.css";
import downloadIcon from "./img/download.svg";
import downloadIconWhite from "./img/downloadWhite.svg";
import ProductInfo from "./ProductInfo.js";
import Logo from "./Logo.js";
import newFlag from "./img/new.svg";
import offerFlag from "./img/offer.svg";
import exclusiveFlag from "./img/exclusive.svg";
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
      urlSuffix = `p-${this.props.productId.toLowerCase()}`;
    } else if (this.props.productListingId) {
      urlSuffix = `p-${this.props.productListingId.toLowerCase()}`;
    }

    if (this.props.onClick) {
      this.props.onClick(urlSuffix);
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
    let image;
    switch (this.props.offer) {
      case IS_OFFER_EXISTING:
        image = offerFlag;
        break;
      case ON_EXCLUSIVE:
        image = exclusiveFlag;
        break;
      case IS_NEW:
        image = newFlag;
        break;

      default:
        image = null;
    }
    return (
      <div className={styles.base} onClick={this.onClick}>
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
          {this.props.offer && (
            <div className={styles.flagHolder}>
              <div className={styles.flag}>
                <Logo image={image} />
                <div className={styles.flagText}>{this.props.flagText}</div>
              </div>
            </div>
          )}
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
  bestDeliveryInfo: PropTypes.string
};
ProductModule.defaultProps = {
  view: "grid"
};
