import * as searchActions from "../actions/search.actions";

const search = (
  state = {
    string: ""
  },
  action
) => {
  switch (action.type) {
    case searchActions.SET_SEARCH_STRING:
      return Object.assign({}, state, {
        string: action.string
      });
    default:
      return state;
  }
};

export default search;
