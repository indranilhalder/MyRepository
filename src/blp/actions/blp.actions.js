import { SUCCESS, REQUESTING, ERROR, FAILURE } from "../../lib/constants";
import {
  setDataLayer,
  ADOBE_DEFAULT_BLP_PAGE_LOAD
} from "../../lib/adobeUtils";

export const GET_ALL_BRANDS_STORE_REQUEST = "GET_ALL_BRANDS_STORE_REQUEST";
export const GET_ALL_BRANDS_STORE_SUCCESS = "GET_ALL_BRANDS_STORE_SUCCESS";
export const GET_ALL_BRANDS_STORE_FAILURE = "GET_ALL_BRANDS_STORE_FAILURE";
const USER_CATEGORY_PATH = "v2/mpl/cms";
const GLOBAL_ID_FOR_FETCHING_BRAND_LISTING = "mbh15a00025";

export function getAllBrandsRequest() {
  return {
    type: GET_ALL_BRANDS_STORE_REQUEST,
    status: REQUESTING
  };
}
export function getAllBrandsSuccess(brandsStores) {
  return {
    type: GET_ALL_BRANDS_STORE_SUCCESS,
    status: SUCCESS,
    brandsStores
  };
}

export function getAllBrandsFailure(error) {
  return {
    type: GET_ALL_BRANDS_STORE_FAILURE,
    status: ERROR,
    error
  };
}

export function getAllBrands(userId, accessToken, cartId) {
  return async (dispatch, getState, { api }) => {
    dispatch(getAllBrandsRequest());

    try {
      const result = await api.get(
        `${USER_CATEGORY_PATH}/defaultpage?pageId=${GLOBAL_ID_FOR_FETCHING_BRAND_LISTING}`
      );

      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(resultJson.error);
      }
      dispatch(getAllBrandsSuccess(resultJson.items[0]));
      setDataLayer(ADOBE_DEFAULT_BLP_PAGE_LOAD, resultJson);
    } catch (e) {
      dispatch(getAllBrandsFailure(e.message));
    }
  };
}
