import { connect } from "react-redux";
import ProductReviewPage from "../components/ProductReviewPage";
import { withRouter } from "react-router-dom";
import {
  addProductToWishList,
  addProductToCart,
  getProductReviews,
  getProductDescription,
  addProductReview
} from "../actions/pdp.actions";
import { setUrlToRedirectToAfterAuth } from "../../auth/actions/auth.actions.js";
import { displayToast } from "../../general/toast.actions";
import {
  showSecondaryLoader,
  hideSecondaryLoader
} from "../../general/secondaryLoader.actions";
const mapDispatchToProps = dispatch => {
  return {
    addProductToCart: (userId, cartId, accessToken, productDetails) => {
      dispatch(addProductToCart(userId, cartId, accessToken, productDetails));
    },
    addProductToWishList: (userId, accessToken, productDetails) => {
      dispatch(addProductToWishList(userId, accessToken, productDetails));
    },
    getProductReviews: (productCode, pageIndex) => {
      dispatch(getProductReviews(productCode, pageIndex));
    },
    getProductDescription: productCode => {
      dispatch(getProductDescription(productCode));
    },
    addProductReview: (productCode, productReview) => {
      dispatch(addProductReview(productCode, productReview));
    },
    setUrlToRedirectToAfterAuth: url => {
      dispatch(setUrlToRedirectToAfterAuth(url));
    },
    displayToast: message => {
      dispatch(displayToast(message));
    },
    showSecondaryLoader: () => {
      dispatch(showSecondaryLoader());
    },
    hideSecondaryLoader: () => {
      dispatch(hideSecondaryLoader());
    }
  };
};

const mapStateToProps = state => {
  return {
    productDetails: state.productDescription.productDetails,
    reviews: state.productDescription.reviews,
    addReviewStatus: state.productDescription.addReviewStatus,
    loadingForAddProduct: state.productDescription.loadingForAddProduct,
    loading: state.productDescription.loading
  };
};

const ProductReviewContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductReviewPage)
);
export default ProductReviewContainer;
