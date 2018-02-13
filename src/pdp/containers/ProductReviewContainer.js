import { connect } from "react-redux";
import ProductReviewPage from "../components/ProductReviewPage";
import { withRouter } from "react-router-dom";
import { addProductToWishList, addProductToBag } from "../actions/pdp.actions";
const mapDispatchToProps = dispatch => {
  return {
    addProductToBag: productDetails => {
      dispatch(addProductToBag(productDetails));
    },
    addProductToWishList: productDetails => {
      dispatch(addProductToWishList(productDetails));
    }
  };
};

const mapStateToProps = state => {
  return {
    productDetails: state.productDescription.productDetails
  };
};

const ProductDescriptionContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductReviewPage)
);

export default ProductDescriptionContainer;
