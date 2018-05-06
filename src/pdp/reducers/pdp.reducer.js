import * as pdpActions from "../actions/pdp.actions";
import { FOLLOW_AND_UN_FOLLOW_BRANDS_IN_PDP_SUCCESS } from "../../account/actions/account.actions";
import { YES, NO } from "../../lib/constants";
import { transferPincodeToPdpPincode } from "./utils";
import { CLEAR_ERROR } from "../../general/error.actions.js";

import concat from "lodash.concat";
import cloneDeep from "lodash.clonedeep";
const productDescription = (
  state = {
    status: null,
    error: null,
    loading: false,
    aboutTheBrand: null,
    productDetails: null,
    isServiceableToPincode: null,
    sizeGuide: {
      loading: false,
      sizeGuideList: []
    },
    emiResult: null,
    reviews: {},
    reviewsStatus: null,
    loadingForAddProduct: false,
    addReviewStatus: false,
    reviewsError: null,
    msdItems: {},
    emiTerms: null,
    storeDetails: null,
    storeStatus: null,
    storeError: null
  },
  action
) => {
  let sizeGuide, currentProductDetails, currentBrandDetails;
  switch (action.type) {
    case CLEAR_ERROR:
      return Object.assign({}, state, {
        loading: false,
        error: null,
        status: null,
        reviewsError: null,
        addReviewStatus: null
      });
    case pdpActions.GET_EMI_TERMS_AND_CONDITIONS_FAILURE:
      return Object.assign({}, state, {
        emiTerms: {
          loading: false,
          error: action.error,
          status: action.status
        }
      });
    case pdpActions.GET_EMI_TERMS_AND_CONDITIONS_REQUEST:
      return Object.assign({}, state, {
        emiTerms: null,
        loading: true,
        status: action.status
      });
    case pdpActions.GET_EMI_TERMS_AND_CONDITIONS_SUCCESS:
      return Object.assign({}, state, {
        emiTerms: {
          loading: false,
          status: action.status,
          data: action.emiTerms
        }
      });
    case pdpActions.PRODUCT_DESCRIPTION_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.PRODUCT_DESCRIPTION_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        productDetails: action.productDescription,
        loading: false
      });

    case pdpActions.PRODUCT_DESCRIPTION_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case pdpActions.CHECK_PRODUCT_PIN_CODE_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.CHECK_PRODUCT_PIN_CODE_SUCCESS:
      const currentPdpDetail = cloneDeep(state.productDetails);
      let currentProductUssId = currentPdpDetail.winningUssID;
      let deliveryOptionObj;
      if (
        action.productPinCode &&
        action.productPinCode.deliveryOptions &&
        action.productPinCode.deliveryOptions.pincodeListResponse
      ) {
        deliveryOptionObj = action.productPinCode.deliveryOptions.pincodeListResponse.find(
          delivery => {
            return delivery.ussid === currentProductUssId;
          }
        );
      }

      let eligibleDeliveryModes = [];
      if (deliveryOptionObj && deliveryOptionObj.isServicable === YES) {
        eligibleDeliveryModes = transferPincodeToPdpPincode(
          deliveryOptionObj.validDeliveryModes
        );
        Object.assign(currentPdpDetail, {
          eligibleDeliveryModes,
          isServiceableToPincode: {
            status: YES,
            pinCode: action.productPinCode.pinCode
          }
        });
      } else {
        Object.assign(currentPdpDetail, {
          isServiceableToPincode: {
            status: NO,
            pinCode: action.productPinCode.pinCode
          }
        });
      }

      return Object.assign({}, state, {
        status: action.status,
        productDetails: currentPdpDetail,
        loading: false
      });

    case pdpActions.CHECK_PRODUCT_PIN_CODE_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case pdpActions.ADD_PRODUCT_TO_CART_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.ADD_PRODUCT_TO_CART_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        loading: false
      });

    case pdpActions.ADD_PRODUCT_TO_CART_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case pdpActions.PRODUCT_SIZE_GUIDE_REQUEST:
      sizeGuide = {
        loading: true,
        data: null
      };
      return Object.assign({}, state, {
        status: action.status,
        sizeGuide
      });

    case pdpActions.PRODUCT_SIZE_GUIDE_SUCCESS:
      sizeGuide = {
        loading: false,
        data: action.sizeGuide
      };
      return Object.assign({}, state, {
        status: action.status,
        sizeGuide
      });

    case pdpActions.PRODUCT_SIZE_GUIDE_FAILURE:
      sizeGuide = {
        loading: false,
        error: action.error
      };
      return Object.assign({}, state, {
        status: action.status,
        sizeGuide
      });

    case pdpActions.PRODUCT_PDP_EMI_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.PRODUCT_PDP_EMI_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        emiResult: action.emiResult,
        loading: false
      });

    case pdpActions.PRODUCT_PDP_EMI_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case pdpActions.PRODUCT_SPECIFICATION_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.PRODUCT_SPECIFICATION_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        productDetails: action.productDetails,
        loading: false
      });

    case pdpActions.PRODUCT_SPECIFICATION_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case pdpActions.ADD_PRODUCT_REVIEW_REQUEST:
      return Object.assign({}, state, {
        addReviewStatus: action.status,
        loadingForAddProduct: true
      });

    case pdpActions.ADD_PRODUCT_REVIEW_SUCCESS:
      return Object.assign({}, state, {
        addReviewStatus: action.status,
        loadingForAddProduct: false
      });

    case pdpActions.ADD_PRODUCT_REVIEW_FAILURE:
      return Object.assign({}, state, {
        addReviewStatus: action.status,
        reviewsError: action.error,
        loadingForAddProduct: false
      });

    case pdpActions.EDIT_PRODUCT_REVIEW_REQUEST:
      return Object.assign({}, state, {
        reviewsStatus: action.status,
        loading: true
      });

    case pdpActions.EDIT_PRODUCT_REVIEW_SUCCESS:
      return Object.assign({}, state, {
        reviewsStatus: action.status,
        loading: false
      });

    case pdpActions.EDIT_PRODUCT_REVIEW_FAILURE:
      return Object.assign({}, state, {
        reviewsStatus: action.status,
        reviewsError: action.error,
        loading: false
      });
    case pdpActions.DELETE_PRODUCT_REVIEW_REQUEST:
      return Object.assign({}, state, {
        reviewsStatus: action.status,
        loading: true
      });

    case pdpActions.DELETE_PRODUCT_REVIEW_SUCCESS:
      return Object.assign({}, state, {
        reviewsStatus: action.status,
        loading: false
      });

    case pdpActions.DELETE_PRODUCT_REVIEW_FAILURE:
      return Object.assign({}, state, {
        reviewsStatus: action.status,
        reviewsError: action.error,
        loading: false
      });

    case pdpActions.GET_PRODUCT_REVIEW_REQUEST:
      return Object.assign({}, state, {
        reviewsStatus: action.status,
        loading: true
      });

    case pdpActions.GET_PRODUCT_REVIEW_SUCCESS:
      const currentReviews = cloneDeep(state.reviews);
      let updatedReviewsObj;
      if (action.reviews.pageNumber === 0) {
        updatedReviewsObj = Object.assign({}, currentReviews, action.reviews);
      } else {
        let updatedReviews = concat(
          currentReviews.reviews,
          action.reviews.reviews
        );
        updatedReviewsObj = Object.assign({}, currentReviews, {
          reviews: updatedReviews,
          pageNumber: action.reviews.pageNumber
        });
      }

      return Object.assign({}, state, {
        reviewsStatus: action.status,
        reviews: updatedReviewsObj,
        loading: false
      });

    case pdpActions.GET_PRODUCT_REVIEW_FAILURE:
      return Object.assign({}, state, {
        reviewsStatus: action.status,
        reviewsError: action.error,
        loading: false
      });

    case pdpActions.PRODUCT_MSD_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.PRODUCT_MSD_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        msdItems: action.msdItems,
        loading: false
      });

    case pdpActions.PRODUCT_MSD_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case pdpActions.GET_PDP_ITEMS_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.GET_PDP_ITEMS_SUCCESS:
      const newMsdItems = cloneDeep(state.msdItems);
      newMsdItems[action.widgetKey] = action.items;
      return Object.assign({}, state, {
        status: action.status,
        msdItems: newMsdItems,
        loading: false
      });
    case pdpActions.GET_PDP_ITEMS_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        loading: false
      });
    case pdpActions.PDP_ABOUT_BRAND_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        aboutTheBrand: action.brandDetails,
        loading: false
      });

    case pdpActions.FOLLOW_UN_FOLLOW_BRAND_REQUEST:
      return Object.assign({}, state, {
        status: action.status
      });

    case FOLLOW_AND_UN_FOLLOW_BRANDS_IN_PDP_SUCCESS:
      currentBrandDetails = cloneDeep(state.aboutTheBrand);
      currentBrandDetails.isFollowing = action.followStatus;
      return Object.assign({}, state, {
        status: action.status,
        aboutTheBrand: currentBrandDetails
      });
    case pdpActions.FOLLOW_UN_FOLLOW_BRAND_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error
      });
    case pdpActions.GET_ALL_STORES_FOR_CLIQ_AND_PIQ_REQUEST:
      return Object.assign({}, state, {
        storeStatus: action.status,
        loading: true
      });

    case pdpActions.GET_ALL_STORES_FOR_CLIQ_AND_PIQ_SUCCESS:
      return Object.assign({}, state, {
        storeStatus: action.status,
        storeDetails: action.storeDetails,
        loading: false
      });

    case pdpActions.GET_ALL_STORES_FOR_CLIQ_AND_PIQ_FAILURE:
      return Object.assign({}, state, {
        storeStatus: action.status,
        storeError: action.error,
        loading: false
      });

    default:
      return state;
  }
};

export default productDescription;
