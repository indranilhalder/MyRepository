import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import Plp from "../components/Plp";
import { showModal, SORT } from "../../general/modal.actions.js";
import { setHeaderText } from "../../general/header.actions";
const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    showSort: () => {
      dispatch(showModal(SORT));
    },
    paginate: (pageNumber, suffix) => {
      ownProps.paginate(pageNumber, suffix);
    },
    setHeaderText: text => {
      dispatch(setHeaderText(text));
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    isFilter: ownProps.isFilter,
    onFilterClick: ownProps.onFilterClick,
    showFilter: ownProps.showFilter,
    productListings: state.productListings.productListings,
    pageNumber: state.productListings.pageNumber,
    loading: state.productListings.loading,
    totalPages: state.productListings.totalPages,
    searchresult: state.productListings.searchresult
  };
};

const PlpContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Plp)
);
export default PlpContainer;
