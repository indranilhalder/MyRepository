import * as headerAction from "./header.actions.js";
const header = (
  state = {
    text: null,
    status: null,
    loading: false,
    error: null
  },
  action
) => {
  switch (action.type) {
    case headerAction.SET_HEADER_TEXT:
      return Object.assign({}, state, {
        text: action.text,
        status: action.status
      });
    case headerAction.CLEAR_HEADER_TEXT:
      return Object.assign({}, state, {
        text: null,
        status: action.status
      });
    default:
      return state;
  }
};
export default header;
