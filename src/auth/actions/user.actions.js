import {
  SUCCESS,
  REQUESTING,
  ERROR,
  FAILURE_LOWERCASE
} from "../../lib/constants";
import {
  GLOBAL_ACCESS_TOKEN,
  CUSTOMER_ACCESS_TOKEN,
  FAILURE_UPPERCASE,
  OTP_VERIFICATION_REQUIRED_CODE,
  OTP_VERIFICATION_REQUIRED_TEXT
} from "../../lib/constants";
import {
  showModal,
  SIGN_UP_OTP_VERIFICATION,
  hideModal,
  FORGOT_PASSWORD_OTP_VERIFICATION,
  NEW_PASSWORD,
  OTP_LOGIN_MODAL
} from "../../general/modal.actions.js";
import * as Cookie from "../../lib/Cookie";
import config from "../../lib/config";
import { SOCIAL_SIGN_UP } from "../../lib/constants";
import {
  authCallsAreInProgress,
  singleAuthCallHasFailed,
  stopLoaderOnLoginForOTPVerification
} from "./auth.actions";
import * as ErrorHandling from "../../general/ErrorHandling.js";
import { OTP_VERIFICATION_REQUIRED_MESSAGE } from "../containers/LoginContainer";
import { displayToast } from "../../general/toast.actions";

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
export const RESET_PASSWORD =
  "v2/mpl/forgottenpasswordtokens/forgotPassword?access_token=";
export const OTP_VERIFICATION_PATH = "v2/mpl/users/registrationOTPVerification";
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
export const FACEBOOK_PLATFORM = "facebook";
export const GOOGLE_PLUS_PLATFORM = "googleplus";
export const LOGIN_WITH_MOBILE = "mobile";
export const LOGIN_WITH_EMAIL = "email";
const FACEBOOK_SCOPE = "email,user_likes";
const LOCALE = "en_US";
const FACEBOOK_FIELDS =
  "first_name.as(firstName),last_name.as(lastName), email, picture.width(60).height(60).as(profileImage)";
const MY_PROFILE = "me";
const FAILURE = "Failure";
const GOOGLE_PLATFORM_URL = "//apis.google.com/js/platform.js";
export const SOCIAL_CHANNEL_GOOGLE_PLUS = "G";
export const SOCIAL_CHANNEL_FACEBOOK = "F";

export function loginUserRequest() {
  return {
    type: LOGIN_USER_REQUEST,
    status: REQUESTING
  };
}

