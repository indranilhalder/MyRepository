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
                color="#ff1744"
                label={this.props.label}
                onClick={() => this.handleClick()}
              />
            </div>
          </div>
        </div>
        <div className={styles.accounSetingFooter}>
          <div className={styles.accountInformationHolder}>
            <div className={styles.accountInformationCount}>37</div>
            <div className={styles.accountInformation}>Followed</div>
            <div className={styles.accountInformation}>brands</div>
          </div>
          <div className={styles.accountInformationHolder}>
            <div className={styles.accountInformationCount}>201</div>
            <div className={styles.accountInformation}>Liked</div>
            <div className={styles.accountInformation}>products</div>
          </div>
          <div className={styles.accountInformationHolder}>
            <div className={styles.accountInformationCount}>03</div>
            <div className={styles.accountInformation}>Orders</div>
            <div className={styles.accountInformation}>placed</div>
          </div>
        </div>
      </div>
    );
  }
}
AccountSetting.propTypes = {
  heading: PropTypes.string,
  label: PropTypes.string,
  onClick: PropTypes.func,
  image: PropTypes.string,
  color: PropTypes.string
};
AccountSetting.defaultProps = {
  image: AccountSettingIcon,
  heading: "Gangesh Roy",
  color: "#ff1744",
  label: "Account Settings"
};
