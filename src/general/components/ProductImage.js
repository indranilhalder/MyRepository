import React from "react";
import styles from "./ProductImage.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import ConnectButton from "./ConnectButton.js";
import { Icon, CircleButton } from "xelpmoc-core";
export default class ProductImage extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder}>
          <Image image={this.props.image} />
          {this.props.ConnectButton && <ConnectButton />}
        </div>
      </div>
    );
  }
}
ProductImage.propTypes = {
  image: PropTypes.string
};
