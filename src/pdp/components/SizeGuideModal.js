import React from "react";
import SlideModal from "../../general/components/SlideModal";
import SizeGuideContainer from "../containers/SizeGuideContainer";
export default class SizeGuideModal extends React.Component {
  render() {
    return (
      <SlideModal closeModal={this.props.closeModal}>
        <SizeGuideContainer />
      </SlideModal>
    );
  }
}
