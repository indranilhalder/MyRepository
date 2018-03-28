import React from "react";
import KycApplicationForm from "./KycApplicationForm";
import BottomSlideModal from "../../general/components/BottomSlideModal";
import PropTypes from "prop-types";
export default class KycApplicationFormWithBottomSlideModal extends React.Component {
  generateOtp(value) {
    if (this.props.generateOtp) {
      this.props.generateOtp(value);
    }
  }
  render() {
    return (
      <BottomSlideModal>
        <KycApplicationForm
          generateOtp={value => this.generateOtp(value)}
          mobileNumber={this.props.mobileNumber}
        />
      </BottomSlideModal>
    );
  }
}
KycApplicationFormWithBottomSlideModal.propTypes = {
  mobileNumber: PropTypes.string,
  generateOtp: PropTypes.func
};
