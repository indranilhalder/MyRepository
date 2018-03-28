import {
  SUCCESS,
  REQUESTING,
  ERROR,
  FAILURE,
  BLP_OR_CLP_FEED_TYPE,
  HOME_FEED_TYPE
} from "../../lib/constants";
import each from "lodash/each";
import delay from "lodash/delay";
import {
  MSD_NUM_RESULTS,
  MSD_WIDGET_LIST,
  MSD_WIDGET_PLATFORM,
  MSD_API_KEY
} from "../../lib/config.js";
import { getMcvId } from "../../lib/adobeUtils.js";

export const HOME_FEED_REQUEST = "HOME_FEED_REQUEST";
export const HOME_FEED_SUCCESS = "HOME_FEED_SUCCESS";
export const HOME_FEED_FAILURE = "HOME_FEED_FAILURE";
export const HOME_FEED_NULL_DATA_SUCCESS = "HOME_FEED_NULL_DATA_SUCCESS";
export const COMPONENT_DATA_REQUEST = "COMPONENT_DATA_REQUEST";
export const COMPONENT_DATA_SUCCESS = "COMPONENT_DATA_SUCCESS";
export const COMPONENT_DATA_FAILURE = "COMPONENT_DATA_FAILURE";
export const COMPONENT_BACK_UP_REQUEST = "COMPONENT_BACK_UP_REQUEST";
export const COMPONENT_BACK_UP_SUCCESS = "COMPONENT_BACK_UP_SUCCESS";
export const COMPONENT_BACK_UP_FAILURE = "COMPONENT_BACK_UP_FAILURE";
export const SINGLE_SELECT_REQUEST = "SINGLE_SELECT_REQUEST";
export const SINGLE_SELECT_SUCCESS = "SINGLE_SELECT_SUCCESS";
export const SINGLE_SELECT_FAILURE = "SINGLE_SELECT_FAILURE";
export const MULTI_SELECT_SUBMIT_REQUEST = "MULTI_SELECT_SUBMIT_REQUEST";
export const MULTI_SELECT_SUBMIT_SUCCESS = "MULTI_SELECT_SUBMIT_SUCCESS";
export const MULTI_SELECT_SUBMIT_FAILURE = "MULTI_SELECT_SUBMIT_FAILURE";
export const HOME_FEED_PATH = "homepage";
export const SINGLE_SELECT_SUBMIT_PATH = "submitSingleSelectQuestion";
export const MULTI_SELECT_SUBMIT_PATH = "submitMultiSelectQuestion";
export const GET_ITEMS_REQUEST = "GET_SALE_ITEMS_REQUEST";
export const GET_ITEMS_SUCCESS = "GET_SALE_ITEMS_SUCCESS";
export const GET_ITEMS_FAILURE = "GET_SALE_ITEMS_FAILURE";

const ADOBE_TARGET_DELAY = 1500;

const ADOBE_TARGET_HOME_FEED_MBOX_NAME = "mboxPOCTest1"; // for local/devxelp/uat2tmpprod
const ADOBE_TARGET_PRODUCTION_HOME_FEED_MBOX_NAME = "PROD_Mobile_Homepage_Mbox";
const ADOBE_TARGET_P2_HOME_FEED_MBOX_NAME = "UAT_Mobile_Homepage_Mbox";

