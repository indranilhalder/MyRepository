import React, { Component } from "react";
import ModalContainer from "./general/containers/ModalContainer";
import { Route, Switch } from "react-router-dom";
import { default as AppStyles } from "./App.css";
import Auth from "./auth/components/MobileAuth.js";
import HomeContainer from "./home/containers/HomeContainer.js";
import ProductListingsContainer from "./plp/containers/ProductListingsContainer";
class App extends Component {
  render() {
    let className = AppStyles.base;
    if (this.props.modalStatus) {
      className = AppStyles.blur;
    }

    return (
      <div className={className}>
        <Switch>
          <Route path="/Products" component={ProductListingsContainer} />
          <Route path="/home" component={HomeContainer} />
          <Route path="/" component={Auth} />
        </Switch>
        <ModalContainer />
      </div>
    );
  }
}

export default App;
