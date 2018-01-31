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
export default class SocialButtons extends Component {
  componentDidMount() {
    //load FaceBook Sdk
    window.fbAsyncInit = function() {
      window.FB.init({
        appId: config.facebook,
        cookie: true,
        xfbml: true,
        version: "v2.11"
      });
      window.FB.AppEvents.logPageView();
    };

    (function(d, s, id) {
      var js,
        fjs = d.getElementsByTagName(s)[0];
      if (d.getElementById(id)) return;
      js = d.createElement(s);
      js.id = id;
      js.src = "https://connect.facebook.net/en_US/sdk.js";
      fjs.parentNode.insertBefore(js, fjs);
    })(document, "script", "facebook-jssdk");

    //Load Google Sdk
    (function() {
      var e = document.createElement("script");
      e.type = "text/javascript";
      e.async = true;
      e.src = "https://apis.google.com/js/client:platform.js?onload=gPOnLoad";
      var t = document.getElementsByTagName("script")[0];
      t.parentNode.insertBefore(e, t);
    })();
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
