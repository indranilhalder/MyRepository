import React from "react";
import styles from "./ProductImage.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import ConnectButton from "./ConnectButton.js";
export default class ProductImage extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder}>
          <Image image={this.props.image} />
        </div>
        {this.props.connectButton && (
          <ConnectButton onClick={this.handleClick} />
        )}
      </div>
    );
  }
}
ProductImage.propTypes = {
  image: PropTypes.string,
  ConnectButton: PropTypes.element
};
