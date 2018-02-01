import React from "react";
import ProductImage from "./ProductImage";
import ProductDescription from "./ProductDescription";
import PropTypes from "prop-types";
import ConnectButton from "./ConnectButton.js";
import styles from "./ProductModule.css";
import downloadIcon from "./img/download.svg";
import downloadIconWhite from "./img/downloadWhite.svg";
import ProductInfo from "./ProductInfo.js";
export default class ProductModule extends React.Component {
  onDownload = () => {
    if (this.props.onDownload) {
      this.props.onDownload();
    }
  };
  onClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
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
      <div className={styles.base}>
        {/* {this.props.view === "grid" && ( */}
        <div
          className={
            this.props.view === "grid"
              ? styles.imageHolder
              : styles.ListimageHolder
          }
          onClick={this.onClick}
        >
          <ProductImage image={this.props.productImage} />
          {this.props.onConnect && (
            <ConnectButton onClick={this.handleConnect} />
          )}
        </div>
        <div
          className={
            this.props.view === "grid" ? styles.content : styles.Listcontent
          }
        >
          <ProductDescription
            {...this.props}
            icon={downloadImage}
            onDownload={this.onDownload}
          />
          {this.props.view === "list" && (
            <ProductInfo
              averageRating={4}
              totalNoOfReviews="65"
              offerText="25% offers from Rs. 35,000"
              bestDeliveryInfo="Tuesday, Sep 12"
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
  onConnect: PropTypes.func
};
ProductModule.defaultProps = {
  view: "grid"
};
