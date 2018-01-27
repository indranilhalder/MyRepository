export const SHOW_MODAL = "SHOW_MODAL";
export const HIDE_MODAL = "HIDE_MODAL";
export const RESTORE_PASSWORD = "RestorePassword";
export const SIGN_UP_OTP_VERIFICATION = "SignUpOtpVerification";
export const FORGOT_PASSWORD_OTP_VERIFICATION = "ForgotPasswordOtpVerification";
export const CONNECT_DETAILS = "ConnectDetails";
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
