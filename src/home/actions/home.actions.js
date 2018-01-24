import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
export const HOME_FEED_REQUEST = "HOME_FEED_REQUEST";
export const HOME_FEED_SUCCESS = "HOME_FEED_SUCCESS";
export const HOME_FEED_FAILURE = "HOME_FEED_FAILURE";
export const COMPONENT_DATA_REQUEST = "COMPONENT_DATA_REQUEST";
export const COMPONENT_DATA_SUCCESS = "COMPONENT_DATA_SUCCESS";
export const COMPONENT_DATA_FAILURE = "COMPONENT_DATA_FAILURE";
export const SINGLE_SELECT_REQUEST = "SINGLE_SELECT_REQUEST";
export const SINGLE_SELECT_SUCCESS = "SINGLE_SELECT_SUCCESS";
export const SINGLE_SELECT_FAILURE = "SINGLE_SELECT_FAILURE";
export const MULTI_SELECT_SUBMIT_REQUEST = "MULTI_SELECT_SUBMIT_REQUEST";
export const MULTI_SELECT_SUBMIT_SUCCESS = "MULTI_SELECT_SUBMIT_SUCCESS";
export const MULTI_SELECT_SUBMIT_FAILURE = "MULTI_SELECT_SUBMIT_FAILURE";
export const HOME_FEED_PATH = "homepage";
export const SINGLE_SELECT_SUBMIT_PATH = "submitSingleSelectQuestion";
export const MULTI_SELECT_SUBMIT_PATH = "submitMultiSelectQuestion";

export function multiSelectSubmitRequest(positionInFeed) {
  return {
    type: MULTI_SELECT_SUBMIT_REQUEST,
    status: REQUESTING,
    positionInFeed
  };
}

export function multiSelectSubmitFailure(positionInFeed, errorMsg) {
  return {
    type: MULTI_SELECT_SUBMIT_FAILURE,
    status: ERROR,
    error: errorMsg
  };
}

export function multiSelectSubmitSuccess(resultJson, positionInFeed) {
  return {
    type: MULTI_SELECT_SUBMIT_SUCCESS,
    status: SUCCESS,
    data: resultJson,
    positionInFeed
  };
}

export function multiSelectSubmit(values, questionId, positionInFeed) {
  return async (dispatch, getState, { api }) => {
    dispatch(multiSelectSubmitRequest(positionInFeed));
    try {
      const result = await api.post(SINGLE_SELECT_SUBMIT_PATH, {
        optionId: values,
        questionId
      });
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(multiSelectSubmitSuccess(resultJson, positionInFeed));
    } catch (e) {
      dispatch(multiSelectSubmitFailure(e.message, positionInFeed));
    }
  };
}

export function singleSelectRequest(positionInFeed) {
  return {
    type: SINGLE_SELECT_REQUEST,
    status: REQUESTING,
    positionInFeed
  };
}

export function singleSelectFailure(error, positionInFeed) {
  return {
    type: SINGLE_SELECT_FAILURE,
    status: ERROR,
    error,
    positionInFeed
  };
}
export function singleSelectSuccess(resultJson, positionInFeed) {
  return {
    type: SINGLE_SELECT_SUCCESS,
    status: SUCCESS,
    data: resultJson,
    positionInFeed
  };
}

export function selectSingleSelectResponse(value, questionId, positionInFeed) {
  return async (dispatch, getState, { api }) => {
    dispatch(singleSelectRequest(positionInFeed));
    try {
      const result = await api.post(SINGLE_SELECT_SUBMIT_PATH, {
        questionId,
        optionId: [value]
      });
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(singleSelectSuccess(resultJson, positionInFeed));
    } catch (e) {
      dispatch(singleSelectFailure(e.message, positionInFeed));
    }
  };
}

export function homeFeedRequest() {
  return {
    type: HOME_FEED_REQUEST,
    status: REQUESTING
  };
}

export function homeFeedSuccess(data) {
  return {
    type: HOME_FEED_SUCCESS,
    status: SUCCESS,
    data
  };
}

export function homeFeedFailure(error) {
  return {
    type: HOME_FEED_FAILURE,
    status: ERROR,
    error
  };
}

export function homeFeed() {
  return async (dispatch, getState, { api }) => {
    dispatch(homeFeedRequest());
    try {
      const result = await api.get(HOME_FEED_PATH);
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }

      dispatch(homeFeedSuccess(resultJson.items));
    } catch (e) {
      dispatch(homeFeedFailure(e.message));
    }
  };
}

export function componentDataRequest(positionInFeed) {
  console.log("request");
  return {
    type: COMPONENT_DATA_REQUEST,
    status: REQUESTING,
    positionInFeed
  };
}

export function componentDataSuccess(data, positionInFeed) {
  console.log("success");
  return {
    type: COMPONENT_DATA_SUCCESS,
    status: SUCCESS,
    data,
    positionInFeed
  };
}

export function componentDataFailure(positionInFeed, error) {
  console.log("fail");
  return {
    type: COMPONENT_DATA_FAILURE,
    status: ERROR,
    positionInFeed,
    error
  };
}

export function getComponentData(positionInFeed, fetchURL) {
  return async (dispatch, getState, { api }) => {
    dispatch(componentDataRequest(positionInFeed));
    try {
      const result = await api.get(
        fetchURL.substring(fetchURL.lastIndexOf("/") + 1)
      );
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      // console.log(resultJson);
      dispatch(componentDataSuccess(resultJson, positionInFeed));
    } catch (e) {
      dispatch(componentDataFailure(positionInFeed, e.message));
    }
  };
}
