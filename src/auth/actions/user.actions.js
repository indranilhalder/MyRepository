import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import {
  showModal,
  OTP_VERIFICATION,
  hideModal
} from "../../general/modal.actions.js";
export const LOGIN_USER_REQUEST = "LOGIN_USER_REQUEST";
export const LOGIN_USER_SUCCESS = "LOGIN_USER_SUCCESS";
export const LOGIN_USER_FAILURE = "LOGIN_USER_FAILURE";
export const SIGN_UP_USER_REQUEST = "SIGN_UP_USER_REQUEST";
export const SIGN_UP_USER_SUCCESS = "SIGN_UP_USER_SUCCESS";
export const SIGN_UP_USER_FAILURE = "SIGN_UP_USER_FAILURE";
export const OTP_VERIFICATION_REQUEST = "OTP_VERIFICATION_REQUEST";
export const OTP_VERIFICATION_SUCCESS = "OTP_VERIFICATION_SUCCESS";
export const OTP_VERIFICATION_FAILURE = "OTP_VERIFICATION_FAILURE";
export const FORGOT_PASSWORD_REQUEST = "FORGOT_PASSWORD_REQUEST";
export const FORGOT_PASSWORD_SUCCESS = "FORGOT_PASSWORD_SUCCESS";
export const FORGOT_PASSWORD_FAILURE = "FORGOT_PASSWORD_FAILURE";
export const FORGOT_PASSWORD_OTP_VERIFICATION_REQUEST =
  "FORGOT_PASSWORD_OTP_VERIFICATION_REQUEST";
export const FORGOT_PASSWORD_OTP_VERIFICATION_SUCCESS =
  "FORGOT_PASSWORD_OTP_VERIFICATION_SUCCESS";
export const FORGOT_PASSWORD_OTP_VERIFICATION_FAILURE =
  "FORGOT_PASSWORD_OTP_VERIFICATION_FAILURE";
export const RESET_PASSWORD_REQUEST = "RESET_PASSWORD_REQUEST";
export const RESET_PASSWORD_SUCCESS = "RESET_PASSWORD_SUCCESS";
export const RESET_PASSWORD_FAILURE = "RESET_PASSWORD_FAILURE";

export const LOGIN = "login";
export const SIGN_UP = "onregistration";
export const FORGOT_PASSWORD = "forgotpassword";
export const FORGOT_PASSWORD_OTP_VERIFICATION = "forgotpasswordotpverification";
export const RESET_PASSWORD = "resetpassword";
export const OTP_VERIFICATION_PATH = "otpverification";

export function loginUserRequest() {
  return {
    type: LOGIN_USER_REQUEST,
    status: REQUESTING
  };
}

export function loginUserSuccess(user) {
  return {
    type: LOGIN_USER_SUCCESS,
    status: SUCCESS,
    user
  };
}

export function loginUserFailure(error) {
  return {
    type: LOGIN_USER_FAILURE,
    status: ERROR,
    error
  };
}

export function loginUser(userLoginDetails) {
  return async (dispatch, getState, { api }) => {
    dispatch(loginUserRequest());
    try {
      const result = await api.post(LOGIN, userLoginDetails);
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      localStorage.setItem("authorizationKey", resultJson.access_token);

      dispatch(loginUserSuccess(resultJson));
    } catch (e) {
      dispatch(loginUserFailure(e.message));
    }
  };
}
export function signUpUserRequest() {
  return {
    type: SIGN_UP_USER_REQUEST,
    status: REQUESTING
  };
}
export function signUpUserSuccess() {
  return {
    type: SIGN_UP_USER_SUCCESS,
    status: SUCCESS
  };
}

export function signUpUserFailure(error) {
  return {
    type: SIGN_UP_USER_FAILURE,
    status: ERROR,
    error
  };
}

export function signUpUser(userObj) {
  return async (dispatch, getState, { api }) => {
    dispatch(signUpUserRequest());
    try {
      const result = await api.post(SIGN_UP, userObj);
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(showModal(OTP_VERIFICATION));
      dispatch(signUpUserSuccess());
    } catch (e) {
      dispatch(signUpUserFailure(e.message));
    }
  };
}

export function otpVerificationRequest() {
  return {
    type: OTP_VERIFICATION_REQUEST,
    status: REQUESTING
  };
}
export function otpVerificationSuccess(user) {
  return {
    type: OTP_VERIFICATION_SUCCESS,
    status: SUCCESS,
    user
  };
}

export function otpVerificationFailure(error) {
  return {
    type: OTP_VERIFICATION_FAILURE,
    status: ERROR,
    error
  };
}
export function otpVerification(userDetails) {
  return async (dispatch, getState, { api }) => {
    dispatch(otpVerificationRequest());
    try {
      const result = await api.post(OTP_VERIFICATION_PATH, userDetails);
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(hideModal());
      dispatch(otpVerificationSuccess(resultJson));
    } catch (e) {
      dispatch(otpVerificationFailure(e.message));
    }
  };
}

export function forgotPasswordRequest() {
  return {
    type: FORGOT_PASSWORD_REQUEST,
    status: REQUESTING
  };
}
export function forgotPasswordSuccess(message) {
  return {
    type: FORGOT_PASSWORD_SUCCESS,
    status: SUCCESS,
    message
  };
}

export function forgotPasswordFailure(error) {
  return {
    type: FORGOT_PASSWORD_FAILURE,
    status: ERROR,
    error
  };
}
export function forgotPassword(userDetails) {
  return async (dispatch, getState, { api }) => {
    dispatch(forgotPasswordRequest());
    try {
      const result = await api.post(FORGOT_PASSWORD, userDetails);
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(forgotPasswordSuccess(resultJson.message));
    } catch (e) {
      dispatch(forgotPasswordFailure(e.message));
    }
  };
}

export function forgotPasswordOtpVerificationRequest() {
  return {
    type: FORGOT_PASSWORD_OTP_VERIFICATION_REQUEST,
    status: REQUESTING
  };
}
export function forgotPasswordOtpVerificationSuccess(message) {
  return {
    type: FORGOT_PASSWORD_OTP_VERIFICATION_SUCCESS,
    status: SUCCESS,
    message
  };
}

export function forgotPasswordOtpVerificationFailure(error) {
  return {
    type: FORGOT_PASSWORD_OTP_VERIFICATION_FAILURE,
    status: ERROR,
    error
  };
}

export function forgotPasswordOtpVerification(userDetails) {
  return async (dispatch, getState, { api }) => {
    dispatch(forgotPasswordOtpVerificationRequest());
    try {
      const result = await api.post(
        FORGOT_PASSWORD_OTP_VERIFICATION,
        userDetails
      );
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(forgotPasswordOtpVerificationSuccess(resultJson.message));
    } catch (e) {
      dispatch(forgotPasswordOtpVerificationFailure(e.message));
    }
  };
}
export function resetPasswordRequest() {
  return {
    type: RESET_PASSWORD_REQUEST,
    status: REQUESTING
  };
}
export function resetPasswordSuccess(message) {
  return {
    type: RESET_PASSWORD_SUCCESS,
    status: SUCCESS,
    message
  };
}

export function resetPasswordFailure(error) {
  return {
    type: RESET_PASSWORD_FAILURE,
    status: ERROR,
    error
  };
}
export function resetPassword(userDetails) {
  return async (dispatch, getState, { api }) => {
    dispatch(resetPasswordRequest());
    try {
      const result = await api.post(RESET_PASSWORD, userDetails);
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(resetPasswordSuccess(resultJson.message));
    } catch (e) {
      dispatch(resetPasswordFailure(e.message));
    }
  };
}
