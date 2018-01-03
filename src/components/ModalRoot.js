import React from "react";
import ModalPanel from "./ModalPanel";
import "./css/ModalRoot.css";
export default class ModalRoot extends React.Component {
  handleClose() {
    if (this.props.hideModal) {
      this.props.hideModal();
    }
  }
  onSaveVideo(video) {
    if (this.props.onSaveVideo) {
      this.props.onSaveVideo(video);
    }
    this.props.hideModal();
  }
  onSaveQuestion(jobApplication) {
    if (this.props.updateJobApplication) {
      this.props.updateJobApplication(jobApplication);
    }
    this.props.hideModal();
  }
  updateJobApplication(jobApplication) {
    if (this.props.updateJobApplication) {
      this.props.updateJobApplication(jobApplication);
    }
    this.props.hideModal();
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
    // const MODAL_COMPONENTS = {

    //   QUESTION_ANSWER_VIDEO_MODAL: questionsAnswerVideo
    // };

    //const SelectedModal = MODAL_COMPONENTS[this.props.modalType];
    const SelectedModal = <div>Noob sibot wins</div>;
    return (
      <ModalPanel>
        <div>
          <div className="ModalRoot-closeButton" />
          {SelectedModal}
        </div>
      </ModalPanel>
    );
  }
}
