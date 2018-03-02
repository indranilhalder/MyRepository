import React from "react";
import styles from "./ElectronicsDescription.css";
import PropTypes from "prop-types";
import { Image } from "xelpmoc-core";
export default class ElectronicsDescription extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder}>
          <div className={styles.imageCard}>
            <Image image={this.props.imageUrl} fit="cover" />
          </div>
        </div>
        <div className={styles.descriptionHolder}>
          <div className={styles.subHeader}>{this.props.descriptionHeader}</div>
          <div className={styles.descriptionText}>{this.props.description}</div>
        </div>
      </div>
    );
  }
}
ElectronicsDescription.propTypes = {
  imageUrl: PropTypes.string,
  descriptionHeader: PropTypes.string,
  description: PropTypes.string
};
