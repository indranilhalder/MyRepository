import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
export const PRODUCT_DESCRIPTION_REQUEST = "PRODUCT_DESCRIPTION_REQUEST";
export const PRODUCT_DESCRIPTION_SUCCESS = "PRODUCT_DESCRIPTION_SUCCESS";
export const PRODUCT_DESCRIPTION_FAILURE = "PRODUCT_DESCRIPTION_FAILURE";
export const PRODUCT_DESCRIPTION_PATH = "pdp";
const FAILURE = "FAILURE";

export function getProductDescriptionRequest() {
  return {
    type: PRODUCT_DESCRIPTION_REQUEST,
    status: REQUESTING
  };
}
export function getProductDescriptionSuccess(productDescription) {
  return {
    type: PRODUCT_DESCRIPTION_SUCCESS,
    status: SUCCESS,
    productDescription
  };
}

export function getProductDescriptionFailure(error) {
  return {
    type: PRODUCT_DESCRIPTION_FAILURE,
    status: ERROR,
    error
  };
}
export function getProductDescription() {
  return async (dispatch, getState, { api }) => {
    dispatch(getProductDescriptionRequest());
    try {
      const result = await api.getMock(PRODUCT_DESCRIPTION_PATH);
      const resultJson = await result.json();
      if (resultJson.status === FAILURE) {
        throw new Error(`${resultJson.message}`);
      }
      // TODO: dispatch a modal here
      dispatch(getProductDescriptionSuccess(resultJson));
    } catch (e) {
      dispatch(getProductDescriptionFailure(e.message));
    }
  };
}
