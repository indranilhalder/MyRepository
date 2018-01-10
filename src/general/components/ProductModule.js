import React from "react";
import ProductImage from "../../auth/components/ProductImage";
import ProductDescription from "./ProductDescription";
import styles from "./ProductModule.css";
import downloadIcon from "./img/download.svg";
import PropTypes from "prop-types";
export default class ProductModule extends React.Component {
  download = () => {
    if (this.props.download) {
      this.props.download();
    }
  };
  onImageClick = () => {
    if (this.props.onImageClick) {
      this.props.onImageClik();
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder} onClick={this.onImageClick}>
          <ProductImage image={this.props.productImage} />
        </div>
        <div className={styles.content} />
        <ProductDescription
          title={this.props.title}
          description={this.props.description}
          price={this.props.price}
          icon={downloadIcon}
          discountPrice={this.props.discountPrice}
          onDownload={this.download}
        />
      </div>
    );
  }
}
ProductModule.PropTypes = {
  productImage: PropTypes.string,
  title: PropTypes.string,
  description: PropTypes.string,
  price: PropTypes.number,
  discountPrice: PropTypes.number,
  download: PropTypes.func,
  onImageClick: PropTypes.func
};
