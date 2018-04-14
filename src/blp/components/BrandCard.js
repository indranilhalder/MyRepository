import React from "react";
import styles from "./BrandCard.css";
import Image from "../../xelpmoc-core/Image";
import PropTypes from "prop-types";
export default class BrandCard extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.container}>
          <div className={styles.imageHolder}>
            <Image image={this.props.image} />
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
  subText: PropTypes.string,
  image: PropTypes.string
};
