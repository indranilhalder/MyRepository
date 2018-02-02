import React, { Component } from "react";
import ModalContainer from "./general/containers/ModalContainer";
import { Route, Switch } from "react-router-dom";
import { default as AppStyles } from "./App.css";
import Auth from "./auth/components/MobileAuth.js";
import HomeContainer from "./home/containers/HomeContainer.js";
import ProductListingContainer from "./plp/containers/ProductListingContainer";
class App extends Component {
  render() {
    let className = AppStyles.base;
    if (this.props.modalStatus) {
      className = AppStyles.blur;
    }

    return (
      <div className={className}>
        <Switch>
          <Route path="/Product" component={ProductListingContainer} />
          <Route path="/home" component={HomeContainer} />
          <Route path="/" component={Auth} />
        </Switch>
        <ModalContainer />
      </div>
    );
  }
}

export default App;
