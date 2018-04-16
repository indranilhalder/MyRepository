import React from "react";
import styles from "./NewBrand.css";
import Image from "../../xelpmoc-core/Image";
import PropTypes from "prop-types";
import Follow from "./Follow";
import Logo from "./Logo";

export default class NewBrand extends React.Component {
  handleClick() {
    if (this.props.follow) {
      this.onFollowClick();
    } else {
      this.onUnFollowClick();
    }
  }

  handleBrandClick = () => {
    this.props.onClick(this.props.webUrl);
  };
  render() {
    let productCount = `${this.props.label && this.props.label.split(" ")[0]}`;
    let totalNumberOfProduct = parseInt(productCount);
    return (
      <div className={styles.base} onClick={this.handleBrandClick}>
        <div className={styles.imageHolder}>
          <Image image={this.props.image} color="transparent" />
          <div className={styles.brandOverlay}>
            <div className={styles.brandTextHolder}>
              <div className={styles.brandWrapper}>
                {totalNumberOfProduct > 0 && (
                  <div className={styles.brandText}>{this.props.label}</div>
                )}
              </div>
              <div className={styles.brandButton}>
                <Follow
                  onClick={follow => this.handleClick(follow)}
                  follow={this.props.follow}
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
  logo: PropTypes.string,
  onClick: PropTypes.func
};
NewBrand.defaultProps = {
  follow: false
};
