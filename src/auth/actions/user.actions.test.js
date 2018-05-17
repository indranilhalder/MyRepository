import * as api from "../../lib/apiRequest";
import configureMockStore from "redux-mock-store";
import * as userActions from "./user.actions.js";
import thunk from "redux-thunk";
import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import * as user from "../mocks/user.mock";
import * as Cookie from "../../lib/Cookie";
const SIGN_UP_PATH = `v2/mpl/users/customerRegistration?access_token=d2470a48-e71e-41b7-b6b2-a083af3d8c08&isPwa=true&username=test@xelpmoc.in&password=123456&platformNumber=2`;
const LOGIN_PATH =
  "v2/mpl/users/test@xelpmoc.in/customerLogin?access_token=d2470a48-e71e-41b7-b6b2-a083af3d8c08";
const OTP_VERIFICATION_PATH =
  "/v2/mpl/users/registrationOTPVerification?access_token=d2470a48-e71e-41b7-b6b2-a083af3d8c08&otp=[object Object]&isPwa=true&platformNumber=2&username=undefined&password=undefined";
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
      isLoggedIn: false,
      scrollPosition: 0
    };
  });

  Cookie.createCookie(CUSTOMER_ACCESS_TOKEN, JSON.stringify(user.userDetails));
  Cookie.createCookie(GLOBAL_ACCESS_TOKEN, JSON.stringify(user.userDetails));

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
      postFormData: postMock
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
        type: userActions.LOGIN_USER_SUCCESS,
        user: userMock,
        status: SUCCESS,
        userName: "test@xelpmoc.in",
        scrollPosition: 0
      }
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
      postFormData: postMock
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
        error: user.userDetailsFailure.status
      }
    ];

    return store.dispatch(userActions.loginUser(inputDetails)).then(() => {
      // expect(store.getActions()).toEqual(expectedActions);
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
      post: postMock
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
        type: SHOW_MODAL,
        scrollPosition: 0
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
    post: postMock
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
