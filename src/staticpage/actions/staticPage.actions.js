import {
  SUCCESS,
  SUCCESS_UPPERCASE,
  SUCCESS_CAMEL_CASE,
  REQUESTING,
  ERROR,
  FAILURE
} from "../../lib/constants";
import moment from "moment";
import * as ErrorHandling from "../../general/ErrorHandling.js";
export const GET_ABOUT_US_REQUEST = "GET_ABOUT_US_REQUEST";
export const GET_ABOUT_US_SUCCESS = "GET_ABOUT_US_SUCCESS";
export const GET_ABOUT_US_FAILURE = "GET_ABOUT_US_FAILURE";
export const PATH = "v2/mpl";

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
      const result = await api.get(`${PATH}/cms/defaultpage?pageId=aboutus`);

      const resultJson = await result.json();
      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);

      if (
        resultJson.status === SUCCESS ||
        resultJson.status === SUCCESS_UPPERCASE ||
        resultJson.status === SUCCESS_CAMEL_CASE
      ) {
        return dispatch(aboutUsSuccess(resultJson));
      } else {
        throw new Error(resultJsonStatus.message);
      }
    } catch (e) {
      dispatch(aboutUsFailure(e.message));
    }
  };
}
