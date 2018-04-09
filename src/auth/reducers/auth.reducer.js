import * as authActions from "../actions/auth.actions";
const auth = (
  state = {
    authCallsInProcess: false,
    authCallsIsSucceed: false,
    error: null
  },
  action
) => {
  switch (action.type) {
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
    default:
      return state;
  }
};
export default auth;
