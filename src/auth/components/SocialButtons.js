import React, { Component } from "react";

import styles from "./SocialButtons.css";
import MediaQuery from "react-responsive";
import { CircleButton, Icon } from "xelpmoc-core";
import facebookImage from "./img/facebook.svg";
import desktopFacebookImage from "./img/facebook_desktop.svg";
import googlePlus from "./img/googlePlus.svg";
import desktopGooglePlus from "./img/googlePlus_desktop.svg";
import PropTypes from "prop-types";
export default class SocialButtons extends Component {
  facebookLogin = () => {
    if (this.props.facebookLogin) {
      this.props.facebookLogin();
    }
  };
  twitterLogin = () => {
    if (this.props.twitterLogin) {
      this.props.twitterLogin();
    }
  };
  googlePlusLogin = () => {
    if (this.props.googlePlusLogin) {
      this.props.googlePlusLogin();
    }
  };

  render() {
    return (
      <div>
        <MediaQuery query="(min-device-width: 1024px)">
          <div className={styles.textHolder}>
            <p className={styles.title}>{this.props.titleText}</p>
          </div>
          <div className={styles.base}>
            <div className={styles.holder}>
              <CircleButton
                color={"rgba(0,0,0,0)"}
                size={50}
                onClick={this.facebookLogin}
                icon={<Icon image={desktopFacebookImage} size={50} />}
              />
            </div>
            <div className={styles.seperator} />
            <div className={styles.holder}>
              <CircleButton
                color={"rgba(0,0,0,0)"}
                size={50}
                onClick={this.googlePlusLogin}
                icon={<Icon image={desktopGooglePlus} size={50} />}
              />
            </div>
          </div>
        </MediaQuery>
        <MediaQuery query="(max-device-width: 1023px)">
          <div className={styles.textHolder}>
            <p className={styles.title}>{this.props.titleText}</p>
          </div>
          <div className={styles.base}>
            <div className={styles.holder}>
              <CircleButton
                color={"rgba(0,0,0,0)"}
                size={45}
                onClick={this.facebookLogin}
                icon={<Icon image={facebookImage} size={45} />}
              />
            </div>
            <div className={styles.seperator} />

            <div className={styles.holder}>
              <CircleButton
                color={"rgba(0,0,0,0)"}
                size={45}
                onClick={this.googlePlusLogin}
                icon={<Icon image={googlePlus} size={45} />}
              />
            </div>
          </div>
        </MediaQuery>
      </div>
    );
  }
}

SocialButtons.propTypes = {
  titleText: PropTypes.string,
  facebookLogin: PropTypes.func,
  twitterLogin: PropTypes.func,
  googlePlusLogin: PropTypes.func
};
SocialButtons.defaultProps = {
  titleText: " Sign in with your social account"
};
