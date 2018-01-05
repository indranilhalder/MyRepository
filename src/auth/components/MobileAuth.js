import React from "react";
import { Route, withRouter } from "react-router-dom";
import AuthHeader from "./AuthHeader.js";
import Login from "./Login.js";

class Auth extends React.Component {
  render() {
    return (
      <AuthHeader>
        <Route path="/login" component={Login} />
        {/*<Route path="/auth/login" component={SignUp} />*/}
      </AuthHeader>
    );
  }
}

const MobileAuth = withRouter(Auth);

export default MobileAuth;

/*
  THere is a Modal Container that listens to the modal state
    That displays modal root
      So we'll need to pass in a a modal type there.

  That means that auth needs a showModal method, so it will require a container.
  It will also need the


*/
