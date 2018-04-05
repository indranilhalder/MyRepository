import { SUCCESS, REQUESTING, FAILURE } from "../../lib/constants";

export const ALL_AUTH_CALLS_REQUEST = "ALL_AUTH_CALLS_REQUEST";
export const ALL_AUTH_CALLS_SUCCESS = "ALL_AUTH_CALLS_SUCCESS";
export const ANY_AUTH_CALLS_FAILED = "ANY_AUTH_CALLS_FAILED";

export function authCallsAreInProgress() {
  return {
    type: ALL_AUTH_CALLS_REQUEST,
    status: REQUESTING
  };
}

export function singleAuthCallHasFailed(error) {
  return {
    type: ANY_AUTH_CALLS_FAILED,
    status: FAILURE,
    error
  };
}

export function setIfAllAuthCallsHaveSucceeded() {
  return {
    type: ALL_AUTH_CALLS_SUCCESS,
    status: SUCCESS
  };
}
