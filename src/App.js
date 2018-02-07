import React, { Component } from "react";
import ModalContainer from "./general/containers/ModalContainer";
import { Route, Switch, Redirect } from "react-router-dom";
import { default as AppStyles } from "./App.css";
import Auth from "./auth/components/MobileAuth.js";
import HomeContainer from "./home/containers/HomeContainer.js";
import ProductListingsContainer from "./plp/containers/ProductListingsContainer";
import ProductDescriptionContainer from "./pdp/containers/ProductDescriptionContainer";
import * as Cookie from "./lib/Cookie";
import {
  HOME_ROUTER,
  PRODUCT_LISTINGS,
  PRODUCT_DESCRIPTION_ROUTER,
  MAIN_ROUTER
} from "../src/lib/constants";
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
            pathname: { MAIN_ROUTER }
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
    this.getAccessToken();
  }

  getAccessToken = () => {
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
  };
  render() {
    let className = AppStyles.base;
    if (this.props.modalStatus) {
      className = AppStyles.blur;
    }

    return (
      <div className={className}>
        <Switch>

          <Route path={PRODUCT_LISTINGS} component={ProductListingsContainer} />
          <Route path={HOME_ROUTER} component={HomeContainer} />
          <Route path={MAIN_ROUTER} component={Auth} />

          <Route
            path={PRODUCT_DESCRIPTION_ROUTER}
            component={ProductDescriptionContainer}
          />
        </Switch>
        <ModalContainer />
      </div>
    );
  }
}

export default App;
