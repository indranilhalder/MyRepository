import React from "react";
import CancelOrderPopUp from "./CancelOrderPopUp.js";
import BottomSlideModal from "../../general/components/BottomSlideModal.js";
export default class CancelOrderModal extends React.Component {
  cancelProduct() {
    if (this.props.cancelProduct) {
      this.props.cancelProduct(
        this.props.data.cancelProductDetails,
        this.props.data.productDetails
      );
    }
  }

  cancelModal() {
    if (this.props.cancelModal) {
      this.props.cancelModal();
    }
  }
  render() {
    return (
      <BottomSlideModal>
        <CancelOrderPopUp
          cancelModal={() => this.cancelModal()}
          cancelProduct={() => this.cancelProduct()}
        />
      </BottomSlideModal>
    );
  }
}
