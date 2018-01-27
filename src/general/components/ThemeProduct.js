import React from "react";
import styles from "./ThemeProduct.css";
import CircleProductImage from "./CircleProductImage";
import PropTypes from "prop-types";

export default class ThemeProduct extends React.Component {
  render() {
    let className = styles.base;
    if (this.props.isWhite) {
      className = styles.colorWhite;
    }
    return (
      <div className={className}>
        <CircleProductImage image={this.props.image} />
        {this.props.label && (
          <div className={styles.label}>{this.props.label}</div>
        )}
        {this.props.price && (
          <div className={styles.price}>{this.props.price}</div>
        )}
      </div>
    );
  }
}
ThemeProduct.propTypes = {
  label: PropTypes.string,
  price: PropTypes.string,
  image: PropTypes.string
};
