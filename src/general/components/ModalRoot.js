import React from "react";
import ReactDOM from "react-dom";
import ModalPanel from "./ModalPanel";
import Loadable from "react-loadable";
import SecondaryLoader from "../../general/components/SecondaryLoader";
import PriceBreakupModal from "../../pdp/components/PriceBreakupModal";
import OrderModal from "../../account/components/OrderModal";

import * as Cookie from "../../lib/Cookie.js";
import {
  LOGGED_IN_USER_DETAILS,
  SUCCESS,
  BANK_COUPON_COOKIE,
  COUPON_COOKIE,
  NO_COST_EMI_COUPON,
  CART_DETAILS_FOR_LOGGED_IN_USER
} from "../../lib/constants.js";
import ItemLevelPopup from "../../cart/components/ItemLevelPopup.js";
import TermsAndConditionsModal from "../../cart/components/TermsAndConditionsModal.js";
import { LOGIN_PATH } from "../../lib/constants";
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
const StoryWidgetContainer = Loadable({
  loader: () => import("../../home/containers/StoryWidgetContainer"),
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

const InvalidCouponPopupContainer = Loadable({
  loader: () => import("../../cart/containers/InvalidCouponPopUpContainer"),
  loading() {
    return <Loader />;
  }
});

const CancelOrderPopUp = Loadable({
  loader: () => import("../../account/components/CancelOrderPopUp.js"),
  loading() {
    return <Loader />;
  }
});
const CliqCashAndNoCostEmiPopup = Loadable({
  loader: () => import("../../cart/components/CliqCashAndNoCostEmiPopup.js"),
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
  componentWillMount() {
    if (this.props.history) {
      this.unlisten = this.props.history.listen((location, action) => {
        this.handleClose();
      });
    }
  }

  componentWillUnmount() {
    modalRoot.removeChild(this.el);
    this.unlisten();
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
  }
  resendOTP(userObj) {
    this.props.resendOTP(userObj);
  }
  resetPassword(userDetails) {
    this.props.resetPassword(userDetails);
  }

  handleRestoreClick(userDetails) {
    this.props.forgotPassword(userDetails);
    this.props.hideModal();
  }
  submitOtpForgotPassword(otpDetails) {
    this.props.forgotPasswordOtpVerification(otpDetails, this.props.ownProps);
  }
  applyBankOffer = couponCode => {
    return this.props.applyBankOffer(couponCode);
  };
  releaseBankOffer = (previousCouponCode, newCouponCode) => {
    return this.props.releaseBankOffer(previousCouponCode, newCouponCode);
  };

  resendOtpForLogin = userDetails => {
    if (this.props.loginUser) {
      this.props.loginUser(userDetails);
    }
  };
  releasePreviousAndApplyNewBankOffer = (
    previousCouponCode,
    newSelectedCouponCode
  ) => {
    this.props.releasePreviousAndApplyNewBankOffer(
      previousCouponCode,
      newSelectedCouponCode
    );
  };
  applyUserCoupon = couponCode => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    this.props.hideModal();
    if (userDetails) {
      return this.props.applyUserCouponForLoggedInUsers(couponCode);
    } else {
      return this.props.applyUserCouponForAnonymous(couponCode);
    }
  };
  releaseUserCoupon = (oldCouponCode, newCouponCode) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    this.props.hideModal();
    if (userDetails) {
      return this.props.releaseUserCoupon(oldCouponCode, newCouponCode);
    } else {
      return this.props.releaseCouponForAnonymous(oldCouponCode, newCouponCode);
    }
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

  navigateToLogin = url => {
    this.props.setUrlToRedirectToAfterAuth(url);
    this.handleClose();
    this.props.history.push(LOGIN_PATH);
  };

  cancelOrderProduct = (cancelProductDetails, productDetails) => {
    this.props.cancelProduct(cancelProductDetails, productDetails);
  };
  continueWithoutBankCoupon = async () => {
    const bankCouponCode = localStorage.getItem(BANK_COUPON_COOKIE);
    const userCouponCode = localStorage.getItem(COUPON_COOKIE);
    const noCostEmiCoupon = localStorage.getItem(NO_COST_EMI_COUPON);
    let cartDetailsLoggedInUser = Cookie.getCookie(
      CART_DETAILS_FOR_LOGGED_IN_USER
    );

    if (this.props.ownProps && this.props.ownProps.couponCode) {
      if (this.props.ownProps.couponCode === bankCouponCode) {
        const releaseCouponCode = await this.props.releaseBankOffer(
          bankCouponCode
        );
        if (releaseCouponCode.status === SUCCESS) {
          localStorage.removeItem(BANK_COUPON_COOKIE);
          this.props.ownProps.redoCreateJusPayApi();
          this.props.hideModal();
        }
      } else if (this.props.ownProps.couponCode === userCouponCode) {
        const releaseCouponCode = await this.props.releaseUserCoupon(
          userCouponCode
        );
        if (releaseCouponCode.status === SUCCESS) {
          localStorage.removeItem(COUPON_COOKIE);
          this.props.ownProps.redoCreateJusPayApi();
          this.props.hideModal();
        }
      } else if (this.props.ownProps.couponCode === noCostEmiCoupon) {
        let carGuId = JSON.parse(cartDetailsLoggedInUser).guid;
        let cartId = JSON.parse(cartDetailsLoggedInUser).code;

        const releaseCouponCode = await this.props.removeNoCostEmi(
          noCostEmiCoupon,
          carGuId,
          cartId
        );
        if (releaseCouponCode.status === SUCCESS) {
          this.props.ownProps.redoCreateJusPayApi();
          this.props.hideModal();
        }
      }
    } else {
      let carGuId = JSON.parse(cartDetailsLoggedInUser).guid;
      let cartId = JSON.parse(cartDetailsLoggedInUser).code;

      Promise.all([
        bankCouponCode && this.props.releaseBankOffer(bankCouponCode),
        userCouponCode && this.props.releaseUserCoupon(userCouponCode),
        noCostEmiCoupon &&
          this.props.removeNoCostEmi(noCostEmiCoupon, carGuId, cartId)
      ]).then(res => {
        localStorage.removeItem(BANK_COUPON_COOKIE);
        localStorage.removeItem(COUPON_COOKIE);
        this.props.ownProps.redoCreateJusPayApi();
        this.props.hideModal();
      });
    }
  };
  render() {
    const couponCode = localStorage.getItem(BANK_COUPON_COOKIE);
    const MODAL_COMPONENTS = {
      RestorePassword: (
        <RestorePassword
          handleCancel={() => this.handleClose()}
          handleRestoreClick={userId => this.handleRestoreClick(userId)}
        />
      ),
      NewPassword: (
        <NewPassword
          userObj={this.props.ownProps}
          displayToast={message => this.props.displayToast(message)}
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
          onClickWrongNumber={() => this.handleClose()}
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
          releaseUserCoupon={(couponCode, newCouponCode) =>
            this.releaseUserCoupon(couponCode, newCouponCode)
          }
          {...this.props.ownProps}
          navigateToLogin={url =>
            this.navigateToLogin(url, { ...this.props.ownProps })
          }
        />
      ),
      BankOffers: (
        <BankOffersDetails
          closeModal={() => this.handleClose()}
          applyBankOffer={couponCode => this.applyBankOffer(couponCode)}
          releaseBankOffer={(couponCode, newCouponCode) =>
            this.releaseBankOffer(couponCode, newCouponCode)
          }
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
      StoryModal: (
        <StoryWidgetContainer
          closeModal={() => this.handleClose()}
          {...this.props.ownProps}
        />
      ),
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
          userObj={this.props.ownProps}
          closeModal={() => this.handleClose()}
          resendOtp={userDetails => this.resendOtpForLogin(userDetails)}
          onClickWrongNumber={() => this.handleClose()}
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
      ),
      NoCostEmiItemBreakUp: (
        <ItemLevelPopup
          emiItemDetails={this.props.ownProps}
          closeModal={() => this.handleClose()}
        />
      ),
      EmiTermsAndConditions: (
        <TermsAndConditionsModal
          emiTermsAndConditions={this.props.ownProps}
          closeModal={() => this.handleClose()}
        />
      ),
      INVALID_BANK_COUPON_POPUP: (
        <InvalidCouponPopupContainer
          {...this.props.ownProps}
          closeModal={() => this.handleClose()}
          changePaymentMethod={() => this.handleClose()}
          // continueWithoutCoupon={() => this.continueWithoutBankCoupon()}
        />
      ),
      PriceBreakup: (
        <PriceBreakupModal
          data={this.props.ownProps}
          closeModal={() => this.handleClose()}
        />
      ),
      OrderModal: (
        <OrderModal
          data={this.props.ownProps}
          closeModal={() => this.handleClose()}
        />
      ),
      CancelOrderPopUp: (
        <CancelOrderPopUp
          data={this.props.ownProps}
          loadingForCancelProduct={this.props.loadingForCancelProduct}
          cancelModal={() => this.handleClose()}
          cancelProduct={(cancelProductDetails, productDetails) =>
            this.cancelOrderProduct(cancelProductDetails, productDetails)
          }
        />
      ),
      CliqCashAndNoCostEmiPopup: (
        <CliqCashAndNoCostEmiPopup
          {...this.props.ownProps}
          handleClose={() => this.handleClose()}
          removeNoCostEmi={couponCode => this.props.removeNoCostEmi(couponCode)}
          continueWithNoCostEmi={() => this.handleClose()}
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
