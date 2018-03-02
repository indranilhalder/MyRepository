import { connect } from "react-redux";
import { getProductDescription } from "../actions/pdp.actions";
import ProductDescriptionPageWrapper from "../components/ProductDescriptionPageWrapper";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    getProductDescription: productCode => {
      dispatch(getProductDescription(productCode));
    }
  };
};

const mapStateToProps = state => {
  return {
    productDetails: state.productDescription.productDetails
  };
};

const ProductDescriptionPageWrapperContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductDescriptionPageWrapper)
);

export default ProductDescriptionPageWrapperContainer;
