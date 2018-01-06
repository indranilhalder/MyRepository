import * as userActions from "../actions/user.actions";

const user = (
  state = {
    user: null,
    status: null,
    error: null,
    loading: false,
    message: null
  },
  action
) => {
  switch (action.type) {
    case userActions.LOGIN_USER_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case userActions.LOGIN_USER_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        user: action.user,
        loading: false
      });
    case userActions.LOGIN_USER_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case userActions.SIGN_UP_USER_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case userActions.SIGN_UP_USER_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        loading: false
      });
    case userActions.SIGN_UP_USER_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case userActions.OTP_VERIFICATION_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case userActions.OTP_VERIFICATION_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        user: action.user,
        loading: false
      });

    case userActions.OTP_VERIFICATION_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case userActions.FORGOT_PASSWORD_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case userActions.FORGOT_PASSWORD_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        loading: false,
        message: action.message
      });

    case userActions.FORGOT_PASSWORD_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        loading: false,
        error: action.error
      });

    case userActions.FORGOT_PASSWORD_OTP_VERIFICATION_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case userActions.FORGOT_PASSWORD_OTP_VERIFICATION_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        loading: false,
        message: action.message
      });

    case userActions.FORGOT_PASSWORD_OTP_VERIFICATION_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        loading: false,
        error: action.error
      });

    case userActions.RESET_PASSWORD_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case userActions.RESET_PASSWORD_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        loading: false,
        message: action.message
      });

    case userActions.RESET_PASSWORD_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        loading: false,
        error: action.error
      });

    default:
      return state;
  }
};

export default user;
