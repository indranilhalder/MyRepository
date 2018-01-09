import React from "react";
// import "./css/ModalPanel.css";
import { default as ModalStyles } from "./ModalPanel.css";
export default class ModalPanel extends React.Component {
  handleClick() {
    if (this.props.closeModal) {
      this.props.closeModal();
    }
  }
  render() {
    return (
      <div className={ModalStyles.base}>
        <div
          className={ModalStyles.background}
          onClick={() => this.handleClick()}
        />
        {this.props.children}
      </div>
    );
  }
}
