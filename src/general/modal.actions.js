export const SHOW_MODAL = "SHOW_MODAL";
export const HIDE_MODAL = "HIDE_MODAL";
export const RESTORE_PASSWORD = "RestorePassword";
export const SIGN_UP_OTP_VERIFICATION = "SignUpOtpVerification";
export const FORGOT_PASSWORD_OTP_VERIFICATION = "ForgotPasswordOtpVerification";
export const CONNECT_DETAILS = "ConnectDetails";
export const PRODUCT_COUPONS = "Coupons";
export const SIZE_GUIDE = "SizeGuide";
export const SORT = "Sort";
export const ADDRESS = "Address";
export const EMI_MODAL = "EmiModal";
export const BANK_OFFERS = "BankOffers";
export const OTP_LOGIN_MODAL = "OtpLoginModal";
export const GENERATE_OTP_FOR_CLIQ_CASH="generateOtpForCliqCash"
export const VERIFY_OTP_FOR_CLIQ_CASH="verifyOtpForCliqCash"
export const NEW_PASSWORD = "NewPassword";

export function showModal(type, ownProps) {
  return {
    type: SHOW_MODAL,
    modalType: type,
    ownProps
  };
}

export function hideModal() {
  return {
    type: HIDE_MODAL,
    modalType: null
  };
}
