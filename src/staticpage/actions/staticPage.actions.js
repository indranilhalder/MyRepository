import {
  SUCCESS,
  SUCCESS_UPPERCASE,
  SUCCESS_CAMEL_CASE,
  REQUESTING,
  ERROR,
  FAILURE
} from "../../lib/constants";
import * as ErrorHandling from "../../general/ErrorHandling.js";
export const GET_ABOUT_US_REQUEST = "GET_ABOUT_US_REQUEST";
export const GET_ABOUT_US_SUCCESS = "GET_ABOUT_US_SUCCESS";
export const GET_ABOUT_US_FAILURE = "GET_ABOUT_US_FAILURE";

export const GET_FAQ_REQUEST = "GET_FAQ_REQUEST";
export const GET_FAQ_SUCCESS = "GET_FAQ_SUCCESS";
export const GET_FAQ_FAILURE = "GET_FAQ_FAILURE";

export const TERMS_CONDITION_REQUEST = "TERMS_CONDITION_REQUEST";
export const TERMS_CONDITION_SUCCESS = "TERMS_CONDITION_SUCCESS";
export const TERMS_CONDITION_FAILURE = "TERMS_CONDITION_FAILURE";
export const PATH = "v2/mpl";
export const ABOUT_US = "aboutus";
export const FAQ = "faq";
export const TNC = "tncPayment";

//ABOUT US
export function aboutUsRequest() {
  return {
    type: GET_ABOUT_US_REQUEST,
    status: REQUESTING
  };
}
export function aboutUsSuccess(aboutUs) {
  return {
    type: GET_ABOUT_US_SUCCESS,
    status: SUCCESS,
    aboutUs
  };
}
export function aboutUsFailure(error) {
  return {
    type: GET_ABOUT_US_FAILURE,
    status: ERROR,
    error
  };
}
export function getAboutUsDetails() {
  return async (dispatch, getState, { api }) => {
    dispatch(aboutUsRequest());
    try {
      const result = await api.get(
        `${PATH}/cms/defaultpage?pageId=${ABOUT_US}`
      );

      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      return dispatch(aboutUsSuccess(resultJson));
    } catch (e) {
      dispatch(aboutUsFailure(e.message));
    }
  };
}

//FAQ

export function faqRequest() {
  return {
    type: GET_FAQ_REQUEST,
    status: REQUESTING
  };
}
export function faqSuccess(faq) {
  return {
    type: GET_FAQ_SUCCESS,
    status: SUCCESS,
    faq
  };
}
export function faqFailure(error) {
  return {
    type: GET_FAQ_FAILURE,
    status: ERROR,
    error
  };
}
export function getFaqDetails() {
  return async (dispatch, getState, { api }) => {
    dispatch(aboutUsRequest());
    try {
      const result = await api.get(`${PATH}/cms/defaultpage?pageId=${FAQ}`);
      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      return dispatch(faqSuccess(resultJson));
    } catch (e) {
      dispatch(faqFailure(e.message));
    }
  };
}
//TERMS AND CONDITION

export function termsAndConditionRequest() {
  return {
    type: TERMS_CONDITION_REQUEST,
    status: REQUESTING
  };
}
export function termsAndConditionSuccess(termsAndCondition) {
  return {
    type: TERMS_CONDITION_SUCCESS,
    status: SUCCESS,
    termsAndCondition
  };
}
export function termsAndConditionFailure(error) {
  return {
    type: TERMS_CONDITION_FAILURE,
    status: ERROR,
    error
  };
}
export function getTermsAndConditionDetails() {
  return async (dispatch, getState, { api }) => {
    dispatch(termsAndConditionRequest());
    try {
      const result = await api.get(`${PATH}/cms/defaultpage?pageId=${TNC}`);

      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      return dispatch(termsAndConditionSuccess(resultJson));
    } catch (e) {
      dispatch(termsAndConditionFailure(e.message));
    }
  };
}
