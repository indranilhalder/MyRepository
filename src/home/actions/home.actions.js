import {
  SUCCESS,
  REQUESTING,
  ERROR,
  FAILURE,
  BLP_OR_CLP_FEED_TYPE,
  HOME_FEED_TYPE,
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS
} from "../../lib/constants";
import each from "lodash.foreach";
import delay from "lodash.delay";
import { MSD_WIDGET_PLATFORM } from "../../lib/config.js";
import {
  setDataLayer,
  ADOBE_HOME_TYPE,
  ADOBE_BLP_PAGE_LOAD,
  ADOBE_CLP_PAGE_LOAD,
  ADOBE_STATIC_PAGE
} from "../../lib/adobeUtils.js";
import { setHeaderText } from "../../general/header.actions.js";
import * as Cookie from "../../lib/Cookie";
import * as ErrorHandling from "../../general/ErrorHandling.js";

import { getMcvId } from "../../lib/adobeUtils.js";
import { getMsdFormData } from "../../lib/msdUtils.js";

export const HOME_FEED_REQUEST = "HOME_FEED_REQUEST";
export const HOME_FEED_SUCCESS = "HOME_FEED_SUCCESS";
export const HOME_FEED_FAILURE = "HOME_FEED_FAILURE";

export const HOME_FEED_BACK_UP_FAILURE = "HOME_FEED_BACK_UP_FAILURE";
export const HOME_FEED_BACK_UP_REQUEST = "HOME_FEED_BACK_UP_REQUEST";
export const HOME_FEED_BACK_UP_SUCCESS = "HOME_FEED_BACK_UP_SUCCESS";
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
export const CLEAR_ITEMS = "CLEAR_ITEMS";

export const GET_PRODUCT_CAPSULES_REQUEST = "GET_PRODUCT_CAPSULES_REQUEST";
export const GET_PRODUCT_CAPSULES_SUCCESS = "GET_PRODUCT_CAPSULES_SUCCESS";
export const GET_PRODUCT_CAPSULES_FAILURE = "GET_PRODUCT_CAPSULES_FAILURE";

export const CLEAR_ITEMS_FOR_PARTICULAR_POSITION =
  "CLEAR_ITEMS_FOR_PARTICULAR_POSITION";
const ADOBE_TARGET_DELAY = 1500;
const MSD_NUM_PRODUCTS = 10;
const MSD_NUM_RESULTS = 10;
const MSD_NUM_BRANDS = 1;
const DISCOVER_MORE_NUM_RESULTS = 10;
const FOLLOWED_WIDGET_WIDGET_LIST = [112]; // weirdly it's not done.
const FRESH_FROM_BRANDS_WIDGET_LIST = [111];
const DISCOVER_MORE_WIDGET_LIST = [110];
const AUTOMATED_BRAND_CAROUSEL_WIDGET_LIST = [113];
const MULTI_CLICK_COMPONENT_WIDGET_LIST = [115];
const AUTO_PRODUCT_RECOMMENDATION_COMPONENT_WIDGET_LIST = [11];

const AUTO_FRESH_FROM_BRANDS = "Auto Fresh From Brands Component";
const DISCOVER_MORE = "Auto Discover More Component";
const AUTOMATED_BRAND_CAROUSEL = "Automated Banner Product Carousel Component";
const FOLLOW_WIDGET = "Auto Following Brands Component";
const MULTI_CLICK_COMPONENT = "Multi Click Component";
const AUTO_PRODUCT_RECOMMENDATION_COMPONENT =
  "Auto Product Recommendation Component";
// TODO Followed Widget

const ADOBE_TARGET_HOME_FEED_MBOX_NAME = "mboxPOCTest1"; // for local/devxelp/uat2tmpprod
const ADOBE_TARGET_PRODUCTION_HOME_FEED_MBOX_NAME = "UAT_Mobile_Homepage_Mbox";
const ADOBE_TARGET_P2_HOME_FEED_MBOX_NAME = "UAT_Mobile_Homepage_Mbox";
export const CATEGORY_REGEX = /msh*/;
export const BRAND_REGEX = /mbh*/;

export function getProductCapsulesRequest() {
  return {
    type: GET_PRODUCT_CAPSULES_REQUEST,
    status: REQUESTING
  };
}

export function getProductCapsulesSuccess(productCapsules, positionInFeed) {
  return {
    type: GET_PRODUCT_CAPSULES_SUCCESS,
    status: SUCCESS,
    productCapsules,
    positionInFeed
  };
}

