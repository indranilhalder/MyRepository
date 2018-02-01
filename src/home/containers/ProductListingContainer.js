import { connect } from "react-redux";
import { productListing } from "../actions/home.actions";
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
    loading: state.home.loading,
    product: state.home.product
  };
};

const ProductListingContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductListing)
);

export default ProductListingContainer;
