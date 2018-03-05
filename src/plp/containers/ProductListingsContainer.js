import { connect } from "react-redux";
import { getProductListings } from "../actions/plp.actions";
import { showModal, SORT } from "../../general/modal.actions";
import ProductListingsPage from "../components/ProductListingsPage";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    showSort: () => {
      dispatch(showModal(SORT));
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    isFilter: ownProps.isFilter,
    loading: state.productListings.loading,
    productListings: state.productListings.productListings,
    pageNumber: state.productListings.pageNumber
  };
};

const ProductListingsContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductListingsPage)
);

export default ProductListingsContainer;