export function getProductCapsulesFailure(error) {
  return {
    type: GET_PRODUCT_CAPSULES_FAILURE,
    error,
    status: FAILURE
  };
}

// {{root_url}}/marketplacewebservices/v2/mpl/users/{{username}}/getProductCapsules?access_token={{customer_access_token}}

export function getProductCapsules(positionInFeed) {
  return async (dispatch, getState, { api }) => {
    dispatch(getProductCapsulesRequest());
    const userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    const customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    try {
      const result = await api.post(
        `v2/mpl/users/${
          JSON.parse(userDetails).userName
        }/getProductCapsules?access_token=${
          JSON.parse(customerCookie).access_token
        }`
      );
      const resultJson = await result.json();
      if (resultJson.errors) {
        throw new Error(`${resultJson.errors[0].message}`);
      }
      dispatch(getProductCapsulesSuccess(resultJson, positionInFeed));
    } catch (e) {
      dispatch(getProductCapsulesFailure(e.message));
    }
  };
}

export function getItemsRequest(positionInFeed) {
  return {
    type: GET_ITEMS_REQUEST,
    positionInFeed,
    status: REQUESTING
  };
}
export function getItemsSuccess(positionInFeed, items, itemIds) {
  return {
    type: GET_ITEMS_SUCCESS,
    status: SUCCESS,
    items,
    positionInFeed,
    itemIds
  };
}
export function clearItemsSuccess(positionInFeed) {
  return {
    type: CLEAR_ITEMS_FOR_PARTICULAR_POSITION,
    status: SUCCESS,
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
export function getItems(positionInFeed, itemIds) {
  return async (dispatch, getState, { api }) => {
    dispatch(getItemsRequest(positionInFeed));
    try {
      let productCodes;
      each(itemIds, itemId => {
        productCodes = `${itemId},${productCodes}`;
      });
      const url = `v2/mpl/cms/page/getProductInfo?isPwa=true&productCodes=${productCodes}`;
      const result = await api.getMiddlewareUrl(url);
      const resultJson = await result.json();

      if (resultJson.status === "FAILURE") {
        throw new Error(`${resultJson.message}`);
      }

      dispatch(getItemsSuccess(positionInFeed, resultJson.results, itemIds));
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
      const result = await api.post(SINGLE_SELECT_SUBMIT_PATH, {
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
      const result = await api.post(SINGLE_SELECT_SUBMIT_PATH, {
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

export function homeFeedBackUpRequest() {
  return {
    type: HOME_FEED_BACK_UP_REQUEST,
    status: REQUESTING
  };
}

export function homeFeedBackupSuccess(data) {
  return {
    type: HOME_FEED_BACK_UP_SUCCESS,
    status: SUCCESS,
    data
  };
}

export function homeFeedBackUpFailure(error) {
  return {
    type: HOME_FEED_BACK_UP_FAILURE,
    status: ERROR,
    error
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

export function homeFeedBackUp() {
  return async (dispatch, getState, { api }) => {
    dispatch(homeFeedBackUpRequest());
    try {
      const result = await api.get(
        `v2/mpl/cms/defaultpage?pageId=defaulthomepage`
      );
      const resultJson = await result.json();
      const failureResponse = ErrorHandling.getFailureResponse(resultJson);
      if (failureResponse.status) {
        dispatch(new Error(failureResponse.message));
      }

      dispatch(homeFeedBackupSuccess(resultJson.items));
    } catch (e) {
      dispatch(homeFeedBackUpFailure(e.message));
    }
  };
}

// this is not simple home feed .it is a general feed like
// brand feed and category feed  . we need to rename this function name like feed
// this is also now used for static pages, so the name brandIdOrCategoryId makes less sense
// however there isn't a good name to replace it.
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
        feedTypeRequest = BLP_OR_CLP_FEED_TYPE;
        try {
          result = await api.getMiddlewareUrl(
            `v2/mpl/cms/defaultpage?pageId=${brandIdOrCategoryId}`
          );
        } catch (e) {
          dispatch(homeFeedSuccess([], feedTypeRequest));
        }

        resultJson = await result.json();
        if (resultJson.errors) {
          dispatch(homeFeedSuccess([], feedTypeRequest));
        } else {
          if (resultJson.pageName) {
            dispatch(setHeaderText(resultJson.pageName));
          }
          dispatch(homeFeedSuccess(resultJson.items, feedTypeRequest));
          if (CATEGORY_REGEX.test(brandIdOrCategoryId)) {
            setDataLayer(
              ADOBE_CLP_PAGE_LOAD,
              resultJson,
              getState().icid.value,
              getState().icid.icidType
            );
          } else if (BRAND_REGEX.test(brandIdOrCategoryId)) {
            setDataLayer(
              ADOBE_BLP_PAGE_LOAD,
              resultJson,
              getState().icid.value,
              getState().icid.icidType
            );
          } else if (
            brandIdOrCategoryId === window.location.pathname.replace("/", "")
          ) {
            setDataLayer(
              ADOBE_STATIC_PAGE,
              resultJson,
              getState().icid.value,
              getState().icid.icidType
            );
          }
        }
      } else {
        let mbox = ADOBE_TARGET_HOME_FEED_MBOX_NAME;

        if (process.env.REACT_APP_STAGE === "production") {
          mbox = ADOBE_TARGET_PRODUCTION_HOME_FEED_MBOX_NAME;
        } else if (process.env.REACT_APP_STAGE === "p2") {
          mbox = ADOBE_TARGET_P2_HOME_FEED_MBOX_NAME;
        }

        delay(() => {
          const isHomeFeedLoading = getState().home.loading;
          if (isHomeFeedLoading) {
            dispatch(homeFeedBackUp());
          }
        }, ADOBE_TARGET_DELAY);

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

      if (
        !resultJson ||
        !resultJson.content ||
        resultJson.status === "FAILURE"
      ) {
        throw new Error("No Data");
      }

      let parsedResultJson = JSON.parse(resultJson.content);

      parsedResultJson = parsedResultJson.items;

      dispatch(homeFeedSuccess(parsedResultJson, feedTypeRequest));
      if (
        window.digitalData &&
        window.digitalData.page &&
        window.digitalData.page.pageInfo &&
        window.digitalData.page.pageInfo.pageName !== "homepage"
      ) {
        setDataLayer(
          ADOBE_HOME_TYPE,
          null,
          getState().icid.value,
          getState().icid.icidType
        );
      }
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

function getMsdPostData(type) {
  if (type === AUTO_FRESH_FROM_BRANDS) {
    return {
      widget_list: FRESH_FROM_BRANDS_WIDGET_LIST
    };
  } else if (type === DISCOVER_MORE) {
    return {
      widget_list: DISCOVER_MORE_WIDGET_LIST
    };
  } else if (type === FOLLOW_WIDGET) {
    return {
      widget_list: FOLLOWED_WIDGET_WIDGET_LIST
    };
  } else if (type === MULTI_CLICK_COMPONENT) {
    return {
      widget_list: MULTI_CLICK_COMPONENT_WIDGET_LIST
    };
  } else if (type === AUTO_PRODUCT_RECOMMENDATION_COMPONENT) {
    return {
      widget_list: AUTO_PRODUCT_RECOMMENDATION_COMPONENT_WIDGET_LIST
    };
  } else {
    return {
      widget_list: AUTOMATED_BRAND_CAROUSEL_WIDGET_LIST
    };
  }
}
export function getComponentData(
  positionInFeed,
  fetchURL,
  postParams: null,
  backUpUrl,
  type
) {
  return async (dispatch, getState, { api }) => {
    dispatch(componentDataRequest(positionInFeed));
    try {
      let postData;
      let result;
      let resultJson;

      if (postParams && postParams.widgetPlatform === MSD_WIDGET_PLATFORM) {
        const widgetSpecificPostData = getMsdPostData(type);

        postData = await getMsdFormData(widgetSpecificPostData.widget_list, [
          MSD_NUM_RESULTS
        ]);

        if (type === AUTOMATED_BRAND_CAROUSEL) {
          postData.append("num_brands", JSON.stringify(MSD_NUM_BRANDS));
          postData.append("num_products", JSON.stringify(MSD_NUM_PRODUCTS));
        }

        if (type === FOLLOW_WIDGET) {
          postData.append("num_brands", JSON.stringify(MSD_NUM_BRANDS));
        }

        if (type === MULTI_CLICK_COMPONENT) {
          postData.append("num_brands", JSON.stringify(MSD_NUM_BRANDS));
        }

        if (
          type === DISCOVER_MORE ||
          type === AUTO_PRODUCT_RECOMMENDATION_COMPONENT
        ) {
          postData.set(
            "num_results",
            JSON.stringify([DISCOVER_MORE_NUM_RESULTS])
          );
        }

        result = await api.postMsd(fetchURL, postData);
        resultJson = await result.json();

        if (resultJson.errors) {
          throw new Error(`${resultJson.errors[0].message}`);
        }

        resultJson.data = resultJson.data[0];

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
