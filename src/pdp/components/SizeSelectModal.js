import React from "react";
import BottomSlideModal from "../../general/components/BottomSlideModal";
import SizeSelector from "./SizeSelector";

export default class SizeSelectModal extends React.Component {
  render() {
    return (
      <BottomSlideModal>
        <SizeSelector {...this.props} />
      </BottomSlideModal>
    );
  }
}
