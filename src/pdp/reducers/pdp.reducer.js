import * as pdpActions from "../actions/pdp.actions";
import { YES, NO } from "../../lib/constants";
import { transferPincodeToPdpPincode } from "./utils";
import cloneDeep from "lodash/cloneDeep";
const productDescription = (
  state = {
    status: null,
    error: null,
    loading: false,
    aboutBrand: null,
    productDetails: null,
    isServiceableToPincode: null,
    sizeGuide: {
      loading: false,
      sizeGuideList: []
    },
    emiResult: null,
    wishList: null,
    reviews: {},
    reviewsStatus: null,
    addReviewStatus: false,
    reviewsError: null,
    msdItems: {},
    emiTerms: null
  },
  action
) => {
  let sizeGuide, currentProductDetails, currentBrandDetails;
  switch (action.type) {
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
      console.log();
      let currentProductUssId = currentPdpDetail.winningUssID;
      const deliveryOptionObj = action.productPinCode.deliveryOptions.pincodeListResponse.find(
        delivery => {
          return delivery.ussid === currentProductUssId;
        }
      );

      let eligibleDeliveryModes = [];
      if (deliveryOptionObj.isServicable === YES) {
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

    case pdpActions.ADD_PRODUCT_TO_WISH_LIST_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.ADD_PRODUCT_TO_WISH_LIST_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        loading: false
      });

    case pdpActions.ADD_PRODUCT_TO_WISH_LIST_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error,
        loading: false
      });

    case pdpActions.REMOVE_PRODUCT_FROM_WISH_LIST_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.REMOVE_PRODUCT_FROM_WISH_LIST_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        loading: false
      });

    case pdpActions.REMOVE_PRODUCT_FROM_WISH_LIST_FAILURE:
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

    case pdpActions.PRODUCT_WISH_LIST_REQUEST:
      return Object.assign({}, state, {
        status: action.status,
        loading: true
      });

    case pdpActions.PRODUCT_WISH_LIST_SUCCESS:
      return Object.assign({}, state, {
        status: action.status,
        wishList: action.wishList,
        loading: false
      });

    case pdpActions.PRODUCT_WISH_LIST_FAILURE:
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
        loading: true
      });

    case pdpActions.ADD_PRODUCT_REVIEW_SUCCESS:
      let reviews = cloneDeep(state.reviews);
      if (!reviews.reviews) {
        reviews.reviews = [action.productReview];
      } else {
        reviews.reviews = reviews.push(action.productReview);
      }
      return Object.assign({}, state, {
        addReviewStatus: action.status,
        loading: false,
        reviews
      });

    case pdpActions.ADD_PRODUCT_REVIEW_FAILURE:
      return Object.assign({}, state, {
        addReviewStatus: action.status,
        reviewsError: action.error,
        loading: false
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
      return Object.assign({}, state, {
        reviewsStatus: action.status,
        reviews: action.reviews,
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
        error: action.error,
        loading: false
      });
    case pdpActions.PDP_ABOUT_BRAND_SUCCESS:
      const newMsdItemsForBrand = cloneDeep(state.msdItems);
      const brandDetails = Object.assign(
        {},
        {
          isFollowing: action.brandDetails.isFollowing,
          //we need to change this brand code to brandId once api will fix
          brandId: action.brandDetails.brandCode
        }
      );
      Object.assign(newMsdItemsForBrand, { brandDetails });
      return Object.assign({}, state, {
        status: action.status,
        msdItems: newMsdItemsForBrand,
        loading: false
      });

    case pdpActions.FOLLOW_UN_FOLLOW_BRAND_REQUEST:
      return Object.assign({}, state, {
        status: action.status
      });

    case pdpActions.FOLLOW_UN_FOLLOW_BRAND_SUCCESS:
      currentBrandDetails = cloneDeep(state.msdItems);
      currentBrandDetails.brandDetails.isFollowing =
        action.brandDetails.isFollowing;
      return Object.assign({}, state, {
        status: action.status,
        msdItems: currentBrandDetails
      });
    case pdpActions.FOLLOW_UN_FOLLOW_BRAND_FAILURE:
      return Object.assign({}, state, {
        status: action.status,
        error: action.error
      });
    default:
      return state;
  }
};

export default productDescription;
