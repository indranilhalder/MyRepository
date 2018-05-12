import {
  FAILURE,
  FAILURE_UPPERCASE,
  ERROR,
  FAILURE_LOWERCASE
} from "../lib/constants.js";
const UN_ABLE_TO_UPDATE_MASTER = "Unable to validate Otp";
export function getFailureResponse(response) {
  if (response.errors) {
    if (response.errors[0].message) {
      return { status: true, message: response.errors[0].message };
    }

    if (response.errors[0].type) {
      return { status: true, message: response.errors[0].type };
    }
  }
  if (response.error) {
    return { status: true, message: response.error };
  }
  if (response.error_message) {
    return { status: true, message: response.error_message };
  }
  if (
    response.status === FAILURE ||
    response.status === FAILURE_UPPERCASE ||
    response.status === ERROR ||
    response.status === FAILURE_LOWERCASE ||
    response.status === UN_ABLE_TO_UPDATE_MASTER
  ) {
    if (response.error) {
      return { status: true, message: response.error };
    } else if (response.message) {
      return { status: true, message: response.message };
    } else {
      return { status: true, message: response.status };
    }
  }
  if (response.errorCode) {
    return { status: true, message: "An exception occurred at back-end." };
  } else {
    return { status: false };
  }
}
