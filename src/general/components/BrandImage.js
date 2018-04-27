import React from "react";
import styles from "./BrandImage.css";
import Image from "../../xelpmoc-core/Image";
import PropTypes from "prop-types";
export default class BrandImage extends React.Component {
  handleClick() {
    if (this.props.onClick) {
      this.props.onClick(this.props.value);
    }
  }
  render() {
    let iconClass = styles.checkIcon;
    if (this.props.selected) {
      iconClass = styles.checkIconActive;
    }
    return (
      <div>
        {this.props.isFollowing === "true" && (
          <div className={styles.base} onClick={() => this.handleClick()}>
            <div className={iconClass} />
            <div className={styles.imageHolder}>
              <Image
                image={this.props.image}
                color="transparent"
                fit="contain"
              />
            </div>
          </div>
        )}
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
