import React from "react";
import SlideModal from "../../general/components/SlideModal";
export default class ConnectDetailsWithModal extends React.Component {
  render() {
    return (
      <SlideModal {...this.props}>
        <div
          style={{
            width: "100%",
            height: "150vh",
            backgroundImage: "linear-gradient(292deg, #48dfe6, #4facfe)"
          }}
        />
      </SlideModal>
    );
  }
}
