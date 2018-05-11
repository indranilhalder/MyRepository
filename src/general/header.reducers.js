import * as headerAction from "./header.actions.js";
import { CART_BAG_DETAILS } from "../lib/constants";
let bagCount = 0;
if (
  localStorage.getItem(CART_BAG_DETAILS) &&
  JSON.parse(localStorage.getItem(CART_BAG_DETAILS))
) {
  bagCount = JSON.parse(localStorage.getItem(CART_BAG_DETAILS)).length;
}
const header = (
  state = {
    text: null,
    status: null,
    loading: false,
    error: null,
    bagCount
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
        bagCount: action.bagCount
      });
    default:
      return state;
  }
};
export default header;
