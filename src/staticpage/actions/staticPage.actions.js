import {
  SUCCESS,
  SUCCESS_UPPERCASE,
  SUCCESS_CAMEL_CASE,
  REQUESTING,
  ERROR,
  FAILURE
} from "../../lib/constants";
import * as ErrorHandling from "../../general/ErrorHandling.js";

export const GET_STATIC_PAGE_REQUEST = "GET_STATIC_PAGE_REQUEST";
export const GET_STATIC_PAGE_FAILURE = "GET_STATIC_PAGE_FAILURE";
export const GET_STATIC_PAGE_SUCCESS = "GET_STATIC_PAGE_SUCCESS";
export const PATH = "v2/mpl";

export function getStaticPageRequest() {
  return {
    type: GET_STATIC_PAGE_REQUEST,
    status: REQUESTING
  };
}

export function getStaticPageSuccess(data) {
  return {
    type: GET_STATIC_PAGE_SUCCESS,
    status: SUCCESS,
    data
  };
}

export function getStaticPageFailure(error) {
  return {
    type: GET_STATIC_PAGE_FAILURE,
    status: FAILURE,
    error
  };
}

export function getStaticPage(pageId) {
  return async (dispatch, getSate, { api }) => {
    dispatch(getStaticPageRequest());
    try {
      const result = await api.get(`${PATH}/cms/defaultpage?pageId=${pageId}`);
      const resultJson = await result.json();

      const resultJsonStatus = ErrorHandling.getFailureResponse(resultJson);
      if (resultJsonStatus.status) {
        throw new Error(resultJsonStatus.message);
      }
      return dispatch(getStaticPageSuccess(resultJson));
    } catch (e) {
      return dispatch(getStaticPageFailure(e.message));
    }
  };
}
