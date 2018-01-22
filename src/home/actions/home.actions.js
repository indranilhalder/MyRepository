import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
export const HOME_FEED_REQUEST = "HOME_FEED_REQUEST";
export const HOME_FEED_SUCCESS = "HOME_FEED_SUCCESS";
export const HOME_FEED_FAILURE = "HOME_FEED_FAILURE";
export const COMPONENT_DATA_REQUEST = "COMPONENT_DATA_REQUEST";
export const COMPONENT_DATA_SUCCESS = "COMPONENT_DATA_SUCCESS";
export const COMPONENT_DATA_FAILURE = "COMPONENT_DATA_FAILURE";
export const HOME_FEED_PATH = "homepage";

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
  return {
    type: COMPONENT_DATA_REQUEST,
    status: REQUESTING,
    positionInFeed
  };
}

export function componentDataSuccess(data, positionInFeed) {
  return {
    type: COMPONENT_DATA_SUCCESS,
    status: SUCCESS,
    data,
    positionInFeed
  };
}

export function componentDataFailure(positionInFeed, error) {
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
      dispatch(componentDataSuccess(resultJson, positionInFeed));
    } catch (e) {
      dispatch(componentDataFailure(positionInFeed, e.message));
    }
  };
}
