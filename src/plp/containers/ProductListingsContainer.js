import { connect } from "react-redux";
import { showModal, SORT } from "../../general/modal.actions";
import ProductListingsPage from "../components/ProductListingsPage";
import { withRouter } from "react-router-dom";
import { setSearchString } from "../../search/actions/search.actions.js";
import { getProductListings, setPage } from "../actions/plp.actions.js";

const mapDispatchToProps = dispatch => {
  return {
    showSort: () => {
      dispatch(showModal(SORT));
    },
    getProductListings: (search: null, suffix, page, isFilter) => {
      dispatch(setSearchString(search));
      dispatch(setPage(page));
      dispatch(getProductListings(suffix, false, isFilter));
    },
    paginate: (page, suffix) => {
      dispatch(setPage(page));
      dispatch(getProductListings(suffix, true, false));
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    searchText: ownProps.searchText ? ownProps.searchText : null,
    isGoBackFromPdpPage: state.productListings.isGoBackFromPdpPage,
    clickedProductModuleRef: state.productListings.clickedProductModuleRef
  };
};

const ProductListingsContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(ProductListingsPage)
);

export default ProductListingsContainer;
