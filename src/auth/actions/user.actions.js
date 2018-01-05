import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";

export const LOGIN_USER_REQUEST = "LOGIN_USER_REQUEST";
export const LOGIN_USER_SUCCESS = "LOGIN_USER_SUCCESS";
export const LOGIN_USER_FAILURE = "LOGIN_USER_FAILURE";
export const SIGN_UP_USER_REQUEST = "SIGN_UP_USER_REQUEST";
export const SIGN_UP_USER_SUCCESS = "SIGN_UP_USER_SUCCESS";
export const SIGN_UP_USER_FAILURE = "SIGN_UP_USER_FAILURE";
export const OTP_VERIFICATION_REQUEST = "OTP_VERIFICATION_REQUEST";
export const OTP_VERIFICATION_SUCCESS = "OTP_VERIFICATION_SUCCESS";
export const OTP_VERIFICATION_FAILURE = "OTP_VERIFICATION_FAILURE";

export const LOGIN = "login";
export const SIGN_UP = "onregistration";
export const OTP_VERIFICATION = "otpverification";

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
      // TODO: dispatch a modal here
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
      // TODO: dispatch a modal here
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
      const result = await api.post(OTP_VERIFICATION, userDetails);
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(otpVerificationSuccess(resultJson));
    } catch (e) {
      dispatch(otpVerificationFailure(e.message));
    }
  };
}
