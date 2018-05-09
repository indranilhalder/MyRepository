import * as headerAction from "./header.actions.js";
const header = (
  state = {
    text: null,
    status: null,
    loading: false,
    error: null,
    bagCount: 0
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
    case headerAction.SET_BAG_COUNT:
      return Object.assign({}, state, {
        text: null,
        bagCount: action.bagCount
      });
    default:
      return state;
  }
};
export default header;
