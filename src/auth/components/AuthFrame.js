import React from "react";
import Icon from "../../xelpmoc-core/Icon";
import PropTypes from "prop-types";
import { TATA_CLIQ_ROOT } from "../../lib/apiRequest.js";
import tataLogo from "./img/tata_Logo.svg";
import { default as styles } from "./AuthFrame.css";
import SocialButtonsContainer from "../containers/SocialButtonsContainer.js";
import {
  TERMS_AND_CONDITION_URL,
  PRIVACY_POLICY_URL,
  SIGN_UP_PATH,
  LOGIN_PATH
} from "../../lib/constants";
export default class AuthFrame extends React.Component {
  goBack() {
    if (this.props.goBack) {
      this.props.goBack();
    }
  }
  redirectPage = url => {
    const urlSuffix = url.replace(TATA_CLIQ_ROOT, "$1");
    this.props.history.push(urlSuffix);
  };
  render() {
    return (
      <div className={styles.base}>
        {this.props.showCrossIcon && (
          <div className={styles.goBack} onClick={() => this.goBack()} />
        )}
        {this.props.showLogo && (
          <div className={styles.center}>
            <div className={styles.logo}>
              <Icon image={tataLogo} size={65} backgroundSize="auto 50px" />
            </div>
          </div>
        )}

        {this.props.children}
        {this.props.showSocialButtons && (
          <div className={styles.socialButtons}>
            <SocialButtonsContainer isSignUp={this.props.isSignUp} />
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
        {this.props.location &&
          this.props.location.pathname === LOGIN_PATH && (
            <div className={styles.legalLinkHolder}>
              <div className={styles.linkLabelLeft}>
                <div
                  className={styles.link}
                  onClick={() => this.redirectPage(TERMS_AND_CONDITION_URL)}
                >
                  T&C
                </div>
              </div>
              <div className={styles.linkLabelMiddle}>
                <div className={styles.callUs}>
                  <a href="tel:9029108282">Call Us</a>
                </div>
              </div>
              <div className={styles.linkLabelRight}>
                <div
                  className={styles.link}
                  onClick={() => this.redirectPage(PRIVACY_POLICY_URL)}
                >
                  Privacy Policy
                </div>
              </div>
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
  type: PropTypes.String,
  showLogo: PropTypes.bool
};

AuthFrame.defaultProps = {
  showSocialButtons: false,
  type: "Login",
  showCrossIcon: true,
  showLogo: true
};
