import { SUCCESS, REQUESTING, ERROR } from "../../lib/constants";
import * as Cookie from "../../lib/Cookie";
import {
  CUSTOMER_ACCESS_TOKEN,
  LOGGED_IN_USER_DETAILS,
  FAILURE_UPPERCASE
} from "../../lib/constants";
export const USER_CART_PATH = "v2/mpl/users";

export const Get_ALL_ORDERS_REQUEST = "Get_ALL_ORDERS_REQUEST";
export const Get_ALL_ORDERS_SUCCESS = "Get_ALL_ORDERS_SUCCESS";
export const Get_ALL_ORDERS_FAILURE = "Get_ALL_ORDERS_FAILURE";

export function getAllOrdersRequest() {
  return {
    type: Get_ALL_ORDERS_REQUEST,
    status: REQUESTING
  };
}
export function getAllOrdersSuccess(orderDetails) {
  return {
    type: Get_ALL_ORDERS_SUCCESS,
    status: SUCCESS,
    orderDetails
  };
}

export function getAllOrdersFailure(error) {
  return {
    type: Get_ALL_ORDERS_FAILURE,
    status: ERROR,
    error
  };
}

export function getAllOrdersDetails() {
  return async (dispatch, getState, { api }) => {
    let userDetails = Cookie.getCookie(LOGGED_IN_USER_DETAILS);
    let customerCookie = Cookie.getCookie(CUSTOMER_ACCESS_TOKEN);
    dispatch(getAllOrdersRequest());
    try {
      const result = await api.get(
        `${USER_CART_PATH}/${
          JSON.parse(userDetails).customerInfo.mobileNumber
        }/orderhistorylist?currentPage=0&access_token=${
          JSON.parse(customerCookie).access_token
        }&pageSize=10&isPwa=true&platformNumber=2`
      );
      const resultJson = await result.json();
      if (resultJson.status === FAILURE_UPPERCASE) {
        throw new Error(resultJson.error);
      }
      dispatch(getAllOrdersSuccess(resultJson));
    } catch (e) {
      dispatch(getAllOrdersFailure(e.message));
    }
  };
}
