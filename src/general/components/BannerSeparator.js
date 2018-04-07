import React from "react";
import styles from "./BannerSeparator.css";
import { Image } from "xelpmoc-core";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import PropTypes from "prop-types";
export default class BannerSeparator extends React.Component {
  handleClick(webURL) {
    if (webURL) {
      const urlSuffix = webURL.replace(TATA_CLIQ_ROOT, "$1");
      this.props.history.push(urlSuffix);
    }
  }
  render() {
    return (
      <div
        className={styles.base}
        style={{
          backgroundImage: `linear-gradient(165deg, ${
            this.props.feedComponentData.startHexCode
          } ,${this.props.feedComponentData.endHexCode})`
        }}
        onClick={() => {
          this.handleClick(this.props.feedComponentData.webURL);
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
