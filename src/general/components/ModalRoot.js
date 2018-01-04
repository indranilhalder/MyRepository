import React from "react";
import ReactDOM from "react-dom";
import ModalPanel from "./ModalPanel";
import RestorePassword from "../../auth/components/RestorePassword";
import "./ModalRoot.css";
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
    console.log(this.props);
    // const questionsAnswerVideo = (
    //   <QuestionAnswerVideo {...this.props.ownProps} />
    // );
    const MODAL_COMPONENTS = {
      RECOVER_PASSWORD: RestorePassword
    };

    const SelectedModal = this.props.modalStatus ? (
      <ModalPanel>
        <React.Fragment>
          <RestorePassword />
        </React.Fragment>
      </ModalPanel>
    ) : null;

    // return (
    //   <ModalPanel>
    //     <React.Fragment>
    //       <RestorePassword />
    //     </React.Fragment>
    //   </ModalPanel>
    // );
    return ReactDOM.createPortal(SelectedModal, this.el);
  }
}
