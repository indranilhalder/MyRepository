import {
  FAILURE,
  FAILURE_UPPERCASE,
  ERROR,
  FAILURE_LOWERCASE
} from "../lib/constants.js";
export function getFailureResponse(response) {
  if (response.errors) {
    return { status: true, message: response.errors[0].message };
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
    response.status === FAILURE_LOWERCASE
  ) {
    if (response.error) {
      return { status: true, message: response.error };
    } else {
      return { status: true, message: response.message };
    }
  } else {
    return { status: false };
  }
}
