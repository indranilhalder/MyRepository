import * as categoriesActions from "../actions/blp.actions";
const brandDefault = (
  state = {
    status: null,
    error: null,
    categories: null,
    loading: false
  },
  action
) => {
  switch (action.type) {
    case categoriesActions.GET_ALL_BRANDS_STORE_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });
    case categoriesActions.GET_ALL_BRANDS_STORE_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });
    case categoriesActions.GET_ALL_BRANDS_STORE_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        brandsStores: action.brandsStores,
        loading: false
      });
    default:
      return state;
  }
};
export default brandDefault;
