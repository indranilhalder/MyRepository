import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import {
  showModal,
  SIGN_UP_OTP_VERIFICATION,
  hideModal,
  FORGOT_PASSWORD_OTP_VERIFICATION
} from "../../general/modal.actions.js";
import * as Cookie from "../../lib/Cookie";
import config from "../../lib/config";
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

export const GLOBAL_ACCESS_TOKEN_REQUEST = "GLOBAL_ACCESS_TOKEN_REQUEST";
export const GLOBAL_ACCESS_TOKEN_SUCCESS = "GLOBAL_ACCESS_TOKEN_SUCCESS";
export const GLOBAL_ACCESS_TOKEN_FAILURE = "GLOBAL_ACCESS_TOKEN_FAILURE";

export const CUSTOMER_ACCESS_TOKEN_REQUEST = "CUSTOMER_ACCESS_TOKEN_REQUEST";
export const CUSTOMER_ACCESS_TOKEN_SUCCESS = "CUSTOMER_ACCESS_TOKEN_SUCCESS";
export const CUSTOMER_ACCESS_TOKEN_FAILURE = "CUSTOMER_ACCESS_TOKEN_FAILURE";

export const REFRESH_TOKEN_REQUEST = "REFRESH_TOKEN_REQUEST";
export const REFRESH_TOKEN_SUCCESS = "REFRESH_TOKEN_SUCCESS";
export const REFRESH_TOKEN_FAILURE = "REFRESH_TOKEN_FAILURE";

export const FACE_BOOK_LOGIN_REQUEST = "FACE_BOOK_LOGIN_REQUEST";
export const FACE_BOOK_LOGIN_FAILURE = "FACE_BOOK_LOGIN_FAILURE";
export const GOOGLE_PLUS_LOGIN_REQUEST = "GOOGLE_PLUS_LOGIN_REQUEST";
export const GOOGLE_PLUS_LOGIN_FAILURE = "GOOGLE_PLUS_LOGIN_FAILURE";

export const GENERATE_CUSTOMER_LEVEL_ACCESS_TOKEN_REQUEST =
  "GENERATE_CUSTOMER_LEVEL_ACCESS_TOKEN_REQUEST";
export const GENERATE_CUSTOMER_LEVEL_ACCESS_TOKEN_FAILURE =
  "GENERATE_CUSTOMER_LEVEL_ACCESS_TOKEN_FAILURE";

export const SOCIAL_MEDIA_REGISTRATION_REQUEST =
  "SOCIAL_MEDIA_REGISTRATION_REQUEST";
export const SOCIAL_MEDIA_REGISTRATION_SUCCESS =
  "SOCIAL_MEDIA_REGISTRATION_SUCCESS";
export const SOCIAL_MEDIA_REGISTRATION_FAILURE =
  "SOCIAL_MEDIA_REGISTRATION_FAILURE";

export const SOCIAL_MEDIA_LOGIN_REQUEST = "SOCIAL_MEDIA_LOGIN_REQUEST";
export const SOCIAL_MEDIA_LOGIN_SUCCESS = "SOCIAL_MEDIA_LOGIN_SUCCESS";
export const SOCIAL_MEDIA_LOGIN_FAILURE = "SOCIAL_MEDIA_LOGIN_FAILURE";

export const GET_CUSTOMER_PROFILE_REQUEST = "GET_CUSTOMER_PROFILE_REQUEST";
export const GET_CUSTOMER_PROFILE_SUCCESS = "GET_CUSTOMER_PROFILE_SUCCESS";
export const GET_CUSTOMER_PROFILE_FAILURE = "GET_CUSTOMER_PROFILE_FAILURE";

export const LOGIN_PATH = "v2/mpl/users";
export const FORGOT_PASSWORD_PATH =
  "v2/mpl/forgottenpasswordtokens/customerForgotPassword";
export const FORGOT_PASSWORD_OTP_VERIFICATION_PATH =
  "v2/mpl/forgottenpasswordtokens/forgotPasswordOTPVerification";
export const RESET_PASSWORD = "resetpassword";
export const OTP_VERIFICATION_PATH =
  "/v2/mpl/users/registrationOTPVerification";
export const TOKEN_PATH = "oauth/token";
export const SIGN_UP = "v2/mpl/users/customerRegistration";
export const SOCIAL_MEDIA_REGISTRATION_PATH =
  "v2/mpl/users/socialMediaRegistration";
