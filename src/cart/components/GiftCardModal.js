import React from "react";
import PropTypes from "prop-types";
import GiftCardPopup from "./GiftCardPopup.js";
import BottomSlideModal from "../../general/components/BottomSlideModal.js";
export default class GiftCardModal extends React.Component {
  addGiftCard(val) {
    if (this.props.addGiftCard) {
      this.props.addGiftCard(val);
    }
  }
  render() {
    return (
      <BottomSlideModal>
        <GiftCardPopup
          addGiftCard={val => this.addGiftCard(val)}
          voucherNumber={this.props.voucherNumber}
          voucherPin={this.props.voucherPin}
          loading={this.props.loading}
        />
      </BottomSlideModal>
    );
  }
}
GiftCardModal.propTypes = {
  voucherNumber: PropTypes.string,
  voucherPin: PropTypes.string,
  addGiftCard: PropTypes.func
};
