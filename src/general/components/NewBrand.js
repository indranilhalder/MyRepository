import React from "react";
import styles from "./NewBrand.css";
import { Image } from "xelpmoc-core";
import PropTypes from "prop-types";
import { FollowUnFollowButtonContainer } from "../../pdp/containers/FollowUnFollowButtonContainer";
import Logo from "./Logo";
import { HOME_FEED_FOLLOW_AND_UN_FOLLOW } from "../../lib/constants";
import Follow from "./Follow";

export default class NewBrand extends React.Component {
  handleBrandClick = () => {
    this.props.onClick(this.props.webUrl);
  };
  render() {
    let productCount = `${this.props.label && this.props.label.split(" ")[0]}`;
    let totalNumberOfProduct = parseInt(productCount);
    return (
      <div className={styles.base}>
        <div className={styles.imageHolder} onClick={this.handleBrandClick}>
          <Image image={this.props.image} color="transparent" />
          <div className={styles.brandOverlay}>
            <div className={styles.brandTextHolder}>
              <div className={styles.brandWrapper}>
                {totalNumberOfProduct > 0 && (
                  <div className={styles.brandText}>{this.props.label}</div>
                )}
              </div>
            </div>
            <div className={styles.brandLogo}>
              <img className={styles.image} src={this.props.logo} alt="" />
            </div>
          </div>
        </div>
        <div className={styles.brandButton}>
          <FollowUnFollowButtonContainer
            brandId={this.props.brandId}
            isFollowing={this.props.isFollowing}
            pageType={HOME_FEED_FOLLOW_AND_UN_FOLLOW}
            positionInFeed={this.props.positionInFeed}
          />
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
