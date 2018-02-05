import { connect } from "react-redux";
import {
  getProductListings,
  getFilteredProductListings
} from "../actions/plp.actions";
import { showModal, SORT } from "../../general/modal.actions";
import ProductListingsPage from "../components/ProductListingsPage";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    getProductListings: () => {
      dispatch(getProductListings());
    },
    showSort: () => {
      dispatch(showModal(SORT));
    },
    onApply: val => {
      dispatch(getFilteredProductListings(val));
    }
  };
};

const mapStateToProps = state => {
  return {
    loading: state.productListings.loading,
    productListings: state.productListings.productListings
  };
};

const ProductListingsContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductListingsPage)
);

export default ProductListingsContainer;
