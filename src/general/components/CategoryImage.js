import React from "react";
import styles from "./CategoryImage.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
export default class CategoryImage extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder}>
          <Image image={this.props.source} />
        </div>
        <div className={styles.textwrapper}>
          <div className={styles.text}>{this.props.text}</div>
        </div>
      </div>
    );
  }
}

CategoryImage.propTypes = {
  source: PropTypes.string,
  text: PropTypes.string
};
CategoryImage.defaultProps = {
  source: "",
  text: ""
};
