import React from "react";
import "./css/ModalPanel.css";

export default class ModalPanel extends React.Component {
  render() {
    return (
      <div className="ModalPanel">
        <div className="ModalPanel-background" />
        <div className="ModalPanel-content">{this.props.children}</div>
      </div>
    );
  }
}
