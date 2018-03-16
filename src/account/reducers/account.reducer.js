import * as accountActions from "../actions/account.actions";
import * as Cookies from "../../lib/Cookie";
import {
  CART_DETAILS_FOR_LOGGED_IN_USER,
  CART_DETAILS_FOR_ANONYMOUS
} from "../../lib/constants";
const cart = (
  state = {
    status: null,
    error: null,
    loading: false,
    type: null,

    customerProfileDetails: null,
    customerProfileDetailsStatus: null,
    customerProfileDetailsError: null
  },
  action
) => {
  switch (action.type) {
    case accountActions.Get_CUSTOMER_PROFILE_REQUEST:
      return Object.assign({}, state, {
        customerProfileDetailsStatus: action.status,
        loading: true
      });

    case accountActions.Get_CUSTOMER_PROFILE_SUCCESS:
      return Object.assign({}, state, {
        customerProfileDetailsStatus: action.status,
        customerProfileDetails: action.customerProfileDetails,
        loading: false
      });

    case accountActions.Get_CUSTOMER_PROFILE_FAILURE:
      return Object.assign({}, state, {
        customerProfileDetailsStatus: action.status,
        customerProfileDetailsError: action.error,
        loading: false
      });

    default:
      return state;
  }
};

export default cart;
