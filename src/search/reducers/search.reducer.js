import * as searchActions from "../actions/search.actions";

const search = (
  state = {
    string: "",
    filters: [],
    sort: ""
  },
  action
) => {
  switch (action.type) {
    case searchActions.SET_SEARCH_STRING:
      return Object.assign({}, state, {
        string: action.searchString
      });
    case searchActions.SET_FILTERS:
      return Object.assign({}, state, {
        filters: action.filters
      });
    case searchActions.SET_SORT:
      return Object.assign({}, state, {
        sort: action.sort
      });
    default:
      return state;
  }
};

export default search;
