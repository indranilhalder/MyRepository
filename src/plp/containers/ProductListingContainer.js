import { connect } from "react-redux";
import { productListing } from "../actions/plp.actions";
import ProductListing from "../components/ProductListing";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    productListing: () => {
      dispatch(productListing());
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
  connect(mapStateToProps, mapDispatchToProps)(ProductListing)
);

export default ProductListingContainer;
