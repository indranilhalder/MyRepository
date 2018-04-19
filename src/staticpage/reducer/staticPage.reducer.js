import * as staticPageActions from "../actions/staticPage.actions";
import cloneDeep from "lodash.clonedeep";
import map from "lodash.map";
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
      const staticPageData = action.data;
      const items = map(action.data.items, item => {
        return {
          ...item[item.componentName]
        };
      });

      staticPageData.items = items;
      return Object.assign({
        status: action.status,
        loading: false,
        data: staticPageData
      });
    default:
      return state;
  }
};
export default staticPage;
