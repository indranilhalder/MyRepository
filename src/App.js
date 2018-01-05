import React, { Component } from "react";
import { Button } from "xelpmoc-core";
import ModalContainer from "./general/containers/ModalContainer";
import { Route } from "react-router-dom";
import { RESTORE_PASSWORD } from "./general/modal.actions.js";
import { default as AppStyles } from "./App.css";
import MediaQuery from "react-responsive";
import MobileAuth from "./MobileAuth.js";
class App extends Component {
  render() {
    let className = AppStyles.base;
    if (this.props.modalStatus) {
      className = AppStyles.blur;
    }

    return (
      <div className={className}>
        <MediaQuery query="(max-device-width: 1024px)">
          <Route path="/auth" component={MobileAuth} />
        </MediaQuery>
      </div>
    );
  }
}

export default App;
