import * as searchActions from "../actions/search.actions";

const search = (
  state = {
    string: "",
    searchResult: {
      loading: false
    }
  },
  action
) => {
  switch (action.type) {
    case searchActions.SET_SEARCH_STRING:
      return Object.assign({}, state, {
        string: action.string
      });
    case searchActions.SEARCH_RESULT_REQUEST:
      return Object.assign({}, state, {
        searchResult: {
          loading: true,
          status: action.status
        }
      });
    case searchActions.SEARCH_RESULT_SUCCESS:
      return Object.assign({}, state, {
        searchResult: {
          loading: false,
          status: action.status,
          result: action.result
        }
      });
    case searchActions.SEARCH_RESULT_FAILURE:
      return Object.assign({}, state, {
        searchResult: {
          loading: false,
          status: action.status,
          error: action.error
        }
      });
    default:
      return state;
  }
};

export default search;
