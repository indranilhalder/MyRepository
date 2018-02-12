import React from "react";
import styles from "./BagPageFooter.css";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
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
          <div className={styles.iconText}>
            <div className={styles.iconHolder}>
              <Icon image={this.props.image} />
            </div>
            <div className={styles.saveLabel} onClick={() => this.onSave()}>
              {this.props.saveText}
            </div>
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
  onClick: PropTypes.func
};
BagPageFooter.defaultProps = {
  saveText: "Save",
  removeText: "Remove"
};
