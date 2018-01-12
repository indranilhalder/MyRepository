import * as homeActions from "../actions/home.actions";
import _ from "lodash";

const home = (
  state = {
    homeFeed: [],
    status: null,
    error: null,
    loading: false
  },
  action
) => {
  switch (action.type) {
    case homeActions.HOME_FEED_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case homeActions.HOME_FEED_SUCCESS:
      let homeFeedClonedData = _.cloneDeep(action.data);
      let homeFeedData = _.map(homeFeedClonedData, subData => {
        return _.extend({}, subData, { loading: false, data: {}, status: "" });
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

    case homeActions.COMPONENT_DATA_REQUEST:
      homeFeedData = _.cloneDeep(state.homeFeed);
      homeFeedData[action.positionInFeed].loading = true;
      return Object.assign({}, state, {
        status: action.status,
        homeFeed: homeFeedData
      });

    case homeActions.COMPONENT_DATA_SUCCESS:
      homeFeedData = _.cloneDeep(state.homeFeed);
      homeFeedData[action.positionInFeed].data = action.data;
      homeFeedData[action.positionInFeed].loading = false;
      return Object.assign({}, state, {
        status: action.status,
        homeFeed: homeFeedData
      });

    case homeActions.COMPONENT_DATA_FAILURE:
      homeFeedData = _.cloneDeep(state.homeFeed);
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
