import * as toastActions from "./toast.actions.js";
const toast = (
  state = {
    toastDisplayed: false,
    toastType: null
  },
  action
) => {
  switch (action.type) {
    case toastActions.SHOW_TOAST:
      return Object.assign({}, state, {
        toastDisplayed: true,
        toastMessage: action.message
      });
    case toastActions.HIDE_TOAST:
      return Object.assign({}, state, {
        toastDisplayed: false,
        toastMessage: null
      });
    default:
      return state;
  }
};
export default toast;
