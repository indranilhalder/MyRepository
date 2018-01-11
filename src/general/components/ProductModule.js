import React from "react";
import ProductImage from "./ProductImage";
import ProductDescription from "./ProductDescription";
import styles from "./ProductModule.css";
import downloadIcon from "./img/download.svg";

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
        <div className={styles.content}>
          <ProductDescription
            {...this.props}
            icon={downloadIcon}
            onDownload={this.onDownload}
          />
        </div>
      </div>
    );
  }
}
