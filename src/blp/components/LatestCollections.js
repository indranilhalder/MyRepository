import React from "react";
import PropTypes from "prop-types";
import styles from "./LatestCollections.css";
import Icon from "../../xelpmoc-core/Icon";
import iconImageURL from "../../general/components/img/whiteArrow.svg";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";

export default class LatestCollections extends React.Component {
  arrowNextClick() {
    if (this.props.feedComponentData.webURL) {
      const urlSuffix = this.props.feedComponentData.webURL
        .replace(TATA_CLIQ_ROOT, "$1")
        .trim();
      const urlPath = new URL(this.props.feedComponentData.webURL).pathname;
      if (urlPath.indexOf("/que") > -1) {
        window.open(urlSuffix, "_blank");
        window.focus();
      } else {
        this.props.history.push(urlSuffix);
        if (this.props.setClickedElementId) {
          this.props.setClickedElementId();
        }
      }
    }
  }
  render() {
    let feedComponentData = this.props.feedComponentData;

    return (
      <div
        className={
          this.props.positionInFeed === 1 ? styles.firstItemBase : styles.base
        }
        style={{ backgroundColor: feedComponentData.startHexCode }}
      >
        <div
          className={styles.LatestCollectionsHolder}
          onClick={() => this.arrowNextClick()}
        >
          <div className={styles.textHolder}>
            {feedComponentData && feedComponentData.title}
          </div>
          <div className={styles.iconHolder}>
            <div className={styles.icon}>
              <Icon image={iconImageURL} size={25} />
            </div>
          </div>
        </div>
      </div>
    );
  }
}
