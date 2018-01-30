import React, { Component } from "react";
import ModalContainer from "./general/containers/ModalContainer";
import { Route, Switch } from "react-router-dom";
import { default as AppStyles } from "./App.css";
import Auth from "./auth/components/MobileAuth.js";
import HomeContainer from "./home/containers/HomeContainer.js";
import * as Cookie from "./lib/Cookie";

class App extends Component {
  componentWillMount() {
    let globalCookie = Cookie.getCookie("sessionObjectGlobal");
    if (!globalCookie) {
      this.props.getGlobalAccessToken();
    }
    let customerCookie = Cookie.getCookie("sessionObjectCustomer");
    console.log(localStorage.getItem("refresh_token"));
    if (!customerCookie && localStorage.getItem("refresh_token")) {
      this.props.refreshToken(localStorage.getItem("refresh_token"));
    }
  }

  render() {
    let className = AppStyles.base;
    if (this.props.modalStatus) {
      className = AppStyles.blur;
    }

    return (
      <div className={className}>
        <Switch>
          <Route path="/home" component={HomeContainer} />
          <Route
            path="/"
            render={routeProps => <Auth {...routeProps} {...this.props} />}
          />
        </Switch>
        <ModalContainer />
      </div>
    );
  }
}

export default App;
