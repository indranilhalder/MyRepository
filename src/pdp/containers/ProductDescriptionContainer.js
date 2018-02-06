import { connect } from "react-redux";
import { getProductDescription } from "../actions/pdp.actions";
import ProductDescriptionPage from "../components/ProductDescriptionPage";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    getProductDescription: () => {
      dispatch(getProductDescription());
    }
  };
};

const mapStateToProps = state => {
  return {
    loading: state.productDescription.loading,
    productDescription: state.productDescription.productDescription
  };
};

const ProductDescriptionContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductDescriptionPage)
);

export default ProductDescriptionContainer;
