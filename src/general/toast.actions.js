import delay from "lodash.delay";
export const TOAST_DELAY = 2000;
export const SHOW_TOAST = "SHOW_TOAST";
export const HIDE_TOAST = "HIDE_MODAL";
export function hideToast() {
  return {
    type: HIDE_TOAST
  };
}
export function showToast(message) {
  return {
    type: SHOW_TOAST,
    message
  };
}
export function displayToast(message) {
  return async dispatch => {
    dispatch(showToast(message));
    delay(() => {
      dispatch(hideToast());
    }, TOAST_DELAY);
  };
}
