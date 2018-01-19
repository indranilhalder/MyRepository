import React from "react";
import styles from "./ProductCapsuleCircle.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";

export default class ProductCapsuleCircle extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base} onClick={() => this.handleClick}>
        <div className={styles.imageHolder}>
          <Image image={this.props.image} fit="contain" />
        </div>
      </div>
    );
  }
}
ProductCapsuleCircle.propTypes = {
  onClick: PropTypes.func
};
