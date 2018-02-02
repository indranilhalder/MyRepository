import React, { Component } from "react";
import ModalContainer from "./general/containers/ModalContainer";
import { Route, Switch, Redirect } from "react-router-dom";
import { default as AppStyles } from "./App.css";
import Auth from "./auth/components/MobileAuth.js";
import HomeContainer from "./home/containers/HomeContainer.js";
import * as Cookie from "./lib/Cookie";
import {
  GLOBAL_ACCESS_TOKEN,
  CUSTOMER_ACCESS_TOKEN,
  REFRESH_TOKEN
} from "./lib/constants.js";

const PrivateRoute = ({ component: Component, ...rest }) => (
  <Route
    {...rest}
    render={props =>
      auth.isAuthenticated === true ? (
        <Component {...props} />
      ) : (
        <Redirect
          to={{
            pathname: "/"
          }}
        />
      )
    }
  />
);

const auth = {
  isAuthenticated: false
};

class App extends Component {
  componentWillMount() {
    let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
    if (!globalCookie) {
      this.props.getGlobalAccessToken();
    }

    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    if (!customerCookie && localStorage.getItem(REFRESH_TOKEN)) {
      this.props.refreshToken(localStorage.getItem(REFRESH_TOKEN));
    }
    if (customerCookie) {
      auth.isAuthenticated = true;
    }
  }

  getAuthToken = () => {
    let customerCookie = Cookie.getCookie("sessionObjectCustomer");
    if (customerCookie) {
      return true;
    }
    return false;
  };

  render() {
    let className = AppStyles.base;
    if (this.props.modalStatus) {
      className = AppStyles.blur;
    }

    return (
      <div className={className}>
        <Switch>
          <PrivateRoute path="/home" component={HomeContainer} />
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
