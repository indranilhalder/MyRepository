// Probably rename this Feed, but no time right now.
import * as homeActions from "../actions/home.actions";
import { FOLLOW_AND_UN_FOLLOW_BRANDS_IN_HOME_FEED_SUCCESS } from "../../account/actions/account.actions";
import cloneDeep from "lodash/cloneDeep";
import map from "lodash/map";
import { PRODUCT_RECOMMENDATION_TYPE } from "../components/Feed.js";

const home = (
  state = {
    homeFeed: [], //array of objects
    status: null,
    error: null,
    loading: false,
    msdIndex: 0,
    feedType: null,
    productCapsules: null,
    productCapsulesStatus: null,
    productCapsulesLoading: null
  },
  action
) => {
  let homeFeedData, toUpdate, componentData, homeFeedClonedData;
  switch (action.type) {
    case homeActions.GET_PRODUCT_CAPSULES_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        productCapsulesLoading: true
      });
    case homeActions.GET_PRODUCT_CAPSULES_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        productCapsulesLoading: false,
        error: action.error
      });
    case homeActions.GET_PRODUCT_CAPSULES_SUCCESS:
      homeFeedClonedData = cloneDeep(state.homeFeed);
      homeFeedClonedData[action.positionInFeed].data = action.productCapsules;
      return Object.assign({}, state, {
        status: action.status,
        productCapsulesLoading: false,
        homeFeed: homeFeedClonedData
      });
    case homeActions.HOME_FEED_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true,
        feedType: action.feedType
      });

    case homeActions.HOME_FEED_SUCCESS:
      homeFeedClonedData = cloneDeep(action.data);
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
        loading: false,
        feedType: action.feedType
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

    case homeActions.COMPONENT_BACK_UP_REQUEST:
      homeFeedData = cloneDeep(state.homeFeed);
      homeFeedData[action.positionInFeed].useBackUpData = false;
      homeFeedData[action.positionInFeed].backUpLoading = true;
      return Object.assign({}, state, {
        status: action.status,
        homeFeed: homeFeedData
      });

    case homeActions.COMPONENT_BACK_UP_FAILURE:
      homeFeedData = cloneDeep(state.homeFeed);
      homeFeedData[action.positionInFeed].useBackUpData = false;
      homeFeedData[action.positionInFeed].backUpLoading = false;
      homeFeedData[action.positionInFeed].error = action.error;
      return Object.assign({}, state, {
        homeFeed: homeFeedData
      });

    case homeActions.COMPONENT_BACK_UP_SUCCESS:
      homeFeedData = cloneDeep(state.homeFeed);
      homeFeedData[action.positionInFeed].useBackUpData = false;
      homeFeedData[action.positionInFeed].backUpLoading = false;
      toUpdate = action.data[action.data.componentName];
      componentData = {
        ...homeFeedData[action.positionInFeed],
        ...toUpdate,
        backUpLoading: false,
        status: action.status,
        loading: false
      };

      homeFeedData[action.positionInFeed] = componentData;
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
    case FOLLOW_AND_UN_FOLLOW_BRANDS_IN_HOME_FEED_SUCCESS:
      homeFeedData = cloneDeep(state.homeFeed);
      const brandComponentData = homeFeedData[action.positionInFeed];
      const indexOfBrandToBeUpdate = brandComponentData.data.findIndex(
        brand => {
          return brand.id === action.brandId;
        }
      );
      brandComponentData.data[indexOfBrandToBeUpdate].isFollowing =
        action.followStatus;
      homeFeedData[action.positionInFeed] = brandComponentData;
      return Object.assign({}, state, {
        homeFeed: homeFeedData,
        status: action.status
      });
    case homeActions.COMPONENT_DATA_SUCCESS:
      if (!state.homeFeed[action.positionInFeed].useBackUpData) {
        homeFeedData = cloneDeep(state.homeFeed);
        componentData = {
          loading: false,
          status: action.status
        };
        if (!action.isMsd) {
          toUpdate = action.data[action.data.componentName];
          componentData = {
            ...homeFeedData[action.positionInFeed],
            ...toUpdate,
            ...componentData
          };
        } else {
          if (action.data.type) {
            action.data.category = action.data.type;
          }
          componentData = {
            ...action.data,
            ...homeFeedData[action.positionInFeed],
            ...componentData
          };
        }
        homeFeedData[action.positionInFeed] = componentData;
        return Object.assign({}, state, {
          status: action.status,
          homeFeed: homeFeedData
        });
      }
      break;

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
