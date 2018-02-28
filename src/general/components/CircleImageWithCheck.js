import React from "react";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import styles from "./CircleImageWithCheck.css";

export default class CircleImageWithCheck extends React.Component {
  handleClick() {
    if (this.prop.selectItem) {
      this.props.selectItem();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.image}>
          <Image image={this.props.image} fit={this.props.fit} />
        </div>
        <div
          className={this.props.selected ? styles.checkSelected : styles.check}
        />
      </div>
    );
  }
}

CircleImageWithCheck.propTypes = {
  selected: PropTypes.bool,
  image: PropTypes.string,
  fit: PropTypes.string,
  value: PropTypes.string,
  selectItem: PropTypes.func
};
