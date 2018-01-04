import React from "react";
// import "./css/ModalPanel.css";
import { default as ModalStyles } from "./ModalPanel.css";
export default class ModalPanel extends React.Component {
  render() {
    return (
      <div className={ModalStyles.base}>
        <div className={ModalStyles.background} />
        {this.props.children}
      </div>
    );
  }
}
