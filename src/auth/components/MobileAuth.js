import React from "react";
import AuthFrame from "./AuthFrame.js";
import { LOGIN_PATH, SIGN_UP_PATH, MAIN_ROUTER } from "../../lib/constants";

export default class Auth extends React.Component {
  navigateToSignUp() {
    this.props.history.push(SIGN_UP_PATH);
  }

  navigateToLogin() {
    this.props.history.push(LOGIN_PATH);
  }

  render() {
    const pathName = this.props.location.pathname;
    let footerText = "";
    let footerClick;
    let showSocialButtons;
    if (pathName === LOGIN_PATH || MAIN_ROUTER) {
      footerText = "Don't have an account? Sign up";
      footerClick = () => this.navigateToSignUp();
      showSocialButtons = true;
    }

    if (pathName === SIGN_UP_PATH) {
      footerText = "Already have an account? Login";
      footerClick = () => this.navigateToLogin();
      showSocialButtons = false;
    }
    return (
      <AuthFrame
        {...this.props}
        showSocialButtons={showSocialButtons}
        footerText={footerText}
        footerClick={footerClick}
      />
    );
  }
}
