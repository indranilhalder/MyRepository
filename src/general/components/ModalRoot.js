import React from "react";
import ReactDOM from "react-dom";
import ModalPanel from "./ModalPanel";
import RestorePassword from "../../auth/components/RestorePassword";
import OtpVerification from "../../auth/components/OtpVerification";
import ConnectDetails from "../../home/components/ConnectDetailsWithModal";
import Sort from "../../plp/components/SortModal";
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

  submitOtp(userDetails) {
    this.props.otpVerification(userDetails);
    this.props.hideModal();
    this.props.history.push("/home");
  }

  resetPassword(userDetails) {
    this.props.resetPassword(userDetails);
    this.props.hideModal();
  }

  handleRestoreClick(userDetails) {
    this.props.forgotPassword(userDetails);
    this.props.hideModal();
  }

  submitOtpForgotPassword(userDetails) {
    this.props.forgotPasswordOtpVerification(userDetails);
    this.props.hideModal();
  }
  render() {
    const MODAL_COMPONENTS = {
      RestorePassword: (
        <RestorePassword
          handleCancel={() => this.handleClose()}
          handleRestoreClick={() => this.handleRestoreClick()}
        />
      ),
      SignUpOtpVerification: (
        <OtpVerification
          closeModal={() => this.handleClose()}
          submitOtp={() => this.submitOtp()}
        />
      ),
      ForgotPasswordOtpVerification: (
        <OtpVerification
          closeModal={() => this.handleClose()}
          submitOtp={() => this.submitOtpForgotPassword()}
        />
      ),

      ConnectDetails: (
        <ConnectDetails
          closeModal={() => this.handleClose()}
          {...this.props.ownProps}
        />
      ),

    
      Sort: <Sort />

    };

    let SelectedModal = MODAL_COMPONENTS[this.props.modalType];

    const Modal = this.props.modalStatus ? (
      <ModalPanel
        closeModal={() => {
          this.handleClose();
        }}
      >
        {SelectedModal}
      </ModalPanel>
    ) : null;

    return ReactDOM.createPortal(Modal, this.el);
  }
}
