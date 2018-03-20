import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
export const GET_SAVED_CARD_REQUEST = "GET_SAVED_CARD_REQUEST";
export const GET_SAVED_CARD_SUCCESS = "GET_SAVED_CARD_SUCCESS";
export const GET_SAVED_CARD_FAILURE = "GET_SAVED_CARD_FAILURE";

export const EDIT_SAVED_CARD_REQUEST = "EDIT_SAVED_CARD_REQUEST";
export const EDIT_SAVED_CARD_SUCCESS = "EDIT_SAVED_CARD_SUCCESS";
export const EDIT_SAVED_CARD_FAILURE = "EDIT_SAVED_CARD_FAILURE";

export const ADD_SAVED_CARD_REQUEST = "ADD_SAVED_CARD_REQUEST";
export const ADD_SAVED_CARD_SUCCESS = "ADD_SAVED_CARD_SUCCESS";
export const ADD_SAVED_CARD_FAILURE = "ADD_SAVED_CARD_FAILURE";

export const REMOVE_SAVED_CARD_REQUEST = "REMOVE_SAVED_CARD_REQUEST";
export const REMOVE_SAVED_CARD_SUCCESS = "REMOVE_SAVED_CARD_SUCCESS";
export const REMOVE_SAVED_CARD_FAILURE = "REMOVE_SAVED_CARD_FAILURE";

export const USER_PATH = "v2/mpl/users";
const CARD_TYPE = "BOTH";

export function getSavedCardRequest() {
  return {
    type: GET_SAVED_CARD_REQUEST,
    status: REQUESTING
  };
}
export function getSavedCardSuccess(savedCards) {
  return {
    type: GET_SAVED_CARD_SUCCESS,
    status: SUCCESS,
    savedCards
  };
}

export function getSavedCardFailure(error) {
  return {
    type: GET_SAVED_CARD_FAILURE,
    status: ERROR,
    error
  };
}

export function getSavedCardDetails(userId, customerAccessToken) {
  return async (dispatch, getState, { api }) => {
    dispatch(getSavedCardRequest());
    try {
      const result = await api.post(
        `${USER_PATH}/${userId}/payments/savedCards?access_token=${customerAccessToken}&cardType=${CARD_TYPE}`
      );
      const resultJson = await result.json();

      if (resultJson.errors) {
        throw new Error(resultJson.errors[0].message);
      }
      dispatch(getSavedCardSuccess(resultJson));
    } catch (e) {
      dispatch(getSavedCardFailure(e.message));
    }
  };
}

export function editSavedCardRequest() {
  return {
    type: EDIT_SAVED_CARD_REQUEST,
    status: REQUESTING
  };
}
export function editSavedCardSuccess(savedCards) {
  return {
    type: EDIT_SAVED_CARD_SUCCESS,
    status: SUCCESS,
    savedCards
  };
}

export function editSavedCardFailure(error) {
  return {
    type: EDIT_SAVED_CARD_FAILURE,
    status: ERROR,
    error
  };
}

export function editSavedCardDetails(userId, customerAccessToken) {
  return async (dispatch, getState, { api }) => {
    dispatch(editSavedCardRequest());
    try {
      const result = await api.post(
        `${USER_PATH}/${userId}/payments/savedCards?access_token=${customerAccessToken}&cardType=${CARD_TYPE}`
      );
      const resultJson = await result.json();

      if (resultJson.errors) {
        throw new Error(resultJson.errors[0].message);
      }
      dispatch(editSavedCardSuccess(resultJson));
    } catch (e) {
      dispatch(editSavedCardFailure(e.message));
    }
  };
}

export function addCardRequest() {
  return {
    type: ADD_SAVED_CARD_REQUEST,
    status: REQUESTING
  };
}
export function addCardSuccess(savedCards) {
  return {
    type: ADD_SAVED_CARD_SUCCESS,
    status: SUCCESS,
    savedCards
  };
}

export function addCardFailure(error) {
  return {
    type: ADD_SAVED_CARD_FAILURE,
    status: ERROR,
    error
  };
}

export function addCardDetails(userId, customerAccessToken) {
  return async (dispatch, getState, { api }) => {
    dispatch(addCardRequest());
    try {
      const result = await api.post(
        `${USER_PATH}/${userId}/payments/savedCards?access_token=${customerAccessToken}&cardType=${CARD_TYPE}`
      );
      const resultJson = await result.json();

      if (resultJson.errors) {
        throw new Error(resultJson.errors[0].message);
      }
      dispatch(addCardSuccess(resultJson));
    } catch (e) {
      dispatch(addCardFailure(e.message));
    }
  };
}

export function removeSavedCardRequest() {
  return {
    type: REMOVE_SAVED_CARD_REQUEST,
    status: REQUESTING
  };
}
export function removeSavedCardSuccess(savedCards) {
  return {
    type: REMOVE_SAVED_CARD_SUCCESS,
    status: SUCCESS,
    savedCards
  };
}

export function removeSavedCardFailure(error) {
  return {
    type: REMOVE_SAVED_CARD_FAILURE,
    status: ERROR,
    error
  };
}

export function removeSavedCardDetails(userId, customerAccessToken) {
  return async (dispatch, getState, { api }) => {
    dispatch(removeSavedCardRequest());
    try {
      const result = await api.post(
        `${USER_PATH}/${userId}/payments/savedCards?access_token=${customerAccessToken}&cardType=${CARD_TYPE}`
      );
      const resultJson = await result.json();

      if (resultJson.errors) {
        throw new Error(resultJson.errors[0].message);
      }
      dispatch(removeSavedCardSuccess(resultJson));
    } catch (e) {
      dispatch(removeSavedCardFailure(e.message));
    }
  };
}
