import { connect } from "react-redux";
import { withRouter } from "react-router-dom";
import Plp from "../components/Plp";
import { showModal, SORT } from "../../general/modal.actions.js";
import { setHeaderText } from "../../general/header.actions";
import {
  showFilter,
  hideFilter,
  setClearUrl,
  setClearUrlToNull
} from "../../plp/actions/plp.actions.js";

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
    },
    showFilter: () => {
      dispatch(showFilter());
    },
    hideFilter: () => {
      dispatch(hideFilter());
    },
    setClearUrl: url => {
      dispatch(setClearUrl(url));
    },
    setClearUrlToNull: () => {
      dispatch(setClearUrlToNull());
    }
  };
};

const mapStateToProps = (state, ownProps) => {
  return {
    isFilter: ownProps.isFilter,
    onFilterClick: ownProps.onFilterClick,
    isFilterOpen: state.productListings.isFilterOpen,
    productListings: state.productListings.productListings,
    pageNumber: state.productListings.pageNumber,
    loading: state.productListings.loading,
    searchresult: state.productListings.searchresult,
    clearUrl: state.productListings.urlToReturnToAfterClear
  };
};

const PlpContainer = withRouter(
  connect(mapStateToProps, mapDispatchToProps)(Plp)
);
export default PlpContainer;
