import React from "react";
import PropTypes from "prop-types";
import { Image } from "xelpmoc-core";
import styles from "./TopCategories.css";

import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
export default class TopCategories extends React.Component {
  handleClick(webURL) {
    if (webURL) {
      let urlSuffix = webURL.replace(TATA_CLIQ_ROOT, "$1");
      this.props.history.push(urlSuffix);
    }
  }
  render() {
    const { feedComponentData } = this.props;
    console.log(feedComponentData);
    return (
      feedComponentData.items && (
        <div className={styles.base}>
          <div className={styles.headerText}>{feedComponentData.title}</div>
          <div className={styles.categorieHolder}>
            <div className={styles.leftSection}>
              {feedComponentData.items[0] && (
                <div
                  className={styles.oneImageHolder}
                  onClick={() =>
                    this.handleClick(feedComponentData.items[0].webURL)
                  }
                >
                  <div className={styles.bigImageHolder}>
                    <div className={styles.overlay}>
                      <div className={styles.labelText}>
                        {feedComponentData.items[0].title}
                      </div>
                    </div>
                    <Image
                      image={
                        feedComponentData.items[0] &&
                        feedComponentData.items[0].imageURL
                      }
                      fit="cover"
                    />
                  </div>
                </div>
              )}
            </div>
            <div className={styles.rightSection}>
              {feedComponentData.items[1] && (
                <div
                  className={styles.twoByTwo}
                  onClick={() =>
                    this.handleClick(feedComponentData.items[1].webURL)
                  }
                >
                  <div className={styles.twoByTwoImageHolder}>
                    <div className={styles.tShirtHolder}>
                      <div className={styles.overlay}>
                        <div className={styles.labelText}>
                          {feedComponentData.items[1].title}
                        </div>
                      </div>
                      <Image
                        image={
                          feedComponentData.items[1] &&
                          feedComponentData.items[1].imageURL
                        }
                        fit="cover"
                      />
                    </div>
                  </div>
                </div>
              )}
              <div className={styles.footWareHolder}>
                {feedComponentData.items[2] && (
                  <div
                    className={styles.twoByTwoImageHolder}
                    onClick={() =>
                      this.handleClick(feedComponentData.items[2].webURL)
                    }
                  >
                    <div className={styles.tShirtHolder}>
                      <div className={styles.overlay}>
                        <div className={styles.labelText}>
                          {feedComponentData.items[2].title}
                        </div>
                      </div>
                      <Image
                        image={
                          feedComponentData.items[2] &&
                          feedComponentData.items[2].imageURL
                        }
                        fit="cover"
                      />
                    </div>
                  </div>
                )}
              </div>
            </div>
          </div>
        </div>
      )
    );
  }
}
TopCategories.propTypes = {
  topProductText: PropTypes.string,
  topImageUrl: PropTypes.string,
  tShirtProductText: PropTypes.string,
  tShirtImageUrl: PropTypes.string,
  footwearsProductText: PropTypes.string,
  footwearsImageUrl: PropTypes.string
};
