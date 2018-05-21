import { connect } from "react-redux";
import SizeGuideMain from "../components/SizeGuideMain";
import { withRouter } from "react-router-dom";
import { getProductSizeGuide } from "../actions/pdp.actions.js";

const mapStateToProps = state => {
  return {
    sizeData: state.productDescription.sizeGuide.data,
    loading: state.productDescription.sizeGuide.loading,
    productCode: state.productDescription.productDetails.productListingId,
    category: state.productDescription.productDetails.rootCategory,
    productName: state.productDescription.productDetails.productName
  };
};

const mapDispatchToProps = dispatch => {
  return {
    getSizeGuide: productCode => {
      dispatch(getProductSizeGuide(productCode));
    }
  };
};

const SizeGuideContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(SizeGuideMain)
);

export default SizeGuideContainer;
