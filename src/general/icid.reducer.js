import * as icidActions from "./icid.actions";
const icid = (
  state = {
    value: null,
    icidType: null
  },
  action
) => {
  switch (action.type) {
    case icidActions.SET_ICID:
      return Object.assign({}, state, {
        value: action.icid,
        icidType: action.icidType
      });
    case icidActions.CLEAR_ICID:
      return Object.assign({}, state, {
        value: null,
        icidType: null
      });
    default:
      return state;
  }
};

export default icid;
