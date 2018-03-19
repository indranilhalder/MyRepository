import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
export const GET_SAVED_CARD_REQUEST = "GET_SAVED_CARD_REQUEST";
export const GET_SAVED_CARD_SUCCESS = "GET_SAVED_CARD_SUCCESS";
export const GET_SAVED_CARD_FAILURE = "GET_SAVED_CARD_FAILURE";
export const USER_PATH = "v2/mpl/users";

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

export function getSavedCartDetails(userId, customerAccessToken) {
  console.log(userId);
  return async (dispatch, getState, { api }) => {
    dispatch(getSavedCardRequest());
    try {
      const result = await api.post(
        `${USER_PATH}/${userId}/payments/savedCards?access_token=${customerAccessToken}&cardType=BOTH`
      );
      const resultJson = await result.json();
      console.log(resultJson);
      if (resultJson.errors) {
        throw new Error(resultJson.errors[0].message);
      }
      dispatch(getSavedCardSuccess(resultJson));
    } catch (e) {
      dispatch(getSavedCardFailure(e.message));
    }
  };
}
