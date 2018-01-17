import React from "react";
import styles from "./BrandImage.css";
import { Image } from "xelpmoc-core";
export default class BrandImage extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick();
    }
  }
  render() {
    let iconClass = styles.checkIcon;
    if (this.props.selected) {
      iconClass = styles.checkIconActive;
    }
    return (
      <div className={styles.base} onClick={() => this.handleClick()}>
        <div className={iconClass} />
        <div className={styles.imageHolder}>
          <Image image={this.props.image} />
        </div>
      </div>
    );
  }
}
