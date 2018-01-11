import React from "react";
import styles from "./BrandImage.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
export default class BrandImage extends React.Component {
  onSelected = () => {
    if (this.props.onSelect) {
      this.props.onSelect();
    }
  };
  render() {
    return (
      <div className={styles.base} onClick={this.onSelected}>
        <div className={this.props.selected ? styles.active : styles.inactive}>
          <div className={styles.imageHolder}>
            <Image image={this.props.source} />
          </div>
        </div>
      </div>
    );
  }
}
BrandImage.propTypes = {
  source: PropTypes.string,
  selected: PropTypes.bool,
  onSelect: PropTypes.func
};
BrandImage.defaultProps = {
  source: "",
  selected: false
};
