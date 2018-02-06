import * as api from "../../lib/apiRequest";
import configureMockStore from "redux-mock-store";
import * as userActions from "./user.actions.js";
import thunk from "redux-thunk";
import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import * as user from "../mocks/user.mock";
import * as Cookie from "../../lib/Cookie";
const SIGN_UP_PATH =
  "v2/mpl/users/customerRegistration?access_token=undefined&isPwa=true&username=undefined&password=123456&platformNumber=2";
const LOGIN_PATH =
  "v2/mpl/users/test@xelpmoc.in/customerLogin?access_token=undefined&password=123456&isPwa=true";
const OTP_VERIFICATION_PATH =
  "/v2/mpl/users/registrationOTPVerification?access_token=undefined&otp=[object Object]&isPwa=true&platformNumber=2&username=undefined&password=undefined";
import {
  GLOBAL_ACCESS_TOKEN,
  CUSTOMER_ACCESS_TOKEN
} from "../../lib/constants";
import {
  SIGN_UP_OTP_VERIFICATION,
  SHOW_MODAL,
  HIDE_MODAL
} from "../../general/modal.actions";
import "../mocks/localStorage.mock";

let userMock,
  inputDetails,
  apiMock,
  initialState,
  mockStore,
  postMock,
  middleWares;

describe("User Actions", () => {
  beforeEach(() => {
    userMock = user.userDetails;
    inputDetails = {};
    inputDetails.username = "test@xelpmoc.in";
    inputDetails.password = "123456";

    initialState = {
      user: null,
      status: null,
      error: null,
      isLoggedIn: false
    };
  });

  Cookie.createCookie(
    CUSTOMER_ACCESS_TOKEN,
    JSON.stringify(user.userDetails.access_token)
  );
  Cookie.createCookie(
    GLOBAL_ACCESS_TOKEN,
    JSON.stringify(user.userDetails.access_token)
  );

  //Login Test Case
  it("LOG_IN", () => {
    postMock = jest.fn();
    const loginResponse = userMock;

    const result = {
      status: SUCCESS,
      json: () => loginResponse
    };

    postMock.mockReturnValueOnce(result);

    apiMock = {
      postMock: postMock
    };

    middleWares = [
      thunk.withExtraArgument({
        api: apiMock
      })
    ];
    mockStore = configureMockStore(middleWares);
    const store = mockStore(initialState);

    const expectedActions = [
      { type: userActions.LOGIN_USER_REQUEST, status: REQUESTING },
      { type: userActions.LOGIN_USER_SUCCESS, user: userMock, status: SUCCESS }
    ];

    return store.dispatch(userActions.loginUser(inputDetails)).then(() => {
      expect(store.getActions()).toEqual(expectedActions);
      expect(postMock.mock.calls.length).toBe(1);
      expect(postMock.mock.calls[0][0]).toBe(LOGIN_PATH);
    });
  });

  it("LOG_IN_FAILURE", () => {
    postMock = jest.fn();
    const loginResponse = user.userDetailsFailure;

    const result = {
      status: ERROR,
      json: () => loginResponse
    };

    postMock.mockReturnValueOnce(result);

    apiMock = {
      postMock: postMock
    };

    middleWares = [
      thunk.withExtraArgument({
        api: apiMock
      })
    ];
    mockStore = configureMockStore(middleWares);
    const store = mockStore(initialState);
    const expectedActions = [
      { type: userActions.LOGIN_USER_REQUEST, status: REQUESTING },
      {
        type: userActions.LOGIN_USER_FAILURE,
        status: ERROR,
        error: user.userDetailsFailure.message
      }
    ];

    return store.dispatch(userActions.loginUser(inputDetails)).then(() => {
      expect(store.getActions()).toEqual(expectedActions);
      expect(postMock.mock.calls.length).toBe(1);
      expect(postMock.mock.calls[0][0]).toBe(LOGIN_PATH);
    });
  });

  //Sign Up Test Case
  it("SIGN_UP", () => {
    postMock = jest.fn();
    const signUpResponse = "";

    const result = {
      status: SUCCESS,
      json: () => signUpResponse
    };

    postMock.mockReturnValueOnce(result);

    apiMock = {
      postMock: postMock
    };

    middleWares = [
      thunk.withExtraArgument({
        api: apiMock
      })
    ];
    mockStore = configureMockStore(middleWares);
    const store = mockStore(initialState);

    const expectedActions = [
      { type: userActions.SIGN_UP_USER_REQUEST, status: REQUESTING },
      {
        modalType: SIGN_UP_OTP_VERIFICATION,
        ownProps: { password: "123456", username: "test@xelpmoc.in" },
        type: SHOW_MODAL
      },
      {
        type: userActions.SIGN_UP_USER_SUCCESS,
        status: SUCCESS
      }
    ];

    return store.dispatch(userActions.signUpUser(inputDetails)).then(() => {
      expect(store.getActions()).toEqual(expectedActions);
      expect(postMock.mock.calls.length).toBe(1);
      expect(postMock.mock.calls[0][0]).toBe(SIGN_UP_PATH);
    });
  });
});