export const SOCIAL_MEDIA_LOGIN_PATH = "v2/mpl/users";
const COOKIE_POLICY = "single_host_origin";
const REQUEST_VISIBLE_ACTIONS = "http://schema.org/AddAction";
const SCOPE = "https://www.googleapis.com/auth/plus.login email";
const PLATFORM_NUMBER = "2";
const CLIENT_ID = "gauravj@dewsolutions.in";
const CUSTOMER_PROFILE_PATH = "v2/mpl/users";
const FACEBOOK_PLATFORM = "facebook";
const GOOGLE_PLUS_PLATFORM = "googleplus";

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
  let customerCookie = Cookie.getCookie("sessionObjectCustomer");
  return async (dispatch, getState, { api }) => {
    dispatch(loginUserRequest());
    try {
      const result = await api.post(
        `${LOGIN_PATH}/${
          userLoginDetails.username
        }/customerLogin?access_token=${
          JSON.parse(customerCookie).access_token
        }&password=${userLoginDetails.password}&isPwa=true`
      );
      const resultJson = await result.json();
      if (resultJson.status === "Failure") {
        throw new Error(`${resultJson.message}`);
      }
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
  let globalCookie = Cookie.getCookie("sessionObjectGlobal");

  return async (dispatch, getState, { api }) => {
    dispatch(signUpUserRequest());
    try {
      const result = await api.post(
        `${SIGN_UP}?access_token=${
          JSON.parse(globalCookie).access_token
        }&isPwa=true&loginId=${userObj.loginId}&password=${
          userObj.password
        }&platformNumber=${PLATFORM_NUMBER}`
      );
      const resultJson = await result.json();
      if (resultJson.status === "Failure") {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(showModal(SIGN_UP_OTP_VERIFICATION, userObj));
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
export function otpVerification(otpDetails, userDetails) {
  let globalCookie = Cookie.getCookie("sessionObjectGlobal");
  return async (dispatch, getState, { api }) => {
    dispatch(otpVerificationRequest());
    try {
      const result = await api.post(
        `${OTP_VERIFICATION_PATH}?access_token=${
          JSON.parse(globalCookie).access_token
        }&otp=${otpDetails}&isPwa=true&platformNumber=${PLATFORM_NUMBER}&username=${
          userDetails.loginId
        }&password=${userDetails.password}`
      );
      const resultJson = await result.json();
      if (resultJson.status === "Failure") {
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
  let globalCookie = Cookie.getCookie("sessionObjectGlobal");
  return async (dispatch, getState, { api }) => {
    dispatch(forgotPasswordRequest());
    try {
      const result = await api.post(
        `${FORGOT_PASSWORD_PATH}?access_token=${
          JSON.parse(globalCookie).access_token
        }&platformNumber=2&isPwa=true&username=${userDetails}`
      );
      const resultJson = await result.json();
      if (resultJson.status === "Failure") {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(showModal(FORGOT_PASSWORD_OTP_VERIFICATION, userDetails));
      dispatch(forgotPasswordSuccess(resultJson));
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

export function forgotPasswordOtpVerification(otpDetails, userDetails) {
  let globalCookie = Cookie.getCookie("sessionObjectGlobal");
  return async (dispatch, getState, { api }) => {
    dispatch(forgotPasswordOtpVerificationRequest());
    try {
      const result = await api.post(
        `${FORGOT_PASSWORD_OTP_VERIFICATION_PATH}?access_token=${
          JSON.parse(globalCookie).access_token
        }&platformNumber=2&otp=${otpDetails}&isPwa=true&username=${userDetails}`
      );
      const resultJson = await result.json();
      if (resultJson.status === "Failure") {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here

      dispatch(forgotPasswordOtpVerificationSuccess(resultJson));
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
      if (resultJson.status === "Failure") {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(resetPasswordSuccess(resultJson));
    } catch (e) {
      dispatch(resetPasswordFailure(e.message));
    }
  };
}

export function globalAccessTokenRequest() {
  return {
    type: GLOBAL_ACCESS_TOKEN_REQUEST,
    status: REQUESTING
  };
}
export function globalAccessTokenSuccess(globalAccessTokenDetails) {
  return {
    type: GLOBAL_ACCESS_TOKEN_SUCCESS,
    status: SUCCESS,
    globalAccessTokenDetails
  };
}

export function globalAccessTokenFailure(error) {
  return {
    type: GLOBAL_ACCESS_TOKEN_FAILURE,
    status: ERROR,
    error
  };
}

export function getGlobalAccessToken() {
  return async (dispatch, getState, { api }) => {
    dispatch(globalAccessTokenRequest());
    try {
      const result = await api.post(
        `${TOKEN_PATH}?grant_type=client_credentials&client_id=${CLIENT_ID}&client_secret=secret`
      );
      const resultJson = await result.json();
      if (resultJson.status === "Failure") {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(globalAccessTokenSuccess(resultJson));
    } catch (e) {
      dispatch(globalAccessTokenFailure(e.message));
    }
  };
}

export function refreshTokenRequest() {
  return {
    type: REFRESH_TOKEN_REQUEST,
    status: REQUESTING
  };
}
export function refreshTokenSuccess(customerAccessTokenDetails) {
  return {
    type: REFRESH_TOKEN_SUCCESS,
    status: SUCCESS,
    customerAccessTokenDetails
  };
}

export function refreshTokenFailure(error) {
  return {
    type: REFRESH_TOKEN_FAILURE,
    status: ERROR,
    error
  };
}

export function refreshToken() {
  let customerCookie = Cookie.getCookie("sessionObjectCustomer");
  return async (dispatch, getState, { api }) => {
    dispatch(refreshTokenRequest());
    try {
      const result = await api.post(
        `${TOKEN_PATH}?refresh_token=${
          JSON.parse(customerCookie).refresh_token
        }&client_id=${CLIENT_ID}&client_secret=secret&grant_type=refresh_token`
      );
      const resultJson = await result.json();
      if (resultJson.status === "Failure") {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(refreshTokenSuccess(resultJson));
    } catch (e) {
      dispatch(refreshTokenFailure(e.message));
    }
  };
}

export function customerAccessTokenRequest() {
  return {
    type: CUSTOMER_ACCESS_TOKEN_REQUEST,
    status: REQUESTING
  };
}
export function customerAccessTokenSuccess(customerAccessTokenDetails) {
  return {
    type: CUSTOMER_ACCESS_TOKEN_SUCCESS,
    status: SUCCESS,
    customerAccessTokenDetails
  };
}

export function customerAccessTokenFailure(error) {
  return {
    type: CUSTOMER_ACCESS_TOKEN_FAILURE,
    status: ERROR,
    error
  };
}
export function customerAccessToken(userDetails) {
  let globalCookie = Cookie.getCookie("sessionObjectGlobal");
  return async (dispatch, getState, { api }) => {
    dispatch(customerAccessTokenRequest());
    try {
      const result = await api.post(
        `${TOKEN_PATH}?grant_type=password&client_id=${CLIENT_ID}&client_secret=secret&username=${
          userDetails.username
        }&password=${userDetails.password}&access_token=${
          JSON.parse(globalCookie).access_token
        }`
      );
      const resultJson = await result.json();
      if (resultJson.status === "Failure") {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(customerAccessTokenSuccess(resultJson));
      dispatch(loginUser(userDetails));
    } catch (e) {
      dispatch(customerAccessTokenFailure(e.message));
    }
  };
}

export function faceBookLoginRequest() {
  return {
    type: FACE_BOOK_LOGIN_REQUEST,
    status: REQUESTING
  };
}

export function faceBookLoginFailure(error) {
  return {
    type: FACE_BOOK_LOGIN_FAILURE,
    status: ERROR,
    error
  };
}

export function facebookLogin() {
  return async dispatch => {
    try {
      dispatch(faceBookLoginRequest());
      window.FB.login(
        function(resp) {
          if (resp.authResponse) {
            window.FB.api(
              "/me",
              { locale: "en_US", fields: "name, email" },
              function(response) {
                dispatch(
                  socialMediaRegistration(
                    response.email,
                    resp.authResponse.accessToken,
                    FACEBOOK_PLATFORM
                  )
                );
              }
            );
          } else {
            console.log("User cancelled login or did not fully authorize.");
          }
        },
        {
          scope: "email,user_likes"
        }
      );
    } catch (e) {
      dispatch(faceBookLoginFailure(e));
    }
  };
}

export function googlePlusLoginRequest() {
  return {
    type: GOOGLE_PLUS_LOGIN_REQUEST,
    status: REQUESTING
  };
}

export function googlePlusLoginFailure(error) {
  return {
    type: GOOGLE_PLUS_LOGIN_FAILURE,
    status: ERROR,
    error
  };
}

export function googlePlusLogin() {
  return async dispatch => {
    try {
      dispatch(googlePlusLoginRequest());
      window.gapi.auth.signIn({
        callback: function(authResponse) {
          window.gapi.client.load("plus", "v1", function() {
            var request = window.gapi.client.plus.people.get({
              userId: "me"
            });
            request.execute(function(resp) {
              dispatch(
                socialMediaRegistration(
                  resp.emails[0].value,
                  authResponse.access_token,
                  GOOGLE_PLUS_PLATFORM
                )
              );
            });
          });
        },
        clientid: config.google,
        cookiepolicy: COOKIE_POLICY,
        requestvisibleactions: REQUEST_VISIBLE_ACTIONS,
        scope: SCOPE
      });
    } catch (e) {
      dispatch(googlePlusLoginFailure(e));
    }
  };
}

export function generateCustomerLevelAccessTokenRequest() {
  return {
    type: GENERATE_CUSTOMER_LEVEL_ACCESS_TOKEN_REQUEST,
    status: REQUESTING
  };
}

export function generateCustomerLevelAccessTokenFailure(error) {
  return {
    type: GENERATE_CUSTOMER_LEVEL_ACCESS_TOKEN_FAILURE,
    status: ERROR,
    error
  };
}

export function generateCustomerLevelAccessTokenForSocialMedia(
  userName,
  accessToken,
  platForm
) {
  let globalCookie = Cookie.getCookie("sessionObjectGlobal");
  return async (dispatch, getState, { api }) => {
    dispatch(customerAccessTokenRequest());
    try {
      const result = await api.post(
        `${TOKEN_PATH}?grant_type=password&client_id=${CLIENT_ID}&client_secret=secret&username=${userName}&access_token=${
          JSON.parse(globalCookie).access_token
        }&isSocialMedia=Y&socialMediaPlatform=${platForm}`
      );
      const resultJson = await result.json();
      if (resultJson.status === "Failure") {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(customerAccessTokenSuccess(resultJson));
    } catch (e) {
      dispatch(customerAccessTokenFailure(e.message));
    }
  };
}

export function socialMediaRegistrationRequest() {
  return {
    type: SOCIAL_MEDIA_REGISTRATION_REQUEST,
    status: REQUESTING
  };
}

export function socialMediaRegistrationFailure(error) {
  return {
    type: SOCIAL_MEDIA_REGISTRATION_FAILURE,
    status: ERROR,
    error
  };
}

export function socialMediaRegistration(userName, accessToken, platForm) {
  let globalCookie = Cookie.getCookie("sessionObjectGlobal");
  let customerCookie = Cookie.getCookie("sessionObjectCustomer");
  return async (dispatch, getState, { api }) => {
    dispatch(socialMediaRegistrationRequest());
    try {
      const result = await api.post(
        `${SOCIAL_MEDIA_REGISTRATION_PATH}?access_token=${
          JSON.parse(globalCookie).access_token
        }&emailId=${userName}&socialMedia=${platForm}&platformNumber=${PLATFORM_NUMBER}&isPwa=true`
      );
      const resultJson = await result.json();
      dispatch(
        generateCustomerLevelAccessTokenForSocialMedia(
          userName,
          accessToken,
          platForm
        )
      );
      if (resultJson.status === "Failure") {
        if (
          resultJson.error ===
          "Email Id already exists, please try with another email Id!"
        ) {
          if (customerCookie) {
            dispatch(socialMediaLogin(userName, platForm));
          }
        } else {
          throw new Error(`${resultJson.message}`);
        }
      }
    } catch (e) {
      dispatch(socialMediaRegistrationFailure(e.message));
    }
  };
}

export function socialMediaLoginRequest() {
  return {
    type: SOCIAL_MEDIA_LOGIN_REQUEST,
    status: REQUESTING
  };
}

export function socialMediaLoginSuccess(user) {
  return {
    type: SOCIAL_MEDIA_LOGIN_SUCCESS,
    status: SUCCESS,
    user
  };
}

export function socialMediaLoginFailure(error) {
  return {
    type: SOCIAL_MEDIA_LOGIN_FAILURE,
    status: ERROR,
    error
  };
}

export function socialMediaLogin(userName, platform) {
  let customerCookie = Cookie.getCookie("sessionObjectCustomer");
  return async (dispatch, getState, { api }) => {
    dispatch(socialMediaLoginRequest());
    try {
      const result = await api.post(
        `${SOCIAL_MEDIA_LOGIN_PATH}/${userName}/loginSocialUser?access_token=${
          JSON.parse(customerCookie).access_token
        }&emailId=${userName}&socialMedia=${platform}&platformNumber=${PLATFORM_NUMBER}&isPwa=true`
      );
      const resultJson = await result.json();
      if (resultJson.status === "Failure") {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here

      dispatch(socialMediaLoginSuccess(resultJson));
    } catch (e) {
      dispatch(socialMediaLoginFailure(e.message));
    }
  };
}

export function getCustomerProfileRequest() {
  return {
    type: GET_CUSTOMER_PROFILE_REQUEST,
    status: REQUESTING
  };
}

export function getCustomerProfileSuccess(user) {
  return {
    type: GET_CUSTOMER_PROFILE_SUCCESS,
    status: SUCCESS,
    user
  };
}

export function getCustomerProfileFailure(error) {
  return {
    type: GET_CUSTOMER_PROFILE_FAILURE,
    status: ERROR,
    error
  };
}

export function getCustomerProfile() {
  let customerCookie = Cookie.getCookie("sessionObjectCustomer");
  return async (dispatch, getState, { api }) => {
    dispatch(getCustomerProfileRequest());
    try {
      const result = await api.post(
        `${CUSTOMER_PROFILE_PATH}/9886973967/getMyProfile?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true`
      );
      const resultJson = await result.json();
      if (resultJson.status === "Failure") {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(getCustomerProfileSuccess(resultJson));
    } catch (e) {
      dispatch(getCustomerProfileFailure(e.message));
    }
  };
}
