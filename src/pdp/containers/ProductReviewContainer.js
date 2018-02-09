import { connect } from "react-redux";

import ProductReviewPage from "../components/ProductReviewPage";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {};
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
