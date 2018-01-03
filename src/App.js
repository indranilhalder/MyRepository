import React, { Component } from "react";
import logo from "./logo.svg";
import styles from "./App.css";
import { Input } from "xelpmoc-core";
import ModalPortal from "./components/ModalPortal";
import ModalContainer from "./containers/ModalContainer";
class App extends Component {
  render() {
    console.log(styles);
    const modal = true ? (
      <ModalPortal>
        <ModalContainer />
      </ModalPortal>
    ) : null;
    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to React</h1>
        </header>
        <Input />
        {modal}
      </div>
    );
  }
}

export default App;
