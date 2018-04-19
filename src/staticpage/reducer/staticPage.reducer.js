import cloneDeep from "lodash/cloneDeep";
import * as staticPageAction from "../actions/staticPage.actions";

import { SUCCESS } from "../../lib/constants";
const staticPage = (
  state = {
    status: null,
    error: null,
    loading: false,
    type: null,

    aboutUs: null,
    aboutUsStatus: null,
    aboutUsError: null,
    aboutUsLoading: false
  },
  action
) => {
  switch (action.type) {
    case staticPageAction.GET_ABOUT_US_REQUEST:
      return Object.assign({}, state, {
        aboutUsStatus: action.status,
        aboutUsLoading: true
      });

    case staticPageAction.GET_ABOUT_US_SUCCESS:
      return Object.assign({}, state, {
        aboutUsStatus: action.status,
        aboutUs: action.aboutUs,
        aboutUsLoading: false
      });

    case staticPageAction.GET_ABOUT_US_FAILURE:
      return Object.assign({}, state, {
        aboutUsStatus: action.status,
        aboutUsError: action.error,
        aboutUsLoading: false
      });
    default:
      return state;
  }
};
export default staticPage;