it("SIGN_UP_FAILURE", () => {
  postMock = jest.fn();
  const signUpResponse = user.userDetailsLoginFailure;

  const result = {
    status: ERROR,
    json: () => signUpResponse
  };

  postMock.mockReturnValueOnce(result);

  apiMock = {
    postMock: postMock
  };

  middleWares = [
    thunk.withExtraArgument({
      api: apiMock
    })
  ];
  mockStore = configureMockStore(middleWares);
  const store = mockStore(initialState);

  const expectedActions = [
    { type: userActions.SIGN_UP_USER_REQUEST, status: REQUESTING },
    {
      type: userActions.SIGN_UP_USER_FAILURE,
      status: ERROR,
      error: user.userDetailsLoginFailure.message
    }
  ];

  return store.dispatch(userActions.signUpUser(inputDetails)).then(() => {
    expect(store.getActions()).toEqual(expectedActions);
    expect(postMock.mock.calls.length).toBe(1);
    expect(postMock.mock.calls[0][0]).toBe(SIGN_UP_PATH);
  });
});

//Otp Verification Test Case
it("OTP_VERIFICATION", () => {
  postMock = jest.fn();
  const otpResponse = userMock;

  const result = {
    status: SUCCESS,
    json: () => otpResponse
  };

  postMock.mockReturnValueOnce(result);

  apiMock = {
    postMock: postMock
  };

  middleWares = [
    thunk.withExtraArgument({
      api: apiMock
    })
  ];
  mockStore = configureMockStore(middleWares);
  const store = mockStore(initialState);

  const expectedActions = [
    { type: userActions.OTP_VERIFICATION_REQUEST, status: REQUESTING },
    {
      modalType: null,
      type: HIDE_MODAL
    },
    {
      type: userActions.OTP_VERIFICATION_SUCCESS,
      user: userMock,
      status: SUCCESS
    }
  ];

  return store
    .dispatch(userActions.otpVerification(inputDetails, user.userDetails))
    .then(() => {
      expect(store.getActions()).toEqual(expectedActions);
      expect(postMock.mock.calls.length).toBe(1);
      expect(postMock.mock.calls[0][0]).toBe(OTP_VERIFICATION_PATH);
    });
});

it("OTP_VERIFICATION_FAILURE", () => {
  postMock = jest.fn();
  const otpVerificationResponse = user.userDetailsLoginFailure;

  const result = {
    status: ERROR,
    json: () => otpVerificationResponse
  };

  postMock.mockReturnValueOnce(result);

  apiMock = {
    postMock: postMock
  };

  middleWares = [
    thunk.withExtraArgument({
      api: apiMock
    })
  ];
  mockStore = configureMockStore(middleWares);
  const store = mockStore(initialState);
  const expectedActions = [
    { type: userActions.OTP_VERIFICATION_REQUEST, status: REQUESTING },

    {
      type: userActions.OTP_VERIFICATION_FAILURE,
      status: ERROR,
      error: user.userDetailsLoginFailure.message
    }
  ];

  return store
    .dispatch(userActions.otpVerification(inputDetails, ""))
    .then(() => {
      expect(store.getActions()).toEqual(expectedActions);
      expect(postMock.mock.calls.length).toBe(1);
      expect(postMock.mock.calls[0][0]).toBe(OTP_VERIFICATION_PATH);
    });
});
