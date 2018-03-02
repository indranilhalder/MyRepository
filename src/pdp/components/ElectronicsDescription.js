import React from "react";
import ProductVideo from "../../general/components/ProductVideo";
import styles from "./ElectronicsDescription.css";
import PropTypes from "prop-types";
import { Image } from "xelpmoc-core";
export default class ElectronicsDescription extends React.Component {
  render() {
    //console.log(this.props.value.textList);
    if (this.props.value.textList) {
      if (this.props.value.textList[0]) {
        console.log(
          this.props.value.textList[0].match(/<h3 [^>]+>([^<]+)<\/h3>/)
        );
      }
    }
    return (
      <div className={styles.base}>
        {this.props.value.imageList && (
          <div className={styles.imageHolder}>
            <div className={styles.imageCard}>
              <Image image={this.props.value.imageList[0]} fit="cover" />
            </div>
          </div>
        )}
        {this.props.value.textList && (
          <div
            className={
              this.props.value.textList[0].indexOf("<h2>")
                ? styles.descriptionHolder
                : styles.noOffset
            }
          >
            <div
              className={styles.descriptionText}
              dangerouslySetInnerHTML={{ __html: this.props.value.textList[0] }}
            />
          </div>
        )}
        {this.props.value.videoList && (
          <ProductVideo url={this.props.value.videoList[0]} />
        )}
      </div>
    );
  }
}
ElectronicsDescription.propTypes = {
  imageUrl: PropTypes.string,
  descriptionHeader: PropTypes.string,
  description: PropTypes.string
};
