import React from "react";
import styles from "./AccountSetting.css";
import { ProfileImage } from "xelpmoc-core";
import PropTypes from "prop-types";
import AccountSettingIcon from "../../general/components/img/groy.jpg";
import UnderLinedButton from "../../general/components/UnderLinedButton";
export default class AccountSetting extends React.Component {
  handleClick = () => {
    if (this.props.onClick) {
      this.props.onClick();
    }
  };
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.accountHolder}>
          <div className={styles.accountHolderLeft}>
            <div className={styles.accountImage}>
              <ProfileImage image={this.props.image} />
            </div>
            <div className={styles.headingText}>{this.props.heading}</div>
          </div>
          <div className={styles.accountHolderRight}>
            <div className={styles.button}>
              <UnderLinedButton
                size="14px"
                fontFamily="regular"
                color={this.props.color}
                label={this.props.label}
                onClick={() => this.handleClick()}
              />
            </div>
          </div>
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
  image: AccountSettingIcon,
  isDetails: false,
  heading: "Ananya R. Patel",
  color: "#ff1744",
  label: "Account Settings"
};
