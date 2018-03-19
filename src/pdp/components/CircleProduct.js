import React from "react";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import styles from "./CircleProduct.css";
export default class CircleProduct extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder}>
          <div className={styles.image}>
            <Image image={this.props.productImage} fit="cover" />
          </div>
        </div>
        <div className={styles.textHolder}>{this.props.productName}</div>
      </div>
    );
  }
}
CircleProduct.propTypes = {
  productImage: PropTypes.string,
  productName: PropTypes.string
};
