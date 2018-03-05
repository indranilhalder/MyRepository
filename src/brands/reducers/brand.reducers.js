import * as brandActions from "../actions/brand.actions";
const brandDetails = (
  state = { brandDetails: null, status: null, error: null, isLoading: false },
  action
) => {
  switch (action.type) {
    case brandActions.GET_BRAND_DETAIL_REQUEST:
      return Object.assign({}, state, {
        status: action.status
      });
    case brandActions.GET_BRAND_DETAIL_SUCCESS:
      return Object.assign({}, state, {
        brandDetails: action.brandDetails,
        status: action.status
      });
    case brandActions.GET_BRAND_DETAIL_FAILURE:
      return Object.assign({}, state, {
        brandDetails: action.brandDetails,
        status: action.status
      });
    default:
      return state;
  }
};
export default brandDetails;
