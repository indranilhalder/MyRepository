import React from "react";
import styles from "./CircleProductImage.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
export default class CircleProductImage extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    return (
      <div className={styles.base} onClick={() => this.handleClick()}>
        <div className={styles.imageHolder}>
          <Image image={this.props.image} />
        </div>
        {this.props.label && (
          <div className={styles.textOverlay}>
            <div className={styles.label}>{this.props.label}</div>
          </div>
        )}
      </div>
    );
  }
}
CircleProductImage.propTypes = {
  onClick: PropTypes.func
};
