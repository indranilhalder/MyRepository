import React from "react";
import styles from "./ProductImage.css";
import Image from "../../xelpmoc-core/Image";
import PropTypes from "prop-types";

export default class ProductImage extends React.Component {
  onClickImage() {
    if (this.props.onClickImage) {
      this.props.onClickImage();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder} onClick={() => this.onClickImage()}>
          <Image image={this.props.image} />
        </div>
      </div>
    );
  }
}
ProductImage.propTypes = {
  image: PropTypes.string
};
