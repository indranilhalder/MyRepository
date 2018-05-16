import * as authActions from "../actions/auth.actions";
import { CLEAR_ERROR } from "../../general/error.actions";
const auth = (
  state = {
    authCallsInProcess: false,
    authCallsIsSucceed: false,
    error: null,
    redirectToAfterAuthUrl: null
  },
  action
) => {
  switch (action.type) {
    case CLEAR_ERROR:
      return Object.assign({}, state, {
        error: null
      });
    case authActions.SET_URL_TO_REDIRECT_TO_AFTER_AUTH:
      return Object.assign({}, state, {
        redirectToAfterAuthUrl: action.url
      });
    case authActions.CLEAR_URL_TO_REDIRECT_TO_AFTER_AUTH:
      return Object.assign({}, state, {
        redirectToAfterAuthUrl: null
      });
    case authActions.ALL_AUTH_CALLS_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        authCallsInProcess: true,
        authCallsIsSucceed: false
      });
    case authActions.ANY_AUTH_CALLS_FAILED:
      return Object.assign({}, state, {
        status: action.status,
        authCallsInProcess: false,
        error: action.error
      });

    case authActions.ALL_AUTH_CALLS_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        authCallsInProcess: false,
        authCallsIsSucceed: true
      });
    case authActions.SET_FALSE_ALL_AUTH_CALLS_SUCCESS_FLAG:
      return Object.assign({}, state, {
        authCallsIsSucceed: false
      });
    case authActions.STOP_LOADING_ON_LOGIN:
      return Object.assign({}, state, {
        authCallsInProcess: false
      });
    default:
      return state;
  }
};
export default auth;
