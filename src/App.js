import React, { Component } from "react";
import { Button } from "xelpmoc-core";
import ModalContainer from "./general/containers/ModalContainer";
import { default as AppStyles } from "./App.css";
class App extends Component {
  render() {
    let className = AppStyles.base;
    if (this.props.modalStatus) {
      className = AppStyles.blur;
    }
    console.log(className);
    console.log(this.props);

    return (
      <div className="App">
        <header className="App-header">
          <h1 className="App-title">Welcome to Tata</h1>
        </header>
        <ModalContainer />
      </div>
    );
  }
}

export default App;
