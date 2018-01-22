import React from "react";
import styles from "./NewBrand.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
// import CoreButton from './Button';
import Follow from "./Follow";
import Logo from "./Logo";
import { Icon } from "xelpmoc-core";
import newBrandIcon from "./img/Nike.svg";
import newBrandImage from "./img/brandGirl.jpg";

export default class NewBrand extends React.Component {
  onFollowClick() {
    if (this.props.onFollowClick) {
      this.props.onFollowClick();
    }
  }
  onUnFollowClick() {
    if (this.props.onUnFollowClick) {
      this.props.onUnFollowClick();
    }
  }
  handleClick() {
    if (this.props.follow) {
      this.onFollowClick();
    } else {
      this.onUnFollowClick();
    }
  }
  render() {
    return (
      <div className={styles.base}>
        <div
          className={styles.imageHolder}
          style={{ backgroundImage: `url(${this.props.image})` }}
        >
          {/* <Image image={this.props.newBrandImage} color="transparent" /> */}
          <div className={styles.brandOverlay}>
            <div className={styles.brandTextHolder}>
              <div className={styles.brandWrapper}>
                <div className={styles.brandText}>24 new products</div>
              </div>
              <div className={styles.brandButton}>
                <Follow
                  onClick={() => this.handleClick()}
                  follow={this.props.follow}
                  onFollowClick={() => {
                    this.props.onFollowClick();
                  }}
                  onUnFollowClick={() => {
                    this.props.onFollowClick();
                  }}
                />
              </div>
            </div>
            <div className={styles.brandIcon}>
              <Logo image={this.props.newBrandImage} />
            </div>
          </div>
        </div>
      </div>
    );
  }
}
NewBrand.propTypes = {
  image: PropTypes.string,
  onFollowClick: PropTypes.func,
  onUnFollowClick: PropTypes.func,
  follow: PropTypes.bool,
  newBrandImage: PropTypes.string
};
NewBrand.defaultProps = {
  image: newBrandImage,
  follow: true,
  newBrandImage: newBrandIcon
};
