import React from "react";
import AuthHeader from "./AuthHeader.js";
import Login from "./Login.js";

export default class MobileAuthLogin extends React.Component {
  render() {
    return (
      <div>
        <AuthHeader>
          <Login />
        </AuthHeader>
      </div>
    );
  }
}
