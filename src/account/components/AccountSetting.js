import React from "react";
import styles from "./AccountSetting.css";
import ProfileImage from "../../xelpmoc-core/ProfileImage";
import PropTypes from "prop-types";
import UnderLinedButton from "../../general/components/UnderLinedButton";
export default class AccountSetting extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    let heading, name;
    if (this.props.heading) {
      heading = this.props.heading;
      name = heading.trim();
    }

    return (
      <div className={styles.base}>
        <div className={styles.accountHolder}>
          <div className={styles.profileImageHolder}>
            <div className={styles.accountImage}>
              {this.props.image && <ProfileImage image={this.props.image} />}
              {name &&
                name !== "undefined" && (
                  <div className={styles.accountImageText}>
                    {this.props.firstName}
                  </div>
                )}
            </div>
            <div className={styles.headingText}>
              {name && name !== "undefined" && this.props.heading}
              {this.props.lastName &&
                this.props.lastName !== "undefined" &&
                this.props.lastName}
            </div>
          </div>
          {this.props.label && (
            <div className={styles.buttonHolder}>
              <div className={styles.button}>
                <UnderLinedButton
                  size="14px"
                  fontFamily="regular"
                  color="#ff1744"
                  label={this.props.label}
                  onClick={() => this.handleClick()}
                />
              </div>
            </div>
          )}
        </div>
        {this.props.isDetails && (
          <div className={styles.accountSettingFooter}>
            <div className={styles.accountInformationHolder}>
              <div className={styles.accountInformationCount}>
                {this.props.followedBrandsCount}
              </div>
              <div className={styles.accountInformation}>
                {this.props.followedBrands}
              </div>
            </div>
            <div className={styles.accountInformationHolder}>
              <div className={styles.accountInformationCount}>
                {this.props.likedProductsCount}
              </div>
              <div className={styles.accountInformation}>
                {this.props.likedProducts}
              </div>
            </div>
            <div className={styles.accountInformationHolder}>
              <div className={styles.accountInformationCount}>
                {this.props.orderPlacedCount}
              </div>
              <div className={styles.accountInformation}>
                {this.props.orderPlaced}
              </div>
            </div>
          </div>
        )}
      </div>
    );
  }
}

AccountSetting.propTypes = {
  orderPlaced: PropTypes.string,
  orderPlacedCount: PropTypes.string,
  likedProductsCount: PropTypes.string,
  likedProducts: PropTypes.string,
  followedBrandsCount: PropTypes.string,
  followedBrands: PropTypes.string,
  onClick: PropTypes.func,
  isDetails: PropTypes.bool
};
AccountSetting.defaultProps = {
  isDetails: false
};
