import { SUCCESS, REQUESTING, ERROR, FAILURE } from "../../lib/constants";
export const SET_SEARCH_STRING = "SET_SEARCH_STRING";
export const SEARCH_RESULT_REQUEST = "SEARCH_RESULT_REQUEST";
export const SEARCH_RESULT_SUCCESS = "SEARCH_RESULT_SUCCESS";
export const SEARCH_RESULT_FAILURE = "SEARCH_RESULT_FAILURE";
export const SEARCH_PATH = "/v2/mpl/searchAndSuggest?searchString=";
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
export function getSearchResultsSuccess(result) {
  return {
    type: SEARCH_RESULT_SUCCESS,
    status: SUCCESS,
    result
  };
}

export function getSearchResultsFailure(error) {
  return {
    type: SEARCH_RESULT_FAILURE,
    status: ERROR,
    error
  };
}
export function getSearchResults(searchString) {
  console.log(searchString);
  return async (dispatch, getState, { api }) => {
    dispatch(getSearchResultsRequest());
    console.log(`${SEARCH_PATH}${searchString}&category=all`);
    try {
      const result = await api.post(
        `${SEARCH_PATH}${searchString}&category=all`
      );
      const resultJson = await result.json();
      console.log(resultJson);
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(getSearchResultsSuccess(searchString));
    } catch (e) {
      dispatch(getSearchResultsFailure(e.message));
    }
  };
}
//{{root_url}}/marketplacewebservices/v2/mpl/searchAndSuggest?searchString=alp&category=all
