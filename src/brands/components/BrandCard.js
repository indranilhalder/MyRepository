import React from "react";
import styles from "./BrandCard.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
class BrandCard extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.container}>
          <div className={styles.imageHolder}>
            <Image image={this.props.image} size={50} />
          </div>
          <div className={styles.textContainer}>
            <div className={styles.text}>{this.props.text}</div>
            <div className={styles.subText}>{this.props.subText}</div>
          </div>
        </div>
      </div>
    );
  }
}
BrandCard.propTypes = {
  text: PropTypes.string,
  subText: PropTypes.string
};
export default BrandCard;
