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
const mapDispatchToProps = dispatch => {
  return {
    addProductToCart: productDetails => {
      dispatch(addProductToCart(productDetails));
    },
    addProductToWishList: productDetails => {
      dispatch(addProductToWishList(productDetails));
    },
    getProductReviews: productCode => {
      dispatch(getProductReviews(productCode));
    },
    getProductDescription: productCode => {
      dispatch(getProductDescription(productCode));
    },
    addProductReview: (productCode, productReview) => {
      dispatch(addProductReview(productCode, productReview));
    }
  };
};

const mapStateToProps = state => {
  return {
    productDetails: state.productDescription.productDetails,
    reviews: state.productDescription.reviews,
    addReviewStatus: state.productDescription.addReviewStatus
  };
};

const ProductReviewContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductReviewPage)
);

export default ProductReviewContainer;
