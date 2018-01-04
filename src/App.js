import React, { Component } from "react";
import { Button } from "xelpmoc-core";
import ModalPortal from "./general/components/ModalPortal";
import ModalContainer from "./general/containers/ModalContainer";
import { default as AppStyles } from "./App.css";
class App extends Component {
  render() {
    let className = AppStyles.base;
    if (this.props.modalStatus) {
      className = AppStyles.blur;
    }

    const modal = this.props.modalStatus ? (
      <ModalPortal>
        <ModalContainer />
      </ModalPortal>
    ) : null;
    return <div className={className}>{modal}</div>;
  }
}

export default App;
