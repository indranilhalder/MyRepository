import * as categoriesActions from "../actions/brand.actions";
const brand = (
  state = {
    status: null,
    error: null,
    categories: null
  },
  action
) => {
  switch (action.type) {
    case categoriesActions.GET_ALL_BRANDS_STORE_REQUEST:
      return Object.assign({}, state, {
        status: action.status
      });
    case categoriesActions.GET_ALL_BRANDS_STORE_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error
      });
    case categoriesActions.GET_ALL_BRANDS_STORE_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        brandsStores: action.brandsStores
      });
    default:
      return state;
  }
};
export default brand;
