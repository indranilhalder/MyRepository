import React from "react";
import ReactDOM from "react-dom";
import ModalPanel from "./ModalPanel";
import NewPassword from "../../auth/components/NewPassword";
import RestorePassword from "../../auth/components/RestorePassword";
import OtpVerification from "../../auth/components/OtpVerification";
import ConnectDetailsWithModal from "../../home/components/ConnectDetailsWithModal";
import Sort from "../../plp/components/SortModal";
import AddressModalContainer from "../../plp/containers/AddressModalContainer";
import SizeGuideModal from "../../pdp/components/SizeGuideModal";
import EmiModal from "../../pdp/containers/EmiListContainer";
import ProductCouponDetails from "../../pdp/components/ProductCouponDetails.js";
import BankOffersDetails from "../../cart/components/BankOffersDetails.js";

const modalRoot = document.getElementById("modal-root");
export default class ModalRoot extends React.Component {
  constructor(props) {
    super(props);
    this.el = document.createElement("div");
    this.state = {
      loggedIn: false
    };
  }

  componentDidMount() {
    modalRoot.appendChild(this.el);
  }

  componentWillUnmount() {
    modalRoot.removeChild(this.el);
  }

  handleClose() {
    if (this.props.hideModal) {
      this.props.hideModal();
    }
  }

  submitOtp(otpDetails) {
    this.props.otpVerification(otpDetails, this.props.ownProps);
    this.props.hideModal();
  }
  resendOTP(userObj) {
    this.props.resendOTP(userObj);
  }
  resetPassword(userDetails) {
    this.props.resetPassword(userDetails);
    this.props.hideModal();
  }

  handleRestoreClick(userDetails) {
    this.props.forgotPassword(userDetails);
    this.props.hideModal();
  }

  submitOtpForgotPassword(otpDetails) {
    this.props.forgotPasswordOtpVerification(otpDetails, this.props.ownProps);
    this.props.hideModal();
  }

  applyBankOffer = couponCode => {
    this.props.applyBankOffer(couponCode);
  };
  releaseBankOffer = couponCode => {
    this.props.releaseBankOffer(couponCode);
  };

  applyUserCoupon = couponCode => {
    this.props.applyUserCoupon(couponCode);
  };

  releaseUserCoupon = couponCode => {
    this.props.releaseUserCoupon(couponCode);
  };
  getUserAddress = () => {
    this.props.getUserAddress();
  };

  render() {
    const MODAL_COMPONENTS = {
      RestorePassword: (
        <RestorePassword
          handleCancel={() => this.handleClose()}
          handleRestoreClick={userId => this.handleRestoreClick(userId)}
        />
      ),
      NewPassword: (
        <NewPassword
          {...this.props.ownProps}
          handleCancel={() => this.handleClose}
          onContinue={userDetails => this.resetPassword(userDetails)}
        />
      ),
      SignUpOtpVerification: (
        <OtpVerification
          userObj={this.props.ownProps}
          closeModal={() => this.handleClose()}
          resendOtp={userObj => this.resendOTP(userObj)}
          submitOtp={otpDetails => this.submitOtp(otpDetails)}
        />
      ),
      ForgotPasswordOtpVerification: (
        <OtpVerification
          closeModal={() => this.handleClose()}
          submitOtp={otpDetails => this.submitOtpForgotPassword(otpDetails)}
          userObj={this.props.ownProps}
          resendOtp={userName => this.handleRestoreClick(userName)}
        />
      ),
      Sort: <Sort />,
      Address: (
        <AddressModalContainer
          closeModal={() => this.handleClose()}
          {...this.props.ownProps}
        />
      ),
      ConnectDetails: (
        <ConnectDetailsWithModal
          closeModal={() => this.handleClose()}
          {...this.props.ownProps}
        />
      ),
      Coupons: (
        <ProductCouponDetails
          closeModal={() => this.handleClose()}
          applyUserCoupon={couponCode => this.applyUserCoupon(couponCode)}
          releaseUserCoupon={couponCode => this.releaseUserCoupon(couponCode)}
          {...this.props.ownProps}
        />
      ),
      BankOffers: (
        <BankOffersDetails
          closeModal={() => this.handleClose()}
          applyBankOffer={couponCode => this.applyBankOffer(couponCode)}
          releaseBankOffer={couponCode => this.releaseBankOffer(couponCode)}
          {...this.props.ownProps}
        />
      ),
      SizeGuide: <SizeGuideModal closeModal={() => this.handleClose()} />,
      EmiModal: <EmiModal />,
      OtpLoginModal: (
        <OtpVerification
          submitOtp={val => this.props.loginUser(val)}
          {...this.props.ownProps}
        />
      )
    };

    let SelectedModal = MODAL_COMPONENTS[this.props.modalType];
    //let SelectedModal = MODAL_COMPONENTS["NewPassword"];
    const Modal = this.props.modalStatus ? (
      <ModalPanel
        closeModal={() => {
          this.handleClose();
        }}
      >
        {SelectedModal}
      </ModalPanel>
    ) : null;

    return ReactDOM.createPortal(Modal, this.el);
  }
}
