import React from "react";
import PropTypes from "prop-types";
import { Image } from "xelpmoc-core";
import styles from "./TopCategories.css";
export default class TopCategories extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.headerText}>Top Categories</div>
        <div className={styles.categorieHolder}>
          <div className={styles.leftSection}>
            <div className={styles.productImageHolder}>
              <div className={styles.overlay}>
                <div className={styles.labelText}>
                  {this.props.topProductText}
                </div>
              </div>
              <Image image={this.props.topImageUrl} fit="cover" />
            </div>
          </div>
          <div className={styles.rightSection}>
            <div className={styles.tShirtImageHolder}>
              <div className={styles.productImageHolder}>
                <div className={styles.overlay}>
                  <div className={styles.labelText}>
                    {this.props.tShirtProductText}
                  </div>
                </div>
                <Image image={this.props.tShirtImageUrl} fit="cover" />
              </div>
            </div>
            <div className={styles.footwearsImageHolder}>
              <div className={styles.productImageHolder}>
                <div className={styles.overlay}>
                  <div className={styles.labelText}>
                    {this.props.footwearsProductText}
                  </div>
                </div>
                <Image image={this.props.footwearsImageUrl} fit="cover" />
              </div>
            </div>
          </div>
        </div>
      </div>
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
