import React, { Component } from "react";
import logo from "./logo.svg";
import styles from "./App.css";

import { Input } from "xelpmoc-core";

class App extends Component {
  render() {
    console.log(styles);

    return (
      <div className="App">
        <header className="App-header">
          <img src={logo} className="App-logo" alt="logo" />
          <h1 className="App-title">Welcome to React</h1>
        </header>
        <Input />
      </div>
    );
  }
}

export default App;
