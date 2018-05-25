import React from "react";
import Image from "../../xelpmoc-core/Image";
import styles from "./TopCategories.css";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
const TOP_PRODUCT_INDEX = 0;
const TOP_RIGHT_PRODUCT_INDEX = 1;
const BOTTOM_RIGHT_PRODUCT_INDEX = 2;

export default class TopCategories extends React.Component {
  handleClick = index => {
    const itemToClick = this.props.feedComponentData.items[index];
    const urlSuffix = itemToClick.webURL.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
    if (this.props.setClickedElementId) {
      this.props.setClickedElementId();
    }
  };
  render() {
    if (this.props.feedComponentData.items.length < 3) {
      return null;
    }
    const topProduct = this.props.feedComponentData.items[TOP_PRODUCT_INDEX];
    const topRightProduct = this.props.feedComponentData.items[
      TOP_RIGHT_PRODUCT_INDEX
    ];
    const bottomRightProduct = this.props.feedComponentData.items[
      BOTTOM_RIGHT_PRODUCT_INDEX
    ];

    return (
      <div
        className={
          this.props.positionInFeed === 1 ? styles.firstItemBase : styles.base
        }
      >
        {this.props.feedComponentData.title && (
          <div className={styles.headerText}>
            {this.props.feedComponentData.title}
          </div>
        )}

        <div className={styles.categorieHolder}>
          <div className={styles.leftSection}>
            <div
              className={styles.oneImageHolder}
              onClick={() => this.handleClick(TOP_PRODUCT_INDEX)}
            >
              <div className={styles.bigImageHolder}>
                <div className={styles.labelText}>{topProduct.title}</div>

                <Image image={topProduct.imageURL} fit="cover" />
              </div>
            </div>
          </div>
          <div className={styles.rightSection}>
            <div
              className={styles.twoByTwo}
              onClick={() => this.handleClick(TOP_RIGHT_PRODUCT_INDEX)}
            >
              <div className={styles.twoByTwoImageHolder}>
                <div className={styles.tShirtHolder}>
                  <div className={styles.labelText}>
                    {topRightProduct.title}
                  </div>

                  <Image image={topRightProduct.imageURL} fit="cover" />
                </div>
              </div>
            </div>
            <div
              className={styles.footWareHolder}
              onClick={() => this.handleClick(BOTTOM_RIGHT_PRODUCT_INDEX)}
            >
              <div className={styles.twoByTwoImageHolder}>
                <div className={styles.tShirtHolder}>
                  <div className={styles.labelText}>
                    {bottomRightProduct.title}
                  </div>

                  <Image image={bottomRightProduct.imageURL} fit="cover" />
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    );
  }
}
