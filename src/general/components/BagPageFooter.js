import React from "react";
import styles from "./BagPageFooter.css";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
export default class BagPageFooter extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.icon}>
          <Icon image={this.props.image} />
          <div className={styles.saveLabel}>{this.props.saveText}</div>
          <div className={styles.removeLabel}>{this.props.removeText}</div>
        </div>
      </div>
    );
  }
}

BagPageFooter.propTypes = {
  image: PropTypes.string
};
BagPageFooter.defaultProps = {
  saveText: "Save",
  removeText: "Remove"
};
