import React from "react";
import styles from "./NewBrand.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import Follow from "./Follow";
import Logo from "./Logo";

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
        <div className={styles.imageHolder}>
          <Image image={this.props.image} color="transparent" />
          <div className={styles.brandOverlay}>
            <div className={styles.brandTextHolder}>
              <div className={styles.brandWrapper}>
                <div className={styles.brandText}>{this.props.label}</div>
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
            <div className={styles.brandLogo}>
              <Logo image={this.props.logo} />
            </div>
          </div>
        </div>
      </div>
    );
  }
}
NewBrand.propTypes = {
  image: PropTypes.string,
  label: PropTypes.string,
  onFollowClick: PropTypes.func,
  onUnFollowClick: PropTypes.func,
  follow: PropTypes.bool,
  logo: PropTypes.string
};
NewBrand.defaultProps = {
  follow: false
};
