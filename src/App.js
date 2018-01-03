import React, { Component } from "react";
import { Button } from "xelpmoc-core";
import ModalPortal from "./components/ModalPortal";
import ModalContainer from "./containers/ModalContainer";
import { default as AppStyles } from "./App.css";
class App extends Component {
  render() {
    let className = AppStyles.base;
    if (this.props.modalStatus) {
      className = AppStyles.blur;
    }
    console.log(className);
    console.log(this.props);
    const modal = this.props.modalStatus ? (
      <ModalPortal>
        <ModalContainer />
      </ModalPortal>
    ) : null;
    return (
      <div className={className}>
        <Button
          label="Show Modal"
          width={100}
          onClick={() => {
            this.props.showModal();
          }}
        />
        {modal}
      </div>
    );
  }
}

export default App;