export function loginUserSuccess(user, userName) {
  return {
    type: LOGIN_USER_SUCCESS,
    status: SUCCESS,
    user,
    userName
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
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    let loginDetails = new FormData();
    loginDetails.append("password", userLoginDetails.password);
    loginDetails.append("isPwa", true);
    dispatch(loginUserRequest());
    try {
      let url = `${LOGIN_PATH}/${
        userLoginDetails.username
      }/customerLogin?access_token=${JSON.parse(customerCookie).access_token}`;
      if (userLoginDetails.otp) {
        url = `${url}&otp=${userLoginDetails.otp}`;
      }
      const result = await api.postFormData(url, loginDetails);
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJson.errorCode) {
        if (resultJsonStatus.message === "Invalid OTP. Please try again") {
          dispatch(displayToast(resultJsonStatus.message));
        }
        dispatch(stopLoaderOnLoginForOTPVerification());

        if (
          resultJson.errorCode === OTP_VERIFICATION_REQUIRED_CODE ||
          resultJson.status === OTP_VERIFICATION_REQUIRED_TEXT
        ) {
          return dispatch(
            showModal(OTP_LOGIN_MODAL, {
              username: userLoginDetails.username,
              password: userLoginDetails.password
            })
          );
        }
      }
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      return dispatch(loginUserSuccess(resultJson, userLoginDetails.username));
    } catch (e) {
      return dispatch(loginUserFailure(e.message));
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
  let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(signUpUserRequest());
    try {
      let suffix = "";
      if (userObj.emailId) {
        suffix = `&emailId=${userObj.emailId}`;
      }
      let result = await api.post(
        `${SIGN_UP}?access_token=${
          JSON.parse(globalCookie).access_token
        }&isPwa=true&username=${userObj.username}&password=${
          userObj.password
        }&platformNumber=${PLATFORM_NUMBER}${suffix}`
      );

      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
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
export function otpVerificationSuccess(user, userName) {
  return {
    type: OTP_VERIFICATION_SUCCESS,
    status: SUCCESS,
    user,
    userName
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
  let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(otpVerificationRequest());
    try {
      const result = await api.post(
        `${OTP_VERIFICATION_PATH}?access_token=${
          JSON.parse(globalCookie).access_token
        }&otp=${otpDetails}&isPwa=true&platformNumber=${PLATFORM_NUMBER}&username=${
          userDetails.username
        }&password=${userDetails.password}&emailId=${userDetails.emailId}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      dispatch(hideModal());
      return dispatch(otpVerificationSuccess(resultJson, userDetails.username));
    } catch (e) {
      return dispatch(otpVerificationFailure(e.message));
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
  let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(forgotPasswordRequest());
    try {
      const result = await api.post(
        `${FORGOT_PASSWORD_PATH}?access_token=${
          JSON.parse(globalCookie).access_token
        }&platformNumber=2&isPwa=true&username=${userDetails}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
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
  let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(forgotPasswordOtpVerificationRequest());
    try {
      const result = await api.post(
        `${FORGOT_PASSWORD_OTP_VERIFICATION_PATH}?access_token=${
          JSON.parse(globalCookie).access_token
        }&platformNumber=2&otp=${otpDetails}&isPwa=true&username=${userDetails}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      // TODO: dispatch a modal here
      dispatch(
        showModal(NEW_PASSWORD, {
          otpDetails: otpDetails,
          userName: userDetails
        })
      );
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
  const globalAccessToken = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(resetPasswordRequest());
    try {
      const url = `${RESET_PASSWORD}${
        JSON.parse(globalAccessToken).access_token
      }&username=${userDetails.username}&newPassword=${
        userDetails.newPassword
      }&otp=${userDetails.otp}`;
      const result = await api.post(url);
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
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
        `${TOKEN_PATH}?grant_type=client_credentials&client_id=${CLIENT_ID}&client_secret=secret&isPwa=true`
      );
      const resultJson = await result.json();

      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      return dispatch(globalAccessTokenSuccess(resultJson));
    } catch (e) {
      return dispatch(globalAccessTokenFailure(e.message));
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
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(refreshTokenRequest());
    try {
      const result = await api.post(
        `${TOKEN_PATH}?refresh_token=${
          JSON.parse(customerCookie).refresh_token
        }&client_id=${CLIENT_ID}&client_secret=secret&grant_type=refresh_token`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      // TODO: dispatch a modal here
      return dispatch(refreshTokenSuccess(resultJson));
    } catch (e) {
      return dispatch(refreshTokenFailure(e.message));
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
  let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    let userLoginDetails = new FormData();
    userLoginDetails.append("username", userDetails.username);
    userLoginDetails.append("password", userDetails.password);
    dispatch(customerAccessTokenRequest());
    // this is our first call so we are setting true that from this
    // we started loading and  this loading will end with merge cart
    // id request success
    dispatch(authCallsAreInProgress());
    try {
      const result = await api.postFormData(
        `${TOKEN_PATH}?grant_type=password&client_id=${CLIENT_ID}&client_secret=secret&access_token=${
          JSON.parse(globalCookie).access_token
        }`,
        userLoginDetails
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      return dispatch(customerAccessTokenSuccess(resultJson));
    } catch (e) {
      return dispatch(customerAccessTokenFailure(e.message));
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

export function facebookLogin(isSignUp) {
  return async dispatch => {
    try {
      dispatch(faceBookLoginRequest());
      const authResponse = await new Promise((resolve, reject) => {
        window.FB.login(
          resp => {
            if (resp.authResponse) {
              resolve(resp);
            } else {
              reject("User cancelled login or did not fully authorize");
            }
          },
          {
            scope: FACEBOOK_SCOPE
          }
        );
      });

      const graphResponse = await new Promise((resolve, reject) => {
        window.FB.api(
          `/${MY_PROFILE}`,
          { locale: LOCALE, fields: FACEBOOK_FIELDS },
          response => {
            resolve(response);
          }
        );
      });

      return { ...authResponse.authResponse, ...graphResponse };
    } catch (e) {
      return dispatch(faceBookLoginFailure(e));
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

export function loadGoogleSignInApi() {
  const scope = SCOPE;
  const clientId = process.env.REACT_APP_GOOGLE_CLIENT_ID;
  return new Promise((resolve, reject) => {
    const firstJS = document.getElementsByTagName("script")[0];
    const js = document.createElement("script");

    js.src = GOOGLE_PLATFORM_URL;
    js.id = "gapi-client";

    js.onload = () => {
      window.gapi.load("auth2", () => {
        if (!window.gapi.auth2.getAuthInstance()) {
          window.gapi.auth2
            .init({
              client_id: clientId,
              fetch_basic_profile: true,
              ux_mode: "popup",
              scope: scope
                ? (Array.isArray(scope) && scope.join(" ")) || scope
                : null
            })
            .then(
              () =>
                resolve({
                  status: SUCCESS
                }),
              err => {
                resolve({
                  provider: "google",
                  type: "load",
                  error: "Failed to load SDK",
                  status: ERROR,
                  err
                });
              }
            );
        } else {
          resolve({
            status: SUCCESS
          });
        }
      });
    };

    if (!firstJS) {
      document.appendChild(js);
    } else {
      firstJS.parentNode.appendChild(js);
    }
  });
}

export function googlePlusLogin(type) {
  return async dispatch => {
    try {
      dispatch(googlePlusLoginRequest());

      const googleResponse = await window.gapi.auth2.getAuthInstance().signIn();
      if (googleResponse.code > 400) {
        throw new Error(`${googleResponse.message}`);
      }

      const basicProfile = googleResponse.getBasicProfile();
      const profileImage = basicProfile.getImageUrl();
      const email = basicProfile.getEmail();
      const name = basicProfile.getName();
      const id = basicProfile.getId();
      let firstName, lastName;
      if (name) {
        firstName = name.split(" ")[0];
        lastName = name.split(" ")[1];
      }
      const accessToken = googleResponse.getAuthResponse().access_token;

      return { email, id, accessToken, profileImage, firstName, lastName };
    } catch (e) {
      return dispatch(googlePlusLoginFailure(e.message));
    }
  };
}

export function generateCustomerLevelAccessTokenForSocialMedia(
  userName,
  id,
  accessToken,
  platForm,
  socialChannel
) {
  return async (dispatch, getState, { api }) => {
    dispatch(customerAccessTokenRequest());
    try {
      const result = await api.post(
        `${TOKEN_PATH}?grant_type=password&client_id=${CLIENT_ID}&client_secret=secret&username=${userName}&social_token=${accessToken}&isSocialMedia=Y&social_channel=${socialChannel}&userId_param=${id}`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      return dispatch(customerAccessTokenSuccess(resultJson));
    } catch (e) {
      return dispatch(customerAccessTokenFailure(e.message));
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

export function socialMediaRegistrationSuccess(user) {
  return {
    type: SOCIAL_MEDIA_REGISTRATION_SUCCESS,
    status: SUCCESS,
    user
  };
}

export function socialMediaRegistration(
  userName,
  id,
  accessToken,
  platForm,
  socialChannel
) {
  let globalCookie = Cookie.getCookie(GLOBAL_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(socialMediaRegistrationRequest());
    try {
      const result = await api.post(
        `${SOCIAL_MEDIA_REGISTRATION_PATH}?access_token=${
          JSON.parse(globalCookie).access_token
        }&emailId=${userName}&socialMedia=${platForm}&platformNumber=${PLATFORM_NUMBER}&isPwa=true`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (resultJsonStatus.status) {
        throw new Error(`${resultJsonStatus.message}`);
      }
      return dispatch(socialMediaRegistrationSuccess(resultJson));
    } catch (e) {
      return dispatch(socialMediaRegistrationFailure(e.message));
    }
  };
}

export function socialMediaLoginRequest() {
  return {
    type: SOCIAL_MEDIA_LOGIN_REQUEST,
    status: REQUESTING
  };
}

export function socialMediaLoginSuccess(user, loginType, userDetailObj) {
  return {
    type: SOCIAL_MEDIA_LOGIN_SUCCESS,
    status: SUCCESS,
    user,
    loginType,
    userDetailObj
  };
}

export function socialMediaLoginFailure(error) {
  return {
    type: SOCIAL_MEDIA_LOGIN_FAILURE,
    status: ERROR,
    error
  };
}

export function socialMediaLogin(
  userName,
  platform,
  customerAccessToken,
  userDetailObj
) {
  return async (dispatch, getState, { api }) => {
    dispatch(socialMediaLoginRequest());
    try {
      const result = await api.post(
        `${SOCIAL_MEDIA_LOGIN_PATH}/${userName}/loginSocialUser?access_token=${customerAccessToken}&emailId=${userName}&socialMedia=${platform}&platformNumber=${PLATFORM_NUMBER}&isPwa=true`
      );
      const resultJson = await result.json();

      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }

      return dispatch(
        socialMediaLoginSuccess(resultJson, platform, userDetailObj)
      );
    } catch (e) {
      return dispatch(socialMediaLoginFailure(e.message));
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
  let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
  return async (dispatch, getState, { api }) => {
    dispatch(getCustomerProfileRequest());
    try {
      const result = await api.post(
        `${CUSTOMER_PROFILE_PATH}/9886973967/getMyProfile?access_token=${
          JSON.parse(customerCookie).access_token
        }&isPwa=true`
      );
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      // TODO: dispatch a modal here
      dispatch(getCustomerProfileSuccess(resultJson));
    } catch (e) {
      dispatch(getCustomerProfileFailure(e.message));
    }
  };
}
