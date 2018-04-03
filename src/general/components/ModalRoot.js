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
import SizeSelectModal from "../../pdp/components/SizeSelectModal.js";
import BankOffersDetails from "../../cart/components/BankOffersDetails.js";
import KycDetailsPopup from "../../auth/components/KycDetailsPopup.js";
import KycApplicationForm from "../../account/components/KycApplicationForm.js";
import GiftCardModal from "../../cart/components/GiftCardModal";
import UpdateRefundDetailsPopup from "../../account/components/UpdateRefundDetailsPopup.js";
import KycApplicationFormWithBottomSlideModal from "../../account/components/KycApplicationFormWithBottomSlideModal";
import KycDetailPopUpWithBottomSlideModal from "../../account/components/KycDetailPopUpWithBottomSlideModal";
const modalRoot = document.getElementById("modal-root");
export default class ModalRoot extends React.Component {
  constructor(props) {
    super(props);
    this.el = document.createElement("div");
    this.state = {
      loggedIn: false,
      customerDetails: null
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
  onUpdate(val) {
    if (this.props.submitSelfCourierReturnInfo) {
      const returnDetails = {};
      returnDetails.awbNumber = val.awbNumber;
      returnDetails.lpname = val.logisticsPartner;
      returnDetails.amount = val.courierCharge;
      returnDetails.orderId = this.props.ownProps.orderId;
      returnDetails.transactionId = this.props.ownProps.transactionId;
      returnDetails.file = val.file;
      this.props.submitSelfCourierReturnInfo(returnDetails);
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

  submitOtpForUpdateProfile(otpDetails) {
    this.props.updateProfile(this.props.ownProps, otpDetails);
    this.props.hideModal();
  }


  generateOtpForCliqCash = kycDetails => {
    if (this.props.getOtpToActivateWallet) {
      this.props.getOtpToActivateWallet(kycDetails, true);
    }
  };
  verifyOtpForCliqCash = otpDetails => {
    let kycDetails = {};
    kycDetails.firstName = this.props.ownProps.firstName;
    kycDetails.lastName = this.props.ownProps.lastName;
    kycDetails.mobileNumber = this.props.ownProps.mobileNumber;
    kycDetails.otp = otpDetails;
    if (this.props.verifyWallet) {
      this.props.verifyWallet(kycDetails, true);
    }
  };

  resendOtp = () => {
    if (this.props.getOtpToActivateWallet) {
      let kycDetails = {};
      kycDetails.firstName = this.props.ownProps.firstName;
      kycDetails.lastName = this.props.ownProps.lastName;
      kycDetails.mobileNumber = this.props.ownProps.mobileNumber;
      this.props.getOtpToActivateWallet(kycDetails, true);
    }
  };

  generateOtpForEgv = () => {
    this.props.generateOtpForEgv();
  };
  verifyOtp(val) {
    let customerDetailsWithOtp = {};
    customerDetailsWithOtp.firstName = this.state.firstName;
    customerDetailsWithOtp.mobileNumber = this.state.mobileNumber;
    customerDetailsWithOtp.lastName = this.state.lastName;
    customerDetailsWithOtp.otp = val.otp;
    this.props.verifyWallet(customerDetailsWithOtp);
  }
  generateOtp(val) {
    let customerDetails = {};
    customerDetails.firstName = val.firstName;
    customerDetails.mobileNumber = val.mobileNumber;
    customerDetails.lastName = val.lastName;
    this.setState(customerDetails);
    this.props.getOtpToActivateWallet(customerDetails);
  }
  resendOtp() {
    let customerDetails = {};
    customerDetails.firstName = this.state.firstName;
    customerDetails.mobileNumber = this.state.mobileNumber;
    customerDetails.lastName = this.state.lastName;
    this.props.getOtpToActivateWallet(customerDetails);
  }

  addGiftCard = val => {
    if (this.props.redeemCliqVoucher) {
      this.props.redeemCliqVoucher(val, true);
    }
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
      UpdateProfileOtpVerification: (
        <OtpVerification
          userObj={this.props.ownProps}
          closeModal={() => this.handleClose()}
          submitOtp={otpDetails => this.submitOtpForUpdateProfile(otpDetails)}
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
      UpdateRefundDetailsPopup: (
        <UpdateRefundDetailsPopup
          closeModal={() => this.handleClose()}
          onUpdate={val => this.onUpdate(val)}
          {...this.props.ownProps}
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
      generateOtpForCliqCash: (
        <KycApplicationFormWithBottomSlideModal
          closeModal={() => this.handleClose()}
          generateOtp={KycDetails => this.generateOtpForCliqCash(KycDetails)}
          {...this.props.ownProps}
        />
      ),
      verifyOtpForCliqCash: (
        <KycDetailPopUpWithBottomSlideModal
          closeModal={() => this.handleClose()}
          submitOtp={otpDetails => this.verifyOtpForCliqCash(otpDetails)}
          {...this.props.ownProps}
          resendOtp={() => this.resendOtp()}
        />
      ),
      SizeGuide: <SizeGuideModal closeModal={() => this.handleClose()} />,
      GenerateOtpForEgv: (
        <KycApplicationFormWithBottomSlideModal
          closeModal={() => this.handleClose()}
          generateOtp={val => this.generateOtp(val, this.props.ownProps)}
          {...this.props.ownProps}
        />
      ),
      verifyOtp: (
        <KycDetailPopUpWithBottomSlideModal
          closeModal={() => this.handleClose()}
          mobileNumber={this.state.mobileNumber}
          submitOtp={val => this.verifyOtp(val, this.props.ownProps)}
          resendOtp={val => this.resendOtp(val, this.props.ownProps)}
          {...this.props.ownProps}
        />
      ),
      EmiModal: <EmiModal />,
      OtpLoginModal: (
        <OtpVerification
          submitOtp={val => this.props.loginUser(val)}
          {...this.props.ownProps}
        />
      ),
      SizeSelector: (
        <SizeSelectModal
          {...this.props.ownProps}
          history={this.props.history}
          closeModal={() => this.handleClose()}
        />
      ),
      GiftCardModal: (
        <GiftCardModal
          closeModal={() => this.handleClose()}
          addGiftCard={val => this.addGiftCard(val)}
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
