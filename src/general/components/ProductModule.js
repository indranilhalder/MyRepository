import React from "react";
import ProductImage from "./ProductImage";
import ProductDescription from "./ProductDescription";
import PropTypes from "prop-types";
import ConnectButton from "./ConnectButton.js";

import styles from "./ProductModule.css";
import downloadIcon from "./img/download.svg";
import downloadIconWhite from "./img/downloadWhite.svg";
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
        <div className={styles.imageHolder} onClick={this.onClick}>
          <ProductImage image={this.props.productImage} />
          {this.props.onConnect && (
            <ConnectButton onClick={this.handleConnect} />
          )}
        </div>
        <div className={styles.content}>
          <ProductDescription
            {...this.props}
            icon={downloadImage}
            onDownload={this.onDownload}
          />
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
