import React from "react";
import { Route, Switch } from "react-router-dom";
import AuthFrame from "./AuthFrame.js";
import LoginContainer from "../containers/LoginContainer.js";
import SignUpContainer from "../containers/SignUpContainer.js";
const LOGIN_PATH = "/login";
const SIGN_UP_PATH = "/sign_up";

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
    if (pathName === LOGIN_PATH || "/") {
      footerText = "Don't have an account? Sign up";
      footerClick = () => this.navigateToSignUp();
    }

    if (pathName === SIGN_UP_PATH) {
      footerText = "Already have an account? Login";
      footerClick = () => this.navigateToLogin();
    }
    return (
      <AuthFrame footerText={footerText} footerClick={footerClick}>
        <Switch>
          <Route path="/login" component={LoginContainer} />
          <Route path="/sign_up" component={SignUpContainer} />
          <Route path="*" component={LoginContainer} />
        </Switch>
      </AuthFrame>
    );
  }
}
