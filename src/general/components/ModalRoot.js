import React from "react";
import ReactDOM from "react-dom";
import ModalPanel from "./ModalPanel";
import RestorePassword from "../../auth/components/RestorePassword";
import OtpVerification from "../../auth/components/OtpVerification";
const modalRoot = document.getElementById("modal-root");
export default class ModalRoot extends React.Component {
  constructor(props) {
    super(props);
    this.el = document.createElement("div");
  }
  componentDidMount() {
    modalRoot.appendChild(this.el);
  }
  componentWillUnmount() {
    modalRoot.removeChild(this.el);
  }
  handleClose() {
    if (this.props.hideModal) {
      this.props.hideModal();
    }
  }

  updateJob(job) {
    if (this.props.updateJob) {
      this.props.updateJob(job);
    }
    this.props.hideModal();
  }
  render() {
    const MODAL_COMPONENTS = {
      RestorePassword: <RestorePassword />,
      OtpVerification: <OtpVerification />
    };

    let SelectedModal = MODAL_COMPONENTS[this.props.modalType];
    const Modal = this.props.modalStatus ? (
      <ModalPanel>{SelectedModal}</ModalPanel>
    ) : null;

    return ReactDOM.createPortal(Modal, this.el);
  }
}
