import React from "react";
import ModalPanel from "./ModalPanel";
import RestorePassword from "../../auth/components/RestorePassword";
import "./ModalRoot.css";
export default class ModalRoot extends React.Component {
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
    // const questionsAnswerVideo = (
    //   <QuestionAnswerVideo {...this.props.ownProps} />
    // );
    const MODAL_COMPONENTS = {
      RECOVER_PASSWORD: RestorePassword
    };

    const SelectedModal = MODAL_COMPONENTS["RECOVER_PASSWORD"];

    return (
      <ModalPanel>
        <React.Fragment>
          <RestorePassword />
        </React.Fragment>
      </ModalPanel>
    );
  }
}
