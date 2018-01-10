import React from "react";
import ProductImage from "./ProductImage";
import ProductDescription from "./ProductDescription";
import styles from "./ProductModule.css";
import downloadIcon from "./img/download.svg";
import PropTypes from "prop-types";
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
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder} onClick={this.onClick}>
          <ProductImage image={this.props.productImage} />
        </div>
        <div className={styles.content} />
        <ProductDescription
          title={this.props.title}
          description={this.props.description}
          price={this.props.price}
          icon={downloadIcon}
          discountPrice={this.props.discountPrice}
          onDownload={this.onDownload}
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
  onDownload: PropTypes.func,
  onClick: PropTypes.func
};