export function getItemsRequest(positionInFeed) {
  return {
    type: GET_ITEMS_REQUEST,
    positionInFeed,
    status: REQUESTING
  };
}
export function getItemsSuccess(positionInFeed, items) {
  return {
    type: GET_ITEMS_SUCCESS,
    status: SUCCESS,
    items,
    positionInFeed
  };
}
export function getItemsFailure(positionInFeed, errorMsg) {
  return {
    type: GET_ITEMS_FAILURE,
    errorMsg,
    status: FAILURE
  };
}
export function getItems(positionInFeed, itemIds, isPdp: false) {
  return async (dispatch, getState, { api }) => {
    dispatch(getItemsRequest(positionInFeed));
    try {
      let productCodes;
      each(itemIds, itemId => {
        productCodes = `${itemId},${productCodes}`;
      });
      const url = `v2/mpl/products/productInfo?productCodes=${productCodes}`;
      const result = await api.get(url);
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(getItemsSuccess(positionInFeed, resultJson.results));
    } catch (e) {
      dispatch(getItemsFailure(positionInFeed, e.message));
    }
  };
}
export function multiSelectSubmitRequest(positionInFeed) {
  return {
    type: MULTI_SELECT_SUBMIT_REQUEST,
    status: REQUESTING,
    positionInFeed
  };
}
export function multiSelectSubmitFailure(positionInFeed, errorMsg) {
  return {
    type: MULTI_SELECT_SUBMIT_FAILURE,
    status: ERROR,
    error: errorMsg
  };
}
export function multiSelectSubmitSuccess(resultJson, positionInFeed) {
  return {
    type: MULTI_SELECT_SUBMIT_SUCCESS,
    status: SUCCESS,
    data: resultJson,
    positionInFeed
  };
}
export function multiSelectSubmit(values, questionId, positionInFeed) {
  return async (dispatch, getState, { api }) => {
    dispatch(multiSelectSubmitRequest(positionInFeed));
    try {
      const result = await api.postMock(SINGLE_SELECT_SUBMIT_PATH, {
        optionId: values,
        questionId
      });
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(multiSelectSubmitSuccess(resultJson, positionInFeed));
    } catch (e) {
      dispatch(multiSelectSubmitFailure(e.message, positionInFeed));
    }
  };
}
export function singleSelectRequest(positionInFeed) {
  return {
    type: SINGLE_SELECT_REQUEST,
    status: REQUESTING,
    positionInFeed
  };
}
export function singleSelectFailure(error, positionInFeed) {
  return {
    type: SINGLE_SELECT_FAILURE,
    status: ERROR,
    error,
    positionInFeed
  };
}
export function singleSelectSuccess(resultJson, positionInFeed) {
  return {
    type: SINGLE_SELECT_SUCCESS,
    status: SUCCESS,
    data: resultJson,
    positionInFeed
  };
}
export function selectSingleSelectResponse(value, questionId, positionInFeed) {
  return async (dispatch, getState, { api }) => {
    dispatch(singleSelectRequest(positionInFeed));
    try {
      const result = await api.postMock(SINGLE_SELECT_SUBMIT_PATH, {
        questionId,
        optionId: [value]
      });
      const resultJson = await result.json();
      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }
      dispatch(singleSelectSuccess(resultJson, positionInFeed));
    } catch (e) {
      dispatch(singleSelectFailure(e.message, positionInFeed));
    }
  };
}
export function homeFeedRequest(feedType) {
  return {
    type: HOME_FEED_REQUEST,
    status: REQUESTING,
    feedType
  };
}
export function homeFeedSuccess(data, feedType) {
  return {
    type: HOME_FEED_SUCCESS,
    status: SUCCESS,
    data,
    feedType
  };
}
export function homeFeedFailure(error) {
  return {
    type: HOME_FEED_FAILURE,
    status: ERROR,
    error
  };
}

