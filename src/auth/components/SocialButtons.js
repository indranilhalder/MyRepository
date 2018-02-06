import React, { Component } from "react";

import styles from "./SocialButtons.css";
import MediaQuery from "react-responsive";
import { CircleButton, Icon } from "xelpmoc-core";
import facebookImage from "./img/facebook.svg";
import desktopFacebookImage from "./img/facebook_desktop.svg";
import googlePlus from "./img/googlePlus.svg";
import desktopGooglePlus from "./img/googlePlus_desktop.svg";
import PropTypes from "prop-types";
import config from "../../lib/config";
const FACEBOOK_VERSION = "v2.11";
const FACEBOOK_SDK = "https://connect.facebook.net/en_US/sdk.js";
const GOOGLE_PLUS_SDK =
  "https://apis.google.com/js/client:platform.js?onload=gPOnLoad";
const SCRIPT = "script";
const FACEBOOK_JSDK = "facebook-jssdk";
const TYPE = "text/javascript";
export default class SocialButtons extends Component {
  componentDidMount() {
    //load FaceBook Sdk
    window.fbAsyncInit = () => {
      window.FB.init({
        appId: config.facebook,
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

    //Load Google Sdk
    (() => {
      var e = document.createElement(SCRIPT);
      e.type = TYPE;
      e.async = true;
      e.src = GOOGLE_PLUS_SDK;
      var t = document.getElementsByTagName(SCRIPT)[0];
      t.parentNode.insertBefore(e, t);
    })();
  }

  componentWillReceiveProps(nextProps) {
    if (this.props.user.isLoggedIn === true) {
      this.props.history.push("/home");
    }
  }

  facebookLogin = () => {
    if (this.props.facebookLogin) {
      this.props.facebookLogin();
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
        <MediaQuery query="(min-device-width: 1025px)">
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
  titleText: PropTypes.string,
  facebookLogin: PropTypes.func,
  googlePlusLogin: PropTypes.func
};
SocialButtons.defaultProps = {
  titleText: " Sign in with your social account"
};
