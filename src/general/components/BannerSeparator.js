import React from "react";
import styles from "./BannerSeparator.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import image from "./img/download.svg";
export default class BannerSeparator extends React.Component {
  render() {
    return (
      <div
        className={styles.base}
        style={{
          backgroundImage: `linear-gradient(165deg, ${
            this.props.feedComponentData.startHexCode
          } ,${this.props.feedComponentData.endHexCode})`
        }}
      >
        <div className={styles.downloadInnerBox}>
          <div className={styles.downloadIcon}>
            <Image
              image={this.props.feedComponentData.iconImageURL}
              color="transparent"
            />
          </div>
          <div className={styles.downloadBox}>
            {this.props.feedComponentData.title}
          </div>
          <div className={styles.downloadLabel}>
            {this.props.feedComponentData.description}
          </div>
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
