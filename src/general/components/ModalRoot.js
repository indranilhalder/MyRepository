import React from "react";
import ReactDOM from "react-dom";
import ModalPanel from "./ModalPanel";
import Loadable from "react-loadable";
import SecondaryLoader from "../../general/components/SecondaryLoader";

import * as Cookie from "../../lib/Cookie.js";
import { LOGGED_IN_USER_DETAILS } from "../../lib/constants.js";
const modalRoot = document.getElementById("modal-root");
const GenerateOtp = "GenerateOtpForEgv";
const RestorePasswords = "RestorePassword";

const Loader = () => {
  return (
    <div>
      <SecondaryLoader />
    </div>
  );
};
const NewPassword = Loadable({
  loader: () => import("../../auth/components/NewPassword"),
  loading() {
    return <Loader />;
  }
});

const RestorePassword = Loadable({
  loader: () => import("../../auth/components/RestorePassword"),
  loading() {
    return <Loader />;
  }
});

const OtpVerification = Loadable({
  loader: () => import("../../auth/components/OtpVerification"),
  loading() {
    return <Loader />;
  }
});

const ConnectDetailsWithModal = Loadable({
  loader: () => import("../../home/components/ConnectDetailsWithModal"),
  loading() {
    return <Loader />;
  }
});

const Sort = Loadable({
  loader: () => import("../../plp/components/SortModal"),
  loading() {
    return <Loader />;
  }
});

const SizeGuideModal = Loadable({
  loader: () => import("../../pdp/components/SizeGuideModal"),
  loading() {
    return <Loader />;
  }
});

const AddressModalContainer = Loadable({
  loader: () => import("../../plp/containers/AddressModalContainer"),
  loading() {
    return <Loader />;
  }
});

const EmiModal = Loadable({
  loader: () => import("../../pdp/containers/EmiListContainer"),
  loading() {
    return <Loader />;
  }
});

const OfferModal = Loadable({
  loader: () => import("../../pdp/components/OfferModal"),
  loading() {
    return <Loader />;
  }
});

const ProductCouponDetails = Loadable({
  loader: () => import("../../pdp/components/ProductCouponDetails.js"),
  loading() {
    return <Loader />;
  }
});

const SizeSelectModal = Loadable({
  loader: () => import("../../pdp/components/SizeSelectModal.js"),
  loading() {
    return <Loader />;
  }
});

const BankOffersDetails = Loadable({
  loader: () => import("../../cart/components/BankOffersDetails.js"),
  loading() {
    return <Loader />;
  }
});

const GiftCardModal = Loadable({
  loader: () => import("../../cart/components/GiftCardModal"),
  loading() {
    return <Loader />;
  }
});

const UpdateRefundDetailsPopup = Loadable({
  loader: () => import("../../account/components/UpdateRefundDetailsPopup.js"),
  loading() {
    return <Loader />;
  }
});

const KycApplicationFormWithBottomSlideModal = Loadable({
  loader: () =>
    import("../../account/components/KycApplicationFormWithBottomSlideModal"),
  loading() {
    return <Loader />;
  }
});

