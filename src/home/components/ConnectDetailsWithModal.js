import React from "react";
import SlideModal from "../../general/components/SlideModal";
import ConnectBaseWidget from "./ConnectBaseWidget";
export default class ConnectDetailsWithModal extends React.Component {
  render() {
    return (
      <SlideModal {...this.props}>
        <ConnectBaseWidget {...this.props.data} />
      </SlideModal>
    );
  }
}
