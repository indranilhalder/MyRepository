import React from "react";
import BottomSlideModal from "../../general/components/BottomSlideModal";
import SortContainer from "../containers/SortContainer.js";
export default class SortModal extends React.Component {
  render() {
    return (
      <BottomSlideModal>
        <SortContainer />
      </BottomSlideModal>
    );
  }
}
