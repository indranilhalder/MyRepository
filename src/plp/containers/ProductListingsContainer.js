import { connect } from "react-redux";
import { getProducts } from "../actions/plp.actions";
import ProductListingsPage from "../components/ProductListingsPage";
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
    loading: state.products.loading,
    products: state.products.products
  };
};

const ProductListingsContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductListingsPage)
);

export default ProductListingsContainer;
