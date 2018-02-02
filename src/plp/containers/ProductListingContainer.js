import { connect } from "react-redux";
import { getProducts } from "../actions/plp.actions";
import ProductListingPage from "../components/ProductListingPage";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    getProducts: () => {
      dispatch(getProducts());
    }
  };
};

const mapStateToProps = state => {
  return {
    loading: state.productListing.loading,
    product: state.productListing.product
  };
};

const ProductListingContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductListingPage)
);

export default ProductListingContainer;
