import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
export const SET_SEARCH_STRING = "SET_SEARCH_STRING";
export const SEARCH_RESULT_REQUEST = "SEARCH_RESULT_REQUEST";
export const SEARCH_RESULT_SUCCESS = "SEARCH_RESULT_SUCCESS";
export const SEARCH_RESULT_FAILURE = "SEARCH_RESULT_FAILURE";
export function setSearchString(string) {
  return {
    type: SET_SEARCH_STRING,
    string
  };
}

export function getSearchResultsRequest() {
  return {
    type: SEARCH_RESULT_REQUEST,
    status: REQUESTING
  };
}
export function getSearchResultsSuccess(cartDetails) {
  return {
    type: SEARCH_RESULT_SUCCESS,
    status: SUCCESS,
    cartDetails
  };
}

export function getSearchResultsFailure(error) {
  return {
    type: SEARCH_RESULT_FAILURE,
    status: ERROR,
    error
  };
}
//{{root_url}}/marketplacewebservices/v2/mpl/searchAndSuggest?searchString=alp&category=all
