import user from "../reducers/user.reducer";
import * as userActions from "../actions/user.actions";
import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
let initialState, dummyUser;
beforeEach(() => {
  initialState = {
    user: null,
    error: null,
    status: null,
    loading: false
  };

  dummyUser = {
    userName: "test@gmail.com",
    password: "123456"
  };
});

const loggedInState = {
  user: dummyUser,
  error: null,
  status: null,
  loading: false
};

it("should return initial state", () => {
  expect(user(undefined, {})).toEqual({
    user: null,
    status: null,
    error: null,
    loading: false
  });
});

//Request test case

it("should call  LOGIN_USER_REQUEST", () => {
  expect(user(initialState, userActions.loginUserRequest())).toEqual({
    status: REQUESTING,
    user: null,
    error: null,
    loading: true
  });
});

it("should call  SIGN_UP_USER_REQUEST", () => {
  expect(user(initialState, userActions.signUpUserRequest())).toEqual({
    status: REQUESTING,
    user: null,
    error: null,
    loading: true
  });
});

it("should call  OTP_VERIFICATION_REQUEST", () => {
  expect(user(initialState, userActions.otpVerificationRequest())).toEqual({
    status: REQUESTING,
    user: null,
    error: null,
    loading: true
  });
});

//Failure test case
it("Should call LOGIN_USER_FAILURE", () => {
  expect(
    user(initialState, userActions.loginUserFailure("Login Failed"))
  ).toEqual({
    status: ERROR,
    error: "Login Failed",
    user: null,
    loading: false
  });
});

it("Should call SIGN_UP_USER_FAILURE", () => {
  expect(
    user(initialState, userActions.signUpUserFailure("Sign Up Failed"))
  ).toEqual({
    status: ERROR,
    error: "Sign Up Failed",
    user: null,
    loading: false
  });
});

it("Should call OTP_VERIFICATION_FAILURE", () => {
  expect(
    user(initialState, userActions.otpVerificationFailure("Otp Failed"))
  ).toEqual({
    status: ERROR,
    error: "Otp Failed",
    user: null,
    loading: false
  });
});

//Success test case
it("should call LOGIN_USER_SUCCESS ", () => {
  expect(user(initialState, userActions.loginUserSuccess(dummyUser))).toEqual({
    user: dummyUser,
    status: SUCCESS,
    error: null,
    loading: false
  });
});

it("should call SIGN_UP_USER_SUCCESS ", () => {
  expect(user(initialState, userActions.signUpUserSuccess())).toEqual({
    status: SUCCESS,
    error: null,
    loading: false,
    user: null
  });
});

it("should call OTP_VERIFICATION_SUCCESS ", () => {
  expect(
    user(initialState, userActions.otpVerificationSuccess(dummyUser))
  ).toEqual({
    status: SUCCESS,
    error: null,
    loading: false,
    user: dummyUser
  });
});
