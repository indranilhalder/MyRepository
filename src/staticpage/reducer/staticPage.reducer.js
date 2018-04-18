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
    aboutUsLoading: false,

    faq: null,
    faqStatus: null,
    faqError: null,
    faqLoading: false,

    termsAndCondition: null,
    termsAndConditionStatus: null,
    termsAndConditionError: null,
    termsAndConditionLoading: false
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

    case staticPageAction.GET_FAQ_REQUEST:
      return Object.assign({}, state, {
        faqStatus: action.status,
        faqLoading: true
      });

    case staticPageAction.GET_FAQ_SUCCESS:
      return Object.assign({}, state, {
        faqStatus: action.status,
        faq: action.faq,
        faqLoading: false
      });

    case staticPageAction.GET_FAQ_FAILURE:
      return Object.assign({}, state, {
        faqStatus: action.status,
        faqError: action.error,
        faqLoading: false
      });

    case staticPageAction.TERMS_CONDITION_REQUEST:
      return Object.assign({}, state, {
        termsAndConditionStatus: action.status,
        termsAndConditionLoading: true
      });

    case staticPageAction.TERMS_CONDITION_SUCCESS:
      return Object.assign({}, state, {
        termsAndConditionStatus: action.status,
        termsAndCondition: action.termsAndCondition,
        termsAndConditionLoading: false
      });

    case staticPageAction.TERMS_CONDITION_FAILURE:
      return Object.assign({}, state, {
        termsAndConditionStatus: action.status,
        termsAndConditionError: action.error,
        termsAndConditionLoading: false
      });
    default:
      return state;
  }
};
export default staticPage;
