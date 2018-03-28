import React from "react";
import KycDetailsPopup from "../../auth/components/KycDetailsPopup";
import BottomSlideModal from "../../general/components/BottomSlideModal";
import PropTypes from "prop-types";
export default class KycDetailPopUpWithBottomSlideModal extends React.Component {
  resendOtp() {
    if (this.props.resendOtp) {
      this.props.resendOtp();
    }
  }
  submitOtp(value) {
    if (this.props.submitOtp) {
      this.props.submitOtp(value);
    }
  }
  render() {
    return (
      <BottomSlideModal>
        <KycDetailsPopup
          mobileNumber={this.props.mobileNumber}
          submitOtp={value => this.submitOtp(value)}
          resendOtp={() => this.resendOtp()}
        />
      </BottomSlideModal>
    );
  }
}
KycDetailPopUpWithBottomSlideModal.propTypes = {
  mobileNumber: PropTypes.string,
  submitOtp: PropTypes.func,
  resendOtp: PropTypes.func
};
