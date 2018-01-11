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
      let data = _.cloneDeep(action.data);
      let homeFeedData = _.map(data, subData => {
        return _.extend({}, subData, { loading: false, data: {} });
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
      data = _.cloneDeep(state.homeFeed);
      _.each(data, (value, index) => {
        if (index === action.positionInFeed) {
          value.loading = true;
        }
      });

      return Object.assign({}, state, {
        status: action.status,
        homeFeed: data
      });

    case homeActions.COMPONENT_DATA_SUCCESS:
      data = _.cloneDeep(state.homeFeed);
      _.each(data, (value, index) => {
        if (index === action.positionInFeed) {
          value.data = action.data;
          value.loading = false;
        }
      });
      return Object.assign({}, state, {
        status: action.status,
        homeFeed: data
      });

    case homeActions.COMPONENT_DATA_FAILURE:
      data = _.cloneDeep(state.homeFeed);
      _.each(data, (value, index) => {
        if (index === action.positionInFeed) {
          value.loading = false;
        }
      });

      return Object.assign({}, state, {
        status: action.status,
        homeFeed: data
      });

    default:
      return state;
  }
};
