import React from "react";
import Image from "../../xelpmoc-core/Image";
import PropTypes from "prop-types";
import styles from "./CircleProduct.css";
export default class CircleProduct extends React.Component {
  onClick(val) {
    if (this.props.onClick) {
      this.props.onClick(val);
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder} onClick={val => this.onClick(val)}>
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
  productName: PropTypes.string,
  onClick: PropTypes.func
};
