import React from "react";
import styles from "./BrandImage.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
export default class BrandImage extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.history.push("/productListings");
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
          <Image
            image={this.props.image}
            color="transparent"
            fit={this.props.fit}
          />
        </div>
      </div>
    );
  }
}
BrandImage.propTypes = {
  image: PropTypes.string,
  selected: PropTypes.bool,
  onClick: PropTypes.func
};
BrandImage.defaultProps = {
  image: "",
  selected: false,
  fit: "contain"
};
