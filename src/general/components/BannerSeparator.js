import React from "react";
import styles from "./BannerSeparator.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import image from "./img/downloadWhite.svg";
export default class BannerSeparator extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.downloadInnerBox}>
          <div className={styles.downloadIcon}>
            <Image image={this.props.bannerImage} color="transparent" />
          </div>
          <div className={styles.downloadBox}>
            {this.props.bannerHeaderText}
          </div>
          <div className={styles.downloadLabel}>{this.props.bannerText}</div>
        </div>
      </div>
    );
  }
}
BannerSeparator.propTypes = {
  bannerImage: PropTypes.string,
  bannerHeaderText: PropTypes.string,
  bannerText: PropTypes.string
};
BannerSeparator.defaultProps = {
  bannerImage: image,
  bannerHeaderText: "Seen something you like",
  bannerText: "Save it for later with our Save Feature."
};
