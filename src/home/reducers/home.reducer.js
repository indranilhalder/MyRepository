import * as homeActions from "../actions/home.actions";
import cloneDeep from "lodash/cloneDeep";
import map from "lodash/map";
import { PRODUCT_RECOMMENDATION_TYPE } from "../components/Feed.js";

const home = (
  state = {
    homeFeed: [], //array of objects
    status: null,
    error: null,
    loading: false,
    msdIndex: 0
  },
  action
) => {
  let homeFeedData;
  switch (action.type) {
    case homeActions.HOME_FEED_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case homeActions.HOME_FEED_SUCCESS:
      const homeFeedClonedData = cloneDeep(action.data);
      homeFeedData = map(homeFeedClonedData, subData => {
        // we do this because TCS insists on having the data that backs a component have an object that wraps the data we care about.
        return {
          ...subData[subData.componentName],
          loading: false,
          status: ""
        };
      });

      return Object.assign({}, state, {
        status: action.status,
        homeFeed: homeFeedData,
        loading: false
      });
    case homeActions.HOME_FEED_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case homeActions.SINGLE_SELECT_REQUEST:
    case homeActions.MULTI_SELECT_SUBMIT_REQUEST:
      homeFeedData = cloneDeep(state.homeFeed);
      homeFeedData[action.positionInFeed].submitLoading = true;
      return Object.assign({}, state, {
        status: action.status,
        homeFeed: homeFeedData
      });
    case homeActions.SINGLE_SELECT_FAILURE:
    case homeActions.MULTI_SELECT_SUBMIT_FAILURE:
      homeFeedData = cloneDeep(state.homeFeed);
      homeFeedData[action.positionInFeed].submitLoading = false;

      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        homeFeed: homeFeedData
      });

    case homeActions.SINGLE_SELECT_SUCCESS:
    case homeActions.MULTI_SELECT_SUBMIT_SUCCESS:
      homeFeedData = cloneDeep(state.homeFeed);
      homeFeedData[action.positionInFeed].submitLoading = false;
      homeFeedData[action.positionInFeed].type = PRODUCT_RECOMMENDATION_TYPE;
      homeFeedData[action.positionInFeed].data = action.data;
      return Object.assign({}, state, {
        status: action.status,
        homeFeed: homeFeedData
      });

    case homeActions.COMPONENT_DATA_REQUEST:
      homeFeedData = cloneDeep(state.homeFeed);
      homeFeedData[action.positionInFeed].loading = true;
      return Object.assign({}, state, {
        status: action.status,
        homeFeed: homeFeedData
      });

    case homeActions.GET_ITEMS_SUCCESS:
      homeFeedData = cloneDeep(state.homeFeed);
      homeFeedData[action.positionInFeed].items = action.items;
      return Object.assign({}, state, {
        homeFeed: homeFeedData,
        status: action.status
      });

    case homeActions.COMPONENT_DATA_SUCCESS:
      homeFeedData = cloneDeep(state.homeFeed);
      let componentData;
      let key = null;
      componentData = {
        loading: false,
        status: action.status
      };
      let toUpdate;

      if (!action.isMsd) {
        key = Object.keys(action.data)[0];
        toUpdate = action.data[key];
        componentData = {
          ...homeFeedData[action.positionInFeed],
          ...toUpdate,
          ...componentData
        };
      } else {
        componentData = {
          ...homeFeedData[action.positionInFeed],
          data: action.data,
          ...componentData
        };
      }

      homeFeedData[action.positionInFeed] = componentData;
      return Object.assign({}, state, {
        status: action.status,
        homeFeed: homeFeedData
      });

    case homeActions.COMPONENT_DATA_FAILURE:
      homeFeedData = cloneDeep(state.homeFeed);
      homeFeedData[action.positionInFeed].loading = false;
      homeFeedData[action.positionInFeed].status = action.error;

      return Object.assign({}, state, {
        status: action.status,
        homeFeed: homeFeedData
      });

    default:
      return state;
  }
};

export default home;
