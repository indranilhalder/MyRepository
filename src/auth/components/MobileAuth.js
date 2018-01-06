import React from "react";
import { Route, withRouter, Switch } from "react-router-dom";
import AuthFrame from "./AuthFrame.js";
import Login from "./Login.js";
import SignUp from "./SignUp.js";
import LoginContainer from "../containers/LoginContainer.js";
import PropTypes from "prop-types";

const LOGIN_PATH = "/login";
const SIGN_UP_PATH = "/sign_up";

export default class Auth extends React.Component {
  navigateToSignUp() {
    this.props.history.push(SIGN_UP_PATH);
  }

  render() {
    // I have the router
    // If the path is /login, I need to navigate to /sign_up
    // if hte path is /sign_up I need to navigate to /login
    const pathName = this.props.location.pathname;
    let footerText = "";
    let footerClick;
    if (pathName === LOGIN_PATH) {
      footerText = "Don't have an account? Sign up";
      footerClick = this.navigateToSignUp;
    }

    if (pathName === SIGN_UP_PATH) {
      footerText = "Already have an account? Login";
    }
    return (
      <AuthFrame
        footerText={footerText}
        footerClick={() => this.navigateToSignUp()}
      >
        <LoginContainer />
      </AuthFrame>
    );
  }
}

// Auth

// Auth needs a container that passes in router

/*
  THere is a Modal Container that listens to the modal state
    That displays modal root
      So we'll need to pass in a a modal type there.

  That means that auth needs a showModal method, so it will require a container.
  It will also need the


*/
