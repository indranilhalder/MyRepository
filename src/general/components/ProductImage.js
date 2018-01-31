import React from "react";
import styles from "./ProductImage.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";

export default class ProductImage extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder}>
          <Image image={this.props.image} />
        </div>
      </div>
    );
  }
}
ProductImage.propTypes = {
  image: PropTypes.string
};
