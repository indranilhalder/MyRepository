import * as icidActions from "./icid.actions";
const icid = (
  state = {
    value: null
  },
  action
) => {
  switch (action.type) {
    case icidActions.SET_ICID:
      return Object.assign({}, state, {
        value: action.icid
      });
    case icidActions.CLEAR_ICID:
      return Object.assign({}, state, {
        value: null
      });
    default:
      return state;
  }
};

export default icid;