// this is not simple home feed .it is a general feed like
// brand feed and category feed  . we need to rename this function name like feed
export function homeFeed(brandIdOrCategoryId: null) {
  return async (dispatch, getState, { api }) => {
    if (brandIdOrCategoryId) {
      dispatch(homeFeedRequest(BLP_OR_CLP_FEED_TYPE));
    } else {
      dispatch(homeFeedRequest());
    }
    try {
      let url, result, feedTypeRequest, resultJson;
      if (brandIdOrCategoryId) {
        result = await api.get(
          `v2/mpl/cms/defaultpage?pageId=${brandIdOrCategoryId}`
        );
        feedTypeRequest = BLP_OR_CLP_FEED_TYPE;
        resultJson = await result.json();
        if (resultJson.errors) {
          dispatch(homeFeedSuccess([], feedTypeRequest));
        } else {
          dispatch(homeFeedSuccess(resultJson.items, feedTypeRequest));
        }
      } else {
        let mbox = ADOBE_TARGET_HOME_FEED_MBOX_NAME;

        if (process.env.REACT_APP_STAGE === "production") {
          mbox = ADOBE_TARGET_PRODUCTION_HOME_FEED_MBOX_NAME;
        } else if (process.env.REACT_APP_STAGE === "p2") {
          mbox = ADOBE_TARGET_P2_HOME_FEED_MBOX_NAME;
        }

        const mcvId = await getMcvId();
        resultJson = await api.postAdobeTargetUrl(
          null,
          mbox,
          mcvId,
          null,
          true
        );
        resultJson = resultJson[0];
        feedTypeRequest = HOME_FEED_TYPE;
      }

      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson}`);
      }
      let parsedResultJson = JSON.parse(resultJson.content);
      parsedResultJson = parsedResultJson.items;
      dispatch(homeFeedSuccess(parsedResultJson, feedTypeRequest));
    } catch (e) {
      dispatch(homeFeedFailure(e.message));
    }
  };
}

export function getComponentDataBackUpRequest(positionInFeed) {
  return {
    type: COMPONENT_BACK_UP_REQUEST,
    positionInFeed,
    status: REQUESTING
  };
}

export function getComponentDataBackUpFailure(positionInFeed, error) {
  return {
    type: COMPONENT_BACK_UP_FAILURE,
    positionInFeed,
    error,
    status: ERROR
  };
}

export function getComponentDataBackUpSuccess(positionInFeed, data) {
  return {
    type: COMPONENT_BACK_UP_SUCCESS,
    positionInFeed,
    status: SUCCESS,
    data
  };
}

export function getComponentDataBackUp(url, positionInFeed) {
  return async (dispatch, getState, { api }) => {
    dispatch(getComponentDataBackUpRequest(positionInFeed));
    try {
      const result = await api.getWithoutApiUrlRoot(url);
      const resultJson = await result.json();
      if (resultJson.errors) {
        throw new Error(`${resultJson.errors[0].message}`);
      }

      let parsedResultJson = resultJson;
      parsedResultJson = parsedResultJson.items[0];
      dispatch(getComponentDataBackUpSuccess(positionInFeed, parsedResultJson));
    } catch (e) {
      dispatch(getComponentDataBackUpFailure(positionInFeed, e.message));
    }
  };
}

export function componentDataRequest(positionInFeed) {
  return {
    type: COMPONENT_DATA_REQUEST,
    status: REQUESTING,
    positionInFeed
  };
}

export function componentDataSuccess(data, positionInFeed, isMsd: false) {
  return {
    type: COMPONENT_DATA_SUCCESS,
    status: SUCCESS,
    data,
    positionInFeed,
    isMsd
  };
}
export function componentDataFailure(positionInFeed, error) {
  return {
    type: COMPONENT_DATA_FAILURE,
    status: ERROR,
    positionInFeed,
    error
  };
}
export function getComponentData(
  positionInFeed,
  fetchURL,
  postParams: null,
  backUpUrl
) {
  return async (dispatch, getState, { api }) => {
    dispatch(componentDataRequest(positionInFeed));
    try {
      let postData;
      let result;
      let resultJson;
      const mcvId = await getMcvId();
      if (postParams && postParams.widgetPlatform === MSD_WIDGET_PLATFORM) {
        postData = {
          ...postParams,
          api_key: MSD_API_KEY,
          num_results: MSD_NUM_RESULTS,
          mad_uuid: mcvId,
          widget_list: MSD_WIDGET_LIST //TODO this is going to change.
        };

        result = await api.postMsd(fetchURL, postData, true);
        resultJson = await result.json();

        if (resultJson.errors) {
          throw new Error(`${resultJson.errors[0].message}`);
        }
        dispatch(componentDataSuccess(resultJson, positionInFeed, true));
      } else {
        delay(() => {
          const isFetchUrlDataLoading = getState().home.homeFeed[positionInFeed]
            .loading;
          if (isFetchUrlDataLoading && backUpUrl) {
            dispatch(getComponentDataBackUp(backUpUrl, positionInFeed));
          }
        }, ADOBE_TARGET_DELAY);

        const mcvId = await getMcvId();
        resultJson = await api.postAdobeTargetUrl(
          fetchURL,
          postParams && postParams.mbox ? postParams.mbox : null,
          mcvId,
          null,
          false
        );

        resultJson = resultJson[0];
        if (resultJson.errors) {
          throw new Error(`${resultJson.errors[0].message}`);
        }
        let parsedResultJson = JSON.parse(resultJson.content);
        parsedResultJson = parsedResultJson.items[0];
        dispatch(componentDataSuccess(parsedResultJson, positionInFeed));
      }
    } catch (e) {
      dispatch(componentDataFailure(positionInFeed, e.message));
    }
  };
}
