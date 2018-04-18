import * as staticPageActions from "../actions/staticPage.actions";
const staticPage = (
  state = {
    status: null,
    error: null,
    loading: false,
    type: null,
    data: null
  },
  action
) => {
  switch (action.type) {
    case staticPageActions.GET_STATIC_PAGE_REQUEST:
      return Object.assign({
        status: action.status,
        loading: true
      });
    case staticPageActions.GET_STATIC_PAGE_FAILURE:
      return Object.assign({
        status: action.status,
        loading: false,
        error: action.error
      });
    case staticPageActions.GET_STATIC_PAGE_SUCCESS:
      return Object.assign({
        status: action.status,
        loading: false,
        data: action.data
      });
    default:
      return state;
  }
};
export default staticPage;
