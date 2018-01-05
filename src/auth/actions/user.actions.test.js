import * as api from "../../lib/apiRequest";
import configureMockStore from "redux-mock-store";
import * as userActions from "./user.actions.js";
import thunk from "redux-thunk";
import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import * as user from "../mocks/user.mock";
import "../mocks/localStorage.mock";
let userMock,
  inputDetails,
  apiMock,
  initialState,
  mockStore,
  putMock,
  postMock,
  middlewares;

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

  it("LOG_IN", () => {
    postMock = jest.fn();
    const loginResponse = userMock;

    const result = {
      status: SUCCESS,
      json: () => loginResponse
    };

    postMock.mockReturnValueOnce(result);

    apiMock = {
      post: postMock
    };

    middlewares = [
      thunk.withExtraArgument({
        api: apiMock
      })
    ];
    mockStore = configureMockStore(middlewares);
    const store = mockStore(initialState);

    const expectedActions = [
      { type: userActions.LOGIN_USER_REQUEST, status: REQUESTING },
      { type: userActions.LOGIN_USER_SUCCESS, user: userMock, status: SUCCESS }
    ];

    return store.dispatch(userActions.loginUser(inputDetails)).then(() => {
      expect(localStorage.getItem("authorizationKey")).toEqual(
        "d2470a48-e71e-41b7-b6b2-a083af3d8c08"
      );
      expect(store.getActions()).toEqual(expectedActions);
      expect(postMock.mock.calls.length).toBe(1);
      expect(postMock.mock.calls[0][0]).toBe(userActions.LOGIN);
    });
  });

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

    middlewares = [
      thunk.withExtraArgument({
        api: apiMock
      })
    ];
    mockStore = configureMockStore(middlewares);
    const store = mockStore(initialState);

    const expectedActions = [
      { type: userActions.SIGN_UP_USER_REQUEST, status: REQUESTING },
      {
        type: userActions.SIGN_UP_USER_SUCCESS,
        status: SUCCESS
      }
    ];

    return store.dispatch(userActions.signUpUser(inputDetails)).then(() => {
      expect(localStorage.getItem("authorizationKey")).toEqual(
        "d2470a48-e71e-41b7-b6b2-a083af3d8c08"
      );
      expect(store.getActions()).toEqual(expectedActions);
      expect(postMock.mock.calls.length).toBe(1);
      expect(postMock.mock.calls[0][0]).toBe(userActions.SIGN_UP);
    });
  });
});

it("OTP_VERIFICATION", () => {
  postMock = jest.fn();
  const otpResponse = userMock;

  const result = {
    status: SUCCESS,
    json: () => otpResponse
  };

  postMock.mockReturnValueOnce(result);

  apiMock = {
    post: postMock
  };

  middlewares = [
    thunk.withExtraArgument({
      api: apiMock
    })
  ];
  mockStore = configureMockStore(middlewares);
  const store = mockStore(initialState);

  const expectedActions = [
    { type: userActions.OTP_VERIFICATION_REQUEST, status: REQUESTING },
    {
      type: userActions.OTP_VERIFICATION_SUCCESS,
      user: userMock,
      status: SUCCESS
    }
  ];

  return store.dispatch(userActions.otpVerification(inputDetails)).then(() => {
    expect(localStorage.getItem("authorizationKey")).toEqual(
      "d2470a48-e71e-41b7-b6b2-a083af3d8c08"
    );
    expect(store.getActions()).toEqual(expectedActions);
    expect(postMock.mock.calls.length).toBe(1);
    expect(postMock.mock.calls[0][0]).toBe(userActions.OTP_VERIFICATION);
  });
});
