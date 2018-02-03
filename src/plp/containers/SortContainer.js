import { connect } from "react-redux";
import { hideModal } from "../../general/modal.actions";
import Sort from "../../general/components/Sort";
import { withRouter } from "react-router-dom";

const mapDispatchToProps = dispatch => {
  return {
    onCloseSort: () => {
      dispatch(hideModal());
    }
  };
};

const mapStateToProps = state => {
  return {
    loading: state.productListings.loading,
    sortList: state.productListings.productListings.sorts
  };
};

const ProductListingsContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Sort)
);

export default ProductListingsContainer;
