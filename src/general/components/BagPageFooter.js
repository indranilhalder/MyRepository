import React from "react";
import styles from "./BagPageFooter.css";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
import image from "./img/download.svg";
export default class BagPageFooter extends React.Component {
  onSave() {
    if (this.props.onSave) {
      this.props.onSave();
    }
  }
  onRemove() {
    if (this.props.onRemove) {
      this.props.onRemove();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.wrapper}>
          <div className={styles.saveButton} onClick={() => this.onSave()}>
            <div className={styles.iconHolder}>
              <Icon image={image} />
            </div>
            <div className={styles.saveLabel}>{this.props.saveText}</div>
          </div>
          <div className={styles.removeLabel} onClick={() => this.onRemove()}>
            {this.props.removeText}
          </div>
        </div>
      </div>
    );
  }
}
BagPageFooter.propTypes = {
  image: PropTypes.string,
  onSave: PropTypes.func,
  onRemove: PropTypes.func
};
BagPageFooter.defaultProps = {
  saveText: "Save",
  removeText: "Remove"
};
