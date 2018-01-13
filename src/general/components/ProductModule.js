import React from "react";
import ProductImage from "./ProductImage";
import ProductDescription from "./ProductDescription";
import PropTypes from "prop-types";
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
  render() {
    let downloadImage = downloadIcon;
    if (this.props.isWhite) {
      downloadImage = downloadIconWhite;
    }
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder} onClick={this.onClick}>
          <ProductImage image={this.props.productImage} />
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
  isWhite: PropTypes.bool
};
