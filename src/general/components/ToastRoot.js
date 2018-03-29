import React from "react";
import ReactDOM from "react-dom";
import Toast from "./Toast";
const toastRoot = document.getElementById("toast-root");

export default class ToastRoot extends React.Component {
  constructor(props) {
    super(props);
    this.el = document.createElement("div");
  }

  componentDidMount() {
    toastRoot.appendChild(this.el);
  }
  render() {
    if (this.props.toastDisplayed) {
      const toast = <Toast data={this.props.message} />;
      return ReactDOM.createPortal(toast, this.el);
    } else {
      return null;
    }
  }
}