const KycDetailPopUpWithBottomSlideModal = Loadable({
  loader: () =>
    import("../../account/components/KycDetailPopUpWithBottomSlideModal"),
  loading() {
    return <Loader />;
  }
});

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
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    if (userDetails) {
      this.props.applyUserCouponForLoggedInUsers(couponCode);
    } else {
      this.props.applyUserCouponForAnonymous(couponCode);
    }

    this.props.hideModal();
  };
  releaseUserCoupon = (oldCouponCode, newCouponCode) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    if (userDetails) {
      this.props.releaseUserCoupon(oldCouponCode, newCouponCode);
    } else {
      this.props.releaseCouponForAnonymous(oldCouponCode, newCouponCode);
    }

    this.props.hideModal();
  };
  getUserAddress = () => {
    this.props.getUserAddress();
  };

  submitOtpForUpdateProfile(otpDetails) {
    this.props.updateProfile(this.props.ownProps, otpDetails);
    this.props.hideModal();
  }

  generateOtpForCliqCash = kycDetails => {
    this.setState(kycDetails);
    if (this.props.getOtpToActivateWallet) {
      this.props.getOtpToActivateWallet(kycDetails, true);
    }
  };
  verifyOtpForCliqCash = otpDetails => {
    let customerDetailsWithOtp = {};
    customerDetailsWithOtp.firstName = this.state.firstName;
    customerDetailsWithOtp.mobileNumber = this.state.mobileNumber;
    customerDetailsWithOtp.lastName = this.state.lastName;
    customerDetailsWithOtp.otp = otpDetails;
    if (this.props.verifyWallet) {
      this.props.verifyWallet(customerDetailsWithOtp, true);
    }
  };

  resendOtp = () => {
    if (this.props.getOtpToActivateWallet) {
      let kycDetails = {};
      kycDetails.firstName = this.state.firstName;
      kycDetails.lastName = this.state.lastName;
      kycDetails.mobileNumber = this.state.mobileNumber;
      this.props.getOtpToActivateWallet(kycDetails);
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
    customerDetailsWithOtp.otp = val;
    this.props.verifyWallet(customerDetailsWithOtp);
  }
  wrongNumber() {
    this.props.hideModal();
    this.props.showModal(GenerateOtp);
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
  resendOtpForUpdateProfile = () => {
    this.props.updateProfile(this.props.ownProps);
  };

  addGiftCard = val => {
    if (this.props.redeemCliqVoucher) {
      this.props.redeemCliqVoucher(val, true);
    }
  };
  onClickWrongNumber() {
    this.props.showModal(RestorePasswords);
  }
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
          handleCancel={() => this.handleClose()}
          onContinue={userDetails => this.resetPassword(userDetails)}
        />
      ),
      SignUpOtpVerification: (
        <OtpVerification
          userObj={this.props.ownProps}
          closeModal={() => this.handleClose()}
          resendOtp={userObj => this.resendOTP(userObj)}
          submitOtp={otpDetails => this.submitOtp(otpDetails)}
          onClickWrongNumber={() => this.handleClose()}
        />
      ),
      UpdateProfileOtpVerification: (
        <OtpVerification
          userObj={this.props.ownProps}
          closeModal={() => this.handleClose()}
          submitOtp={otpDetails => this.submitOtpForUpdateProfile(otpDetails)}
          resendOtp={userName =>
            this.resendOtpForUpdateProfile(this.props.ownProps)
          }
        />
      ),
      ForgotPasswordOtpVerification: (
        <OtpVerification
          closeModal={() => this.handleClose()}
          submitOtp={otpDetails => this.submitOtpForgotPassword(otpDetails)}
          userObj={this.props.ownProps}
          resendOtp={userName => this.handleRestoreClick(userName)}
          onClickWrongNumber={() => this.onClickWrongNumber()}
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
          releaseUserCoupon={(oldCouponCode, newCouponCode) =>
            this.releaseUserCoupon(oldCouponCode, newCouponCode)
          }
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
          loadingForGetOtpToActivateWallet={
            this.props.loadingForGetOtpToActivateWallet
          }
        />
      ),
      verifyOtpForCliqCash: (
        <KycDetailPopUpWithBottomSlideModal
          closeModal={() => this.handleClose()}
          mobileNumber={this.state.mobileNumber}
          submitOtp={otpDetails =>
            this.verifyOtpForCliqCash(otpDetails, this.props.ownProps)
          }
          {...this.props.ownProps}
          resendOtp={val => this.resendOtp(val, this.props.ownProps)}
          loadingForVerifyWallet={this.props.loadingForVerifyWallet}
          wrongNumber={() => this.wrongNumber()}
        />
      ),
      SizeGuide: <SizeGuideModal closeModal={() => this.handleClose()} />,
      GenerateOtpForEgv: (
        <KycApplicationFormWithBottomSlideModal
          closeModal={() => this.handleClose()}
          generateOtp={val => this.generateOtp(val, this.props.ownProps)}
          {...this.props.ownProps}
          loadingForGetOtpToActivateWallet={
            this.props.loadingForGetOtpToActivateWallet
          }
        />
      ),
      verifyOtp: (
        <KycDetailPopUpWithBottomSlideModal
          closeModal={() => this.handleClose()}
          mobileNumber={this.state.mobileNumber}
          submitOtp={val => this.verifyOtp(val, this.props.ownProps)}
          resendOtp={val => this.resendOtp(val, this.props.ownProps)}
          wrongNumber={() => this.wrongNumber()}
          {...this.props.ownProps}
          loadingForVerifyWallet={this.props.loadingForVerifyWallet}
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
      ),
      OfferModal: (
        <OfferModal
          closeModal={() => this.handleClose()}
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
