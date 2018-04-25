import React, { Component } from "react";

import styles from "./SocialButtons.css";
import MediaQuery from "react-responsive";
import CircleButton from "../../xelpmoc-core/CircleButton";
import Icon from "../../xelpmoc-core/Icon";
import facebookImage from "./img/facebook.svg";
import desktopFacebookImage from "./img/facebook_desktop.svg";
import googlePlus from "./img/googlePlus.svg";
import desktopGooglePlus from "./img/googlePlus_desktop.svg";
import PropTypes from "prop-types";

import { SOCIAL_SIGN_UP, HOME_ROUTER } from "../../lib/constants";
const FACEBOOK_VERSION = "v2.11";
const FACEBOOK_SDK = "https://connect.facebook.net/en_US/sdk.js";
const SCRIPT = "script";
const FACEBOOK_JSDK = "facebook-jssdk";
const SIGN_IN_TEXT = "Sign in with your social account";
const SIGN_UP_TEXT = "Sign Up with your social account";
export default class SocialButtons extends Component {
  componentDidMount() {
    //load FaceBook Sdk
    window.fbAsyncInit = () => {
      window.FB.init({
        appId: process.env.REACT_APP_FACEBOOK_CLIENT_ID,
        cookie: true,
        xfbml: true,
        version: FACEBOOK_VERSION
      });
      window.FB.AppEvents.logPageView();
    };

    ((d, s, id) => {
      var js,
        fjs = d.getElementsByTagName(s)[0];
      if (d.getElementById(id)) return;
      js = d.createElement(s);
      js.id = id;
      js.src = FACEBOOK_SDK;
      fjs.parentNode.insertBefore(js, fjs);
    })(document, SCRIPT, FACEBOOK_JSDK);
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.user) {
      if (nextProps.user.isLoggedIn === true) {
        if (this.props.redirectToAfterAuthUrl) {
          this.props.history.replace(this.props.redirectToAfterAuthUrl);
          this.props.clearUrlToRedirectToAfterAuth();
        } else {
          this.props.history.replace(HOME_ROUTER);
        }
      }
    }
  }

  facebookLogin = () => {
    if (this.props.signUp) {
      this.props.facebookLogin(true);
    } else {
      this.props.facebookLogin(false);
    }
  };
  googlePlusLogin = () => {
    if (this.props.signUp) {
      this.props.googlePlusLogin(this.props.signUp);
    } else {
      this.props.googlePlusLogin(false);
    }
  };

  render() {
    let titleText =
      this.props.type === SOCIAL_SIGN_UP
        ? this.props.titleTextSignIp
        : this.props.titleTextLogin;
    return (
      <div className={styles.base}>
        <div className={styles.textHolder}>
          <p className={styles.title}>{titleText}</p>
        </div>
        <MediaQuery query="(min-device-width: 1025px)">
          <div className={styles.base}>
            <div className={styles.holder}>
              <CircleButton
                color={"rgba(0,0,0,0)"}
                size={50}
                onClick={this.facebookLogin}
                icon={<Icon image={desktopFacebookImage} size={50} />}
              />
            </div>
            <div className={styles.separator} />
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
        <MediaQuery query="(max-device-width: 1024px)">
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
            <div className={styles.separator} />

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
  titleTextSignIp: PropTypes.string,
  titleTextLogin: PropTypes.string,
  facebookLogin: PropTypes.func,
  googlePlusLogin: PropTypes.func
};
SocialButtons.defaultProps = {
  titleTextLogin: SIGN_IN_TEXT,
  titleTextSignIp: SIGN_UP_TEXT
};
