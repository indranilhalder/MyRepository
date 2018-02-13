import React from "react";
import { Icon } from "xelpmoc-core";
import PropTypes from "prop-types";
import tataLogo from "./img/tata_Logo.svg";
import { default as styles } from "./AuthFrame.css";
import SocialButtons from "./SocialButtons.js";

export default class AuthFrame extends React.Component {
  render() {
    return (
      <div className={styles.base}>
        <div className={styles.center}>
          <div className={styles.logo}>
            <Icon image={tataLogo} size={70} />
          </div>
        </div>
        {this.props.children}
        {this.props.showSocialButtons && (
          <div className={styles.socialButtons}>
            <SocialButtons {...this.props} />
          </div>
        )}

        {this.props.footerText && (
          <div
            onClick={() => this.props.footerClick()}
            className={styles.footer}
          >
            {this.props.footerText}
          </div>
        )}
      </div>
    );
  }
}

AuthFrame.propTypes = {
  footerText: PropTypes.string,
  footerClick: PropTypes.func,
  showSocialButtons: PropTypes.bool,
  type: PropTypes.String
};

AuthFrame.defaultProps = {
  showSocialButtons: false,
  type: "Login"
};
